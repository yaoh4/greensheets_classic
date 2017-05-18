package gov.nih.nci.cbiit.scimgmt.gsdb.dao.impl;

import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.*;
import gov.nih.nci.cbiit.scimgmt.gsdb.dao.GSFormsDAO;
import gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb.*;
import gov.nih.nci.cbiit.scimgmt.gsdb.model.WorkingTemplates;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.*;


@Repository
@Transactional(rollbackFor = Exception.class)
public class GSFormsDAOImpl implements GSFormsDAO {

    private static final Logger logger = Logger.getLogger(GSFormsDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public static final String WORKING_TEMPLATES_QUERY =
            "select gm.ACTIVITY_CODE as activityCode, gm.APPL_TYPE_CODE as applTypeCode, " +
            "       u.FORM_ROLE_CODE as formRoleCode, u.ID as templateId, u.CREATE_DATE as createDate, '0' as currentFlag " +
            " from ( " +
            " select distinct * from ( " +
            " select f.FORM_ROLE_CODE, t.ID, t.CREATE_DATE from FORMS_T f, FORM_TEMPLATES_T t where f.FTM_ID=t.id and f.FORM_ROLE_CODE != 'DM' " +
            " union all " +
            " select m.FORM_ROLE_CODE, t.ID, t.CREATE_DATE from FORM_GRANT_MATRIX_T m, FORM_TEMPLATES_T t where m.FTM_ID = t.id and m.FORM_ROLE_CODE != 'DM' " +
            " )) u left outer join FORM_GRANT_MATRIX_T gm on u.ID=gm.FTM_ID " +
            " ORDER BY u.CREATE_DATE, u.FORM_ROLE_CODE";

    public static final String TEMPLATE_MATRIX_QUERY =
            "select t.ACTIVITY_CODE as activityCode, t.APPL_TYPE_CODE as applTypeCode, " +
                    "       t.FORM_ROLE_CODE as formRoleCode, t.TEMPLATE_ID as templateId, t.START_DATE as createDate, " +
                    "       NVL2(m.ACTIVITY_CODE, '1', '0') as currentFlag " +
                    " from FORM_TEMPLATES_MATRIX_T t left outer join FORM_GRANT_MATRIX_T m on t.TEMPLATE_ID=m.FTM_ID " +
                    " ORDER BY t.START_DATE, t.FORM_ROLE_CODE, t.APPL_TYPE_CODE, t.ACTIVITY_CODE";

    public static final String XSD_MODULE = "gov/nih/nci/cbiit/scimgmt/gsdb/domain/oldgsfb/GsForm.xsd";

    @Override
    public List<WorkingTemplates> populateFormTemplateMatrix() throws Exception {
        logger.info("---- populateFormTemplateMatrix() started ---");
        Session session = sessionFactory.getCurrentSession();

        // First, try to retrieve the FormTemplatesMatrix records from the database ordered by
        // 'startDate', 'formRoleCode', 'applTypeCode', and 'activityCode'.  The order is important to reuse the
        // same XML content for consecutive templates.
        // If the table is empty, populate the FormTemplatesMatrix table from the existing templates content

        List<WorkingTemplates> r = _retrieveFormTemplateMatrix(session);
        if (r != null && r.size() > 0) {
            logger.info("populateFormTemplateMatrix() retrieved " + r.size() + " template matrix entries from FORM_TEMPLATES_MATRIX_T");
            return r;
        }

        logger.info("populateFormTemplateMatrix() -- FORM_TEMPLATES_MATRIX_T is empty - creating entries for all current and used templates");
        Query q = session.createNativeQuery(WORKING_TEMPLATES_QUERY, "WorkingTemplatesResult");
        List<WorkingTemplates> result = q.getResultList();

        // update applTypeCode and activityCode from templates HTML clobs
        result = _fillTypeMechInWorkingTemplates(result, session);

        List<FormTemplatesMatrix> ftms = new ArrayList<>();

        logger.info("populateFormTemplateMatrix() retrieved " + result.size() + " current and used templates");
        for (WorkingTemplates wt : result) {
            // do not create a new form template matrix entry if it is already created
            boolean found = false;

            for (FormTemplatesMatrix ftm : ftms) {
                //Duplicated record should not appear
                //Set the end date to null in this case
                if (ftm.getActivityCode().equals(wt.getActivityCode()) &&
                    ftm.getApplTypeCode().equals(wt.getApplTypeCode()) &&
                    ftm.getFormRoleCode().equals(wt.getFormRoleCode()) &&
                    ftm.getFormTemplates().getId().equals(wt.getTemplateId())) {

                    found = true;
                    logger.warn("Duplicated matrix record: " + wt.getTemplateId() + "," + wt.getFormRoleCode() + "," + wt.getApplTypeCode() + "," + wt.getActivityCode());
                    if (ftm.getEndDate() != null && wt.isCurrentFlag()) {
                        ftm.setEndDate(null);
                    }
                    break;
                }
            }
            if (!found) {
                FormTemplatesMatrix ftm = new FormTemplatesMatrix();
                ftms.add(ftm);
                ftm.setActivityCode(wt.getActivityCode());
                ftm.setApplTypeCode(wt.getApplTypeCode());
                ftm.setFormRoleCode(wt.getFormRoleCode());
                ftm.setFormTemplates(session.load(FormTemplates.class, wt.getTemplateId()));
                ftm.setStartDate(wt.getCreateDate());
                if (!wt.isCurrentFlag()) {
                    ftm.setEndDate(wt.getCreateDate());  // will be replaced with the start date of the "next" record
                                                         // with the same type/mech
                }

                // Find and reset the previous record 'endDate' if the record represents the same role/type/mech combination
                if (ftms.size() > 1) {
                    for (int i = ftms.size() - 2; i >= 0; i--) {
                        FormTemplatesMatrix previousFtm = ftms.get(i);
                        if (previousFtm != null
                                && ftm.getActivityCode().equals(previousFtm.getActivityCode())
                                && ftm.getApplTypeCode().equals(previousFtm.getApplTypeCode())
                                && ftm.getFormRoleCode().equals(previousFtm.getFormRoleCode())) {
                            previousFtm.setEndDate(ftm.getStartDate());
                            break;
                        }
                    }
                }
            }
        }

        //save the list of newly created FormTemplateMatrix entries
        logger.info("Saving template matrix entries into FORM_TEMPLATES_MATRIX_T...");
        for (FormTemplatesMatrix ftm : ftms) {
            session.persist(ftm);
        }

        logger.info("populateFormTemplateMatrix() created " + ftms.size() + " matrix entries");
        return null;
    }

    @Override
    public List<WorkingTemplates> retrieveFormTemplateMatrix() throws Exception {
        logger.info("---- populateFormTemplateMatrix() started ---");
        Session session = sessionFactory.getCurrentSession();
        return _retrieveFormTemplateMatrix(session);
    }

    private List<WorkingTemplates> _retrieveFormTemplateMatrix(Session session) {

        Query query = session.createNativeQuery(TEMPLATE_MATRIX_QUERY, "WorkingTemplatesResult");
        List<WorkingTemplates> r = query.getResultList();
        logger.info("retrieveFormTemplateMatrix() retrieved " + r.size() + " template matrix entries from FORM_TEMPLATES_MATRIX_T");
        return r;
    }

    /**
         * Get applTypeCode and activityCode from template HTML and update the WorkingTemplate attributes.
         *
         * Templates that are not current do not have appl type code and activity code defined in it's
         * Working template, so we need to extract this information from html clob
         *
         * @param usedTemplates - list of templates retrieved from FORM_TEMPLATES_T
         * @param session - current hibernate session
         * @return - updated list of templates
         */
    private List<WorkingTemplates> _fillTypeMechInWorkingTemplates(List<WorkingTemplates> usedTemplates, Session session) {
        logger.info("Fill out type/mechs for non-current templates ...");
        // Make sure that each template has the correct type/mech
        int count = 0;
        for (WorkingTemplates wt : usedTemplates) {
            if (wt.getActivityCode() == null || wt.getApplTypeCode() == null) {
                FormTemplates t = session.load(FormTemplates.class, wt.getTemplateId());
                String html = t.getTemplateHtml();
                int startIndex = html.indexOf("Greensheet Type:");
                html = html.substring(startIndex, html.indexOf("<", startIndex));
                String[] ss = html.split(" ");
                wt.setApplTypeCode(ss[2]);
                wt.setActivityCode(ss[5]);
                wt.setCurrentFlag(false);
                // clear session for each, say, 500 objects, to avoid memory overflow
                count++;
                logger.info("Processed " + count + " templates");
                if ((count % 500) == 0) {
                    logger.info("Session clear for " + count + " templates");
                    session.clear();
                }
            }
            else {
                wt.setCurrentFlag(true);
            }
        }
        logger.info("Extracted type/mech info for " + count + " records");
        return usedTemplates;
    }

    @Override
    public void populateModules() throws Exception {
        logger.info("Populate FORM_MODULE_T with modules ...");
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //Retrieve modules from db
        CriteriaQuery<FormModules> crModule = builder.createQuery(FormModules.class);
        crModule.from(FormModules.class);
        Query qModules = session.createQuery(crModule);
        List<FormModules> rModules = qModules.getResultList();
        if (rModules == null || rModules.size() == 0) {
            rModules = new ArrayList<>();
            rModules.add(new FormModules(null, null, "Program Competing",  "Program Competing", false, false, new HashSet<FormTemplates>()));
            rModules.add(new FormModules(null, null, "Program Non Competing", "Program Non Competing", false, false, new HashSet<FormTemplates>()));
            rModules.add(new FormModules(null, null, "Specialist Competing",  "Specialist Competing", false, false, new HashSet<FormTemplates>()));
            rModules.add(new FormModules(null, null, "Specialist Non Competing", "Specialist Non Competing", false, false, new HashSet<FormTemplates>()));
            rModules.add(new FormModules(null, null, "Revision", "Revision", false, false, new HashSet<FormTemplates>()));

            for (FormModules m : rModules) {
                session.persist(m);
            }
            logger.info("Added 4 modules to FORM_MODULE_T");
        }
        else {
            logger.info("FORM_MODULE_T is up to date");
        }
    }

    /**
     * Update relationship from each forms record to created form_template_matrix record
     * @throws Exception
     */
    public void populateFormsToMatrixLinks() throws Exception {
        logger.info("populateFormsToMatrixLinks() started...");
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        //Retrieve forms from db
        CriteriaQuery<Forms> crForms = builder.createQuery(Forms.class);
        Root<Forms> root = crForms.from(Forms.class);
        crForms.where(builder.notEqual(root.get("formRoleCode"), "DM"));
        Query qForms = session.createQuery(crForms);
        List<Forms> forms = qForms.getResultList();

        //Retrieve template matrix from db
        CriteriaQuery<FormTemplatesMatrix> crTemplateMatrix = builder.createQuery(FormTemplatesMatrix.class);
        crTemplateMatrix.from(FormTemplatesMatrix.class);
        Query qTemplateMatrix = session.createQuery(crTemplateMatrix);
        List<FormTemplatesMatrix> matrixes = qTemplateMatrix.getResultList();
        int count = 0;
        for (Forms f : forms) {
            if (f.getFormTemplatesMatrix() == null) {
                for (FormTemplatesMatrix m : matrixes) {
                    if (f.getFormTemplates() != null && f.getFormTemplates().getId() == m.getFormTemplates().getId()) {
                        f.setFormTemplatesMatrix(m);
//                        m.getFormses().add(f);
                        break;
                    }
                }
                session.persist(f);
                count++;
                if (count % 1000 == 0) {
                    logger.info("Updated " + count + " forms records...");
                }
            }
        }
        logger.info("populateFormsToMatrixLinks() persisted " + count + " forms records total.");
    }


    @Override
    public int extractTemplateContent(List<WorkingTemplates> templateMatrix, int nextEntry) throws Exception {
        logger.info("-- extractTemplateContext() started with entry " + nextEntry + " ---");
        Session session = sessionFactory.getCurrentSession();
        session.clear();

        // Try to reuse the XML template because WorkingTemplates are sorted by date, so hopefully
        // the same template will be used for multiple type/mech combinations
        String currentPgmTemplateXml = null;
        String currentSpecTemplateXml = null;
        GreensheetFormElement currentPgmForm = null;
        GreensheetFormElement currentSpecForm = null;

        Map<String, List<FormElements>> mapPgmElements = new HashMap<>(); // key is "<type>,<mech>", value is a ordered list of form elements
        Map<String, List<FormElements>> mapSpecElements = new HashMap<>(); // key is "<type>,<mech>", value is a ordered list of form elements
        Set<String> invalidPgmTypeMechs = new HashSet<>(); // each entry is a "<type>,<mech>" which has invalid elements array
        Set<String> invalidSpecTypeMechs = new HashSet<>(); // each entry is a "<type>,<mech>" which has invalid elements array

        // Retrieve questions from db
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<FormQuestions> criteria = builder.createQuery(FormQuestions.class);
        criteria.from(FormQuestions.class);
        Query query = session.createQuery(criteria);
        List<FormQuestions> r = query.getResultList();
        // Convert into mapQuestions
        Map<String, List<FormQuestions>> mapQuestions = new HashMap<>(); // key is GSID, value is a list that contains all versions of question
        for (FormQuestions q : r) {
            if (!mapQuestions.containsKey(q.getGsQuestionId())) {
                mapQuestions.put(q.getGsQuestionId(), new ArrayList<>());
            }
            mapQuestions.get(q.getGsQuestionId()).add(q);
        }

        //Retrieve modules from db
        CriteriaQuery<FormModules> crModule = builder.createQuery(FormModules.class);
        crModule.from(FormModules.class);
        Query qModules = session.createQuery(crModule);
        List<FormModules> rModules = qModules.getResultList();
        HashMap<String, FormModules> mapModules = new HashMap<>(); // key is a module name
        if (rModules != null) {
            for (FormModules m : rModules) {
                mapModules.put(m.getName(), m);
            }
        }

        //Prepare JAXB parser
        JAXBContext context = JAXBContext.newInstance(gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb.ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        InputStream xsdStream = GreensheetFormElement.class.getClassLoader().getResourceAsStream(XSD_MODULE);
        URL systemURL = GreensheetFormElement.class.getClassLoader().getResource(XSD_MODULE);
        String systemID = systemURL.toString();
        StreamSource xsdSource = new StreamSource(xsdStream, systemID);
        Schema schema = sf.newSchema(xsdSource);
        unmarshaller.setSchema(schema);

        int counter = 0;  // counter of populated templates
        int skipCounter = 0;  // counter of templates which do not have XML blob
        List <FormTemplates> templatesToSave = new ArrayList<>();
        int returnedIndex = -1;
        for (int index = nextEntry; index < templateMatrix.size(); index++) {

            WorkingTemplates tMatrix = templateMatrix.get(index);

            logger.info("-- extractTemplateContext() processing matrix entry " + tMatrix.getFormRoleCode() + "," + tMatrix.getApplTypeCode() + "," + tMatrix.getActivityCode());
            // build a list of form_template_matrix entries for each template
            FormTemplates tmpl = session.get(FormTemplates.class, tMatrix.getTemplateId()); // existing template

            String xml = tmpl.getTemplateXml();
            if (xml == null || xml.isEmpty()) {
                skipCounter++;
                continue; // Template does not have an xml - just skip it because the connection between template and template_matrix already established
            }
            // Trim all garbage characters after the last tag surrounded in <> brackets
            int lastChar = xml.lastIndexOf('>');
            if (xml.length() > lastChar + 1) {
                xml = xml.substring(0, lastChar + 1);
            }

            Map<String, List<FormElements>> mapElements = null;
            Set<String> invalidTypeMechs = null;
            String formId = "";
            boolean commitTransaction = false;  // flag if we done with the current SPEC and PGM templates - save everything and exit
            if (tMatrix.getFormRoleCode().equals("PGM")) {
                if (currentPgmTemplateXml == null) {
                    currentPgmTemplateXml = xml;
                    JAXBElement<GreensheetFormElement> unmarshallObject = (JAXBElement<GreensheetFormElement>) unmarshaller.unmarshal(new StringReader(xml));
                    currentPgmForm = unmarshallObject.getValue();

                    mapPgmElements.clear();
                    invalidPgmTypeMechs.clear();
                    List<QuestionDefElement> qdefs = currentPgmForm.getGreensheetQuestions().getQuestionDef();
                    logger.info("-- extractTemplateContext() parsing for PGM " + qdefs.size() + " top questions of the new template ID=" + tMatrix.getTemplateId());
                    for (QuestionDefElement qdef : qdefs) {
                        processEachQuestionElement(tmpl.getCreateDate(), qdef, null, null, mapPgmElements, mapQuestions, invalidPgmTypeMechs);
                    }
                    calculateHierarchicalOrder(mapPgmElements);
                }
                else if (!currentPgmTemplateXml.equals(xml)) {  // hit the new XML template - save everything and exit
                    commitTransaction = true;
                }

                mapElements = mapPgmElements;
                invalidTypeMechs = invalidPgmTypeMechs;
                formId = currentPgmForm.getGreensheetQuestions().getId();
            }
            else if (tMatrix.getFormRoleCode().equals("SPEC")) {
                if (currentSpecTemplateXml == null) {
                    currentSpecTemplateXml = xml;
                    JAXBElement<GreensheetFormElement> unmarshallObject = (JAXBElement<GreensheetFormElement>) unmarshaller.unmarshal(new StringReader(xml));
                    currentSpecForm = unmarshallObject.getValue();

                    mapSpecElements.clear();
                    invalidSpecTypeMechs.clear();
                    List<QuestionDefElement> qdefs = currentSpecForm.getGreensheetQuestions().getQuestionDef();
                    logger.info("-- extractTemplateContext() parsing for SPEC " + qdefs.size() + " top questions of the new template ID=" + tMatrix.getTemplateId());
                    for (QuestionDefElement qdef : qdefs) {
                        processEachQuestionElement(tmpl.getCreateDate(), qdef, null, null, mapSpecElements, mapQuestions, invalidSpecTypeMechs);
                    }
                    calculateHierarchicalOrder(mapSpecElements);
                }
                else if (!currentSpecTemplateXml.equals(xml)) {
                    commitTransaction = true;
                }

                mapElements = mapSpecElements;
                invalidTypeMechs = invalidSpecTypeMechs;
                formId = currentSpecForm.getGreensheetQuestions().getId();
            }

            if (commitTransaction) {
                returnedIndex = index;
                break;
            }

            templatesToSave.add(tmpl);
            // populate hibernate template object with elements
            String key = tMatrix.getApplTypeCode() + "," + tMatrix.getActivityCode();
            logger.info(++counter + " form: " + formId + " for key=" + key + " and role=" + tMatrix.getFormRoleCode());

            if (invalidTypeMechs.contains(key)) {  // we need to look at this issue before going any further
                throw new Exception("Form (id=" + tMatrix.getTemplateId() + ") elements cannot be created for key=" + key + " and role=" + tMatrix.getFormRoleCode());
            }

            List<FormElements> elements = mapElements.get(key);
            FormElements firstElement = null;
            if (elements == null || elements.isEmpty()) {
                logger.error("The " + tMatrix.getFormRoleCode() + " template " + tMatrix.getTemplateId() + " does not have entries for " + key);
            }
            else {
                firstElement = elements.get(0);
                if (tmpl.getFormElementses() == null) {
                    tmpl.setFormElementses(new ArrayList<>());
                }
                for (FormElements el : elements) {
                    tmpl.getFormElementses().add(el);  // all you need is to save the template
                    el.setFormTemplates(tmpl);
                }
            }

            // Attach the template to module if needed
            if (tMatrix.isCurrentFlag() && firstElement != null) {
                //Extract module name from the first question GS Id
                String moduleName = firstElement.getFormQuestions().getGsQuestionId();
                moduleName = moduleName.substring(0, moduleName.indexOf("Q_"));
                if (mapModules.containsKey(moduleName)) {
                    tmpl.setFormModules(mapModules.get(moduleName));
                }
            }
        }


        // save all questions first
        logger.info("-- extractTemplateContext() - saving question / answers...");
        for (List<FormQuestions> questions :  mapQuestions.values()) {
            for (FormQuestions q : questions) {
                if (q.getId() == null || q.getId() == 0) {
                    //David increases the length of FORM_ANSWERS_T TEXT field to 500
                    //Still, make sure it does not exceed limitation
                    for (FormAnswers fa : q.getFormAnswerses()) {
                        if (fa.getText() != null && fa.getText().length() > 500) {
                            logger.error("WARNING: Answer text length is " + fa.getText().length() + " will be truncated.  GSID=" + q.getGsQuestionId() + ", RESP_ID=" + q.getGsResponseId() + ", ANSWERID=" + fa.getGsAnswerId() +
                                        ", Q date=" + q.getGsfbChangeDate().toString());
                            logger.error("WARNING: Answer original text:<" + fa.getText() + ">");
                            fa.setText(fa.getText().substring(0,249));
                        }
                    }
                    session.persist(q);
                }
            }
        }

        // save templates with its elements
        logger.info("-- extractTemplateContext() - skipping " + skipCounter + " forms (no xml template)");
        logger.info("-- extractTemplateContext() - saving " + templatesToSave.size() + " forms...");
        for (FormTemplates f : templatesToSave) {
            session.persist(f);
        }
        nextEntry += templatesToSave.size();

        logger.info("Completed " + nextEntry + " templates");
        return returnedIndex;
    }

    /**
     * Update the hierarchicalOrder field for "dependent" elements
     * @param mapElements
     */
    private void calculateHierarchicalOrder(Map<String, List<FormElements>> mapElements) {
        Map<Integer, Integer> mapOrder = new HashMap<>(); // map contains level as a key, next order number as a value
        int currentHierarchyLevel;

        for (Map.Entry<String, List<FormElements>> entry : mapElements.entrySet()) {
            mapOrder.clear();
            currentHierarchyLevel = 0;
            for(FormElements el : entry.getValue()) {
                // Calculate hierarchical order
                // 1. Find element level in hierarhy
                int level = 0;
                FormElements parentElement = el.getFormElements();
                while (parentElement != null) {
                    level++;
                    parentElement = parentElement.getFormElements();
                }

                // Assign the hierarchical order
                Integer hOrder = mapOrder.get(level);
                if (hOrder == null) {
                    hOrder = 0;
                } else {
                    hOrder++;
                }
                mapOrder.put(level, hOrder);

                el.setHierarchicalOrder(hOrder);
                // if we are one level up clean up the current level of the level we were on previous step
                if (level < currentHierarchyLevel) {
                    for (int i = level+1; i <= currentHierarchyLevel; i++) {
                        mapOrder.remove(i);
                    }
                }
                currentHierarchyLevel = level;  //remember the level we are on.
            }
        }
    }

    /**
     * Recursive method to go through hierarchical structure of XML template and collect list of elements for each template
     * and map of all questions
     *
     * @param createDate  - template create date
     * @param xmlQuestion    - template current question
     * @param parentXmlQuestion - parent question in hierarchy or null if this is a top level question
     * @param parentXmlAnswer - answer of parent question in hierarchy the current question depends on or null
     * @param parentElements - collector of lists of elements for each type/mech combination
     * @param mapQuestions - collector of all questions with possible answers
     * @param invalidTypeMechs - collector of typemechs which encounter error during elements construction
     * @throws Exception - throws Exception with error text
     */
    private void processEachQuestionElement(Date createDate, QuestionDefElement xmlQuestion, QuestionDefElement parentXmlQuestion, SelectionDefElement parentXmlAnswer,
                                            Map<String, List<FormElements>> parentElements,
                                            Map<String, List<FormQuestions>> mapQuestions, Set<String> invalidTypeMechs) throws Exception
    {
        FormQuestions formQuestion = null;
        boolean isNewQuestion = false;
        // each question includes a list of type/mech combinations it belongs to
        List<TypeMechElement> typemechs = xmlQuestion.getGrantTypeMechs().getTypeMech();
        for (TypeMechElement typemech : typemechs) {
            String key = typemech.getType() + "," + typemech.getMech();
            // Create or get a list of form elements for the given type/mech combination collected so far
            List<FormElements> parentFormElements = parentElements.get(key);
            if (parentFormElements == null) {
                if (parentXmlQuestion != null) { // sanity check
                    logger.trace("WARNING: " + key + " does not have top level element but child element " + xmlQuestion.getId() + " exists");
//                    invalidTypeMechs.add(key);
                    continue;
                }
                parentFormElements = new ArrayList<>();
                parentElements.put(key, parentFormElements);
            }

            // Create a new form element for the given question and add it to a list
            FormElements element = new FormElements();
            parentFormElements.add(element);
            element.setOrder(parentFormElements.size() - 1);

            //Find parent element and parent answer and connect just created element with its parent
            //The parent element is expected to have a question with the same gsQiestionID as "parentQuestion" parameter
            //and the date is the same as a template creation date
            if (parentXmlQuestion != null) {
                boolean parentExist = false;
                for (int ind = parentFormElements.size() - 2; ind >= 0; ind--) {
                    FormElements parentElement = parentFormElements.get(ind);
                    FormQuestions q = parentElement.getFormQuestions();
                    if (parentXmlQuestion.getId().equals(q.getGsQuestionId())) {
                        element.setFormElements(parentElement);
                        if (parentElement.getFormElementses() == null) {
                            parentElement.setFormElementses(new HashSet<>());
                        }
                        parentElement.getFormElementses().add(element); // add the child element reference to the parent element

                        for (FormAnswers answer : parentElement.getFormQuestions().getFormAnswerses()) {
                            if (parentXmlAnswer.getId().equals(answer.getGsAnswerId())) {
                                element.setFormAnswers(answer);
                                break;
                            }
                        }
                        // sanity check
                        if (element.getFormAnswers() == null) {
                            logger.error("ERROR: element " + xmlQuestion.getId() + " has parent question " + parentXmlQuestion.getId() + " but does not have parent answer " + parentXmlAnswer.getId());
                            invalidTypeMechs.add(key);
                        }
                        parentExist = true;
                        break;
                    }
                }
                if (!parentExist) {
                    logger.trace("WARNING: element " + xmlQuestion.getId() +  " for typemech " + key +  " doesn't have parent question " + parentXmlQuestion.getId());
                    parentFormElements.remove(element);  // do not add this element for the given type/mech elements list
                    continue;
                }
            }

            element.setRequired(true);
            element.setReadonly(false);
            element.setLearnMore(xmlQuestion.getQInstructions());
            element.setText(xmlQuestion.getQText());
            element.setGsfbChangeDate(createDate);

            //Finally, find or create a question for the element
            //But only once for all type/mech combinations
            if (formQuestion == null) {
                List<FormQuestions> listQuestions = mapQuestions.get(xmlQuestion.getId());
                if (listQuestions == null) {
                    listQuestions = new ArrayList<>();  //initialize a list for all versions of the question
                    mapQuestions.put(xmlQuestion.getId(), listQuestions);
                }

                // Create a new question if not created yet
                // Before creating a new instance of the question make sure that the previous version of the question
                // is exactly the same as the current one
                if (listQuestions.size() > 0 && isEqualQuestion(listQuestions.get(listQuestions.size() - 1), xmlQuestion)) {
                    formQuestion = listQuestions.get(listQuestions.size() - 1);
                }
                else {
                    formQuestion = new FormQuestions();
                    listQuestions.add(formQuestion);
                    isNewQuestion = true;
                }
            }
            element.setFormQuestions(formQuestion);
        }

        // Now, prepare the question
        if (formQuestion != null) {
            if (isNewQuestion) {
                formQuestion.setGsQuestionId(xmlQuestion.getId());
                formQuestion.setText(xmlQuestion.getQText());
                formQuestion.setGsfbChangeDate(createDate); // same date as template's create_date
            }
            for (ResponseDefElement response : xmlQuestion.getResponseDefsList().getResponseDef()) {
                // filter virtual COMMENT and FILE types
                if (response.getType() != ResponseTypes.COMMENT && response.getType() != ResponseTypes.FILE) {

                    if (isNewQuestion) {
                        formQuestion.setGsResponseId(response.getId());
                        formQuestion.setAnswerType(response.getType().value());

                        //Create answers if any
                        if (formQuestion.getFormAnswerses() == null) {
                            formQuestion.setFormAnswerses(new HashSet<>());
                        }
                    }
                    for (SelectionDefElement answer : response.getSelectionDef()) {
                        if (isNewQuestion) {
                            FormAnswers fa = new FormAnswers();
                            formQuestion.getFormAnswerses().add(fa);

                            fa.setGsAnswerId(answer.getId());
                            fa.setOrder(formQuestion.getFormAnswerses().size() - 1);
                            fa.setText(answer.getValue());
                            fa.setDefault_(false);
                            fa.setFormQuestions(formQuestion);
                        }
//                        fa.setValue(answer.getValue());  // do not use value as gsAnswerId is a real value
                        if (answer.getQuestionDef() != null && answer.getQuestionDef().size() > 0) {
                            for (QuestionDefElement childQuestion : answer.getQuestionDef()) {
                                processEachQuestionElement(createDate, childQuestion, xmlQuestion, answer, parentElements, mapQuestions, invalidTypeMechs);
                            }
                        }
                    }
                    break;
                }
            }
            if (xmlQuestion.getResponseDefsList().getResponseDef().size() < 3) {
                logger.trace("WARNING: Question " + xmlQuestion.getId() + " has only two responses - COMMENT and FILE");
            }
        }
    }

    private boolean isEqualQuestion(FormQuestions formQ, QuestionDefElement xmlQ) {
        if (!StringUtils.equals(formQ.getGsQuestionId(), xmlQ.getId()) ||
            !StringUtils.equals(formQ.getText(), xmlQ.getQText())) {
            return false;
        }

        for (ResponseDefElement response : xmlQ.getResponseDefsList().getResponseDef()) {
            // filter virtual COMMENT and FILE types
            if (response.getType() != ResponseTypes.COMMENT && response.getType() != ResponseTypes.FILE) {

                if (!StringUtils.equals(formQ.getGsResponseId(), response.getId()) ||
                    !StringUtils.equals(formQ.getAnswerType(), response.getType().value()) ||
                    formQ.getFormAnswerses().size() != response.getSelectionDef().size()) {
                    return false;
                }

                for (FormAnswers formA : formQ.getFormAnswerses()) {
                    // answers should be in the same order
                    if (formA.getOrder() >= response.getSelectionDef().size()) {
                        return false;
                    }

                    SelectionDefElement xmlA = response.getSelectionDef().get(formA.getOrder());
                    if (!StringUtils.equals(formA.getGsAnswerId(), xmlA.getId()) ||
                        !StringUtils.equals(formA.getText(), xmlA.getValue())) {
                        return false;
                    }
                }
                return true;
            }
        }
        // response is not found, make sure that the question does not have answers
        return formQ.getFormAnswerses().isEmpty();
    }
}
