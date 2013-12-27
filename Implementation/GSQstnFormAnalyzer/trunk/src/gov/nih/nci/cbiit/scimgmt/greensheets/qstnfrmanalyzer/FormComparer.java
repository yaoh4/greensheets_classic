package gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer;

import gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer.dataaccess.I2EformDAO;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class FormComparer {

	private static Logger logger = Logger.getLogger(FormComparer.class);
	
	private String outputDir = "";
	private String formListFile = "";

	private I2EformDAO i2eFormDAO = null;
	
	/**
	 * Write an output file listing which forms have identical content
	 * @param formGroup  which staff group's forms to compare (Program or Specialist or DM)
	 */
	public List<GSForm> listIdenticalForms(String formGroup) {
		logger.info("    <<  Determining which " + formGroup + " forms are identical  >>");
		
		List<GSForm> allForms = new LinkedList<GSForm>();
		
		// build a list of files found in the filesystem that we can compare
		File dirLocation = new File(outputDir);
		if (dirLocation.exists() && dirLocation.isDirectory()) {
			FormFileFilter formFileFilter = new FormFileFilter();
			formFileFilter.pattern = formGroup + "_[^q]*qstns.xml";
			File[] formFiles = dirLocation.listFiles(formFileFilter);
			// comparing each file to all other files of the same group
			for (int j = 0; j < formFiles.length; j++) {
				String fname = formFiles[j].getName();
				logger.debug("    > >> >>>  Processing file " + fname);
				int typePos = fname.indexOf('_') + 1;
				String type = fname.substring(typePos, typePos+1);
				String mech = fname.substring(typePos + 2, typePos + 5);
				if (j == 0) {
					GSForm newForm = new GSForm();
					newForm.setFormStaffRole(GSForm.determineStaffRole(formGroup));
					newForm.setGeneratedName(formGroup + "_form_1");
					newForm.setInitialFilename(fname);
					newForm.getApplicability().add(type + mech);
					allForms.add(newForm);
				}
				else {
					GSForm oneForm = null;
					boolean discrepancyEncountered = false;
					boolean sameAsAnotherForm = false;
					try {
						Iterator<GSForm> itr = allForms.iterator();
						while (itr.hasNext() && !sameAsAnotherForm ) {
							oneForm = itr.next();
							String comparisonFileName = outputDir + File.separator + 
									oneForm.getInitialFilename();
							BufferedInputStream comparisonFile = new BufferedInputStream(
									new FileInputStream(comparisonFileName));
							BufferedInputStream thisFile = new BufferedInputStream(
									new FileInputStream(formFiles[j]));
							discrepancyEncountered = false;
							int byteRead = 0;
							while (byteRead != -1 && !discrepancyEncountered) {
								byteRead = thisFile.read();
								if (byteRead != comparisonFile.read()) {
									discrepancyEncountered = true;
									logger.debug("        > " + formGroup + " " + type+mech +
											" form is not the same as " + oneForm.getInitialFilename());
								}
							}
							if (!discrepancyEncountered) {
								byteRead = comparisonFile.read();
								if (byteRead != -1) {
									discrepancyEncountered = true;
									logger.debug("     - - - - Comparison file longer than this file.");
								}
							}
							comparisonFile.close();
							thisFile.close();
							
							if ( !discrepancyEncountered ) {
								oneForm.getApplicability().add(type+mech);
								sameAsAnotherForm = true;
								logger.debug("    !!! !!! !!!  Form read from " + fname + " is the same " +
										"as read from " + oneForm.getInitialFilename() + "!!!");
							}
						}  // end of iterating over unique forms already present
						if ( discrepancyEncountered || !sameAsAnotherForm ) {
							GSForm newForm = new GSForm();
							newForm.setFormStaffRole(GSForm.determineStaffRole(formGroup));
							newForm.setInitialFilename(fname);
							newForm.getApplicability().add(type+mech);
							newForm.setGeneratedName(formGroup+"_form_" + (allForms.size()+1));
							allForms.add(newForm);
							logger.debug("      = = = = =  New form detected - read from " + 
									fname);
						}
					}
					catch (IOException ioe) {
						logger.error("     ***  Problem comparing " + fname + " with " 
								+ oneForm.getInitialFilename() + ":\n\t" + ioe);
					}
				} // end of what we do when this is not the 1st file
			}  // end of going through all files for the same group of forms

			
			logger.info("   === == = --- -- -  Beginning to count form usage frequency  - -- --- = == ===");
			populateOccurrenceCounts(allForms);
			logger.info("\n\t   === == = --- -- -  Now sorting the list of forms according to usage frequency  - -- --- = == ===");
			Collections.sort(allForms, new Comparator<GSForm>() {
				@Override
				public int compare(GSForm f1, GSForm f2) {  
					if (f1.getOccurrences() == f2.getOccurrences())  { return 0; }
					else { return (f2.getOccurrences() > f1.getOccurrences() ? 1 : -1); }
						// "backwards" because we want forms with heavier usage sorted first,
						// and less frequently used forms at the end of the list
				}
			});
			
			String resultsFileName = outputDir + System.getProperty("file.separator") +
					formGroup + "_" + formListFile;
			try {
				PrintWriter resultsWriter = new PrintWriter(new BufferedWriter(new FileWriter(resultsFileName)));
				Iterator<GSForm> frmItr = allForms.iterator();
				while (frmItr.hasNext()) {
					GSForm aForm = frmItr.next();
					resultsWriter.println(aForm.getGeneratedName() + ": first read from " + 
							aForm.getInitialFilename());
					resultsWriter.println("   > Times filled out in last 5 FYs: " + aForm.getOccurrences());
					resultsWriter.println("   > Applicability: " + aForm.getApplicabilityAsString());
				}
				resultsWriter.flush();
				resultsWriter.close();
			}
			catch (IOException ioe) {
				logger.error("Error writing byte-by-byte comparison results into the file "
						+ resultsFileName + ":\n\t" + ioe);
			}
		} 
		else {
			logger.error("  !!! Unable to create output file(s) in directory " + outputDir);
		}
		return allForms;
	} // end of the method
	
	
	public void populateOccurrenceCounts(List<GSForm> formList) {
		if (i2eFormDAO != null) {
			for (GSForm oneForm : formList) {
				long occurrenceCount = 0l;
				for (String typeMech : oneForm.getApplicability()) {
					logger.debug("  >> >>  Form " + oneForm.getGeneratedName() + 
							": counting how many " + typeMech + " greensheets were actually filled out.");
					long thisTypeMechOccCount = i2eFormDAO.determineCount(oneForm.getFormStaffRole(), typeMech);
					logger.debug("  >> >>  >>  --- there were " + thisTypeMechOccCount + " of them.");
					occurrenceCount = occurrenceCount + thisTypeMechOccCount;
				}
				oneForm.setOccurrences(occurrenceCount);
			}
		}
		else {
			logger.error(" * * *  No Data Access Object present  * * *");
		}
	}
	
	
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public String getFormListFile() {
		return formListFile;
	}
	public void setFormListFile(String formListFile) {
		this.formListFile = formListFile;
	}
	public I2EformDAO getI2eFormDAO() {
		return i2eFormDAO;
	}
	public void setI2eFormDAO(I2EformDAO i2eFormDAO) {
		this.i2eFormDAO = i2eFormDAO;
	}


	private class FormFileFilter implements FilenameFilter {
		public String pattern = "";  // must be set from the instantiating method
		@Override
		public boolean accept(File dir, String name) {
			return name.matches(pattern);
		}
	}
}
