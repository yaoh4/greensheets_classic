/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr;

import gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager;
import gov.nih.nci.iscs.i2e.greensheets.GreensheetsResourceException;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * Production Implementation of GreensheetResourceManager interface
 * 
 * @see gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetResourceManagerImpl implements GreensheetResourceManager {

	private static final Logger logger = Logger
			.getLogger(GreensheetResourceManagerImpl.class);

	private boolean frozen = false;

	/**
	 * @see gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager#getResource(String,
	 *      int)
	 */
	public String getResource(String id, int type)
			throws GreensheetsResourceException {

		if (id.indexOf("F") > -1) {
			this.frozen = true;
			id = id.substring(1, id.length());
			logger.debug("New String Id = " + id);
		}

		GreensheetTemplateWrapper tw = null;
		try {
			tw = this.loadVelocityTemplate(id);
		} catch (Exception e) {
			throw new GreensheetsResourceException();
		}

		return tw.getTemplate();
	}

	public GreensheetTemplateWrapper loadVelocityTemplate(String id)
			throws Exception {

		logger.debug("START Template Load" + new java.util.Date().toString());

		GreensheetTemplateWrapper tw = null;

		Map map = (Map) AppConfigProperties.getInstance().getProperty(
				"TEMPLATE_RESOURCE_MAP");

		Object o = map.get(id);
		if (o != null) {

			tw = (GreensheetTemplateWrapper) o;

		} else {

			Connection conn = DbConnectionHelper.getInstance().getConnection();
			String sqlSelect = null;
			if (this.frozen) {
				sqlSelect = "SELECT ft.id,ft.template_html FROM form_templates_t ft where ft.id="
						+ id;
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) {
					int tempId = rs.getInt(1);
					java.sql.Clob clob = (java.sql.Clob) rs.getObject(2);

					ByteArrayOutputStream out = new ByteArrayOutputStream();

					Reader reader = clob.getCharacterStream();
					int i = 0;

					while (i > -1) {
						i = reader.read();
						out.write(i);
					}
					reader.close();
					String template = out.toString();
					out.close();

					int x = template.lastIndexOf("</html>");

					String tmp = template.substring(0, x + 7);

					tw = new GreensheetTemplateWrapper(tempId, tmp);
				}

			} else {

				sqlSelect = "SELECT ft.id, fgm.activity_code,fgm.appl_type_code, ft.template_html FROM form_templates_t ft, form_grant_matrix_t fgm where ft.id=fgm.ftm_id and ft.id="
						+ id;

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) {
					int tempId = rs.getInt(1);
					String actCode = rs.getString(2);
					String type = rs.getString(3);
					java.sql.Clob clob = (java.sql.Clob) rs.getObject(4);

					ByteArrayOutputStream out = new ByteArrayOutputStream();

					Reader reader = clob.getCharacterStream();
					int i = 0;

					while (i > -1) {
						i = reader.read();
						out.write(i);
					}
					reader.close();
					String template = out.toString();
					out.close();

					int x = template.lastIndexOf("</html>");

					String tmp = template.substring(0, x + 7);

					tw = new GreensheetTemplateWrapper(tempId, tmp);
					String cache = ((Properties) AppConfigProperties
							.getInstance().getProperty(
									GreensheetsKeys.KEY_CONFIG_PROPERTIES))
							.getProperty("greensheet.template.cache");

					StringTokenizer st = new StringTokenizer(cache, ",");
					while (st.hasMoreTokens()) {
						String val = st.nextToken();
						if (val.equalsIgnoreCase(type + actCode)) {
							map.put(id, tw);
							logger
									.debug("<<<<<<<<<<<<<<<<<<adding templId to Map "
											+ id);
						}
					}
				}

			}

			DbConnectionHelper.getInstance().freeConnection(conn);
			logger.debug("END Template Load" + new java.util.Date().toString());
		}
		return tw;

	}

}
