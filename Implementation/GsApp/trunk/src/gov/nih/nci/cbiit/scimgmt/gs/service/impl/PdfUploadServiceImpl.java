package gov.nih.nci.cbiit.scimgmt.gs.service.impl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;

import gov.nih.nci.cbiit.atsc.dao.GrantDAO;
import gov.nih.nci.cbiit.scimgmt.gs.dao.PdfUploadDAO;
import gov.nih.nci.cbiit.scimgmt.gs.service.PdfUploadService;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class PdfUploadServiceImpl implements PdfUploadService {

	private static final Logger logger = Logger.getLogger(PdfUploadServiceImpl.class);
    private static final String UPLOAD_FOLDER = "pdfupload.root";
    private static final String PDF_UPLOAD_URL = "pdfupload.url";
    private static final String PDF_UPLOAD_PGM = "pdfupload.docType.pgm";
    private static final String PDF_UPLOAD_SPEC = "pdfupload.docType.spec";
    private static final String LINE_FEED = "\r\n";
    
    private static final int SUCCESS = 200;

    private static Properties p;

    private PdfUploadDAO pdfUploadDAO;
   	private GrantDAO grantDAO;
   	static GreensheetFormService greensheetFormService;

   	public PdfUploadDAO getPdfUploadDAO() {
   		return pdfUploadDAO;
   	}

   	public void setPdfUploadDAO(PdfUploadDAO pdfUploadDAO) {
   		this.pdfUploadDAO = pdfUploadDAO;
   	}
   	
   	public GrantDAO getGrantDAO() {
   		return grantDAO;
   	}

   	public void setGrantDAO(GrantDAO grantDAO) {
   		this.grantDAO = grantDAO;
   	}

   	public GreensheetFormService getGreensheetFormService() {
   		return greensheetFormService;
   	}

   	public void setGreensheetFormService(GreensheetFormService greensheetFormService) {
   		this.greensheetFormService = greensheetFormService;
   	}

    @Override
	public int generatePdfUploads() {

        int responseCode = 0;
        
        p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
		
		Map<String, String> applGroupTypeMap = pdfUploadDAO.getPfdUploadCandidates();
        FileOutputStream fileOuputStream = null;

		if (applGroupTypeMap != null) {
			for (String applGroup : applGroupTypeMap.keySet()) {

				// Get grant info for every Greensheet candidates
				String[] split = applGroup.split("\t");
				Long applId = new Long(split[0]);
				String groupType = split[1];
				logger.debug("Finding grant info for applId: " + applId + " and Type: " + groupType);
				FormGrantProxy grant = grantDAO.findGSGrantInfo(null, applId, groupType, null);
				
				String filename = "";
				
				try {
					
					if(grant.getActivityCode() != null) {		
						
						// Get Greensheet for for a grant
						GreensheetFormProxy form = greensheetFormService.getGreensheetForm(grant, groupType);

						// Generate PDF file for the Greensheet
						byte[] pdfdoc = new GreensheetFormMgrImpl().getGreensheetFormAsPdf(form, grant, "YES", "NO", "NO");

						filename = grant.getApplID() + "_" + groupType + "_GS.pdf";

						// Include "pdfupload.root" property in greensheetconfig.properties file to store PDF files for testing purposes 
						if(p.getProperty(UPLOAD_FOLDER) != null) {
							//write bytes[] into a file
							fileOuputStream = new FileOutputStream(p.getProperty(UPLOAD_FOLDER)+"/"+filename);
							fileOuputStream.write(pdfdoc);
						}
												
						responseCode = sendPdfFile(grant.getApplID(), groupType, pdfdoc, filename);
						
						if(responseCode == SUCCESS) {
							logger.info(filename + " was succssfuly posted.");
							
							// Update upload status to COMPLETE (i.e. 114)
							pdfUploadDAO.updateUploadStatus(form.getId());
							
						} else {
							logger.error(filename + " failed on upload (Response Code:" +  responseCode + ")");
						}
					}
				} catch (GreensheetBaseException e) {
					logger.error("Problem with retrieving greensheet form for appl ID: " + grant.getApplID() + " (Response Code:" +  responseCode + ")" , e);
				} catch (IOException e) {
		            logger.error("Problem communicating with eRA server.", e);
		            return responseCode;
		        } catch (Exception e) {
		            logger.error("Problem uploading the PDF file:" + filename + " (Response Code:" +  responseCode + ")", e);
		            return responseCode;
		        } finally {
		        	if (fileOuputStream != null) {
		                try {
		                    fileOuputStream.close();
		                } catch (IOException e) {
		                    logger.error("Problem with stroring file: " + filename, e);
		                }
		            }
		        }
			}
		} else {
			logger.info("There is no Greensheet to upload.");
		}

		return responseCode;
	}
    
    // Send the Greensheet PDF file to eRA
 	private int sendPdfFile(String applId, String groupType, byte[] pdfdoc, String fileName) throws Exception {
 		
 		// creates a unique boundary based on time stamp
 		String boundary = "===" + System.currentTimeMillis() + "===";
 		
 		String requestURL = p.getProperty(PDF_UPLOAD_URL) + "/";						
		if(groupType.equals(GreensheetGroupType.PGM.getName())) {
			requestURL += p.getProperty(PDF_UPLOAD_PGM);
		} else if(groupType.equals(GreensheetGroupType.SPEC.getName())) {
			requestURL += p.getProperty(PDF_UPLOAD_SPEC);							
		}						
		requestURL += "/" + applId + "/";
 		
 		logger.info("Connecting: " + requestURL);
 		
 		// Generating the header for the request 
		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		URL url = new URL(requestURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		((HttpsURLConnection)httpConn).setSSLSocketFactory(sslsocketfactory);
		httpConn.setUseCaches(false);
		httpConn.setDoOutput(true); // indicates POST method
		httpConn.setRequestMethod("POST");
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Content-Type",	"multipart/form-data; boundary=" + boundary);
		httpConn.setRequestProperty("User-Agent", "Java Test Agent");

		OutputStream outputStream = httpConn.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"),	true);
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"docFile\"; filename=\"" + fileName + "\"").append(LINE_FEED);
		writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();
		
		// Include the file content
		outputStream.write(pdfdoc);
		outputStream.flush();

		writer.append(LINE_FEED);
		writer.flush();    
		
		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		writer.close();		
		
		// checks server's status code first
		List<String> response = new ArrayList<String>();
		int status = httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.add(line);
			}
			reader.close();
			httpConn.disconnect();
		} else {
			return status;
		}
			
		for (String line : response) {
			logger.info(line);
		}

 		return status;
 	}
}
