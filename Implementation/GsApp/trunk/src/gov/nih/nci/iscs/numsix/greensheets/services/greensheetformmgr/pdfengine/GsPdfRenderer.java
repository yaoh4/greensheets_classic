/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.pdfengine;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.dom4j.*;
import org.dom4j.io.*;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GsPdfRenderer {


	private Date cutOffDate;

	private GreensheetForm form;

	private GsGrant grant;

	private String commentOption;

	private String commentOptionSepPage;

	private String generateAllQuestions;

	private static final Logger logger = Logger.getLogger(GsPdfRenderer.class);

	public GsPdfRenderer(GreensheetForm form, GsGrant grant,
			String commentOption, String commentOptionSepPage,
			String generateAllQuestions) {
		this.form = form;
		this.grant = grant;
		this.commentOption = commentOption;
		this.commentOptionSepPage = commentOptionSepPage;
		this.generateAllQuestions = generateAllQuestions;
		Calendar c = Calendar.getInstance();
		c.set(2005, 2, 24);
		this.cutOffDate = c.getTime();
	}

	public byte[] generatePdf() throws GreensheetBaseException {
		byte[] result = null;
		try {

			String srcKey = null;
			if (form.getGroupType().equals(GreensheetGroupType.PGM)
					&& grant.getType().equalsIgnoreCase("5")
					|| grant.getType().equalsIgnoreCase("8")
					|| grant.getType().equalsIgnoreCase("4")) {

				if (useOldXmlSrc()) {
					srcKey = "PNC_QUESTIONS_SRC_3.23.05";
				} else {
					srcKey = "PNC_QUESTIONS_SRC";
				}
			} else if (form.getGroupType().equals(GreensheetGroupType.PGM)) {
				if (useOldXmlSrc()) {
					srcKey = "PC_QUESTIONS_SRC_3.23.05";
				} else {
					srcKey = "PC_QUESTIONS_SRC";
				}
			} else if (form.getGroupType().equals(GreensheetGroupType.SPEC)
					&& grant.getType().equalsIgnoreCase("5")
					|| grant.getType().equalsIgnoreCase("8")
					|| grant.getType().equalsIgnoreCase("4")) {

				if (useOldXmlSrc()) {
					srcKey = "SNC_QUESTIONS_SRC_3.23.05";
				} else {
					srcKey = "SNC_QUESTIONS_SRC";
				}
			} else if (form.getGroupType().equals(GreensheetGroupType.SPEC)) {
				if (useOldXmlSrc()) {
					srcKey = "SC_QUESTIONS_SRC_3.23.05";
				} else {
					srcKey = "SC_QUESTIONS_SRC";
				}
			}
			
			logger.debug("\n\n*************** SRC " + srcKey);

			File questSrc = (File) AppConfigProperties.getInstance()
					.getProperty(srcKey);
			SAXReader reader = new SAXReader();
			Document questionsXml = reader.read(questSrc);
			Document gsFormXml = this.generateGsFormXml(questionsXml);

			this.printXmlFile("generateGsFormXml.xml", gsFormXml);

			this.generatePdfReadyXml(gsFormXml);

			this.printXmlFile("pdfReadyXml.xml", gsFormXml);

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
	 * @return
	 */
	private boolean useOldXmlSrc() {
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
			Element valueElm = null;
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
						logger.debug(" selVal " + selVal);

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

	private void printXmlFile(String fileName, Document document)
			throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileWriter(fileName), format);
		writer.write(document);
		writer.close();
	}

}