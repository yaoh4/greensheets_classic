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
public class EditPreferencesAction extends DispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp) {     
        String forward = null;
        forward = "editpreferences";
 
        return mapping.findForward(forward);

    }

 
  

    

}
