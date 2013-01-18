/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.Map;

/**
 * Provides services for GreensheetForms
 * 
 * @author kpuscas, Number Six Software
 */
public interface GreensheetFormMgr {

    /**
     * Method findGreensheetForGrant.
     * 
     * @param grant
     * @param type
     * @return GreensheetFormProxy
     * @throws GreensheetBaseException
     */
    //	public GreensheetFormProxy findGreensheetForGrant(GsGrant grant, //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    //			GreensheetGroupType type) throws GreensheetBaseException;
    public GreensheetFormProxy findGreensheetForGrant(FormGrantProxy grant,
            GreensheetGroupType type) throws GreensheetBaseException;

    /**
     * Method saveForm.
     * 
     * @param form
     * @param newVals
     * @param user
     * @throws GreensheetBaseException
     */

    public void saveForm(GreensheetFormProxy form, Map newVals, GsUser user,
            FormGrantProxy grant) throws GreensheetBaseException;

    //			GsGrant grant) throws GreensheetBaseException;	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.

    /**
     * Method submitForm.
     * 
     * @param form
     * @param newVals
     * @param user
     * @throws GreensheetBaseException
     */
    public void submitForm(GreensheetFormProxy form, Map newVals, GsUser user,
            FormGrantProxy grant) throws GreensheetBaseException;

    //			GsGrant grant) throws GreensheetBaseException;	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.

    /**
     * Method changeLock.
     * 
     * @param form
     * @param user
     * @throws GreensheetBaseException
     */
    public void changeLock(GreensheetFormProxy form, GsUser user)
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
    //	public byte[] getGreensheetFormAsPdf(GreensheetFormProxy form, GsGrant grant,	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    //			String commentOption, String commentOptionSepPage,
    //			String generateAllQuestions) throws GreensheetBaseException;
    public byte[] getGreensheetFormAsPdf(GreensheetFormProxy form, FormGrantProxy grant,
            String commentOption, String commentOptionSepPage,
            String generateAllQuestions) throws GreensheetBaseException;
}
