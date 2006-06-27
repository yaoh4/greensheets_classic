package gov.nih.nci.iscs.numsix.greensheets.services.reports;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class SpecialistStatusDAO {

	private static final Logger logger = Logger
			.getLogger(SpecialistStatusDAO.class);

	public static List getSpecialistStatusData() throws GreensheetBaseException {

		List l = new ArrayList();

		Connection conn = null;
		try {
			logger.debug("Searching for stuff");
			conn = DbConnectionHelper.getInstance().getConnection();

			String sql = "select fv.full_grant_num, fv.appl_Id, ft.submitted_user_id, ft.submitted_date"
					+ " from forms_t ft, form_grant_vw fv, appl_forms_t aft"
					+ " where ft.id=aft.frm_id and fv.appl_id = aft.appl_id and ft.form_role_code='SPEC'"
					+ " and fv.spec_form_status='SUBMITTED'";

			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery(sql);
			int i = 0;
			while (r.next()) {
				String userId = r.getString("submitted_user_id");
				String applId = Integer.toString(r.getInt("appl_id"));
				String grantNum = r.getString("full_grant_num");
				java.sql.Date date = r.getDate("submitted_date");
				l.add(new SpecialistStatusVO(grantNum, applId, userId, date));

			}

			r.close();
			s.close();
		} catch (SQLException se) {
			throw new GreensheetBaseException(
					"Error retrieving Specialist Status Info", se);

		} finally {
			DbConnectionHelper.getInstance().freeConnection(conn);
		}

		return l;

	}

}
