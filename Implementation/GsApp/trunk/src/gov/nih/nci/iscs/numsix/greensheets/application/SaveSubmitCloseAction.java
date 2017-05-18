/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsDuplicateFormsException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsStaleDataException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Action manages the Save, Submit, and Close operations for the greensheet form.
 * 
 * @author kpuscas, Number Six Software
 */
public class SaveSubmitCloseAction extends DispatchAction {

    private static final Logger logger = Logger
            .getLogger(SaveSubmitCloseAction.class);

    public static ArrayList<String> actionGrants;

    /**
     * Method save.
     * 
     * @param mapping
     * @param aForm
     * @param req
     * @param resp
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward save(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("save() Begin");
        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and Refresh your grants list");

        } else {

            if (actionGrants == null)
                actionGrants = new ArrayList<String>();

            String id = req.getParameter(GreensheetsKeys.KEY_FORM_UID);
            String poc = req.getParameter("POC");

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(id);
            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }
            GsUser user = gus.getUser();
            GreensheetFormProxy form = gfs.getForm();
            form.setPOC(poc);
            //			GsGrant grant = gfs.getGrant(); //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            FormGrantProxy grant = gfs.getGrant();
            String grantInfo = form.getGroupTypeAsString() + "," + grant.getFullGrantNum();
            System.out.println("<<<<< The grantInfo is " + grantInfo);

            if (actionGrants.size() > 0) {

                System.out.println("<<<<< The current active grant in application are :");
                for (int i = 0; i < actionGrants.size(); i++) {
                    System.out.println("<<<<< " + actionGrants.get(i));
                }

                if (actionGrants.contains(grantInfo)) {
                    logger.info("Same Greensheet try to save/submit from diffrent window when trying to save " +
                            "a greensheet... Forwarding to the page informing the user of that.");
                    updateActionGrants("REMOVE", grantInfo);
                    return mapping.findForward("staleDataRedirect");
                }
            }

            //			GreensheetFormMgr mgr = GreensheetMgrFactory	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            //					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
            GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); //For time being -- Abdul Latheef 
            try {
                logger.info("***** Going to save the form, the grant is " + grant.getFullGrantNum());
                updateActionGrants("ADD", grantInfo);
                mgr.saveForm(form, req.getParameterMap(), user, grant);
                updateActionGrants("REMOVE", grantInfo);
            } catch (GsStaleDataException gsde) {
                // When we tried to save the greensheet we detected that someone beat us to it, 
                // so we need to tell the user who it was and when, and tell them to re-retrieve
                // the greensheet.
                req.setAttribute(GreensheetsKeys.KEY_STALEDATA_USERNAME, gsde.getUsername());
                req.setAttribute(GreensheetsKeys.KEY_STALEDATA_TIMESTAMP, gsde.getLastUpdateDateAsString());
                logger.info("Stale data (optimistic locking failure) detected when trying to save " +
                        "a greensheet... Forwarding to the page informing the user of that.");
                return mapping.findForward("staleDataRedirect");
            } catch (GsDuplicateFormsException dupeExcp) {
                return mapping.findForward("formAlreadyExistsNotification");
            }

            try {
                GreensheetActionHelper.setFormDisplayInfo(req, id);
            } catch (GreensheetBaseException e) {
                if (e.getMessage().contains("sessionTimeOut"))
                    return mapping.findForward("sessionTimeOut");
            }

            req.setAttribute(GreensheetsKeys.KEY_GRANT_ID, grant
                    .getFullGrantNumber());

            String group = form.getGroupTypeAsString();
            if (group != null) {
                group = group.trim();
            }

            if (group != null && "DM".equalsIgnoreCase(group)) {
                if (grant.isDummy()) {
                    /* Because this is a DM checklist that was retrieved through a request URL
                     * generated by GPMATS with a request parameter specifying NO appl_id and 
                     * only the full grant number (parameter name GRANT_ID), once this greensheet
                     * is saved and re-retrieved, we want the same type of request to be submitted
                     * again for the re-retrieval after the save.  Except we aren't appending more 
                     * request parameters - we are adding request attributes.  (In the retrieve
                     * greensheets action we will check not only parameters but attributes as well, 
                     * so this will work.
                    */
                    req.removeAttribute(GreensheetsKeys.KEY_APPL_ID);
                    if (req.getParameterMap().containsKey(GreensheetsKeys.KEY_APPL_ID)) {
                        req.getParameterMap().remove(GreensheetsKeys.KEY_APPL_ID);
                    }
                    logger.info("The user is saving a DM checklist for a dummy grant, so we are removing " +
                            "appl_id from request parameters and attributes for when the form data is " +
                            "re-retrieved.");
                } else {
                    /* Because, as the grant is non-dummy and the grensheet type is DM, this means
                     * that the request to retrieve this greensheet did not include the request 
                     * parameter GRANT_ID and the only included request parameter was APPL_ID, 
                     * now that we are saving this greensheet and will follow this by re-retrieving 
                     * it, we want the submitted request (to re-retrieve) to be similar in including 
                     * only the appl_id and not the full grant number in grant_id. 
                     */
                    req.removeAttribute(GreensheetsKeys.KEY_GRANT_ID);
                    if (req.getParameterMap().containsKey(GreensheetsKeys.KEY_GRANT_ID)) {
                        req.getParameterMap().remove(GreensheetsKeys.KEY_GRANT_ID);
                    }
                    req.setAttribute(GreensheetsKeys.KEY_APPL_ID, grant.getApplId());
                    logger.info("The user is saving a DM checklist for a non-dummy grant, so we are " +
                            "removing request parameters/attributes " + GreensheetsKeys.KEY_GRANT_ID +
                            " from the request to re-retrieve the form after it is saved, and are " +
                            "setting request attribute " + GreensheetsKeys.KEY_APPL_ID + ".");
                }
            }

            req.setAttribute(GreensheetsKeys.KEY_GS_GROUP_TYPE, form
                    .getGroupTypeAsString());

            req.setAttribute("TEMPLATE_ID", Integer.toString(form
                    .getTemplateId()));
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, id);
            req.setAttribute(GreensheetsKeys.KEY_AGT_ID, (grant.getActionId() == 0 ? null : grant.getActionId()));
            
            forward = "retrievegreensheet";
        }
        logger.debug("save() End");
        return mapping.findForward(forward);

    }

    /**
     * Method submit.
     * 
     * @param mapping
     * @param aForm
     * @param req
     * @param resp
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward submit(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("submit() Begin");
        if (req.getSession().isNew()) {

            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and Refresh your grants list");

        } else {

            if (actionGrants == null)
                actionGrants = new ArrayList<String>();

            String id = req.getParameter(GreensheetsKeys.KEY_FORM_UID);
            String poc = req.getParameter("POC");
            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(id);
            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }
            GsUser user = gus.getUser();
            GreensheetFormProxy form = gfs.getForm();
            form.setPOC(poc);

            //			GsGrant grant = gfs.getGrant(); //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            FormGrantProxy grant = gfs.getGrant();

            String grantInfo = form.getGroupTypeAsString() + "," + grant.getFullGrantNum();

            if (actionGrants.size() > 0) {
                System.out.println("<<<<< The current active grant in application are :");
                for (int i = 0; i < actionGrants.size(); i++) {
                    System.out.println("<<<<< " + actionGrants.get(i));
                }

                if (actionGrants.contains(grantInfo)) {
                    logger.info("Same Greensheet try to save/submit from diffrent window when trying to submit " +
                            "a greensheet... Forwarding to the page informing the user of that.");
                    updateActionGrants("REMOVE", grantInfo);
                    return mapping.findForward("staleDataRedirect");
                }
            }

            //			GreensheetFormMgr mgr = GreensheetMgrFactory	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            //					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
            GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); //For time being -- Abdul Latheef

            try {
                if (grant.getFullGrantNum() != null)
                    logger.info("***** Going to submit the form, the grant is " + grant.getFullGrantNum());
                updateActionGrants("ADD", grantInfo);
                mgr.submitForm(form, req.getParameterMap(), user, grant);
                updateActionGrants("REMOVE", grantInfo);
            } catch (GsStaleDataException gsde) {
                // When we tried to submitthe greensheet we detected that someone beat us to saving it, 
                // so we need to tell the user who it was and when, and tell them to re-retrieve
                // the greensheet.
                req.setAttribute(GreensheetsKeys.KEY_STALEDATA_USERNAME, gsde.getUsername());
                req.setAttribute(GreensheetsKeys.KEY_STALEDATA_TIMESTAMP, gsde.getLastUpdateDateAsString());
                logger.info("Stale data (optimistic locking failure) detected when trying to submit " +
                        "a greensheet... Forwarding to the page informing the user of that.");
                return mapping.findForward("staleDataRedirect");
            } catch (GsDuplicateFormsException dupeExcp) {
                return mapping.findForward("formAlreadyExistsNotification");
            }

            try {
                req.setAttribute(GreensheetsKeys.KEY_ACTION_CONFIRM_MESSAGE,
                        "The Greensheet Form for Grant "
                                + gus.getFormSessionGrant(id).getFullGrantNumber()
                                + " has been submitted");
            } catch (NullPointerException e) {

                return mapping.findForward("error");
            }

            gus.removeGreensheetFormSession(id);
        }
        logger.debug("submit() End");
        return (mapping.findForward("actionConfirm"));

    }

    /**
     * Method close.
     * 
     * @param mapping
     * @param aForm
     * @param req
     * @param resp
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward close(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("close() Begin");
        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and Refresh your grants list");

        } else {

            String id = req.getParameter(GreensheetsKeys.KEY_FORM_UID);

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            gus.removeGreensheetFormSession(id);

            forward = "closeDialog";
        }
        logger.debug("close() End");
        return mapping.findForward(forward);

    }

    synchronized void updateActionGrants(String action, String grantInfo) {

        if (action.equalsIgnoreCase("REMOVE")) {
            actionGrants.remove(grantInfo);
        } else if (action.equalsIgnoreCase("ADD")) {
            actionGrants.add(grantInfo);
        } else if (action.equalsIgnoreCase("CLEAR")) {
            actionGrants.clear();
        }

    }

}
