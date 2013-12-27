package gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer.xmlxform.XmlProcessor;

import org.apache.log4j.Logger;

public class FlowController {

	private static Logger logger = Logger.getLogger(FlowController.class);
	
	private String inputFilesDir = "";
	private String outputDir = "";
	private String[] filenamePrefixes = {"PC", "SC", "DC", "PNC", "SNC", "DNC"};
	
	private XmlProcessor xmlProcessor;
	private FormComparer formComparer;
	
	public void execute() {
		logger.info("| Flow controller started the flow.");
		
		List<GSForm> formList = null;
		
		cleanOutResultsDir();
		
		logger.info("  < < <  GENERATING INDIVIDUAL FILES FOR EACH FORM (EACH TYPE/MECH)  > > >");
		generateIndivFormFilesForComparison();

		logger.info("\n" + "  < < <  NOW STARTING TO COMPARE INDIVIDUAL FORM FILES BYTE BY BYTE  > > >");
		for (int i = 0; i < filenamePrefixes.length; i++) {
			formList = compareFormsByteByByte(filenamePrefixes[i]);
			generateDistinctFormFiles(filenamePrefixes[i], formList);
		}
		
		logger.info("| Flow controller completed execution.");
	}

	
	private void cleanOutResultsDir() {
		logger.info(" * Deleting all files in the output directory *");
		if (outputDir != null && !"".equals(outputDir)) {
			File theDir = new File(outputDir);
			if (theDir.exists() && theDir.isDirectory()) {
				File[] allFiles = theDir.listFiles();
				for (int i = 0; i < allFiles.length; i++) {
					allFiles[i].delete();
				}
			}
		}
	}
	
	private void generateIndivFormFilesForComparison() {
		for (int i = 0; i < filenamePrefixes.length; i++) {
			String typeMechListFileName = inputFilesDir + System.getProperty("file.separator") 
					+ "ql" + filenamePrefixes[i] + ".txt";
			File typeMechListFile = new File(typeMechListFileName);
			if (typeMechListFile.exists() && typeMechListFile.canRead()) {
				try {
					BufferedReader rdr = new BufferedReader(new FileReader(typeMechListFile));
					String oneLine = rdr.readLine();
					while (oneLine != null) {
						logger.debug("We read this: " + oneLine);
						Scanner commaSplitter = new Scanner(oneLine);
						commaSplitter.useDelimiter(",");
						String applType = commaSplitter.next().trim();
						String fundingMech = commaSplitter.next().trim();
						logger.debug("   > Type: " + applType + "; mech: " + fundingMech); 
						commaSplitter.close();
						if (xmlProcessor != null) {
							xmlProcessor.writeOneFormsQuestions(filenamePrefixes[i], 
									applType, fundingMech);
						}
						// this below needs to stay the last line in the loop
						oneLine = rdr.readLine();
					}
					rdr.close();
				}
				catch (IOException ioe) {
					logger.error(" * *  Error reading the list of typemechs from " + 
							typeMechListFileName + ":\n\t" + ioe);
				}
			}  // end of processing one single line (one type/mech) form the type/mech list file 
			else {
				logger.error("Unable to read the list of types/mechs from \n\t" +
						typeMechListFileName);
			}			
		}  // end of processing for forms of the same type (Program / Specialist / DM)		
	}
	
	
	
	public List<GSForm> compareFormsByteByByte(String filenamePrefix) {
		if (formComparer != null) {
			return formComparer.listIdenticalForms(filenamePrefix);
		}
		else { return null; }
	}
	

	
	private void generateDistinctFormFiles(String filenamePrefix, List<GSForm> allForms) {
		if (xmlProcessor != null) {
			xmlProcessor.writeFormDefXml(filenamePrefix, allForms);
		}
	}

	
	public void postProcess() {
		logger.info("...Post-processing begins...");
		if (xmlProcessor != null) {
			try {
				xmlProcessor.writeFormSummaryFile();
			}
			catch (IOException ioe) {
				logger.error("  ## ## ##  Input/output error during post-processing: \n\t" + ioe);
			}
		}
		logger.info("...Post-processing is done...");
	}
	
	
	public XmlProcessor getXmlProcessor() {
		return xmlProcessor;
	}
	public void setXmlProcessor(XmlProcessor xmlProcessor) {
		this.xmlProcessor = xmlProcessor;
	}
	public FormComparer getFormComparer() {
		return formComparer;
	}
	public void setFormComparer(FormComparer formComparer) {
		this.formComparer = formComparer;
	}
	public String getInputFilesDir() {
		return inputFilesDir;
	}
	public void setInputFilesDir(String inputFilesDir) {
		this.inputFilesDir = inputFilesDir;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

}
