package gov.nih.nci.cbiit.scimgmt.gs.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import gov.nih.nci.cbiit.scimgmt.gs.dao.PdfUploadDAO;

public class PdfUploadDAOImpl implements PdfUploadDAO {
	private static final Logger logger = Logger.getLogger(PdfUploadDAOImpl.class);
	private static SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}
	
	public Map<String, String> getPfdUploadCandidates() {
		
		String stringQuery = "SELECT aft.appl_id, ft.form_role_code "
				+ "FROM APPL_FORMS_T aft, "
				+ "  FORMS_T ft "
				+ "WHERE aft.frm_id        = ft.id "
				+ "AND ft.form_status      = 'FROZEN' "
				+ "AND ft.form_role_code  != 'REV' "
				+ "AND ft.upload_status_id = 113 " // Pending to eRA upload
				+ "AND appl_id            IS NOT NULL";
				
		logger.debug("SQL: " + stringQuery);

		Map<String, String> applGroupTypeMap = new HashMap<String, String>();        
        try{   
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery).addScalar("form_role_code", new StringType()).addScalar("appl_id", new LongType());

			 List<Object[]> results = query.list();
            for(Object[] result : results) {
            	String formRoleCode = result[0].toString();
            	Long applId = Long.parseLong(result[1].toString());           	
            	applGroupTypeMap.put(applId + "\t" + formRoleCode , "1");
        	}
        } catch (Throwable ex) {
            logger.error("Error occurred while generating the list of candidates for PDF Upload.");   
            throw ex;
        }
       
		return applGroupTypeMap;
    }
	
	public int updateUploadStatus(int frmId) {
		int updatedRecords = 0;
		
		String stringQuery = "UPDATE Forms_t "
				+ " SET upload_status_id = 114 "
				+ " WHERE id = :frmId";
		
		logger.info(stringQuery + "frmId: " + frmId);
		try {
			updatedRecords = sessionFactory.getCurrentSession().createSQLQuery(stringQuery).setParameter("frmId", frmId).executeUpdate();
		} catch (Throwable ex) {
            logger.error("Error occurred while updating upload status of form ID: " + frmId);   
            throw ex;
        }
		
		return updatedRecords;
	}
		
}
