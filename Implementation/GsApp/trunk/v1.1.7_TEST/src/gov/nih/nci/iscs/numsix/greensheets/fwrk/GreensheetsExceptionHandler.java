
/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.fwrk;

import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.*;
import org.apache.struts.action.*;
import org.apache.struts.config.*;
/**
 *  Exception Handler for GreensheetBaseExceptions allows handeling of 
 *  nested exceptions
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class GreensheetsExceptionHandler extends ExceptionHandler {

    /**
     * @see org.apache.struts.action.ExceptionHandler#execute(Exception, ExceptionConfig, ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(
        Exception ex,
        ExceptionConfig config,
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest req,
        HttpServletResponse resp)
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
            Properties p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_ERROR_MESSAGES);
            if (p != null) {
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i] != null) {

                        String errorMsg = p.getProperty(keys[i]);
                        if (errorMsg == null) {
                            errorMsg = ex.getMessage();
                        }
                        error = new ActionError("error.exception", errorMsg);
                        property = error.getKey();
                        storeException(req, property, error, forward, config.getScope());
                    }
                }
            } else {
                error = new ActionError("error.exception", "Error during ErrorMessage Init");
                property = error.getKey();
                storeException(req, property, error, forward, config.getScope());

            }
        } else {
            error = new ActionError("error.exception", "A System Error Occurred");
            property = error.getKey();
            storeException(req, property, error, forward, config.getScope());
            

        }
        req.setAttribute("ERROR_STACK_TRACE",org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex));

        ex.printStackTrace();

        return mapping.findForward("error");

    }

    /**
     * @see org.apache.struts.action.ExceptionHandler#storeException(HttpServletRequest, String, ActionError, ActionForward, String)
     */
    protected void storeException(
        HttpServletRequest req,
        String property,
        ActionError error,
        ActionForward forward,
        String scope) {

        ActionErrors errors = (ActionErrors) req.getAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            errors = new ActionErrors();
        }
        errors.add(property, error);
        req.setAttribute(Globals.ERROR_KEY, errors);
    }

}
