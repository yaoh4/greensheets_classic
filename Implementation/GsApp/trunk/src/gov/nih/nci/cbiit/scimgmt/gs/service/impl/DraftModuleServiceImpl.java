package gov.nih.nci.cbiit.scimgmt.gs.service.impl;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import gov.nih.nci.cbiit.scimgmt.gs.dao.FormUtilDAO;
import gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb.FormType;
import gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb.ModuleType;
import gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb.QuestionElementType;
import gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb.QuestionType;
import gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb.SkipRuleType;
import gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb.TypeMechType;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormAnswersDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormElementsDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormGrantMatrixDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormModulesDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormQuestionsDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormTemplatesDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormTemplatesMatrixDraft;
import gov.nih.nci.cbiit.scimgmt.gs.service.DraftModuleService;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

/**
 * Implementation of the interface to handle draft modules deployed from GS Form Builder.
 */
public class DraftModuleServiceImpl implements DraftModuleService {

    private static final Logger logger = Logger.getLogger(DraftModuleServiceImpl.class);

    public static final String MODULE_PREFIX = "Module_";
    public static final String MODULE_SUFFIX = ".xml";
    public static final String PROP_QUESTIONS_ROOT = "questions.root";
    public static final String PROP_XML_PATH = "xml.path";
    public static final String PROP_XSLT_PATH = "xslt.path";
    public static final String XSD_MODULE = "gov/nih/nci/cbiit/scimgmt/gs/domain/gsfb/GsModules.xsd";
    public static String gsFormTranslator = "";
    public static String gsFormXmlTranslator = ""; 
    public static Properties p;

	private FormUtilDAO formUtilDAO;

	public FormUtilDAO getFormUtilDAO() {
		return formUtilDAO;
	}

	public void setFormUtilDAO(FormUtilDAO formUtilDAO) {
		this.formUtilDAO = formUtilDAO;
	}

	@Override
    public FormModulesDraft importModuleFromXML(String module, String role_code, Map<String, FormQuestionsDraft> mapQuestions) throws GreensheetBaseException {

		p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
		gsFormTranslator = p.getProperty(PROP_QUESTIONS_ROOT) + File.separator + p.getProperty(PROP_XSLT_PATH) + File.separator + "GsFormTranslator.xslt";
		gsFormXmlTranslator = p.getProperty(PROP_QUESTIONS_ROOT) + File.separator + p.getProperty(PROP_XSLT_PATH) + File.separator + "GsFormXmlTranslator.xslt";
		
		ModuleType moduleObject =  parseModuleFromFile(module);
        return convertModuleToFormModuleDraft(moduleObject, mapQuestions, module);
    }

    public ModuleType parseModuleFromFile(String module) throws GreensheetBaseException {
        String xmlPath = p.getProperty(PROP_QUESTIONS_ROOT) + File.separator + p.getProperty(PROP_XML_PATH) + File.separator;

        String fileName = xmlPath + MODULE_PREFIX + module + MODULE_SUFFIX;

        try {
            JAXBContext context = JAXBContext.newInstance(ModuleType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            InputStream xsdStream = ModuleType.class.getClassLoader().getResourceAsStream(XSD_MODULE);
            URL systemURL = ModuleType.class.getClassLoader().getResource(XSD_MODULE);
            if (systemURL == null) {
                throw new GreensheetBaseException("Cannot find resource: " + XSD_MODULE);
            }
            String systemID = systemURL.toString();
            StreamSource xsdSource = new StreamSource(xsdStream, systemID);

            Schema schema = sf.newSchema(xsdSource);

            unmarshaller.setSchema(schema);
            @SuppressWarnings("unchecked")
			JAXBElement<ModuleType> unmarshallObject = (JAXBElement<ModuleType>) unmarshaller.unmarshal(new File(fileName));

            return unmarshallObject.getValue();

        } catch (JAXBException e) {
            String errMSg = "JAXB exception - Failed to parse " + fileName;
            logger.error(errMSg, e);
            throw new GreensheetBaseException(errMSg, e);
        } catch (SAXException e) {
            String errMSg = "SAX exception - Failed to parse " + fileName;
            logger.error(errMSg, e);
            throw new GreensheetBaseException(errMSg, e);
        } catch (Exception e) {
            String errMSg = "General exception - Failed to parse " + fileName;
            logger.error(errMSg, e);
            throw new GreensheetBaseException(errMSg, e);

        }
    }

    /**
     * Fill out the Hibernate FormModulesDraft object with ModuleType content and fill out the map of Hibernate questions.
     * @param moduleObject module object parsed from GSFB xml
     * @param mapQuestions placeholder for Hibernate question objects
     * @return filled out Hibernate FormModulesDraft object
     */
    private FormModulesDraft convertModuleToFormModuleDraft(ModuleType moduleObject, Map<String, FormQuestionsDraft> mapQuestions, String module) throws GreensheetBaseException {
    	FormModulesDraft fm = new FormModulesDraft();
		fm.setModuleUuid(moduleObject.getUuid());
		fm.setName(moduleObject.getModuleName());
		fm.setDescription(moduleObject.getDescription());
		fm.setShowDdEmptyoptionText(moduleObject.isShowPleaseSelectOptionInDropDown());
		fm.setShowCheckAll(moduleObject.isInsertCheckAllThatApplyForMultiSelectAnswers());

        Set<FormTemplatesDraft> formDrafts = new HashSet<>();
        fm.setFormTemplatesDrafts(formDrafts);

        for (FormType formObject : moduleObject.getGsForms().getForm()) {
            //Add new hibernate template
            FormTemplatesDraft templateDraft = new FormTemplatesDraft();
            formDrafts.add(templateDraft);
            templateDraft.setDescription(formObject.getDescription());
            templateDraft.setGsfbCreatedUserId(formObject.getCreatedBy());
            templateDraft.setGsfbChangedUserId(formObject.getModifiedBy());
            templateDraft.setGsfbChangeDate(formObject.getModifiedOn().toGregorianCalendar().getTime());
            templateDraft.setTemplateUuid(formObject.getUuid());
        	templateDraft.setName(formObject.getName());
        	
        	List<TypeMechType> typeMechs = formObject.getTypeMechs();
        	if(typeMechs != null && !typeMechs.isEmpty()) {        		
        		templateDraft.setTemplateHtml(
            		generateHtmlTemplate(Integer.toString(typeMechs.get(0).getGrantType()),
            				formObject.getTypeMechs().get(0).getGrantMechanism(), 
            				module));
        		templateDraft.setTemplateXml(
            		generateXmlTemplate(Integer.toString(typeMechs.get(0).getGrantType()),
            				formObject.getTypeMechs().get(0).getGrantMechanism(), 
            				module));
        	}
        	else {
        		logger.error("Form " + formObject.getUuid() + " has no Type/Mech info.");
        		formDrafts.remove(templateDraft);
        		continue;
        	}
        	
            templateDraft.setRevisionNum(1);
            templateDraft.setFormModulesDraft(fm);
            
            // Add all Type/mechs hibernate entries to the curent template
            Set<FormTemplatesMatrixDraft> formTemplatesMatrixDrafts = new HashSet<>();
            templateDraft.setFormTemplatesMatrixDrafts(formTemplatesMatrixDrafts);
            for (TypeMechType typeMech : formObject.getTypeMechs()) {
                FormTemplatesMatrixDraft formTemplatesMatrixDraft = new FormTemplatesMatrixDraft();
                formTemplatesMatrixDrafts.add(formTemplatesMatrixDraft);

                formTemplatesMatrixDraft.setFormRoleCode(formObject.getFormRole().value());
                formTemplatesMatrixDraft.setApplTypeCode(String.valueOf(typeMech.getGrantType()));
                formTemplatesMatrixDraft.setActivityCode(typeMech.getGrantMechanism());
                formTemplatesMatrixDraft.setFormTemplatesDraft(templateDraft);
            }

            Set<FormGrantMatrixDraft> formGrantMatrixDrafts = new HashSet<>();
            templateDraft.setFormGrantMatrixDrafts(formGrantMatrixDrafts);
            for (TypeMechType typeMech : formObject.getTypeMechs()) {
            	FormGrantMatrixDraft formGrantMatrixDraft = new FormGrantMatrixDraft();
            	formGrantMatrixDrafts.add(formGrantMatrixDraft);
            	
            	formGrantMatrixDraft.setFormRoleCode(formObject.getFormRole().value());
            	formGrantMatrixDraft.setApplTypeCode(String.valueOf(typeMech.getGrantType()));
            	formGrantMatrixDraft.setActivityCode(typeMech.getGrantMechanism());
            	formGrantMatrixDraft.setMajorActivityCode(typeMech.getGrantMechanism().substring(0, 1));
            	formGrantMatrixDraft.setModule(module);
            	formGrantMatrixDraft.setFormTemplatesDraft(templateDraft);
            }
            
            List<FormElementsDraft>formElementsDrafts = new ArrayList<>();
            templateDraft.setFormElementsDrafts(formElementsDrafts);

            // Order all XML form elements according to order index
            List<QuestionElementType> orderedQuestionElements = new ArrayList<>();
            for (QuestionElementType questionElement : formObject.getQuestionElement()) {
                if (orderedQuestionElements.size() > questionElement.getOrder()) {
                    orderedQuestionElements.add(questionElement.getOrder(), questionElement);
                }
                else {
                    orderedQuestionElements.add(questionElement);
                }
            }

            // Put draft elements in a list so we can calculate hierarchical order later
            Map<String, FormElementsDraft> mapElements = new HashMap<>();

            // Set current order, parent element, and parent answer
            for (QuestionElementType questionElement : orderedQuestionElements) {
                FormElementsDraft formElementsDraft = new FormElementsDraft();
                formElementsDrafts.add(formElementsDraft);
                mapElements.put(questionElement.getUuid(), formElementsDraft);  // save in map to retrieve parent element later on

                formElementsDraft.setFormQuestionsDraft(retrieveQuestion(mapQuestions, questionElement));
                formElementsDraft.setElementUuid(questionElement.getUuid());  // ATTENTION - form element does not have it's own uuid - it is the same as questions uuid
                formElementsDraft.setOrder(questionElement.getOrder());
                formElementsDraft.setRequired(questionElement.isIsRequired());
                formElementsDraft.setReadonly(questionElement.isIsReadonly());
                formElementsDraft.setLearnMore(questionElement.getLearnMore());
                formElementsDraft.setText(questionElement.getDescription());
                formElementsDraft.setGsfbChangedUserId(questionElement.getModifiedBy());
                formElementsDraft.setGsfbChangeDate(questionElement.getModifiedOn().toGregorianCalendar().getTime());
                formElementsDraft.setFormTemplatesDraft(templateDraft);
                

                if (questionElement.getSkipRule() != null && questionElement.getSkipRule().getQuestionSkipRule() != null &&
                        !questionElement.getSkipRule().getQuestionSkipRule().isEmpty()) {
                    SkipRuleType.QuestionSkipRule qSkipRule = questionElement.getSkipRule().getQuestionSkipRule().get(0);
                    String parentUUID = qSkipRule.getTriggerQuestionUUID();
                    //Parent element must be already in a map since skip rules must refer only
                    //to previous questions, which are already ordered
                    FormElementsDraft parentElement = mapElements.get(parentUUID);  // must
                    if (parentElement == null) {
                        // Process error - since the question list is ordered the parent element must be already in the map
                        throw new GreensheetBaseException("Error");  //TODO
                    }
                    formElementsDraft.setFormElementsDraft(parentElement);
                    if (parentElement.getFormElementsDrafts() == null) {
                        parentElement.setFormElementsDrafts(new HashSet<FormElementsDraft>());
                    }
                    parentElement.getFormElementsDrafts().add(formElementsDraft);

                    // Set parent answer too
                    if (qSkipRule.getAnswerSkipRule() != null && !qSkipRule.getAnswerSkipRule().isEmpty()) {
                        String answerUUID = qSkipRule.getAnswerSkipRule().get(0).getAnswerValueUUID();
                        FormQuestionsDraft q = mapQuestions.get(parentUUID); // elementUUID and questionUUID are the same
                        if (q == null || answerUUID == null) {
                            throw new GreensheetBaseException("Error"); //TODO - make a real exception
                        }
                        Set<FormAnswersDraft> answers = q.getFormAnswersDrafts();
                        for (FormAnswersDraft answer : answers) {
                            if (answerUUID.equals(answer.getAnswerUuid())) {
                                formElementsDraft.setFormAnswersDraft(answer);
                                break;
                            }
                        }
                        if (formElementsDraft.getFormElementsDraft() != null && formElementsDraft.getFormAnswersDraft() == null) {
                            throw new GreensheetBaseException("Error"); //TODO - make a real exception
                        }
                    }
                }
            }

            // Calculate hierarchical order for each element
            Map<Integer, Integer> mapOrder = new HashMap<>(); // map contains level as a key, next order number as a value
            int currentHierarchyLevel = 0;

            for (FormElementsDraft formELement : formElementsDrafts) {
                // Calculate hierarchical order
                // 1. Find element level in hierarchy
                int level = 0;
                FormElementsDraft parentElement = formELement.getFormElementsDraft();
                while (parentElement != null) {
                    level++;
                    parentElement = parentElement.getFormElementsDraft();
                }

                // Assign the hierarchical order
                Integer hOrder = mapOrder.get(level);
                if (hOrder == null) {
                    hOrder = 0;
                } else {
                    hOrder++;
                }
                mapOrder.put(level, hOrder);

                formELement.setHierarchicalOrder(hOrder);
                // if we are one level up clean up the current level of the level we were on previous step
                if (level < currentHierarchyLevel) {
                    for (int i = level+1; i <= currentHierarchyLevel; i++) {
                        mapOrder.remove(i);
                    }
                }
                currentHierarchyLevel = level;  //remember the level we are on.
            }
        }
        return fm;
    }

    /**
     * Retrieves question from the form element.  If the question is not in a mapQuestions, adds the question to the map.
     * @param mapQuestions - current map of retrieved questions
     * @param questionElement - XML question element to retrieve from
     * @return retrieved question converted from XML to Hibernate type
     */
    private FormQuestionsDraft retrieveQuestion(Map<String, FormQuestionsDraft> mapQuestions, QuestionElementType questionElement) {
        if (!mapQuestions.containsKey(questionElement.getUuid())) {
            FormQuestionsDraft formQuestion = new FormQuestionsDraft();
            formQuestion.setGsQuestionId(questionElement.getGsid());
            formQuestion.setGsResponseId(questionElement.getQuestion().getAnswer().getGsid());
            formQuestion.setQuestionUuid(questionElement.getUuid());
            formQuestion.setShortName(questionElement.getQuestion().getShortName());
            formQuestion.setText(questionElement.getDescription());  //TODO - verify that XML question does not have it's own description
            formQuestion.setAnswerText(questionElement.getQuestion().getAnswer().getDescription());
            formQuestion.setDisplayStyle(questionElement.getQuestion().getAnswer().getDisplayStyle());
            formQuestion.setAnswerConstraint(questionElement.getQuestion().getAnswer().getValueConstraint());
            formQuestion.setAnswerType(questionElement.getQuestion().getAnswer().getType().value());
            formQuestion.setGsfbChangedUserId(questionElement.getQuestion().getModifiedBy());
            formQuestion.setGsfbChangeDate(questionElement.getQuestion().getModifiedOn().toGregorianCalendar().getTime());

            Set<FormAnswersDraft> formAnswersDrafts = new HashSet<>();
            formQuestion.setFormAnswersDrafts(formAnswersDrafts);
            for (QuestionType.Answer.AnswerValue answerValue : questionElement.getQuestion().getAnswer().getAnswerValue()) {
                FormAnswersDraft formAnswersDraft = new FormAnswersDraft();
                formAnswersDrafts.add(formAnswersDraft);
                formAnswersDraft.setFormQuestionsDraft(formQuestion);
                formAnswersDraft.setGsAnswerId(answerValue.getGsid());
                formAnswersDraft.setAnswerUuid(answerValue.getUuid());
                formAnswersDraft.setOrder(answerValue.getOrder());
                formAnswersDraft.setText(answerValue.getLabel());
                formAnswersDraft.setValue(answerValue.getLabel()); //TODO - verify
                formAnswersDraft.setDefault_(answerValue.isIsDefault());
                formAnswersDraft.setFormQuestionsDraft(formQuestion);
            }

            mapQuestions.put(questionElement.getUuid(), formQuestion);
        }
        return mapQuestions.get(questionElement.getUuid());
    }

	@Override
	public <T> List<T> findByUniqueId(Class<T> clazz, String columnName, String uuid) {
		return formUtilDAO.findByUniqueId(clazz, columnName, uuid);
	}
	
	@Override
	public void deleteModule(Object formObj, Integer id) throws GreensheetBaseException {
		formUtilDAO.delete(formObj, id);
		formUtilDAO.deleteQuestions();
	}
	
	@Override
	public void saveModule(FormModulesDraft fmd, Map<String, FormQuestionsDraft> fqdMap) throws GreensheetBaseException {
		for (FormQuestionsDraft formQuestionsDraft : fqdMap.values()) {
			formUtilDAO.persist(formQuestionsDraft);
		}
		
		formUtilDAO.persist(fmd);
	}
	
	private String generateHtmlTemplate(String type, String mech, String module) throws GreensheetBaseException {
       String questionsSrcXml = p.getProperty(PROP_QUESTIONS_ROOT) + File.separator + p.getProperty(PROP_XML_PATH) + File.separator + module + "_Questions.xml";
		StringWriter htmlTemplate = new StringWriter();
        
        try {
        	javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
        	javax.xml.transform.Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(gsFormTranslator));
        	transformer.setParameter("paramType", type);
        	transformer.setParameter("paramMech", mech);
        	transformer.setParameter("paramValidation", "true");
        	transformer.setParameter("paramGenerateVelocityStrings", "true");
	       
        	transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml), new javax.xml.transform.stream.StreamResult(htmlTemplate));
        }
        catch (Exception e) {
        	logger.error("Error generating Html Template.", e);
	    	throw new GreensheetBaseException(e);
        }
        
        return htmlTemplate.toString();
	}
	
	private String generateXmlTemplate(String type, String mech, String module) throws GreensheetBaseException {
		String questionsSrcXml = p.getProperty(PROP_QUESTIONS_ROOT) + File.separator + p.getProperty(PROP_XML_PATH) + File.separator + module + "_Questions.xml";
		StringWriter xmlTemplate = new StringWriter();

	    try {
	    	javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tFactory.newTransformer(new StreamSource(gsFormXmlTranslator));
	    	transformer.setParameter("paramType", type);
	    	transformer.setParameter("paramMech", mech);
        
	    	transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml), new javax.xml.transform.stream.StreamResult(xmlTemplate));
	    }
	    catch (Exception e) {
	    	logger.error("Error generating XML Template.", e);
	    	throw new GreensheetBaseException(e);
	    }
		return xmlTemplate.toString();
    }
}
