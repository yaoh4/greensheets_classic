package gov.nih.nci.iscs.numsix.greensheets.application;

// import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory; //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionAttachment;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.net.URLConnection;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

/**
 * @author kpuscas, Number Six Software
 */
public class QuestionAttachmentsAction extends DispatchAction {

    private static final Logger logger = Logger
            .getLogger(QuestionAttachmentsAction.class);

    public ActionForward findAttachments(ActionMapping mapping,
            ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {

        logger.debug("In Method QuestionAttachmentsAction:findAttachments()");

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has timed out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {
            String respId = req.getParameter("RESP_DEF_ID");
            String formUid = req.getParameter(GreensheetsKeys.KEY_FORM_UID);

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);

            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }

            GreensheetFormProxy form = gfs.getForm();

            QuestionResponseData qrd = form
                    .getQuestionResponseDataByRespId(respId);
            logger.debug("RESP ID = " + respId);

            // Attempt to get the question attachment proxy object for this resp
            // id from form. if not, create a new one.
            QuestionAttachmentsProxy qap = gfs
                    .getQuestionAttachmentProxy(respId);
            if (qap == null) {
                qap = new QuestionAttachmentsProxy(respId);
                if (qrd != null) {
                    Object[] arr = qrd.getQuestionAttachments().values()
                            .toArray();
                    qap.initWithExistingQuestionAttachments(arr);

                    logger.debug("Initialized with existing attachments.");
                }

                // Add to the gfs.
                gfs.addAttachmentsProxy(qap, respId);
                logger
                        .debug("Added the QuestionAttachmentProxy (QAP) to GreensheetFormSession(GFS).");
            } else {
                logger
                        .debug("QuestionAttachmentProxy(QAP) exists in the GreensheetFormSession(GFS). No need to add.");
            }

            logger.debug("Number of Attachments, Response Def ID = "
                    + qap.getAttachmentCount() + ", " + respId);

            req.setAttribute("QA_PROXY", qap);
            req.setAttribute("VALID_FILE_NAMES", qap.getValidFileNames());
            req.setAttribute("RESP_DEF_ID", respId);
            req.setAttribute("NUM_OF_ATTACHMENTS", ""
                    + qap.getAttachmentCount());
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
            req.setAttribute("canEdit", new Boolean(gus.getUser().isCanEdit())
                    .toString());

            forward = "attachments";
        }
        return mapping.findForward(forward);

    }

    public ActionForward attachFile(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("In Method QuestionAttachmentsAction:attachFile()");

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            DynaActionForm form = (DynaActionForm) aForm;

            FormFile file = (FormFile) form.get("fileAttachment");

            String respDefId = (String) form.get("respDefId");
            String formUid = (String) form.get("formUid");

            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }

            QuestionAttachmentsProxy qap = gfs
                    .getQuestionAttachmentProxy(respDefId);

            if (qap == null) {
                return mapping.findForward("sessionTimeOut");
            }

            String maxSize = ((Properties) AppConfigProperties.getInstance()
                    .getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES))
                    .getProperty("file.attachment.size.limit");

            if (file.getFileSize() > Integer.parseInt(maxSize) * 1000000) {
                req.setAttribute("FILE_SIZE_ERROR", "error");
                req.setAttribute("FILE_SIZE_LIMIT", maxSize + "mb");
            } else {

                QuestionAttachment qa = QuestionAttachment
                        .createNewAttachment();
                qa.setFilename(file.getFileName());
                qa.setDocData(file.getFileData());

                qap.addQuestionAttachment(qa);
            }

            req.setAttribute("QA_PROXY", qap);
            req.setAttribute("VALID_FILE_NAMES", qap.getValidFileNames());
            req.setAttribute("RESP_DEF_ID", respDefId);

            req.setAttribute("NUM_OF_ATTACHMENTS", ""
                    + qap.getAttachmentCount());
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
            req.setAttribute("canEdit", new Boolean(gus.getUser().isCanEdit())
                    .toString());
            forward = "attachments";

            logger.debug("Number of Attachments, Response Def ID = "
                    + qap.getAttachmentCount() + ", " + respDefId);
        }
        return mapping.findForward(forward);

    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("In Method QuestionAttachmentsAction:deleteFile()");

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String fileMemoryId = (String) form.get("fileMemoryId");

            String respDefId = (String) form.get("respDefId");
            String formUid = (String) form.get("formUid");

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }

            QuestionAttachmentsProxy qap = gfs
                    .getQuestionAttachmentProxy(respDefId);

            // delete or mark the attachment for deletion
            if (qap!=null){
                if(fileMemoryId != null) {
                qap.removeAttachment(fileMemoryId);
            }// Once, a NullPtrExcp was thrown here
                req.setAttribute("QA_PROXY", qap);
                req.setAttribute("VALID_FILE_NAMES", qap.getValidFileNames());   
            }
            req.setAttribute("RESP_DEF_ID", respDefId);

            /*req.setAttribute("NUM_OF_ATTACHMENTS", ""
            		+ qap.getnewCount());*/
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
            req.setAttribute("canEdit", new Boolean(gus.getUser().isCanEdit())
                    .toString());

            logger.debug("Number of Attachments, Response Def ID = "
                    + qap.getAttachmentCount() + ", " + respDefId);

            forward = "attachments";
        }
        return mapping.findForward(forward);

    }

    public ActionForward viewFile(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("In Method QuestionAttachmentAction:viewFile()");

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");
            return mapping.findForward(forward);
        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String fileMemoryId = (String) form.get("fileMemoryId");

            String respDefId = (String) form.get("respDefId");
            String formUid = (String) form.get("formUid");

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }

            QuestionAttachmentsProxy qap = gfs
                    .getQuestionAttachmentProxy(respDefId);

            QuestionAttachment qa = null;
            if (qap != null) {
                qa = qap.getAttachment(fileMemoryId);
            }
            if (qa != null) {
                byte[] byst = null;
                if (qa.getDocData() == null) {
                    //					GreensheetFormMgr mgr = GreensheetMgrFactory //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
                    //							.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
                    GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); //For time being -- Abdul Latheef
                    mgr.getQuestionAttachmentData(qa);
                    byst = qa.getDocData();
                } else {
                    byst = qa.getDocData();
                }

                resp.reset();
                resp.resetBuffer();

                String fileName = qa.getFilename();

                String contentType = URLConnection.getFileNameMap()
                        .getContentTypeFor(fileName);

                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                resp.setContentType(contentType);
                resp.setContentLength(byst.length);

                String header = "attachment;  filename=\"" + fileName + "\";";

                if (logger.isDebugEnabled()) {
                    logger.debug("");
                }
                logger.debug("Downloading File " + fileName);
                resp.setHeader("Content-Disposition", header);

                // Send content to Browser
                resp.getOutputStream().write(byst);
                resp.getOutputStream().flush();

                req.setAttribute("QA_PROXY", qap);
                req.setAttribute("VALID_FILE_NAMES", qap.getValidFileNames());
                req.setAttribute("RESP_DEF_ID", respDefId);
                req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);

                logger.debug("Number of Attachments, Response Def ID = "
                        + qap.getAttachmentCount() + ", " + respDefId);
            }

            return null;
        }

    }

    public ActionForward cancel(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("In Method QuestionAttachmentsAction:cancel()");

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String formUid = (String) form.get("formUid");
            String respDefId = (String) form.get("respDefId");

            GreensheetUserSession gus = (GreensheetUserSession) req
                    .getSession().getAttribute(
                            GreensheetsKeys.KEY_CURRENT_USER_SESSION);

            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }

            gfs.removeQuestionAttachmentsProxy(respDefId);

            GreensheetFormProxy gform = gfs.getForm();

            try {
                GreensheetActionHelper.setFormDisplayInfo(req, formUid);
            } catch (GreensheetBaseException e) {
                if (e.getMessage().contains("sessionTimeOut"))
                    return mapping.findForward("sessionTimeOut");
            }

            req.setAttribute("TEMPLATE_ID", Integer.toString(gform
                    .getTemplateId()));
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);

            forward = "closeDialog";
        }
        return mapping.findForward(forward);

    }

    public ActionForward save(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        logger.debug("In Method QuestionAttachmentsAction:save()");

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String formUid = (String) form.get("formUid");
            String respDefId = (String) form.get("respDefId");

            GreensheetUserSession gus = (GreensheetUserSession) req
                    .getSession().getAttribute(
                            GreensheetsKeys.KEY_CURRENT_USER_SESSION);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }

            QuestionAttachmentsProxy qap = gfs
                    .getQuestionAttachmentProxy(respDefId);

            if (qap != null) {
                if (qap.getAttachmentMap() != null) {
                    logger.debug("Saving- Num of Attachments = "
                            + qap.getAttachmentMap().size());
                } else {
                    logger.debug("Saving- Num of Attachments = 0");
                }
            } else {
                logger.debug("Saving- QuestionAttachmentsProxy is null. ");
            }
            logger.debug("RespDefId = " + respDefId);

            if (qap != null) {
                if (qap.getAttachmentMap() != null) {
                    req.setAttribute("NUM_OF_ATTACHMENTS", ""
                            + qap.getAttachmentMap().size());
                } else {
                    req.setAttribute("NUM_OF_ATTACHMENTS", "0");
                }
                gfs.updateQRDQuestionAttachments(respDefId, qap.getAttachmentMap());
            } else {
                req.setAttribute("NUM_OF_ATTACHMENTS", "0");
            }

            gfs.removeQuestionAttachmentsProxy(respDefId);

            try {
                GreensheetActionHelper.setFormDisplayInfo(req, formUid);
            } catch (GreensheetBaseException e) {
                if (e.getMessage().contains("sessionTimeOut"))
                    return mapping.findForward("sessionTimeOut");
            }

            req.setAttribute("TEMPLATE_ID", Integer.toString(gfs.getForm()
                    .getTemplateId()));
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);

            forward = "closeDialog";
        }
        return mapping.findForward(forward);

    }

}
