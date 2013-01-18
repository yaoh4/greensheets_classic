/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.pdfengine;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsQuestionsServices;
//import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant; //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class is responsible for retrieving the question xml and adding the
 * required additional information such as the users answers and form header
 * informaiton. The Xml is then sent to the FoEngine for rendering the PDF
 *
 * @author kpuscas, Number Six Software
 */
public class GsPdfRenderer {

    private java.util.Date cutOffDate;

    private java.util.Date cutOffDate2;

    private GreensheetFormProxy form;

//    private GsGrant grant; //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    private FormGrantProxy grant; 

    private String commentOption;

    private String commentOptionSepPage;

    private String generateAllQuestions;

    private static final Logger logger = Logger.getLogger(GsPdfRenderer.class);

//    public GsPdfRenderer(GreensheetFormProxy form, GsGrant grant, //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    public GsPdfRenderer(GreensheetFormProxy form, FormGrantProxy grant,
            String commentOption, String commentOptionSepPage,
            String generateAllQuestions) {
        this.form = form;
        this.grant = grant;
        this.commentOption = commentOption;
        this.commentOptionSepPage = commentOptionSepPage;
        this.generateAllQuestions = generateAllQuestions;
        Calendar c = Calendar.getInstance();
        // This represents a hard coded date when a major change to the
        // question source occurred. Greensheets submitted or frozen before this
        // date
        // use a different set of xml source file for pdf generation.
        c.set(2005, 2, 24);
        this.cutOffDate = c.getTime();

        c.set(2005, 5, 28);
        this.cutOffDate2 = c.getTime();
        logger.debug("GsPdfRenderer - grant " + grant.getFullGrantNumber());
    }

	/**
	 * Public method that is responsible for pdf generation.
	 * 
	 * @return byte[]
	 * @throws GreensheetBaseException
	 */
	public byte[] generatePdf() throws GreensheetBaseException {
		byte[] result = null;
		try {

			String srcKey = null;
			if (form.getGroupType().equals(GreensheetGroupType.PGM)
					&& grant.getType().equalsIgnoreCase("5")
					|| grant.getType().equalsIgnoreCase("8")
					|| grant.getType().equalsIgnoreCase("4")) {

				if (useXmlSrc032305()) {
					srcKey = "PNC_QUESTIONS_SRC_3.23.05";
				}
				if (useXmlSrc052805()) {
					srcKey = "PNC_QUESTIONS_SRC_6.28.05";
				}
			} else if (form.getGroupType().equals(GreensheetGroupType.PGM)) {
				if (useXmlSrc032305()) {
					srcKey = "PC_QUESTIONS_SRC_3.23.05";
				}
				if (useXmlSrc052805()) {
					srcKey = "PC_QUESTIONS_SRC_6.28.05";
				}
			} else if (form.getGroupType().equals(GreensheetGroupType.SPEC)
					&& grant.getType().equalsIgnoreCase("5")
					|| grant.getType().equalsIgnoreCase("8")
					|| grant.getType().equalsIgnoreCase("4")) {

				if (useXmlSrc032305()) {
					srcKey = "SNC_QUESTIONS_SRC_3.23.05";
				}
				if (useXmlSrc052805()) {
					srcKey = "SNC_QUESTIONS_SRC_6.28.05";
				}
			} else if (form.getGroupType().equals(GreensheetGroupType.SPEC)) {
				if (useXmlSrc032305()) {
					srcKey = "SC_QUESTIONS_SRC_3.23.05";
				}
				if (useXmlSrc052805()) {
					srcKey = "SC_QUESTIONS_SRC_6.28.05";
				}
			}

			Document questionsXml = null;
			
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
			GreensheetsQuestionsServices greensheetsQuestionsServices = (GreensheetsQuestionsServices) context.getBean("greensheetsQuestionsServices");
			
			if (srcKey != null) {
				logger.debug("using file source " + srcKey);
				questionsXml = greensheetsQuestionsServices.getGreensheetQuestions(srcKey, false);  
			} else {
				questionsXml = greensheetsQuestionsServices.getGreensheetQuestions(String.valueOf(this.form.getTemplateId()), true);  
			}

			Document gsFormXml = this.generateGsFormXml(questionsXml);

			logger.debug("Generating the pdfReadyXml.xml file.");
			this.generatePdfReadyXml(gsFormXml);

			FoEngine fe = new FoEngine();
			result = fe.renderFormAsPDF(gsFormXml);
			if (result == null) {
				throw new GreensheetBaseException("PDF file is null");
			}
		} catch (MalformedURLException e) {
			throw new GreensheetBaseException("Error generating PDF", e);
		} catch (DocumentException e) {
			throw new GreensheetBaseException("Error generating PDF", e);
		} catch (Exception e) {
			throw new GreensheetBaseException("Error generating PDF", e);
		}
		return result;
	}



    /**
     * Checks to see if the xml source from 03/23/05 should be used. This is
     * stored in files contained in the web-inf directory
     *
     * @return
     */
    private boolean useXmlSrc032305() {
        if ((form.getSubmittedDate() == null
                && (form.getStatus().equals(GreensheetStatus.SUBMITTED) || form
                        .getStatus().equals(GreensheetStatus.FROZEN)) || (form
                .getSubmittedDate() != null && form.getSubmittedDate().before(
                cutOffDate)))) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Checks to see if the xml source from 06/28/05 should be used. This is
     * stored in files contained in the web-inf directory
     *
     * @return
     */
    private boolean useXmlSrc052805() {
        if ((form.getSubmittedDate() == null
                && (form.getStatus().equals(GreensheetStatus.SUBMITTED) || form
                        .getStatus().equals(GreensheetStatus.FROZEN)) || (form
                .getSubmittedDate() != null
                && (form.getSubmittedDate().before(cutOffDate2)) && form
                .getSubmittedDate().after(cutOffDate)))) {
            return true;
        } else {
            return false;
        }
    }

    private Document generateGsFormXml(Document doc) throws Exception {

        File xsltSrc = (File) AppConfigProperties.getInstance().getProperty(
                "FORM_XML_TRANSLATOR");

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(
                xsltSrc));

        // Set the type and mech params
        transformer.setParameter("paramType", grant.getType());
        transformer.setParameter("paramMech", grant.getMech());

        // now lets style the given document
        DocumentSource source = new DocumentSource(doc);
        DocumentResult result = new DocumentResult();
        transformer.transform(source, result);

        // return the transformed document
        Document transformedDoc = result.getDocument();

        return transformedDoc;
    }

    private void generatePdfReadyXml(Document doc) {

        // Add aditional greensheet info
        Element additionalInfoElm = doc.getRootElement().addElement(
                "AdditionalInfo");
        Element generateAllQuestions = additionalInfoElm
                .addElement("DisplayAllQuestions");
        generateAllQuestions.addText(this.generateAllQuestions);

        Element commentOption = additionalInfoElm.addElement("CommentOption");
        commentOption.addText(this.commentOption);

        Element commentOptionSepPage = additionalInfoElm
                .addElement("CommentOptionSeperatePage");
        commentOptionSepPage.addText(this.commentOptionSepPage);

        Element formTitle = additionalInfoElm.addElement("FormTitle");

        String groupType = "";
        if (form.getGroupType().equals(GreensheetGroupType.PGM)) {
            groupType = "PROGRAM";
        } else if (form.getGroupType().equals(GreensheetGroupType.SPEC)) {
            groupType = "SPECIALIST";
        } else if (form.getGroupType().equals(GreensheetGroupType.DM)) {    //Abdul Latheef: Added for the GPMATS enhancements
            groupType = "DOCUMENT MANAGEMENT";
        }

        formTitle.addText(groupType + " Greensheet Type:" + grant.getType()
                + " Mechanism: " + grant.getMech());

        Element formType = additionalInfoElm.addElement("FormType");
        formType.addText(groupType);

        Element grantNumber = additionalInfoElm.addElement("GrantNumber");
        grantNumber.addText(grant.getFullGrantNumber());
        Element submitter = additionalInfoElm.addElement("Submitter");
        if (form.getSubmittedBy() != null)
            submitter.addText(form.getSubmittedBy());

        Element submittedDate = additionalInfoElm.addElement("SubmittedDate");
        if (form.getSubmittedDate() != null)
            submittedDate.addText(form.getSubmittedDateAsString());

        Element institution = additionalInfoElm.addElement("Institution");
        institution.addText(grant.getOrgName());
        Element primarySpecialist = additionalInfoElm
                .addElement("PrimarySpecialist");
        primarySpecialist.addText(grant.getPrimarySpecialist());
        Element status = additionalInfoElm.addElement("Status");
        status.addText(form.getStatusAsString());

        Element poc = additionalInfoElm.addElement("POC");
        if (form.getPOC() != null)
            poc.addText(form.getPOC());
        Element pi = additionalInfoElm.addElement("PI");
        pi.addText(grant.getPi());
        Element lastChangedBy = additionalInfoElm.addElement("LastChangedBy");
        if (form.getChangedBy() != null)
            lastChangedBy.addText(form.getChangedBy());
        Element programDirector = additionalInfoElm
                .addElement("ProgramDirector");
        programDirector.addText(grant.getPdName());
        Element backUpSpecialist = additionalInfoElm
                .addElement("BackUpSpecialist");
        if (grant.getBackupSpecialist() != null)
            backUpSpecialist.addText(grant.getBackupSpecialist());

        // set the response values for the responsedefs
        List list = doc.selectNodes("//ResponseDef");

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Element respDefElm = (Element) iter.next();
            String respDefId = respDefElm.valueOf("@id");
            String respDefType = respDefElm.valueOf("@type");

            QuestionResponseData qrd = form
                    .getQuestionResponseDataByRespId(respDefId);
            Element userElm = null;
            if (respDefType.equalsIgnoreCase("TEXT")
                    || respDefType.equalsIgnoreCase("STRING")
                    || respDefType.equalsIgnoreCase("NUMBER")
                    || respDefType.equalsIgnoreCase("DATE")
                    || respDefType.equalsIgnoreCase("COMMENT")) {

                if (qrd != null) {
                    String inVal = qrd.getUserInputValue();
                    if (inVal != null) {
                        userElm = respDefElm.addElement("UserInput");
                        userElm.addText(inVal);
                    }

                }
            } else if (respDefType.equalsIgnoreCase("RADIO")
                    || respDefType.equalsIgnoreCase("CHECK_BOX")
                    || respDefType.equalsIgnoreCase("DROP_DOWN")) {

                List selList = respDefElm.selectNodes("./SelectionDef");

                for (Iterator iter2 = selList.iterator(); iter2.hasNext();) {
                    Element selDefElm = (Element) iter2.next();
                    String selDefId = selDefElm.valueOf("@id");
                    userElm = selDefElm.addElement("UserSelected");
                    if (qrd != null) {
                        String selVal = qrd.getUserSelectId();
                        String type = qrd.getResponseDefType();
                        // logger.debug(" selVal " + selVal);

                        if (selVal.equalsIgnoreCase(selDefId)
                                && !type
                                        .equalsIgnoreCase(QuestionResponseData.CHECK_BOX)) {
                            userElm.addText("YES");

                        } else if (!selVal.equalsIgnoreCase(selDefId)
                                && !type
                                        .equalsIgnoreCase(QuestionResponseData.CHECK_BOX)) {
                            userElm.addText("NO");

                        } else if (!selVal.equalsIgnoreCase(selDefId)
                                && type
                                        .equalsIgnoreCase(QuestionResponseData.CHECK_BOX)) {

                            if (selVal.indexOf(selDefId) > -1) {
                                userElm.addText("YES");
                            } else {
                                userElm.addText("NO");
                            }

                        }

                    }

                }

            }

        }
    }

    // Abdul Latheef: Commented out the following method.
//    private void printXmlFile(String fileName, Document document)
//            throws Exception {
//        OutputFormat format = OutputFormat.createPrettyPrint();
//        XMLWriter writer = new XMLWriter(new FileWriter(fileName), format);
//        writer.write(document);
//        writer.close();
//    }
}