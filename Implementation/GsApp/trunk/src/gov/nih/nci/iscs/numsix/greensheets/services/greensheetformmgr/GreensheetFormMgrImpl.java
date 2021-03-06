/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
//import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.pdfengine.GsPdfRenderer;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Production Implementation of GreensheetFormMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetFormMgrImpl implements GreensheetFormMgr {

	private static final Logger logger = Logger
			.getLogger(GreensheetFormMgrImpl.class);

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#findGreensheetForGrant(GsGrant,
	 *      GreensheetGroupType)
	 */
//	public GreensheetFormProxy findGreensheetForGrant(GsGrant grant,
	public GreensheetFormProxy findGreensheetForGrant(FormGrantProxy grant,
			GreensheetGroupType type) throws GreensheetBaseException {
		logger.debug("findGreensheetForGrant() Begin");
		GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
		GreensheetFormProxy form = dh.getGreensheetFormForGrant(grant, type);
		logger.debug("findGreensheetForGrant() End.");
		return form;
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#saveForm(GreensheetFormProxy,
	 *      Map, GsUser)
	 */
//	public void saveForm(GreensheetFormProxy form, Map newVals, GsUser user, //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
//			GsGrant grant) throws GreensheetBaseException {
//		logger.debug("saveForm() Begin");
//		this.setAnswerResponseValues(form.getQuestionResponsDataMap(), newVals);
//		GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
//		dh.saveGreensheetFormData(form, grant, user);
//		logger.debug("saveForm() End");
//	}

	public void saveForm(GreensheetFormProxy form, Map newVals, GsUser user,
			FormGrantProxy grant) throws GreensheetBaseException {
		logger.debug("saveForm() Begin");
		this.setAnswerResponseValues(form.getQuestionResponsDataMap(), newVals);
		GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
		dh.saveGreensheetFormData(form, grant, user);
		logger.debug("saveForm() End");
	}	
	
	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#submitForm(GreensheetFormProxy,
	 *      Map, GsUser)
	 */
	public void submitForm(GreensheetFormProxy form, Map newVals, GsUser user,
			FormGrantProxy grant) throws GreensheetBaseException {
//			GsGrant grant) throws GreensheetBaseException {	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
		logger.debug("submitForm() Begin");
		this.setAnswerResponseValues(form.getQuestionResponsDataMap(), newVals);
		GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
		dh.saveGreensheetFormData(form, grant, user);
		dh.changeGreensheetFormStatus(form, GreensheetStatus.SUBMITTED, user);
		logger.debug("submitForm() End");
		/**
		 * commented out the following invocation b/c combined method with above -
		 * ghh 3/9/06 dh.setGreensheetFormSubmitter(form, user);
		 */
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#changeLock(GreensheetFormProxy,
	 *      GsUser)
	 */
	public boolean changeLock(GreensheetFormProxy form, GsUser user)
			throws GreensheetBaseException {
		
		boolean status = false;
		logger.debug("changeLock() Begin");
		GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
		GreensheetStatus newStatus = null;
		if (form.getStatus().equals(GreensheetStatus.SUBMITTED)) {
			newStatus = GreensheetStatus.UNSUBMITTED;
		} else if (form.getStatus().equals(GreensheetStatus.UNSUBMITTED)) {
			newStatus = GreensheetStatus.SUBMITTED;
		}
		if (newStatus != null) {
			status = dh.changeGreensheetFormStatus(form, newStatus, user);
		} else {
			logger.info("Cannot change the lock for 'NOT STARTED' form status. Appl ID: " + form.getApplId());
		}
		logger.debug("changeLock() End");
		
		return status;
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#getQuestionAttachmentData(String)
	 */
	public void getQuestionAttachmentData(QuestionAttachment qa)
			throws GreensheetBaseException {
		logger.debug("getQuestionAttachmentData() Begin");
		AttachmentHelper ah = new AttachmentHelper();
		ah.getQuestionAttachmentData(qa);
		logger.debug("getQuestionAttachmentData() End");
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr#getGreensheetFormAsPFD()
	 */
//	public byte[] getGreensheetFormAsPdf(GreensheetFormProxy form, GsGrant grant,	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
	public byte[] getGreensheetFormAsPdf(GreensheetFormProxy form, FormGrantProxy grant,
			String commentOption, String commentOptionSepPage,
			String generateAllQuestions) throws GreensheetBaseException {
		logger.debug("getGreensheetFormAsPdf() Begin");
		GsPdfRenderer rend = new GsPdfRenderer(form, grant, commentOption,
				commentOptionSepPage, generateAllQuestions);
		// TODO: couldn't this ^^ be changed to obtain the Renderer instance from the 
		// main Spring-managed application context, then before calling .generatePdf()
		// on it, call another init-type method to set all these values in it? 
		// The advantage of having Spring build you a GsPdfRenderer is in that it would
		// inject into it the service-provider object it needs, and all of its 
		// dependencies.
		
		logger.debug("getGreensheetFormAsPdf() End");
		return rend.generatePdf();
	}

	private void setAnswerResponseValues(Map questionResponseDataMap,
			Map newValues) {

		// Go through all the oldValues and check to see if the responseDefId is
		// in
		// the newValues Map as a key. If not assume it was changed and should
		// be deleted
		// from the questionResponseDataMap of existing values. Set the
		// selectionDefId to ""
		logger.debug("setAnswerResponseValues() Begin");
		Iterator qrIter = questionResponseDataMap.values().iterator();
		while (qrIter.hasNext()) {
			QuestionResponseData qrd = (QuestionResponseData) qrIter.next();
			String rId = qrd.getResponseDefId();
			if (!newValues.containsKey(rId)) {
				logger.debug("\n\tREMOVE " + qrd.getResponseDefId()
						+ "  seldefid " + qrd.getSelectionDefId());
				qrd.setSelectionDefId("");

			}

		}

		// go through all the new values and either update existing values or
		// create new QuestionResponseData objects
		// and add them to the questionResponseDataMap for this form.
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

				logger.debug("\n\tExisting qid:" + qrd.getQuestionDefId()
						+ "  key: " + key + "  value: " + value);

			} else if (key.indexOf("_RD_") > -1) {

				String questionDefId = StringUtils.substringBeforeLast(key,
						"_RD_");

				if (key.indexOf("_DD_") > -1 && !(value.indexOf("SEL_X") > -1)) {
					qrd = new QuestionResponseData();
					qrd.setSelectResponseData(questionDefId, key,
							QuestionResponseData.DROP_DOWN, value);

				} else if (key.indexOf("_RB_") > -1) {
					qrd = new QuestionResponseData();
					qrd.setSelectResponseData(questionDefId, key,
							QuestionResponseData.RADIO, value);

				} else if (key.indexOf("_CB_") > -1) {
					qrd = new QuestionResponseData();
					qrd.setSelectResponseData(questionDefId, key,
							QuestionResponseData.CHECK_BOX, value);

				} else if (key.indexOf("_TX_") > -1
						&& !value.equalsIgnoreCase("")) {
					qrd = new QuestionResponseData();
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.TEXT, value);

				} else if (key.indexOf("_NU_") > -1
						&& !value.equalsIgnoreCase("")) {
					qrd = new QuestionResponseData();
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.NUMBER, value);

				} else if (key.indexOf("_ST_") > -1
						&& !value.equalsIgnoreCase("")) {
					qrd = new QuestionResponseData();
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.STRING, value);

				} else if (key.indexOf("_DT_") > -1
						&& !value.equalsIgnoreCase("")) {
					qrd = new QuestionResponseData();
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.DATE, value);

				} else if (key.indexOf("_FL_") > -1
						&& !value.equalsIgnoreCase("")) {

					logger
							.debug("----------------------------------------------Adding new file ???? "
									+ key);

					qrd = new QuestionResponseData();
					qrd.setFileResponseData(questionDefId, key,
							QuestionResponseData.FILE, null);

				} else if (key.indexOf("_CM_") > -1
						&& !value.equalsIgnoreCase("")) {
					qrd = new QuestionResponseData();
					qrd.setInputResponseData(questionDefId, key,
							QuestionResponseData.COMMENT, value);

				}

				if (qrd != null) {
					questionResponseDataMap.put(key, qrd);
					logger.debug("\n\tNew qid:" + qrd.getQuestionDefId()
							+ "  key: " + key + "  value: " + value);
				}
			}

		}
		logger.debug("setAnswerResponseValues() End");
	}

}
