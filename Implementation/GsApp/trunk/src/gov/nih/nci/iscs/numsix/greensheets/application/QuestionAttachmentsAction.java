package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import org.apache.struts.actions.*;
import org.apache.struts.upload.*;
/**
 *
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class QuestionAttachmentsAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(QuestionAttachmentsAction.class);

    public ActionForward findAttachments(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {
            String respId = req.getParameter("RESP_DEF_ID");
            String formUid = req.getParameter(GreensheetsKeys.KEY_FORM_UID);

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            GreensheetForm form = gfs.getForm();

            QuestionResponseData qrd = form.getQuestionResponseDataByRespId(respId);

            QuestionAttachmentsProxy qap = new QuestionAttachmentsProxy(respId);

            if (qrd != null) {
                Object[] arr = qrd.getQuestionAttachments().values().toArray();
                qap.initWithExistingQuestionAttachments(arr);
            }

            gfs.addAttachmentsProxy(qap, respId);

            req.setAttribute("QA_PROXY", qap);
            req.setAttribute("RESP_DEF_ID", respId);
            
            req.setAttribute("NUM_OF_ATTACHMENTS", "" + qap.getAttachmentCount());
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
            req.setAttribute("canEdit", new Boolean(gus.getUser().isCanEdit()).toString());

            forward = "attachments";
        }
        return mapping.findForward(forward);

    }

    public ActionForward attachFile(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
            DynaActionForm form = (DynaActionForm) aForm;

            FormFile file = (FormFile) form.get("fileAttachment");

            String respDefId = (String) form.get("respDefId");
            String formUid = (String) form.get("formUid");

            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);
            QuestionAttachmentsProxy qap = gfs.getQuestionAttachmentProxy(respDefId);

            String maxSize =
                ((Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES)).getProperty(
                    "file.attachment.size.limit");

            if (file.getFileSize() > Integer.parseInt(maxSize) * 1000000) {
                req.setAttribute("FILE_SIZE_ERROR", "error");
                req.setAttribute("FILE_SIZE_LIMIT", maxSize + "mb");
            } else {

                QuestionAttachment qa = QuestionAttachment.createNewAttachment();
                qa.setFilename(file.getFileName());
                qa.setDocData(file.getFileData());

                qap.addQuestionAttachment(qa);
            }

            req.setAttribute("QA_PROXY", qap);
            req.setAttribute("RESP_DEF_ID", respDefId);
            
            req.setAttribute("NUM_OF_ATTACHMENTS", "" + qap.getAttachmentCount());
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
            req.setAttribute("canEdit", new Boolean(gus.getUser().isCanEdit()).toString());
            forward = "attachments";
        }
        return mapping.findForward(forward);

    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String fileMemoryId = (String) form.get("fileMemoryId");

            String respDefId = (String) form.get("respDefId");
            String formUid = (String) form.get("formUid");

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            QuestionAttachmentsProxy qap = gfs.getQuestionAttachmentProxy(respDefId);
            
            // delete or mark the attachment for deletion
            qap.removeAttachment(fileMemoryId);
                    
            req.setAttribute("QA_PROXY", qap);
            req.setAttribute("RESP_DEF_ID", respDefId);
            
            req.setAttribute("NUM_OF_ATTACHMENTS", "" + qap.getAttachmentCount());
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
            req.setAttribute("canEdit", new Boolean(gus.getUser().isCanEdit()).toString());

            forward = "attachments";
        }
        return mapping.findForward(forward);

    }

    public ActionForward viewFile(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");
            return mapping.findForward(forward);
        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String fileMemoryId = (String) form.get("fileMemoryId");

            String respDefId = (String) form.get("respDefId");
            String formUid = (String) form.get("formUid");

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            QuestionAttachmentsProxy qap = gfs.getQuestionAttachmentProxy(respDefId);

            QuestionAttachment qa = qap.getAttachment(fileMemoryId);
            if (qa != null) {
                byte[] byst = null;
                if (qa.getDocData() == null) {
                    GreensheetFormMgr mgr = GreensheetMgrFactory.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
                    mgr.getQuestionAttachmentData(qa);
                    byst = qa.getDocData();
                } 
                else {
                    byst = qa.getDocData();
                }
                
                resp.reset();
                resp.resetBuffer();
                
                String fileName = qa.getFilename();

                String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);

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

                //Send content to Browser
                resp.getOutputStream().write(byst);
                resp.getOutputStream().flush();
                
                req.setAttribute("QA_PROXY", qap);
                req.setAttribute("RESP_DEF_ID", respDefId);
                req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
            }
            
            
   /*         
            List list = qap.getTmpQuestionAttachmentList();

            ListIterator iter = list.listIterator();

            while (iter.hasNext()) {
                QuestionAttachment qa = (QuestionAttachment) iter.next();
                if (qa.getFilename().equalsIgnoreCase(fileName)) {

                    byte[] byst = null;
                    if (qa.getDocData() == null) {
                        GreensheetFormMgr mgr = GreensheetMgrFactory.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
                        mgr.getQuestionAttachmentData(qa);
                        byst = qa.getDocData();
                    } else {
                        byst = qa.getDocData();
                    }

                    

                    String header = "attachment;  filename=\"" + fileName + "\";";

                    if (logger.isDebugEnabled()) {
                        logger.debug("");
                    }
                    logger.debug("Downloading File " + fileName);
                    resp.setHeader("Content-Disposition", header);

                    //Send content to Browser
                    resp.getOutputStream().write(byst);
                    resp.getOutputStream().flush();

                }
            }
            req.setAttribute("QA_PROXY", qap);
            req.setAttribute("RESP_DEF_ID", respDefId);
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);
*/
            return null;
        }

    }

    public ActionForward cancel(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String formUid = (String) form.get("formUid");
            String respDefId = (String) form.get("respDefId");

            GreensheetUserSession gus =
                (GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);

            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);
            gfs.removeQuestionAttachmentsProxy(respDefId);

            GreensheetForm gform = gfs.getForm();
            GreensheetActionHelper.setFormDisplayInfo(req, formUid);
            req.setAttribute("TEMPLATE_ID", Integer.toString(gform.getTemplateId()));
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);

            forward = "closeDialog";
        }
        return mapping.findForward(forward);

    }

    public ActionForward save(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and the Greensheet window and Refresh your grants list");

        } else {

            DynaActionForm form = (DynaActionForm) aForm;
            String formUid = (String) form.get("formUid");
            String respDefId = (String) form.get("respDefId");

            GreensheetUserSession gus = (GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);
            QuestionAttachmentsProxy qap = gfs.getQuestionAttachmentProxy(respDefId);

            gfs.updateQRDQuestionAttachments(respDefId, qap.getAttachmentMap());

            gfs.removeQuestionAttachmentsProxy(respDefId);

            GreensheetActionHelper.setFormDisplayInfo(req, formUid);
            req.setAttribute("TEMPLATE_ID", Integer.toString(gfs.getForm().getTemplateId()));
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, formUid);

            forward = "closeDialog";
        }
        return mapping.findForward(forward);

    }

}
