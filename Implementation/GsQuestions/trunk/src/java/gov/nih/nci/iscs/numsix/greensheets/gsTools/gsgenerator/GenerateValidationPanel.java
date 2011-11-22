package gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator;

/*
 * Created on Mar 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author kkanchinadam
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import org.dom4j.*;
import org.dom4j.io.*;
import org.dom4j.util.*;
import org.w3c.dom.Node;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.xml.sax.helpers.*;

import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

public class GenerateValidationPanel {

    final static Properties p = new Properties();
    
    private static Logger logger = Logger.getLogger(GenerateVelocityPanel.class);

    public GenerateValidationPanel() {

    }

    public static Component createValidationPanel(final Map<String, String> srcMap) {
    	
        JButton btnGenenerate = new JButton("Validate XML files");
        JTextArea label = new JTextArea("\nHit the button to merge all question-definition XML files "
        		+ "(excluding DM) together and validate their XML \nagainst the schema.");
        label.setEditable(false);
        label.setLineWrap(true); label.setWrapStyleWord(true);
        label.setMargin(new Insets(5, 5, 5, 5));
        JButton btnClose = new JButton("Close");

        btnGenenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                	performValidation();               
                } 
                catch (Exception ex) {
                    logger.error(ex);
                }
            }
        });
        btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
       
        JPanel container = new JPanel();
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(2, 0));
        pane.add(btnGenenerate);
        pane.add(btnClose);
        label.setBackground(pane.getBackground());
        container.setLayout(new BorderLayout());
        container.add(label, BorderLayout.CENTER);
        container.add(pane, BorderLayout.SOUTH);
        return container;
    }
    
    private static Document transformXmlDoc(String pcqFileName, String xsltFileName, String xsdName) throws Exception{
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xsltFileName));
        
        SAXReader reader = new SAXReader(true);
        XMLErrorHandler errorHandler = new XMLErrorHandler();
        reader.setErrorHandler(errorHandler);
     //   reader.setFeature("http://apache.org/xml/features/validation/schema", false);
    //    reader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", xsdName);
        
        Document sourceDoc = reader.read(pcqFileName);
        if (errorHandler.getErrors().elements().size() > 0) {
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            writer.write(errorHandler.getErrors());
        }
        
        // Lets style it..
        DocumentSource source = new DocumentSource(sourceDoc);
        DocumentResult result = new DocumentResult();
        transformer.transform(source, result);
        
        //Return the transformed Document
        Document transformedDoc = result.getDocument();
        return transformedDoc;        
    }
    
    private static void writeXmlDocToFile(Document xmlDoc, String fileName) throws IOException{
        XMLWriter writer = new XMLWriter(new FileWriter(fileName));
        writer.write(xmlDoc);
        writer.close();
    }
    
    private static void performValidation() throws Exception {
    	String appInstallationDir = FilesysUtils.getBaseDirectory(GenerateValidationPanel.class.getName()).getParent();
        
        String pcQuestions = appInstallationDir + "/xml/PC_Questions.xml";          
        String mergedQuestions = appInstallationDir + "/xml/mergedDoc.xml"; 
        
        logger.info("About to merg the question-definition source XML files (excluding DM) into one, \n\t" +
        		"in order to validate everything - including a check that all question IDs are unique.");
        logger.debug("Merging the input files");
        
        // Merge the docs.
        String xsltFileName = appInstallationDir + "/xslt/MergeXmlFiles.xslt";
        String xsdName = appInstallationDir + "/schema/GsForm.xsd";
        
        Document mergedXmlDoc = transformXmlDoc(pcQuestions, xsltFileName, xsdName);
        writeXmlDocToFile(mergedXmlDoc, appInstallationDir + "/xml/mergedDoc.xml");
        
        logger.debug("Wrote the merged XML File");
        
        SAXReader reader = new SAXReader(true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setProperty(
                "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", 
                		appInstallationDir + "/schema/GsForm.xsd");
        
        XMLErrorHandler errorHandler = new XMLErrorHandler();
        reader.setErrorHandler(errorHandler);
        
        logger.info("   About to start validating the merged XML file.");
        logger.info("   LOOK FOR THE OUTPUT IN errors.log\n");
        Document document = reader.read(mergedQuestions);
        
        boolean validationPassed = false;
        if (errorHandler.getErrors().elements().size() == 0) {
            validationPassed = true;
        } else {
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            writer.setOutputStream(new BufferedOutputStream(new FileOutputStream(new File(
            		appInstallationDir + "/logs/errors.log"))));
            writer.write(errorHandler.getErrors());
            writer.setOutputStream(System.out);
            writer.write(errorHandler.getErrors());
            // ^ writing the same output twice - to the file and to console.
            logger.error("  ! ! !   Validation errors were written to errors.log   ! ! !");
        }
        
        logger.info(" * * *  Validation passed?  " + validationPassed);
        logger.info("Done processing (validating) the big merged XML file.");
        
        document = null;
    }

}