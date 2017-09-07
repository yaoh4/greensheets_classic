package gov.nih.nci.cbiit.scimgmt.gs.service;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PdfUploadTask {
	private static final Logger logger = Logger.getLogger(PdfUploadTask.class);

	@Autowired
	private PdfUploadService pdfUploadService;
	 
	@Autowired
	ServletContext servletContext;
		
	@Scheduled(cron="${pdfUpload.schedularTime}")
	public void run() {
		try {
			if(servletContext.getAttribute("isPdfUploadRunning") == null ||
				(servletContext.getAttribute("isPdfUploadRunning") != null && 
				servletContext.getAttribute("isPdfUploadRunning").equals(Boolean.FALSE))) {
				
				logger.info(" PDF Upload Job begin ... ");

				servletContext.setAttribute("isPdfUploadRunning", Boolean.TRUE);
				pdfUploadService.generatePdfUploads();
				servletContext.setAttribute("isPdfUploadRunning", Boolean.FALSE);
				
				logger.info(" PDF Upload Job end.");

			} else {
	        	logger.info("PDF Uploader cannot get executed at this moment as the internal job is in progress.");
	        }
			
		} catch (Exception e) {
			logger.error("There is a problem with generating Greensheet PDF files to upload.", e);
		}
	}
}
