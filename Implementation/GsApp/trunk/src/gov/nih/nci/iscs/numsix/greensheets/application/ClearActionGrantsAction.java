/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ClearActionGrantsAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(ClearActionGrantsAction.class);

   
   
    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String forward = "success";
        
        logger.info("<<<< Before clear the list, the size is " + SaveSubmitCloseAction.actionGrants.size());
        
        SaveSubmitCloseAction.actionGrants.clear();
        
        logger.info("<<<< After clear the list, the size is " + SaveSubmitCloseAction.actionGrants.size());

        return (mapping.findForward(forward));

    }

}
