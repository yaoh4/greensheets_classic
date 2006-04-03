package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.apache.struts.actions.*;

/**
 *
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class GreensheetPreferencesAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(GreensheetPreferencesAction.class);

    public ActionForward findAttachments(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("In Method QuestionAttachmentsAction:findAttachments()");
        
        String forward = "attachments";
 
        return mapping.findForward(forward);

    }

    public ActionForward attachFile(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        logger.debug("In Method QuestionAttachmentsAction:attachFile()");
        
        String forward = "attachments";
            
         return mapping.findForward(forward);

    }


}
