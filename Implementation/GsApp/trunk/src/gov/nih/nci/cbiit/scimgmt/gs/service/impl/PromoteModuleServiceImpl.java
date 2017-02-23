package gov.nih.nci.cbiit.scimgmt.gs.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import gov.nih.nci.cbiit.scimgmt.gs.dao.PromoteModuleDAO;
import gov.nih.nci.cbiit.scimgmt.gs.service.PromoteModuleService;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;
import gov.nih.nci.iscs.numsix.greensheets.utils.FilesysUtils;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class PromoteModuleServiceImpl implements PromoteModuleService {

    private static final Logger logger = Logger.getLogger(PromoteModuleServiceImpl.class);

    private PromoteModuleDAO promoteModuleDAO;
    private String[] extensions = new String[] { "xml" };
    private Properties p;
    private String questionsRoot;
    private String xmlPath;
    private static String backupPath;

    private static ProcessNewQuestionDefsService processNewQuestionDefsService;
    private static EmailNotification emailHelper;
    
    public ProcessNewQuestionDefsService getProcessNewQuestionDefsService() {
        return processNewQuestionDefsService;
    }

    public void setProcessNewQuestionDefsService(
            ProcessNewQuestionDefsService processNewQuestionDefsService) {
        this.processNewQuestionDefsService = processNewQuestionDefsService;
    }

    public void setEmailHelper(EmailNotification emailHelper) {
        this.emailHelper = emailHelper;
    }
    
	public EmailNotification getEmailHelper() {
        return emailHelper;
    }
    
	public void initProperties() throws Exception {
        p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        if(p != null) {
        	questionsRoot = p.getProperty("questions.root");        
        	xmlPath = questionsRoot + System.getProperty("file.separator") + p.getProperty("xml.path") + System.getProperty("file.separator");
        	backupPath = questionsRoot + System.getProperty("file.separator") + p.getProperty("backup.path") + System.getProperty("file.separator");
        }
	}

    @SuppressWarnings("unchecked")
    private String getQuestionXMLSourceFileName(String module) throws Exception {

        String xmlFileName = "";

        if(xmlPath == null || extensions == null){
        	return xmlFileName;
        }
        
        List<File> xmlFiles = (List<File>) FileUtils.listFiles(new File(xmlPath), extensions, true);
        String moduleName = module + "_Questions";
        for (int i = 0; i < xmlFiles.size(); i++) {
            String fileName = (String) xmlFiles.get(i).getName();
            if (fileName.toUpperCase().startsWith(moduleName.toUpperCase())) {
                xmlFileName = xmlFiles.get(i).getAbsolutePath();
            }
        }

        return xmlFileName;
    }

    @SuppressWarnings("unchecked")
    private String getModuleXMLSourceFileName(String module) throws Exception {

        String xmlFileName = "";

        if(xmlPath == null || extensions == null){
        	return xmlFileName;
        }
        
        List<File> xmlFiles = (List<File>) FileUtils.listFiles(new File(xmlPath), extensions, true);
        String moduleName = "Module_" + module;
        for (int i = 0; i < xmlFiles.size(); i++) {
            String fileName = (String) xmlFiles.get(i).getName();
            if (fileName.toUpperCase().startsWith(moduleName.toUpperCase())) {
                xmlFileName = xmlFiles.get(i).getAbsolutePath();
            }
        }

        return xmlFileName;
    }
    
    public boolean promoteDraftGreensheets(String module) {
        
        try {
            initProperties();
            promoteModuleDAO.promoteDraftGreensheets(module);

            module = FilesysUtils.getRoleCodeFromModuleName(module);   
            HashSet<String> inActiveMechTypeByModule = processNewQuestionDefsService.getInActiveMechTypeByModule(module);
            HashSet<String> deletions = processNewQuestionDefsService.getDeletionList(inActiveMechTypeByModule, module);            
            HashSet<String> additions = processNewQuestionDefsService.getAdditionList(inActiveMechTypeByModule, module);            
            
            if(!additions.isEmpty()){
            	this.adjustAdditionList(additions, inActiveMechTypeByModule, module) ;
            }
            
            if (!deletions.isEmpty()) {
                this.loadInactiveMechType(module, deletions);
            }

            logger.info("Confirmation of Successful Promotion of Draft Greensheets.");
            
            this.backupFile(new File(getQuestionXMLSourceFileName(module)));
            this.backupFile(new File(getModuleXMLSourceFileName(module)));

            logger.info("Confirmation of Successful Backing up the files.");

            if (emailHelper != null) {
                String subject = "Confirmation of Successful Promotion of Draft Greensheets";
                String content = "The " + module + " Draft Greensheets are successfully promoted to Production.";
                String action = "promoteDraft";
                emailHelper.sendPostProcessEmail(subject, content, "", module, action);
            }
            
            logger.info("Confirmation of Successful Sending emails.");

        } catch (Exception e) {
        	if (emailHelper != null) {
        		String subject = "Unsuccessful Promotion of the DRAFT Greensheets";
        		String content = "Thank you for your request. The request did not go through and it resulted in errors. Please contact the System administrator for assistance.";
        		String action = "promoteDraft";
        		emailHelper.sendPostProcessEmail(subject, content, "", module, action);
        	}
        	logger.error(" Can not promote draft greensheets for module " + module, e);
        	
        	return false;
        }
        
        return true;
    }

	private HashSet<String> adjustAdditionList(HashSet<String> additionList, HashSet<String> inActiveListByModule, String m) {
		if (additionList != null && !additionList.isEmpty()) {
			for (String typeMech : additionList) {
				String[] parts = typeMech.split(",");
			    typeMech = parts[1] + "," + parts[2];
			    if (inActiveListByModule.contains(typeMech)) {
			    	logger.debug("Found one in-active type/mech in addition, remove it from in-active list," + typeMech);
			    	promoteModuleDAO.reActivedMechType(m, typeMech);
                }
			}
        }

        return additionList;
    }

    private void loadInactiveMechType(String module, HashSet<String> inActiveList) {
    	promoteModuleDAO.loadInactiveMechType(module, inActiveList);
    }
    
	private void backupFile(File srcFile) {
		logger.info("......Backup file " + srcFile.getAbsolutePath());

		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileTime = dateFormat.format(date);
		try {
			String desFileName = srcFile.getName();

			int pos = desFileName.lastIndexOf(".");
			if (pos > 0) {
				desFileName = desFileName.substring(0, pos) + "_" + fileTime + desFileName.substring(pos);

			}
			if(backupPath != null) {
				File desFile = new File(backupPath + desFileName);
				FileUtils.copyFile(srcFile, desFile);
			}
		} catch (Exception e) {
			logger.error(e);
		}
    }

    public PromoteModuleDAO getPromoteModuleDAO() {
        return promoteModuleDAO;
    }

    public void setPromoteModuleDAO(PromoteModuleDAO promoteModuleDAO) {
        this.promoteModuleDAO = promoteModuleDAO;
    }
}
