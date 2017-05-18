package gov.nih.nci.iscs.numsix.greensheets.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.dom.DOMResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.dom4j.io.SAXReader;
import org.dom4j.util.XMLErrorHandler;
import org.w3c.dom.Node;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.cbiit.atsc.dao.ProcessNewQuestionDefsDAO;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class ProcessNewQuestionDefsServicesImpl implements ProcessNewQuestionDefsService {

    private static final Logger logger = Logger.getLogger(ProcessNewQuestionDefsServicesImpl.class);

    private ProcessNewQuestionDefsDAO processNewQuestionDefsDAO;
    private String[] extensions = new String[] { "xml" };
    private Map<String, String> moduleXML;
    private Properties p;
    private String moduleName;
    private String questionsRoot;
    private String xmlPath;
    private static String xsltPath;
    private String schemaPath;
    private static String schema;
    public HashSet<String> draftList = new HashSet<String>();
    public HashSet<String> productionList = new HashSet<String>();
    public HashSet<String> mechDynamicList = new HashSet<String>();
    public HashSet<String> updatedMechDynamicList = new HashSet<String>();

	public void initProperties() throws Exception {
        p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        questionsRoot = p.getProperty("questions.root");
        xmlPath = questionsRoot + System.getProperty("file.separator") + p.getProperty("xml.path") + System.getProperty("file.separator");
        xsltPath = questionsRoot + System.getProperty("file.separator") + p.getProperty("xslt.path") + System.getProperty("file.separator");
        schemaPath = questionsRoot + System.getProperty("file.separator") + p.getProperty("schema.path") + System.getProperty("file.separator");
        schema = getXSDFileName(schemaPath);
        
        moduleXML = new HashMap<String, String>();
        moduleXML.put("PC", "PC_Questions.xml");
        moduleXML.put("PNC", "PNC_Questions.xml");
        moduleXML.put("SC", "SC_Questions.xml");
        moduleXML.put("SNC", "SNC_Questions.xml");
    }

    @SuppressWarnings("unchecked")
    private String getXMLSourceFileName(String module) throws Exception {

		List<File> xmlFiles = (List<File>) FileUtils.listFiles(new File(xmlPath), extensions, true);

        String xmlFileName = "";
        String moduleName = module + "_Questions";
        for (int i = 0; i < xmlFiles.size(); i++) {
            String fileName = (String) xmlFiles.get(i).getName();
            if (fileName.toUpperCase().startsWith(moduleName.toUpperCase())) {
                xmlFileName = xmlFiles.get(i).getAbsolutePath();
            }
        }

        return xmlFileName;
    }

    private String getXSDFileName(String schemaPath) throws Exception {

        String xsdFileName = "";

        File folder = new File(schemaPath);
        File[] xsdFiles = folder.listFiles();

        if (xsdFiles.length > 0) {
            xsdFileName = xsdFiles[0].getAbsolutePath();
        }

        return xsdFileName;
    }

    /**
     * Check the Question Definition source XML file to make sure everything is OK.
     * 
     * @param sourceXMLfile
     * @return
     */
    public boolean validateXml(String module) throws Exception {

        initProperties();

        logger.info("the schema is: " + schema);
        module = module.toUpperCase();
        String sourceXMLfile = getXMLSourceFileName(module);

        SAXReader reader = new SAXReader(true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", schema);

        XMLErrorHandler errorHandler = new XMLErrorHandler();
        reader.setErrorHandler(errorHandler);

        boolean validationPassed = false;
        if (errorHandler.getErrors().elements().size() == 0) {
            validationPassed = true;
            logger.info("No errors encountered when parsing and checking against the schema.");
        } else {
            logger.info("Error: " + errorHandler.getErrors().elements().get(0));
            logger.info("! !   Errors encountered when parsing the document and checking it against the schema   ! !");
        }
        
        boolean checksPassed = false;
        if (validationPassed) {
            logger.info("- - - - -");
            logger.info("Beginning question-definition XML integrity checks.");
            javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory
                    .newInstance();
            javax.xml.transform.Transformer transformer = tFactory
                    .newTransformer(new javax.xml.transform.stream.StreamSource(xsltPath + "/checks.xslt"));
            logger.info("Question-definition file XML integrity checking is complete.");

            DOMResult domRes = new DOMResult();
            transformer.transform(new javax.xml.transform.stream.StreamSource(sourceXMLfile), domRes);

            Node doc = domRes.getNode();
            if (((org.w3c.dom.Document) doc).getElementsByTagName("Error").getLength() <= 0) {
                logger.info("No errors discovered.");
                checksPassed = true;
            } else {
                logger.error("  ! ! !  Integrity errors were encountered. \n\t" +
                        "Error messages are being output to the standard output device and integity_errors.log.");
                transformer.transform(new javax.xml.transform.stream.StreamSource(sourceXMLfile),
                        new javax.xml.transform.stream.StreamResult(System.out));
                transformer.transform(new javax.xml.transform.stream.StreamSource(sourceXMLfile),
                        new javax.xml.transform.stream.StreamResult(new BufferedOutputStream(
                                new FileOutputStream("/logs/integrity_errors.log"))));
            }
        }
        return validationPassed && checksPassed;
    }

    public HashSet<String> getModuleMechListFromDB(String type, String module) {
        return processNewQuestionDefsDAO.getModuleMechListFromDB(type, module);
    }

    public HashSet<String> getModuleListFromDB() {
        return processNewQuestionDefsDAO.getModuleListFromDB();
    }

    public HashSet<String> getUpdatedOnlyTypeMechListFromDB(String module) {
        return processNewQuestionDefsDAO.getUpdatedOnlyTypeMechListFromDB(module);
    }

    public void checkDraftModifedFlag() {
        processNewQuestionDefsDAO.checkDraftModifedFlag();
    }

    public void setTypeMechList(String module) {
        setDraftList(processNewQuestionDefsDAO.getModuleTypeMechListFromDraftDB(module));
        setProductionList(processNewQuestionDefsDAO.getModuleTypeMechListFromProdDB(module));
        this.setModuleName(module);
    }

    public void setTypeMechUpdatedList(String module) {
        setDraftList(processNewQuestionDefsDAO.getUpdatedOnlyTypeMechListFromDB(module));
        setProductionList(processNewQuestionDefsDAO.getModuleTypeMechListFromProdDB(module));
        this.setModuleName(module);
    }

    public void setDynamicMechList(String type, String module) {
        setMechDynamicList(processNewQuestionDefsDAO.getModuleMechListFromDB(type, module));
    }

    public void setUpdatedDynamicMechList(String type, String module) {
        setUpdatedMechDynamicList(processNewQuestionDefsDAO.getUpdatedModuleMechListFromDB(type, module));
    }

    public HashSet<String> getAdditionList(HashSet<String> inActiveMechTypeByModuleList, String module) {
        HashSet<String> additionList = new HashSet<String>();

        if(module != null) {
        	if (module.equalsIgnoreCase("PC") || module.equalsIgnoreCase("PNC")) {
        		module = "PGM";
        	} else if (module.equalsIgnoreCase("SC") || module.equalsIgnoreCase("SNC")) {
        		module = "SPEC";
        	} else if (module.equalsIgnoreCase(Constants.REVISION_TYPE)) {
        		module = Constants.REVISION_TYPE;
        	}
        }
          
        for(String typeMech: draftList) {
            String[] parts = typeMech.split(",");
            typeMech = module + "," + parts[1] + "," + parts[2];
            String tm = parts[1] + "," + parts[2];
            if (!productionList.contains(typeMech)) {
                logger.info("Found one in draft but not in Prod, it's " + typeMech);
                additionList.add(typeMech);
            } 
            // if typeMech is already in Prod table (because OGA never remove any type/mech from production), and it is in InActiveMechTypeTable, then add it to additonlist
            else if(inActiveMechTypeByModuleList.contains(tm)){ 
                logger.info("Found one not in Prod but in inactivelist, it's " + typeMech);
                additionList.add(typeMech);       
            }
        }
     
        return additionList;
    }

    public HashSet<String> getDeletionList(HashSet<String> inActiveMechTypeByModuleList, String module) {

        HashSet<String> deletionList = new HashSet<String>();
        
        if(module != null) {
        	for(String typeMech : productionList) {
        		String[] parts = typeMech.split(",");
        		typeMech = module + "," + parts[1] + "," + parts[2];
        		if (!draftList.contains(typeMech.trim())) {
        			deletionList.add(typeMech);
        		}
        	}
        }

        return adjustDeletionList(deletionList, inActiveMechTypeByModuleList, module);
    }

    public HashSet<String> getInActiveMechTypeByModule(String module) {
        return processNewQuestionDefsDAO.getInActiveMechTypeByModule(module);
    }

    private HashSet<String> adjustDeletionList(HashSet<String> deletionList, HashSet<String> inActiveListByModule, String module) {

		if (inActiveListByModule != null && !inActiveListByModule.isEmpty()) {
			for(String typeMech : inActiveListByModule) {
				typeMech = module + "," + typeMech;
				if (deletionList.contains(typeMech)) {
					deletionList.remove(typeMech);
				}
			}
		}

        return deletionList;
    }

    public ProcessNewQuestionDefsDAO getProcessNewQuestionDefsDAO() {
        return processNewQuestionDefsDAO;
    }

    public void setProcessNewQuestionDefsDAO(ProcessNewQuestionDefsDAO processNewQuestionDefsDAO) {
        this.processNewQuestionDefsDAO = processNewQuestionDefsDAO;
    }

    public HashSet<String> getDraftList() {
        return draftList;
    }

    public void setDraftList(HashSet<String> draftList) {
        this.draftList = draftList;
    }

    public HashSet<String> getProductionList() {
        return productionList;
    }

    public void setProductionList(HashSet<String> productionList) {
        this.productionList = productionList;
    }

    public HashSet<String> getMechDynamicList() {
        return mechDynamicList;
    }

    public void setMechDynamicList(HashSet<String> mechDynamicList) {
        this.mechDynamicList = mechDynamicList;
    }

    public String getGreensheetDraftTemplateId(String type, String mech, String moduleName) {
        String templateId = null;
        templateId = processNewQuestionDefsDAO.getGreensheetDraftTemplateId(type, mech, moduleName);
        return templateId;
    }

    public List<FormGrant> retrieveDraftGrantsByFullGrantNum(String type, String mech, String moduleName) {
       return processNewQuestionDefsDAO.retrieveDraftGrantsByFullGrantNum(type, mech, moduleName);
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public HashSet<String> getUpdatedMechDynamicList() {
        return updatedMechDynamicList;
    }

    public void setUpdatedMechDynamicList(HashSet<String> updatedMechDynamicList) {
        this.updatedMechDynamicList = updatedMechDynamicList;
    }

}
