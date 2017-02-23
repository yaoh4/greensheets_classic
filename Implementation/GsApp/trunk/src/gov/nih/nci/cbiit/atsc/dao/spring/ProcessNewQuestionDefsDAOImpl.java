package gov.nih.nci.cbiit.atsc.dao.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.cbiit.atsc.dao.ProcessNewQuestionDefsDAO;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormGrantMatrix;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormGrantMatrixDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormInactiveTypeMech;
import gov.nih.nci.iscs.numsix.greensheets.utils.FilesysUtils;

@SuppressWarnings("serial")
public class ProcessNewQuestionDefsDAOImpl implements ProcessNewQuestionDefsDAO {

    private static final Logger logger = Logger.getLogger(ProcessNewQuestionDefsDAOImpl.class);
    
    private String[] NCList = { "4,UH3", "4,U42", "4,UH2", "4,R01", "4,R03", "4,R33", "4,R00", "4,U44", "4,DP1", "4,R42", "4,P01", "4,R44", "4,U54", "4,R21" };
	private final Map<String, String> moduleRoleCode = new HashMap<String, String>()
    {{
    	put("PC", "PGM");
		put("PNC", "PGM"); 		
		put("SC", "SPEC");
		put("SNC", "SPEC");
    }};
    
    private final Map<String, String> moduleApplTypeCode = new HashMap<String, String>()
    {{
    	put("PC", "('1','2','3','6','7','9','4')");
		put("PNC", "('5','8')"); 		
		put("SC", "('1','2','3','6','7','9','4')");
		put("SNC", "('5','8')");
    }};
   
    private static SessionFactory sessionFactory;

   	public void setSessionFactory(SessionFactory sessionFactory) {
   		this.sessionFactory = sessionFactory;
   	}

   	protected SessionFactory getSessionFactory() {
   		return this.sessionFactory;
   	}

	@SuppressWarnings("unchecked")
	public HashSet<String> getModuleTypeMechListFromProdDB(String module) {
        logger.info("........getModuleTypeMechListFromProdDB for module: " + module);

		HashSet<String> moduleTypeMechList = new HashSet<String>();
		String query = "SELECT fgm FROM FormGrantMatrix AS fgm";
		
		if(module != null && !module.isEmpty()){
        	query = query + " WHERE formRoleCode = '" + moduleRoleCode.get(module) + "'"
        				  + " AND applTypeCode IN " + moduleApplTypeCode.get(module);
		}
		List<FormGrantMatrix> fgms = (List<FormGrantMatrix>) sessionFactory.getCurrentSession().createQuery(query).list();
		
		for (FormGrantMatrix fgm : fgms) {
			String group = fgm.getFormRoleCode();
			String type = fgm.getApplTypeCode();
			String mech = fgm.getActivityCode();
			moduleTypeMechList.add(group + "," + type + "," + mech);
		}

		if (module != null && module.equalsIgnoreCase("PC")) {
			for (int i = 0; i < NCList.length; i++) {
				String tm = moduleRoleCode.get(module) + "," + NCList[i];
				if (moduleTypeMechList.contains(tm)) {
					moduleTypeMechList.remove(tm);
				}
			}
		}
		return moduleTypeMechList;
	}

	@SuppressWarnings("unchecked")
	public HashSet<String> getModuleTypeMechListFromDraftDB(String module) {
        logger.info("........getModuleTypeMechListFromDraftDB for module: " + module);

        HashSet<String> moduleTypeMechList = new HashSet<String>();
        List<FormGrantMatrixDraft> fgmds = new ArrayList<FormGrantMatrixDraft>();
        String query = "SELECT fgmd FROM FormGrantMatrixDraft AS fgmd";
		        
        if(module != null && !module.isEmpty()){
        	query = query + " WHERE module = :module";
        	fgmds = (List<FormGrantMatrixDraft>) sessionFactory.getCurrentSession().createQuery(query).setParameter("module", module).list();
		}
        else{
        	fgmds = (List<FormGrantMatrixDraft>) sessionFactory.getCurrentSession().createQuery(query).list();
        }
		       
		for (FormGrantMatrixDraft fgmd : fgmds) {
			String group = fgmd.getModule();
			String type = fgmd.getApplTypeCode();
			String mech = fgmd.getActivityCode();
			moduleTypeMechList.add(group + "," + type + "," + mech);
		}

		return moduleTypeMechList;
    }

    public void checkDraftModifedFlag() {
        logger.info("........checkDraftModifedFlag");
        sessionFactory.getCurrentSession().createStoredProcedureCall("Forms_pkg.flag_modified_drafts").getOutputs();
    }

	@SuppressWarnings("unchecked")
	public HashSet<String> getUpdatedOnlyTypeMechListFromDB(String module) {
		logger.info("........getUpdatedOnlyTypeMechListFromDB");
		
		HashSet<String> updatedModuleTypeMechList = new HashSet<String>();
		List<FormGrantMatrixDraft> fgmds = new ArrayList<FormGrantMatrixDraft>();
		String query = "SELECT fgmd FROM FormGrantMatrixDraft as fgmd WHERE modifiedFlag = 'Y'";

		if (module != null && !module.isEmpty()) {
			module = FilesysUtils.getRoleCodeFromModuleName(module);
			query = query + " AND module = :module";
			fgmds = sessionFactory.getCurrentSession().createQuery(query).setParameter("module", module).list();
		} else {
			fgmds = sessionFactory.getCurrentSession().createQuery(query).list();
		}

		for (FormGrantMatrixDraft fgmd : fgmds) {
			String group = fgmd.getModule();
			String type = fgmd.getApplTypeCode();
			String mech = fgmd.getActivityCode();
			String mtm = group + "," + type + "," + mech;
			updatedModuleTypeMechList.add(mtm);
		}

		return updatedModuleTypeMechList;
	}

	@SuppressWarnings("unchecked")
	public String getGreensheetDraftTemplateId(String type, String mech, String moduleName) {	
        logger.info("........getGreensheetDraftTemplateId for module:" + moduleName);

        String templateId = null;
		String query = "SELECT fgmd FROM FormGrantMatrixDraft AS fgmd WHERE formRoleCode = :formRoleCode AND applTypeCode = :applTypeCode AND activityCode = :activityCode";
		List<FormGrantMatrixDraft> fgmds = sessionFactory.getCurrentSession().createQuery(query)
				.setParameter("formRoleCode", moduleName)
				.setParameter("applTypeCode", type)
				.setParameter("activityCode", mech)
				.list();

		if(fgmds != null && !fgmds.isEmpty()) {
			templateId = Integer.toString(fgmds.get(0).getFormTemplatesDraft().getId());
			 logger.info("The new template ID: " + templateId);
		}
		
        return templateId;
    }

    public List<FormGrant> retrieveDraftGrantsByFullGrantNum(String type, String mech, String moduleName) {

        FormGrant formGrant = new FormGrant();
        List<FormGrant> formGrantsList = new ArrayList<FormGrant>();
        formGrant.setDummy("");
        formGrant.setOnControl("");
        formGrant.setElectronicallySubmitted("");
        formGrant.setApplId(1);
        formGrant.setFullGrantNum("");
        formGrant.setRfaPaNumber("");
        formGrant.setPiFirstName("Luke");
        formGrant.setPiMiddleName("");
        formGrant.setPiLastName("Walters");
        formGrant.setPiName("Walters,Luke");
        formGrant.setPdLastName("Jacobs");
        formGrant.setPdFirstName("John");
        formGrant.setBkupGmsLastName("Michaels");
        formGrant.setBkupGmsFirstName("Elijah");
        formGrant.setGmsLastName("Livingston");
        formGrant.setGmsFirstName("Matthew");
        formGrant.setIrgPercentileNum(1);
        formGrant.setPriorityScoreNum(1);
        formGrant.setApplTypeCode(type);
        formGrant.setAdminPhsOrgCode("");
        formGrant.setActivityCode(mech);
        formGrant.setSupportYear(2014);
        formGrant.setSuffixCode("");
        formGrant.setApplStatusCode("Draft");
        formGrant.setApplStatusGroupCode("");
        formGrant.setFormerNum("");
        formGrant.setFy(14);
        formGrant.setOrgName("Advanced Science Research Center");
        formGrant.setPdUserId("");
        formGrant.setGmsCode("");
        formGrant.setGmsUserId("");
        formGrant.setBkupGmsCode("");
        formGrant.setBkupGmsUserId("");
        formGrant.setAllGmsUserIds("");
        formGrant.setPgmFormStatus("");
        formGrant.setSpecFormStatus("Draft");
        formGrantsList.add(formGrant);

        return formGrantsList;
    }

	@SuppressWarnings("unchecked")
	public HashSet<String> getModuleMechListFromDB(String type, String module) {
		logger.info("........getModuleMechListFromDB");

		HashSet<String> moduleMechList = new HashSet<String>();
		List<FormGrantMatrixDraft> fgmds = new ArrayList<FormGrantMatrixDraft>();
		String query = "SELECT fgmd FROM FormGrantMatrixDraft AS fgmd";

		if (module != null && !module.isEmpty()) {
			query = query + " WHERE module = :module AND applTypeCode = :applTypeCode";
			fgmds = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("module", module)
					.setParameter("applTypeCode", type)
					.list();
		} else {
			fgmds = sessionFactory.getCurrentSession().createQuery(query).list();
		}

		for (FormGrantMatrixDraft fgmd : fgmds) {
			moduleMechList.add(fgmd.getActivityCode());
		}

		return moduleMechList;
	}

	@SuppressWarnings("unchecked")
	public HashSet<String> getModuleListFromDB() {
   		logger.info("........getModuleListFromDB");

        HashSet<String> moduleList = new HashSet<String>();
        
        String query = "SELECT DISTINCT fgmd FROM FormGrantMatrixDraft AS fgmd WHERE modifiedFlag !='Y'";
        List<FormGrantMatrixDraft> fgmds = sessionFactory.getCurrentSession().createQuery(query).list();
        
        for (FormGrantMatrixDraft fgmd : fgmds) {
        	moduleList.add(fgmd.getModule());
        }
        return moduleList;
    }
   
	@SuppressWarnings("unchecked")
	public HashSet<String> getUpdatedModuleMechListFromDB(String type, String module) {
		logger.info("........getUpdatedModuleMechListFromDB");
		HashSet<String> moduleMechList = new HashSet<String>();
		List<FormGrantMatrixDraft> fgmds = new ArrayList<FormGrantMatrixDraft>();
		String query = "SELECT fgmd FROM FormGrantMatrixDraft AS fgmd ";
		
		if (module != null && !module.isEmpty()) {
			query = query + " WHERE module = :module AND applTypeCode = :applTypeCode AND modifiedFlag = 'Y'";
			fgmds = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("module", module)
					.setParameter("applTypeCode", type)
					.list();
		} else {
			fgmds = sessionFactory.getCurrentSession().createQuery(query).list();
		}
		for (FormGrantMatrixDraft fgmd : fgmds) {
			moduleMechList.add(fgmd.getActivityCode());
		}
		return moduleMechList;
	}

	public HashSet<String> getInActiveMechTypeByModule(String module) {
    	logger.debug("getting InActiveMechType instance for module: " + module);

		HashSet<String> inActiveMechTypeList = new HashSet<String>();
		List<FormInactiveTypeMech> fitmis = findTypeMechByModule(module);

		for (FormInactiveTypeMech fitmi : fitmis) {
			String typeMech = fitmi.getId().getApplTypeCode() + "," + fitmi.getId().getActivityCode();
			inActiveMechTypeList.add(typeMech);
		}
		return inActiveMechTypeList;
	}

    private List<FormInactiveTypeMech> findTypeMechByModule(String module) {
    	logger.debug("getting FormInactiveTypeMech instance for module: " + module);
    	
		try {
			Session session = sessionFactory.getCurrentSession();
/*		    CriteriaBuilder builder = session.getCriteriaBuilder();
		    CriteriaQuery<FormInactiveTypeMech> criteria = builder.createQuery(FormInactiveTypeMech.class);
			Root<FormInactiveTypeMech> root = criteria.from(FormInactiveTypeMech.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id").get("module"), module));
			List<FormInactiveTypeMech> resultList = session.createQuery(criteria).list();
*/
			Criteria criteria = session.createCriteria(FormInactiveTypeMech.class);
		    List<FormInactiveTypeMech> resultList = criteria.add(Restrictions.like("id.module", module)).list();			
	        sessionFactory.getCurrentSession().flush();
			
			if (resultList.size() == 0) {
				logger.debug("get successful, no instance found");
			} else {				
				logger.debug("get successful, instance found");
			}
			return resultList;
		
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}