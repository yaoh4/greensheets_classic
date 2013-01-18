/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.fwrk;

import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * Exception Handler for GreensheetBaseExceptions allows handeling of nested exceptions
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetsExceptionHandler extends ExceptionHandler {

    private static final Logger logger = Logger.getLogger(GreensheetsExceptionHandler.class);

    /**
     * @see org.apache.struts.action.ExceptionHandler#execute(Exception, ExceptionConfig, ActionMapping, ActionForm, HttpServletRequest,
     *      HttpServletResponse)
     */
    public ActionForward execute(Exception ex, ExceptionConfig config,
            ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        ActionForward forward = null;
        ActionError error = null;
        String property = null;
        String path = null;

        // find path for forward
        if (config.getPath() != null) {
            path = config.getPath();
        } else {
            path = mapping.getInput();
        }

        forward = new ActionForward(path);

        if (ex instanceof GreensheetBaseException) {

            GreensheetBaseException bex = (GreensheetBaseException) ex;
            String[] keys = bex.getMessages();
            Properties p = (Properties) AppConfigProperties.getInstance()
                    .getProperty(GreensheetsKeys.KEY_ERROR_MESSAGES);
            if (p != null) {
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i] != null) {

                        String errorMsg = p.getProperty(keys[i]);
                        if (errorMsg == null) {
                            errorMsg = ex.getMessage();
                        }
                        error = new ActionError("error.exception", errorMsg);
                        property = error.getKey();
                        storeException(req, property, error, forward, config
                                .getScope());
                    }
                }
            } else {
                error = new ActionError("error.exception",
                        "Error during ErrorMessage Init");
                property = error.getKey();
                storeException(req, property, error, forward, config.getScope());

            }
        } else {
            error = new ActionError("error.exception",
                    "A System Error Occurred");
            property = error.getKey();
            storeException(req, property, error, forward, config.getScope());

        }
        req.setAttribute("ERROR_STACK_TRACE",
                org.apache.commons.lang.exception.ExceptionUtils
                        .getFullStackTrace(ex));
        logger.debug(org.apache.commons.lang.exception.ExceptionUtils
                .getFullStackTrace(ex));
        // send notification only if session is not expired
        if (!req.getSession().isNew()) {
            EmailNotification.sendEmailNotification(org.apache.commons.lang.exception.ExceptionUtils
                    .getFullStackTrace(ex), req);
        } else {
            return mapping.findForward("sessionTimeOut");
        }
        ex.printStackTrace();

        return mapping.findForward("error");

    }

    /**
     * @see org.apache.struts.action.ExceptionHandler#storeException(HttpServletRequest, String, ActionError, ActionForward, String)
     */
    protected void storeException(HttpServletRequest req, String property,
            ActionError error, ActionForward forward, String scope) {

        ActionErrors errors = (ActionErrors) req
                .getAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            errors = new ActionErrors();
        }
        errors.add(property, error);
        req.setAttribute(Globals.ERROR_KEY, errors);
    }

}
