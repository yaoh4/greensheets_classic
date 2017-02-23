package gov.nih.nci.iscs.numsix.greensheets.application;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import org.apache.log4j.*;

public class CheckSessionTimeOut extends Action {

    private Logger logger = Logger.getLogger(CheckSessionTimeOut.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println(" Checking session......");
      

        return mapping.findForward("");
    }

}
