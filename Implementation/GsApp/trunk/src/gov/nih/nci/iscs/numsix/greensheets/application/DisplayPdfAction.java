/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.ServletContextAware;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormService;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;

/**
 * Action class that display Greensheets without question/answers in a PDF format
 * 
 * @author
 */

public class DisplayPdfAction extends GsBaseAction implements ServletContextAware {

	private static final Logger logger = Logger.getLogger(DisplayPdfAction.class);

	static ServletContext servletContext;
	static GreensheetsFormGrantsService greensheetsFormGrantsService;
	static GreensheetFormService greensheetFormService;

	/**
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		String applId = req.getParameter("APPL_ID");
		String groupType = req.getParameter("GS_GROUP_TYPE");

		logger.info("Display PDF for applID: " + applId + " and Group: " + groupType);

		if(applId == null || groupType == null) {
			logger.error("Null applId or group");
			return null;
		}
		
		FormGrantProxy grant = greensheetsFormGrantsService.findGSGrantInfo(null, Long.valueOf(applId), groupType, null);

		if (grant.getActivityCode() != null) {

			// Get Greensheet for for a grant
			GreensheetFormProxy gsform = greensheetFormService.getGreensheetForm(grant, groupType);

			// Generate PDF file for the Greensheet
			byte[] pdfdoc = new GreensheetFormMgrImpl().getGreensheetFormAsPdf(gsform, grant, "YES", "NO", "NO");

			resp.setContentType("application/pdf");
			resp.setContentLength(pdfdoc.length);

			// Send content to Browser
			resp.getOutputStream().write(pdfdoc);
			resp.getOutputStream().flush();
		}
		return null;

	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public GreensheetsFormGrantsService getGreensheetsFormGrantsService() {
		return greensheetsFormGrantsService;
	}

	public void setGreensheetsFormGrantsService(GreensheetsFormGrantsService greensheetsFormGrantsService) {
		this.greensheetsFormGrantsService = greensheetsFormGrantsService;
	}

	public static GreensheetFormService getGreensheetFormService() {
		return greensheetFormService;
	}

	public static void setGreensheetFormService(GreensheetFormService greensheetFormService) {
		DisplayPdfAction.greensheetFormService = greensheetFormService;
	}
	
}
