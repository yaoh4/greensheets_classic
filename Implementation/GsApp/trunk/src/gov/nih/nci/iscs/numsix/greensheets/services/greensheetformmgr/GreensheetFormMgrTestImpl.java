/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
//import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;  //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.pdfengine.GsPdfRenderer;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Tests Implementation of GreensheetFormMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr *
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetFormMgrTestImpl implements GreensheetFormMgr {

	private static final Logger logger = Logger
			.getLogger(GreensheetFormMgrTestImpl.class);

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#getGreensheetFormAnswers(GreensheetFormProxy)
	 */
	public void getGreensheetFormAnswers(GreensheetFormProxy gsform)
			throws GreensheetBaseException {

		// Map map = new HashMap();
		//
		// QuestionResponseData qrd1 = new QuestionResponseData();
		// qrd1.setSelectResponseData("TESTQ_1", "TESTQ_1_RD_DD_3",
		// QuestionResponseData.DROP_DOWN, "SEL_2");
		//
		// QuestionResponseData qrd2 = new QuestionResponseData();
		// qrd2.setSelectResponseData("TESTQ_9", "TESTQ_9_RD_CB_27",
		// QuestionResponseData.CHECK_BOX, ",SEL_12,SEL_14,");
		//
		// QuestionResponseData qrd3 = new QuestionResponseData();
		// qrd3.setInputResponseData("TESTQ_10", "TESTQ_10_RD_ST_29",
		// QuestionResponseData.STRING, "Hello World Again");
		//
		// QuestionResponseData qrd4 = new QuestionResponseData();
		// QuestionAttachment qa =
		// QuestionAttachment.createExistingAttachment(
		// "testDoc.doc",
		// "C:/dev/JRun4/servers/greensheets_attachments/1R01AG020686-01/PGM/TESTQ_1");
		//
		// qrd4.setFileResponseData("TESTQ_1", "TESTQ_1_RD_FL_2",
		// QuestionResponseData.FILE, qa);
		//
		// map.put("TESTQ_1_RD_DD_3", qrd1);
		// map.put("TESTQ_9_RD_CB_27", qrd2);
		// map.put("TESTQ_10_RD_ST_29", qrd3);
		// map.put("TESTQ_1_RD_FL_2", qrd4);
		//
		// QuestionResponseData qrd5 = new QuestionResponseData();
		// qrd5.setInputResponseData("TESTQ_1", "TESTQ_3_RD_DT_9",
		// QuestionResponseData.DATE, "12/12/2002");
		//
		// QuestionResponseData qrd6 = new QuestionResponseData();
		// qrd6.setInputResponseData("TESTQ_1", "TESTQ_4_RD_TX_12",
		// QuestionResponseData.DATE, "This is some text for a text \n box");
		//
		// map.put("TESTQ_3_RD_DT_9", qrd5);
		// map.put("TESTQ_4_RD_TX_12", qrd6);
		//
		// gsform.setQuestionResponsDataMap(map);

	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#findGreensheetForGrant(GsGrant)
	 */
//	public GreensheetFormProxy findGreensheetForGrant(GsGrant grant, //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
	public GreensheetFormProxy findGreensheetForGrant(FormGrantProxy grant,
			GreensheetGroupType type) throws GreensheetBaseException {

		GreensheetFormProxy pgmForm = new GreensheetFormProxy();

		// pgmForm.setGroupType(type);
		// pgmForm.setStatus(GreensheetStatus.NOT_STARTED);
		// String templateId = type.getName() + "_" + grant.getType() +
		// grant.getMech() + "_1";
		//
		// logger.info("TEMPLATE ID For Form " + templateId);
		//
		// pgmForm.setTemplateId(templateId);

		return pgmForm;
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#saveForm(GreensheetFormProxy,
	 *      Map, GsUser)
	 */
	public void saveForm(GreensheetFormProxy form, Map newVals, GsUser user,
			FormGrantProxy grant) throws GreensheetBaseException {
//			GsGrant grant) throws GreensheetBaseException { //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
		// this.setAnswerResponseValues(form.getQuestionResponsDataMap(),
		// newVals);
		// AttachmentHelper ah = new AttachmentHelper();
		// ah.saveAttachments(form, grant);
		// form.setFormId(111);
		// form.setStatus(GreensheetStatus.SAVED);
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#submitForm(GreensheetFormProxy,
	 *      Map, GsUser)
	 */
	public void submitForm(GreensheetFormProxy form, Map newVals, GsUser user,
			FormGrantProxy grant) throws GreensheetBaseException {
//			GsGrant grant) throws GreensheetBaseException { //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
		this.saveForm(form, newVals, user, grant);

	}

	private void setAnswerResponseValues(Map questionResponseDataMap,
			Map newValues) {
		Iterator iter = newValues.keySet().iterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			String[] newValuesArray = (String[]) newValues.get(key);

			StringBuffer tmp = new StringBuffer();
			String value = null;
			QuestionResponseData qrd = null;

			if (key.indexOf("_CB_") > -1) {
				for (int i = 0; i < newValuesArray.length; i++) {
					String newVal = newValuesArray[i];
					tmp.append(newVal + ",");
					System.out.println(">>>> VALUE " + tmp.toString());
				}
				value = "," + tmp.toString();
			} else {
				value = newValuesArray[0];
			}

			if (questionResponseDataMap.containsKey(key)) {

				qrd = (QuestionResponseData) questionResponseDataMap.get(key);
				if (qrd.getInputValue() != null
						&& !qrd.getInputValue().equalsIgnoreCase(value)) {
					qrd.setInputValue(value);
				} else if (qrd.getSelectionDefId() != null
						&& !qrd.getSelectionDefId().equalsIgnoreCase(
								"," + value + ",")) {
					qrd.setSelectionDefId(value);
				}

				System.out.println("Existing qid:" + qrd.getQuestionDefId()
						+ "  key: " + key + "  value: " + value);

			} else if (key.indexOf("_RD_") > -1) {

				String questionDefId = StringUtils.chomp(key, "_RD_");
				qrd = new QuestionResponseData();
				if (key.indexOf("_DD_") > -1) {
					qrd.setSelectResponseData(questionDefId, key,
							QuestionResponseData.DROP_DOWN, value);
				} else if (key.indexOf("_RB_") > -1) {
					qrd.setSelectResponseData(questionDefId, key,
							QuestionResponseData.RADIO, value);
				} else if (key.indexOf("_CB_") > -1) {
					value = "," + value + ",";
					qrd.setSelectResponseData(questionDefId, key,
							QuestionResponseData.CHECK_BOX, value);
				} else if (key.indexOf("_TX_") > -1) {
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.TEXT, value);
				} else if (key.indexOf("_NU_") > -1) {
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.NUMBER, value);
				} else if (key.indexOf("_ST_") > -1) {
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.STRING, value);
				} else if (key.indexOf("_DT_") > -1) {
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.DATE, value);
				} else if (key.indexOf("_FL_") > -1) {
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.FILE, value);
				} else if (key.indexOf("_CM_") > -1) {
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.COMMENT, value);
				}

				questionResponseDataMap.put(key, qrd);

				System.out.println("New qid:" + qrd.getQuestionDefId()
						+ "  key: " + key + "  value: " + value);
			}

		}
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#changeLock(GreensheetFormProxy,
	 *      GsUser)
	 */
	public void changeLock(GreensheetFormProxy form, GsUser user)
			throws GreensheetBaseException {
		// if (form.getStatus().equals(GreensheetStatus.SUBMITTED)) {
		// form.setStatus(GreensheetStatus.UNLOCKED);
		// } else if (form.getStatus().equals(GreensheetStatus.UNLOCKED)) {
		// form.setStatus(GreensheetStatus.SUBMITTED);
		// } else {
		// throw new GreensheetBaseException("error.changelock");
		// }
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#findGreensheetFormByGrantNumber(String)
	 */
	public GreensheetFormProxy findGreensheetFormByGrantNumber(String grantNumber)
			throws GreensheetBaseException {
		String templateId = "1R01_CURRENT";
		GreensheetFormProxy pgmForm = new GreensheetFormProxy();
		// pgmForm.setGroupType(GreensheetGroupType.PGM);
		// pgmForm.setStatus(GreensheetStatus.NOT_STARTED);
		// pgmForm.setTemplateId(templateId);
		return pgmForm;
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#getQuestionAttachmentData(String)
	 */
	public void getQuestionAttachmentData(QuestionAttachment qa)
			throws GreensheetBaseException {
		AttachmentHelper ah = new AttachmentHelper();
		ah.getQuestionAttachmentData(qa);
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#getGreensheetFormAsPFD()
	 */
//	public byte[] getGreensheetFormAsPdf(GreensheetFormProxy form, GsGrant grant, //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
	public byte[] getGreensheetFormAsPdf(GreensheetFormProxy form, FormGrantProxy grant,
			String commentOption, String commentOptionSepPage,
			String generateAllQuestions) throws GreensheetBaseException {
		GsPdfRenderer rend = new GsPdfRenderer(form, grant, commentOption,
				commentOptionSepPage, generateAllQuestions);
		return rend.generatePdf();
	}

}
