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

import gov.nih.nci.cbiit.scimgmt.gs.service.PdfUploadService;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;

/**
 * Action class that uploads Greensheets into eRA grant
 * 
 * @author
 */

public class PdfUploadAction extends GsBaseAction implements ServletContextAware {

	private static final Logger logger = Logger.getLogger(PdfUploadAction.class);
	
	static ServletContext servletContext;
	static PdfUploadService pdfUploadService;

	/**
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		String forward = null;

		if(servletContext.getAttribute("isPdfUploadRunning") == null ||
				(servletContext.getAttribute("isPdfUploadRunning") != null && 
				servletContext.getAttribute("isPdfUploadRunning").equals(Boolean.FALSE))) {			
			
			logger.debug("PDF Upload execute() Begin");
			
			servletContext.setAttribute("isPdfUploadRunning", Boolean.TRUE);
			int responseCode = pdfUploadService.generatePdfUploads();
			
			if(responseCode == 200) {
				resp.setStatus(Constants.OK_STATUS);
			} else {
				resp.setStatus(responseCode);
			}
            
            servletContext.setAttribute("isPdfUploadRunning", Boolean.FALSE);
            
    		logger.debug("PDF Upload execute() End");

        } else {
        	logger.info("PDF Uploader cannot get executed at this moment as the internal job is in progress.");
        }

		return (mapping.findForward(forward));
	}

	public PdfUploadService getPdfUploadService() {
		return pdfUploadService;
	}

	public void setPdfUploadService(PdfUploadService pdfUploadService) {
		this.pdfUploadService = pdfUploadService;
	}
	
	public void setServletContext(ServletContext servletContext) {  
        this.servletContext = servletContext;
    }
	
	public static ServletContext getServletContext() {
		return servletContext;
	}
}
