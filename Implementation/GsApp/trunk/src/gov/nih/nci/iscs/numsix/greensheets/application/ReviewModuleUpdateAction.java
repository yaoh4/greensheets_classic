/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;
import gov.nih.nci.iscs.numsix.greensheets.utils.FilesysUtils;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class ReviewModuleUpdateAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(ReviewModuleUpdateAction.class);

    private static EmailNotification emailHelper; // must be static or else, even though Spring injects it

    // at web app startup, by the time execute() runs it is null because the instance of this Action 
    // class when execute() runs is a different one, not the one that was created by Spring at startup...
    // WHICH IS BECAUSE WE DON'T HAVE STRUTS1-SPRING INTEGRATION SET UP AT ALL!

    static ProcessNewQuestionDefsService processNewQuestionDefsService;

    public ProcessNewQuestionDefsService getProcessNewQuestionDefsService() {
        return processNewQuestionDefsService;
    }

    public void setProcessNewQuestionDefsService(
            ProcessNewQuestionDefsService processNewQuestionDefsService) {
        this.processNewQuestionDefsService = processNewQuestionDefsService;
    }

    public void setEmailHelper(EmailNotification emailHelper) {
        this.emailHelper = emailHelper;
    }

    public EmailNotification getEmailHelper() {
        return emailHelper;
    }

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */

	public ActionForward execute(ActionMapping mapping, ActionForm aForm, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		ActionErrors errors = new ActionErrors();
		String displayReviewPage = "success";

		HashSet<String> addtionList = null;
		HashSet<String> deletionList = null;
		ArrayList<String> addDelMsg = new ArrayList<String>();
		HashSet<String> inActiveMechTypeByModuleList = null;
		List<String> additionSplitList = new ArrayList<String>();
		List<String> deletionSplitList = new ArrayList<String>();
		ArrayList<String> additionUiList = new ArrayList<String>();
		ArrayList<String> deletionUiList = new ArrayList<String>();
		ArrayList<String> competingTypes = new ArrayList<String>();
		competingTypes.add("1");
		competingTypes.add("2");
		competingTypes.add("3");
		// competingTypes.add("4");
		competingTypes.add("6");
		competingTypes.add("7");
		competingTypes.add("9");
		ArrayList<String> nonCompetingTypes = new ArrayList<String>();
		nonCompetingTypes.add("5");
		nonCompetingTypes.add("8");
		// nonCompetingTypes.add("4");
		ReviewDraftGreensheetsForm rvwDraftForm = (ReviewDraftGreensheetsForm) aForm;
		String moduleName = rvwDraftForm.getModuleName();
		String roleCode = null;

		req.getSession().removeAttribute(GreensheetsKeys.USER_NOT_FOUND);

		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
		GsUser gsUser = gus.getUser();
		GsUserRole gsUserRole = gsUser.getRole();

		// If "role" = "Specialist"
		if (gsUserRole.equals(GsUserRole.SPEC)) {
			if (gus.getUser().getMyPortfolioIds() != null) {
				req.setAttribute("MY_PORTFOLIO_OPT", "true");
			}
			displayReviewPage = "success";
		}

		// If "role" = "Program" or "Analyst"
		if (gsUserRole.equals(GsUserRole.PGM_DIR) || gsUserRole.equals(GsUserRole.PGM_ANL)) {
			displayReviewPage = "success";
		}

		req.getSession().setAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION, gus);
		if (moduleName.equalsIgnoreCase("Program Competing")) {
			roleCode = "PC";
		} else if (moduleName.equalsIgnoreCase("Program Non Competing")) {
			roleCode = "PNC";
		} else if (moduleName.equalsIgnoreCase("Specialist Competing")) {
			roleCode = "SC";
		} else if (moduleName.equalsIgnoreCase("Specialist Non Competing")) {
			roleCode = "SNC";
		}

		processNewQuestionDefsService.setTypeMechList(roleCode);

		inActiveMechTypeByModuleList = processNewQuestionDefsService.getInActiveMechTypeByModule(roleCode);

		addtionList = processNewQuestionDefsService.getAdditionList(inActiveMechTypeByModuleList, roleCode);
		deletionList = processNewQuestionDefsService.getDeletionList(inActiveMechTypeByModuleList, roleCode);

		if (!addtionList.isEmpty()) {
			// DB returns a hashset which contains comma separated module, type/mech values
			for(String iterator : addtionList) {
				for (String s : iterator.split(",")) {
					additionSplitList.add(s.trim());
				}

			}
			for (int i = 0; i < additionSplitList.size(); i++) {
				String a = "Type ".concat(additionSplitList.get(i + 1)).concat("- Mech ")
						.concat(additionSplitList.get(i + 2));
				additionUiList.add(a);
				i = i + 2;

			}
			errors.add("AdditionList", new ActionError("rvwDraftForm.message.AdditionList", moduleName));
			saveErrors(req, errors);

			rvwDraftForm.setDisplayReviewModuleStatusButton(false);
			rvwDraftForm.setDisplayPromoteButton(true);
			rvwDraftForm.setDisplayViewGreensheetButton(false);
			rvwDraftForm.setDisplayRejectButton(true);
		}

		if (!deletionList.isEmpty()) {
			// DB returns a hashset which contains comma separated module, type
			// mech values
			for(String s : deletionList) {
				for (String split : s.split(",")) {
					deletionSplitList.add(split.trim());
				}

			}
			for (int i = 0; i < deletionSplitList.size(); i++) {
				String a = "Type ".concat(deletionSplitList.get(i + 1)).concat("- Mech ")
						.concat(deletionSplitList.get(i + 2));
				deletionUiList.add(a);
				i = i + 2;

			}
			errors.add("deletionList", new ActionError("rvwDraftForm.message.DeletionList", moduleName));
			saveErrors(req, errors);
			rvwDraftForm.setDisplayReviewModuleStatusButton(false);
			rvwDraftForm.setDisplayPromoteButton(true);
			rvwDraftForm.setDisplayViewGreensheetButton(false);
			rvwDraftForm.setDisplayRejectButton(true);
		}

		if ((addtionList.isEmpty()) && (deletionList.isEmpty())) {
			errors.add("NoAddOrDeletion", new ActionError("rvwDraftForm.message.NoAdditionOrDeletion", moduleName));
			saveErrors(req, errors);
			rvwDraftForm.setDisplayReviewModuleStatusButton(false);
			rvwDraftForm.setDisplayPromoteButton(true);
			rvwDraftForm.setDisplayViewGreensheetButton(false);
			rvwDraftForm.setDisplayRejectButton(true);
			req.setAttribute(Constants.DRAFT_ADDITION_LIST, additionUiList);
		}

		if ((!addtionList.isEmpty()) && (!deletionList.isEmpty())) {
			errors.clear();
			addDelMsg.add("1");
			errors.add("AddList", new ActionError("rvwDraftForm.message.AddList", moduleName));
			errors.add("DelList", new ActionError("rvwDraftForm.message.DelList", moduleName));
			errors.add("AddDelMsg", new ActionError("rvwDraftForm.message.AddDelMsg", moduleName));
			req.setAttribute(Constants.DRAFT_ADDITION_DELETION_LIST, addDelMsg);
		}

		req.setAttribute(Constants.DRAFT_IS_REVIEW_MODULE, "1");
		req.setAttribute(Constants.SELECTED_DRAFT_MODULE, moduleName);

		if (!additionUiList.isEmpty()) {
			req.setAttribute(Constants.DRAFT_ADDITION_LIST, additionUiList);
		}
		if (!deletionUiList.isEmpty()) {
			req.setAttribute(Constants.DRAFT_DELETION_LIST, deletionUiList);
		}
		return mapping.findForward(displayReviewPage);
	}
}
