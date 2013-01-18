package gov.nih.nci.iscs.numsix.greensheets.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetPreferencesAction extends DispatchAction {

	private static final Logger logger = Logger
			.getLogger(GreensheetPreferencesAction.class);

	public ActionForward findAttachments(ActionMapping mapping,
			ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		logger.debug("In Method QuestionAttachmentsAction:findAttachments()");

		String forward = "attachments";

		return mapping.findForward(forward);

	}

	public ActionForward attachFile(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		logger.debug("In Method QuestionAttachmentsAction:attachFile()");

		String forward = "attachments";

		return mapping.findForward(forward);

	}

}
