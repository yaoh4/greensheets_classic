package gov.nih.nci.iscs.numsix.apps.docchecker.util;

import gov.nih.nci.iscs.numsix.apps.docchecker.AppManager;
import gov.nih.nci.iscs.numsix.apps.docchecker.DocCheckerConfig;
import gov.nih.nci.iscs.numsix.apps.docchecker.DocCheckerFileAttrib;
import gov.nih.nci.iscs.numsix.apps.docchecker.DocCheckerResults;
import gov.nih.nci.iscs.numsix.apps.docchecker.IConstants;
import gov.nih.nci.iscs.numsix.apps.docchecker.exception.DocCheckerException;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class ReportWriter extends PdfPageEventHelper {
    public PdfTemplate tpl;
    public BaseFont helv;
    public Logger log = Logger.getLogger("gov.nih.nci.iscs.numsix.apps.docchecker.util");
    
	public void writeToPdf(DocCheckerResults docCheckerResults, String fileName) throws DocCheckerException {
		log.debug("Entering ReportWriter.writeToPdf()...");
		Map docCheckerResultsMap;
		long totalFileCount;
		long filesToBeReconciled;
		long filesToBeLocated;
		long filesRequiringNoAction;
		long emptyFilesRequiringNoAction;
		long emptyFilesToBeReconciled;
		long fileLength = 0;
		Document document = null;
		PdfPTable table;
		PdfPCell cell;
		Paragraph nciName;
		Paragraph prepDateTime;
		Paragraph docDescPara;
		String formattedDateTime = null;
		Date currentDate = new Date();
		Format formatter, formatter2;
		DocCheckerFileAttrib fileAttribPrint;
		Set docCheckerResultsMapKeys;
		Iterator docCheckerResultsMapKeysItr;
		String docCheckerResultsCurrent;
		PdfWriter writer = null;
		DocCheckerConfig docCheckerConfig = null;

		docCheckerConfig = AppManager.getInstance().getDocCheckerConfig();

		String reportType = docCheckerConfig.getReportType();
		String buildVersion = docCheckerConfig.getBuildVersion();
		Paragraph tmpText;

		formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
		formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
		formattedDateTime = formatter.format(currentDate);

		// Extract the file counts, results from the passed in DocCheckerResults.
		docCheckerResultsMap = docCheckerResults.getDocCheckerResultsMap();
		totalFileCount = docCheckerResults.getTotalFileCount();
		filesToBeReconciled = docCheckerResults.getFilesToBeReconciled();
		filesToBeLocated = docCheckerResults.getFilesToBeLocated();
		filesRequiringNoAction = docCheckerResults.getFilesRequiringNoAction();
		emptyFilesRequiringNoAction = docCheckerResults.getEmptyFilesRequiringNoAction();
		emptyFilesToBeReconciled = docCheckerResults.getEmptyFilesToBeReconciled();
		
		// step 1: creation of a document-object
		document = new Document();
		log.debug("Creating the .PDF file ...");
		try {
			// step 2: Create a writer that listens to the document and directs a PDF-stream to a file
			writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
			
            // step 3: initialisations + opening the document
            writer.setPageEvent(new ReportWriter());
			document.open();
			
			// step 4: Add a paragraph to the document
			document.setPageSize(PageSize.A4);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);            
			nciName = new Paragraph("NATIONAL CANCER INSTITUTE", new Font(Font.TIMES_ROMAN, 30, Font.BOLD));
			nciName.setAlignment(Element.ALIGN_CENTER);
			document.add(nciName);

			document.add(Chunk.NEWLINE);
			tmpText = new Paragraph("Build Version: " + buildVersion, new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL));
			tmpText.setAlignment(Element.ALIGN_RIGHT);
			document.add(tmpText);			
			tmpText = new Paragraph("Build Date: " + formatter2.format(docCheckerConfig.getBuildDate()), new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL));
			tmpText.setAlignment(Element.ALIGN_RIGHT);
			document.add(tmpText);
			
			document.add(Chunk.NEWLINE);
			prepDateTime = new Paragraph("REPORT PREPARED ON: " + formattedDateTime, new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL));
			prepDateTime.setAlignment(Element.ALIGN_LEFT);
			document.add(prepDateTime);
			
			tmpText = new Paragraph("ENVIRONMENT: " + docCheckerConfig.getRunningEnv(), new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL));
			tmpText.setAlignment(Element.ALIGN_LEFT);
			document.add(tmpText);
			
			tmpText = new Paragraph("REPORT TYPE: " + docCheckerConfig.getReportType(), new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL));
			tmpText.setAlignment(Element.ALIGN_LEFT);
			document.add(tmpText);
			
			if (IConstants.REPORT_TYPE_INCREMENTAL.equalsIgnoreCase(reportType)) {
				formatter = new SimpleDateFormat("MM/dd/yyyy");
				formattedDateTime = formatter.format(docCheckerConfig.getReportLastRun());
				tmpText = new Paragraph("Checked on the files that were attached/altered on or after: " + formattedDateTime, new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL));
				tmpText.setAlignment(Element.ALIGN_LEFT);
				document.add(tmpText);			
			}
			
			document.add(Chunk.NEWLINE);
			
			//document.add(new Paragraph(docDesc, new Font(Font.TIMES_ROMAN, 10, Font.NORMAL)));
			//docDescPara.setSpacingBefore(50f);			
			docDescPara = new Paragraph(
					"This report contains a table of the files that were uploaded using the GREENSHEETS web application. The files sometimes become dislocated or missing in the Web server file system. Below is the summary of the results of the Greensheets Document Checker.",
					new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL));
			document.add(docDescPara);
			document.add(Chunk.NEWLINE);

			table = new PdfPTable(3);
			
			// Add the header row
//			cell = new PdfPCell(new Paragraph("Files"));
			cell = new PdfPCell(new Paragraph("Files", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL)));
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);			

//			cell = new PdfPCell(new Paragraph("# of Files"));
			cell = new PdfPCell(new Paragraph("# of Files", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL)));
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);			
			
//			cell = new PdfPCell(new Paragraph("Comments"));
			cell = new PdfPCell(new Paragraph("Comments", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL)));
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);			

//			cell = new PdfPCell(new Paragraph("Requiring No Action"));
			cell = new PdfPCell(new Paragraph("Requiring No Action", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL)));
//			cell.setBackgroundColor(Color.green);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			table.addCell(String.valueOf(filesRequiringNoAction));
			
			if (emptyFilesRequiringNoAction == 0) {
				cell = new PdfPCell(new Paragraph(""));
//				cell.setBackgroundColor(Color.green);
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);				
			} else {
				table.addCell(String.valueOf(emptyFilesRequiringNoAction) + " empty files found.");	
			}
			
//			cell = new PdfPCell(new Paragraph("To be Reconciled"));
			cell = new PdfPCell(new Paragraph("To be Reconciled", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL)));
//			cell.setBackgroundColor(Color.green);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			table.addCell(String.valueOf(filesToBeReconciled));
			
			if (emptyFilesToBeReconciled == 0) {
				cell = new PdfPCell(new Paragraph(""));
//				cell.setBackgroundColor(Color.green);
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);				
			} else {
				table.addCell(String.valueOf(emptyFilesToBeReconciled) + " empty files found.");	
			}
			
//			cell = new PdfPCell(new Paragraph("To be Located"));
			cell = new PdfPCell(new Paragraph("To be Located", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL)));
//			cell.setBackgroundColor(Color.green);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			table.addCell(String.valueOf(filesToBeLocated));

			cell = new PdfPCell(new Paragraph(""));
//			cell.setBackgroundColor(Color.green);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
//			cell = new PdfPCell(new Paragraph("Total:"));
			cell = new PdfPCell(new Paragraph("Total:", new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL)));
			cell.setBackgroundColor(Color.GRAY);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph(String.valueOf(totalFileCount)));
			cell.setBackgroundColor(Color.GRAY);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph(""));
			cell.setBackgroundColor(Color.GRAY);
//			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			table.setWidthPercentage(40);
			document.add(table);			
			
			document.setPageSize(PageSize.A4.rotate());
			document.setMargins(5, 5, 30, 30);
			document.newPage();

			// Write the results to the table.
//			float[] widths = {0.52f, 0.7f, 0.7f, 0.7f, 0.7f, 0.20f};
			float[] widths = new float[6];
			widths[0] = 62f;	// File Name
			widths[1] = 4f;		// On Disk
			widths[2] = 4f;		// In DB
			widths[3] = 11f;		// File Length
			widths[4] = 9f;		// Action Required
			widths[5] = 10f;	// Comments			
			
			table = new PdfPTable(widths);

			// Add the header row
			cell = new PdfPCell(new Paragraph("File Name"));	// Col 1
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("On Disk"));	// Col 2
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("In DB"));	// Col 3
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("File Length                    (in Bytes)"));	// Col 4
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Action Required"));	// Col 5
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			cell = new PdfPCell(new Paragraph("Comments"));	// Col 6
			cell.setBackgroundColor(Color.green);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			
			docCheckerResultsMapKeys = docCheckerResultsMap.keySet();
			docCheckerResultsMapKeysItr = docCheckerResultsMapKeys.iterator();
			while (docCheckerResultsMapKeysItr.hasNext()) {
				docCheckerResultsCurrent = (String) docCheckerResultsMapKeysItr.next();
				fileAttribPrint = (DocCheckerFileAttrib) docCheckerResultsMap.get(docCheckerResultsCurrent);
				if (fileAttribPrint != null) {
					// File name
					table.addCell(docCheckerResultsCurrent);
					
					// Found on Disk?
					if (fileAttribPrint.getFoundOnDisk()) {
						cell = new PdfPCell(new Paragraph("Y"));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Paragraph(" "));
						table.addCell(cell);						
					}
					
					// Recorded in DB?
					if (fileAttribPrint.getRecordedInDb()) {
						cell = new PdfPCell(new Paragraph("Y"));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);						
					} else {
						cell = new PdfPCell(new Paragraph(" "));
						table.addCell(cell);										
					}
					
					// File length
					fileLength = fileAttribPrint.getFileLength();
					if (fileAttribPrint.getActionRequired().trim() == IConstants.ACTION_LOCATE) {
						cell = new PdfPCell(new Paragraph(" "));
						table.addCell(cell);						
					} else {
						cell = new PdfPCell(new Paragraph(String.valueOf(fileLength)));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);						
					}
					
					table.addCell(fileAttribPrint.getActionRequired());	// Action Required

					table.addCell(fileAttribPrint.getComments());	// Comments
				}
			}
			table.setWidthPercentage(100);
			document.add(table);
			
			// step 5: we close the document
			document.close();
			log.debug("Closed the .PDF file.");
		} catch (DocumentException de) {
			throw new DocCheckerException(this.getClass().getName() + ": Exception occurred while preparing the report file." + " " + de.getMessage());
		} catch (IOException ioe) {
			throw new DocCheckerException(this.getClass().getName() + ": Exception occurred while writing to the .PDF file." + " " + ioe.getMessage());
		}
	}
	
    public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			// initialization of the template
			tpl = writer.getDirectContent().createTemplate(100, 100);
			tpl.setBoundingBox(new Rectangle(-20, -20, 100, 100));
			// initialization of the font
			helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}  	
	
    public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();
		String text = "Page " + writer.getPageNumber() + " of ";
		float textSize = helv.getWidthPoint(text, 12);
		float textBase = document.bottom() - 20;
		cb.beginText();
		cb.setFontAndSize(helv, 12);

		cb.setTextMatrix(document.left(), textBase);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(tpl, document.left() + textSize, textBase);
		cb.saveState();
	}
   
    public void onCloseDocument(PdfWriter writer, Document document) {
		tpl.beginText();
		tpl.setFontAndSize(helv, 12);
		tpl.setTextMatrix(0, 0);
		tpl.showText("" + (writer.getPageNumber() - 1));
		tpl.endText();
	}	
}
