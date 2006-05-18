package gov.nih.nci.iscs.numsix.greensheets.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
/**
 *
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class SaveProgramPreferencesAction extends GsBaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp) {     
        return mapping.findForward(Constants.SUCCESS_KEY);
    }
}
