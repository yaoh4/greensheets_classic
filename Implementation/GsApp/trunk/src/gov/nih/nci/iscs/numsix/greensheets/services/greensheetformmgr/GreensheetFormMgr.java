/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.Map;

/**
 * Provides services for GreensheetForms
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public interface GreensheetFormMgr {

	/**
	 * Method findGreensheetForGrant.
	 * 
	 * @param grant
	 * @param type
	 * @return GreensheetForm
	 * @throws GreensheetBaseException
	 */
	public GreensheetForm findGreensheetForGrant(GsGrant grant,
			GreensheetGroupType type) throws GreensheetBaseException;

	/**
	 * Method saveForm.
	 * 
	 * @param form
	 * @param newVals
	 * @param user
	 * @throws GreensheetBaseException
	 */

	public void saveForm(GreensheetForm form, Map newVals, GsUser user,
			GsGrant grant) throws GreensheetBaseException;

	/**
	 * Method submitForm.
	 * 
	 * @param form
	 * @param newVals
	 * @param user
	 * @throws GreensheetBaseException
	 */
	public void submitForm(GreensheetForm form, Map newVals, GsUser user,
			GsGrant grant) throws GreensheetBaseException;

	/**
	 * Method changeLock.
	 * 
	 * @param form
	 * @param user
	 * @throws GreensheetBaseException
	 */
	public void changeLock(GreensheetForm form, GsUser user)
			throws GreensheetBaseException;

	/**
	 * Method getQuestionAttachmentData.
	 * 
	 * @param qa
	 * @throws GreensheetBaseException
	 */
	public void getQuestionAttachmentData(QuestionAttachment qa)
			throws GreensheetBaseException;

	/**
	 * Method getGreensheetFormAsPFD.
	 */
	public byte[] getGreensheetFormAsPdf(GreensheetForm form, GsGrant grant,
			String commentOption, String commentOptionSepPage,
			String generateAllQuestions) throws GreensheetBaseException;

}
