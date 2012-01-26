package gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import org.dom4j.*;
import org.dom4j.io.*;
import org.dom4j.util.*;
import org.w3c.dom.Node;
// import org.xml.sax.*;
// import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

// import org.xml.sax.helpers.*;

import org.apache.log4j.Logger;

import gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator.FilesysUtils;

public class GenerateVelocityPanel {

    static Properties p;
    static Logger logger = Logger.getLogger(GenerateVelocityPanel.class);

    public GenerateVelocityPanel() {

    }

    public static Component createVelocityPanel(final Map<String, String> srcMap) {

        logger.info("\n" + "* * * Getting ready to load application properties for template generation.");
    	p = FilesysUtils.loadProperties(GenerateVelocityPanel.class.getName(), "gengsapp.properties");
    	logger.info(" Properties loaded. * * *");

        final JComboBox questionSrcList = new JComboBox(srcMap.keySet().toArray());
        final JComboBox targetDatabaseList = new JComboBox();
        targetDatabaseList.addItem("NONE");
        targetDatabaseList.addItem("DEV");
        targetDatabaseList.addItem("TEST");
        targetDatabaseList.addItem("TRAINING");
        targetDatabaseList.addItem("PROD");

        final JRadioButton rbLoadSingleTemplate = new JRadioButton("Load a single template");
        rbLoadSingleTemplate.setEnabled(false);
        final JRadioButton rbLoadAll = new JRadioButton("Load all templates in list file");
        rbLoadAll.setEnabled(false);
        final JCheckBox cbxReplaceExisting = new JCheckBox("Replace templates if they already exist");
        cbxReplaceExisting.setEnabled(false);
        final JCheckBox cbxValidationOn = new JCheckBox("Embed validation that all questions are answered");
        cbxValidationOn.setSelected(true);
        cbxValidationOn.setEnabled(false);

        final JTextField txtType = new JTextField(4);
        final JTextField txtMech = new JTextField(4);

        final JButton btnClose = new JButton("Close");
        final JButton btnGenenerate = new JButton("Generate the Template(s)");

        rbLoadSingleTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rbLoadSingleTemplate.isSelected()){
					txtType.setEnabled(true); txtMech.setEnabled(true);
				}
			}
		});
        rbLoadAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtType.setText(""); txtMech.setText("");
				txtType.setEnabled(false); txtMech.setEnabled(false);
			}
		});        
        
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        targetDatabaseList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JComboBox cb = (JComboBox)e.getSource();
                String sel = (String)cb.getSelectedItem();
                logger.warn("--- Database selected: --- " + sel);
                if(!sel.equalsIgnoreCase("NONE")){
                    rbLoadSingleTemplate.setEnabled(true);
                    rbLoadAll.setEnabled(true);
                    cbxReplaceExisting.setEnabled(true);
                    cbxValidationOn.setEnabled(true);
                    btnGenenerate.setText("Upload the Template(s)");
                }else{
                    rbLoadSingleTemplate.setEnabled(false);
                    rbLoadAll.setEnabled(false);
                    cbxReplaceExisting.setEnabled(false);
                    cbxValidationOn.setEnabled(false);
                }

            }
        } );

        btnGenenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                	logger.info("Processing questions for " + srcMap.get(questionSrcList.getSelectedItem()) + " greensheets.");
                    String type = txtType.getText().trim();
                    String mech = txtMech.getText().trim();

                    String questSrc = (String) srcMap.get(questionSrcList.getSelectedItem());
                    String dbProperties = p.getProperty((String) targetDatabaseList.getSelectedItem());

                    String appInstallationPath = FilesysUtils.getBaseDirectory(GenerateVelocityPanel.class.getName())
                    	.getParentFile().getPath();
                    
                    String questionsSrcXml = appInstallationPath + "/xml/" + questSrc
                            + "_Questions.xml";
                    
                    boolean validationPassed = parseAndValidate(questionsSrcXml, appInstallationPath);
                    
                    // If everything checks out go ahead and transform the xml to a velocity file
                	if (validationPassed) {
                		logger.debug("Sucessfully passed validation checks");

                    	TemplateLoader databaseReader = new TemplateLoader(dbProperties);
                    	Map<String, Long> existingTemplates = databaseReader.getExistingTemplateIDs(questSrc); 
                    	databaseReader = null;

	                    if (rbLoadAll.isSelected()) {
	                    	logger.info("[ = - Starting bulk load of templates for grant type/mechanism combinations defined in "
	                        		+ p.getProperty(questSrc) + ". - = ]");
	                                            	
	                        String typeMechComboListFilePath = p.getProperty("typemech.list.files.dir") + "/" +
	                        	p.getProperty(questSrc);
	                        
	                        FileReader fr = new FileReader(typeMechComboListFilePath);
	                        BufferedReader br = new BufferedReader(fr);
	                        String s = null;
	                        while ((s = br.readLine()) != null) {
	                            type = s.substring(0, 1);
	                            mech = s.substring(2);
	                            logger.info(" * Type/mech " + type + mech);
	                            Long existingTemplateID = existingTemplates.get(type + mech);
	                            if (existingTemplateID != null && existingTemplateID.longValue() > 0l ) {
	                            	if (cbxReplaceExisting.isSelected()) {
			                            generateTemplate(type, mech, questionsSrcXml, dbProperties,
			                                    true, true, questSrc, cbxValidationOn.isSelected());
			                            logger.info("Existing " + type + mech + " " + questSrc + " template with ID "
	                            				+ existingTemplateID.longValue() + " will be replaced with a new one.");  
	                            	}
	                            	else {
	                            		logger.warn("Existing " + type + mech + " " + questSrc + " template with ID "
	                            				+ existingTemplateID.longValue() + " will *NOT* be replaced, per " 
	                            				+ "user-selected settings.");
	                            	}
	                            }
	                            else {
	                            	logger.info("No template for " + type+mech + " " + questSrc + " forms existed "
	                            			+ " before, so a new template will be inserted.");
		                            generateTemplate(type, mech, questionsSrcXml, dbProperties,
		                                    true, false, questSrc, cbxValidationOn.isSelected());
	                            }
	                            logger.info(" * Done with type/mech: " + type + mech + "\n");
	                        }
	                        br.close();
	                        fr.close();
	                        
	                        logger.info("[ = - Bulk load of templates for grant type/mechanism combinations defined in "
	                        		+ p.getProperty(questSrc) + " is complete - = ]");
	                        
	                    } 
	                    else if (rbLoadSingleTemplate.isSelected()) {
                            Long existingTemplateID = existingTemplates.get(type + mech);
                            if (existingTemplateID != null && existingTemplateID.longValue() > 0l ) {
                            	if (cbxReplaceExisting.isSelected()) {
		                            generateTemplate(type, mech, questionsSrcXml, dbProperties,
		                                    true, true, questSrc, cbxValidationOn.isSelected());
		                            logger.info("Existing " + type + mech + " " + questSrc + " template with ID "
                            				+ existingTemplateID.longValue() + " has just been replaced with a new one.");  
                            	}
                            	else {
                            		logger.warn("Existing " + type + mech + " " + questSrc + " template with ID "
                            				+ existingTemplateID.longValue() + " will *NOT* be replaced, per " 
                            				+ "user-selected settings.");
                            	}
                            }
                            else {
                            	logger.info("No template for " + type+mech + " " + questSrc + " forms existed "
                            			+ "before, so a new template will be inserted.");
	                            generateTemplate(type, mech, questionsSrcXml, dbProperties,
	                                    true, false, questSrc, cbxValidationOn.isSelected());
                            }
                        }
	                    else {
	                        generateTemplate(type, mech, questionsSrcXml, dbProperties, false, false,
	                                questSrc, cbxValidationOn.isSelected());
	                    }
                	} // end of what we do if validation was passed.
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        });

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbLoadSingleTemplate);
        bg.add(rbLoadAll);

        JPanel dbOptionsPanel = new JPanel();
        dbOptionsPanel.setLayout(new BorderLayout());

        JPanel dpOptPanLeft = new JPanel();
        dpOptPanLeft.setLayout(new GridLayout(2, 0));
        dpOptPanLeft.add(rbLoadSingleTemplate);
        dpOptPanLeft.add(rbLoadAll);


        JPanel dbOptPanRight = new JPanel();
        dbOptPanRight.setLayout(new GridLayout(2, 0));
        dbOptPanRight.add(cbxValidationOn);
        dbOptPanRight.add(cbxReplaceExisting);


        dbOptionsPanel.add(dpOptPanLeft,BorderLayout.WEST);
        dbOptionsPanel.add(dbOptPanRight,BorderLayout.EAST);

        JPanel container = new JPanel();

        JPanel labels = new JPanel();

        labels.setLayout(new GridLayout(7, 0));
        labels.add(new JLabel("Question Source"));
        labels.add(new JLabel("Select DB"));
        labels.add(new JLabel("Select Options"));
        labels.add(new JLabel("Specify Type"));
        labels.add(new JLabel("Specify Mech"));
        labels.add(new JLabel("Hit the Button"));
        labels.add(new JLabel("I'm Done"));

        JPanel pane = new JPanel();

        pane.setLayout(new GridLayout(7, 0));
        pane.add(questionSrcList);
        pane.add(targetDatabaseList);
        pane.add(dbOptionsPanel);
        pane.add(txtType);
        pane.add(txtMech);
        pane.add(btnGenenerate);
        pane.add(btnClose);

        container.setLayout(new BorderLayout());
        container.add(labels, BorderLayout.WEST);
        container.add(pane, BorderLayout.CENTER);

        return container;
    }

    /**
     * Check the Question Definition source XML file to make sure everything is OK.
     * @param sourceXMLfile
     * @return
     */
    private static boolean parseAndValidate(String sourceXMLfile, String appInstallationPath) 
    		throws Exception {
        SAXReader reader = new SAXReader(true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setProperty(
                "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", appInstallationPath
                        + "/schema/GsForm.xsd");

        XMLErrorHandler errorHandler = new XMLErrorHandler();
        reader.setErrorHandler(errorHandler);

        // now lets parse the document
        logger.info("Beginning parsing to check well-formedness and schema compliance.");
        Document document = reader.read(sourceXMLfile);
        logger.info("Finished parsing to check well-formedness and schema compliance, and...");
        boolean validationPassed = false;
        if (errorHandler.getErrors().elements().size() == 0) {
            validationPassed = true;
            logger.info("No errors encountered when parsing and checking against the schema.");
        } else {
        	logger.error(" ! !   Errors encountered when parsing the document and checking it against the schema   ! !");
        	logger.error(" These errors are being output to the console and parsing_errors.log");
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            writer.write(errorHandler.getErrors());
            writer.close();
            logger.error("\n = -  Error output complete  - =");
            writer = new XMLWriter(new BufferedOutputStream(new FileOutputStream(
            		appInstallationPath + "/logs/parsing_errors.log"))
            	, OutputFormat.createPrettyPrint());
            writer.write(errorHandler.getErrors());
            writer.close();
        }
        document = null;

        boolean checksPassed = false;
        if (validationPassed) {
	        /* This below is a mysterious "transformation" (check) that needs to be understood better */
	        logger.info("- - - - -");
	        logger.info("Beginning question-definition XML integrity checks.");
	        javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory
	                .newInstance();
	        javax.xml.transform.Transformer transformer = tFactory
	                .newTransformer(new javax.xml.transform.stream.StreamSource(appInstallationPath
	                        + "/xslt/checks.xslt"));
	        logger.info("Question-definition file XML integrity checking is complete.");
	
	        DOMResult domRes = new DOMResult();
	        transformer.transform(new javax.xml.transform.stream.StreamSource(sourceXMLfile), domRes);
	
	        Node doc = domRes.getNode();
	        if (((org.w3c.dom.Document) doc).getElementsByTagName("Error").getLength() <= 0) {
	        	logger.info("No errors discovered.");
	            checksPassed = true;
	        } else {
	        	logger.error("  ! ! !  Integrity errors were encountered. \n\t" +
	        			"Error messages are being output to the standard output device and integity_errors.log.");
	            transformer.transform(new javax.xml.transform.stream.StreamSource(sourceXMLfile),
	                    new javax.xml.transform.stream.StreamResult(System.out));
	            transformer.transform(new javax.xml.transform.stream.StreamSource(sourceXMLfile), 
	            		new javax.xml.transform.stream.StreamResult(new BufferedOutputStream(
	            				new FileOutputStream(appInstallationPath + "/logs/integrity_errors.log"))));
	        }
        }
        return validationPassed && checksPassed;
    }
    
    private static void generateTemplate(String type, String mech, String questionsSrcXml,
            String dbProperties, boolean actuallyUploadToDB, boolean replaceExistingTemplate,
            String questionsGroup, boolean validationOff) throws Exception {

        String appInstallationPath = FilesysUtils.getBaseDirectory(GenerateVelocityPanel.class.getName())
    		.getParentFile().getPath();

        String indivTemplateFileName = appInstallationPath + "/vm/" + questionsGroup + "_" + type + "_" + mech + ".vm";
        String qSrcFileName = appInstallationPath + "/vm/" + questionsGroup + "_" + type + "_" + mech + "_qSrc.xml";

        javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory
        	.newInstance();
    	javax.xml.transform.Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(appInstallationPath
                + "/xslt/GsFormTranslator.xslt"));
        transformer.setParameter("paramType", type);
        transformer.setParameter("paramMech", mech);
        transformer.setParameter("paramValidation", new Boolean(validationOff).toString());
        transformer.setParameter("paramGenerateVelocityStrings", "true");
        logger.info("Beginning to generate an HTML template with embedded Velocity markup for " + type + mech);
        
        /* *** This is where all the magic happens! *** */
        transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),
                new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream(indivTemplateFileName)));
        /* *** This ^ is where all the magic happens! *** */
        
        logger.info("Finished generating the file with HTML+Velocity for " + type + mech);

        logger.info("Generating XML question-source file for " + type + mech + ".");
        logger.info("    (It's quite unclear what point this serves - it just sticks the original source file ");
        logger.info("        between <GreensheetForm></GreensheetForm> tags.)");
    	transformer = tFactory.newTransformer(new StreamSource(appInstallationPath + "/xslt/GsFormXmlTranslator.xslt"));
        transformer.setParameter("paramType", type);
        transformer.setParameter("paramMech", mech);
        transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),
                new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream(
                		qSrcFileName)));	// TODO: investigate xsi prefix error
        
        if (actuallyUploadToDB) {
            TemplateLoader tl = new TemplateLoader(indivTemplateFileName, qSrcFileName, type, mech, questionsGroup, dbProperties);
            if (replaceExistingTemplate) {
                tl.replaceTemplate();
            } else {
                tl.loadNewTemplate();
            }
        }
    }
}