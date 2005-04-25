package gov.nih.nci.iscs.numsix.gsTools.gsgenerator;

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

public class GenerateValidationPanel {

    final static Properties p = new Properties();

    public GenerateValidationPanel() {

    }

    public static Component createValidationPanel(final Map srcMap) {

       
        JButton btnGenenerate = new JButton("Validate XML");

        btnGenenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    	performValidation();               
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
       
        JPanel dbOptionsPanel = new JPanel();
        dbOptionsPanel.setLayout(new BorderLayout());


        JPanel container = new JPanel();

        JPanel pane = new JPanel();

        pane.setLayout(new GridLayout(1, 0));
 
        pane.add(btnGenenerate);

        container.setLayout(new BorderLayout());
        container.add(pane, BorderLayout.CENTER);

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
        String root = System.getProperty("user.dir");

        System.out.println("User.Dir " + root);
        
        String pcQuestions = System.getProperty("user.dir") + "/xml/PC_Questions.xml";          
        String mergedQuestions = System.getProperty("user.dir") + "/xml/mergedDoc.xml"; 
        
        System.out.println("Merging the input files");
        
        // Merge the docs.
        String xsltFileName = root + "/xslt/MergeXmlFiles.xslt";
        String xsdName = root + "/schema/GsForm.xsd";
        Document mergedXmlDoc = transformXmlDoc(pcQuestions, xsltFileName, xsdName);
        writeXmlDocToFile(mergedXmlDoc, root + "/xml/mergedDoc.xml");
        
        System.out.println("\nWrote the merged XML File");
        
        SAXReader reader = new SAXReader(true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setProperty(
                "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", root
                        + "/schema/GsForm.xsd");
        
        XMLErrorHandler errorHandler = new XMLErrorHandler();
        reader.setErrorHandler(errorHandler);
        
        
        Document document = reader.read(mergedQuestions);
        
        boolean validationPassed = false;
        boolean checksPassed = false;
        if (errorHandler.getErrors().elements().size() == 0) {
            validationPassed = true;
        } else {
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            writer.write(errorHandler.getErrors());
        }
        
        System.out.println("Done Reading the input files");
    }

    private static void generateTemplates(String type, String mech, String questionsSrcXml,
            String dbProperties, boolean loadClob, boolean replace,
            String selected, boolean validationOff) throws Exception {

        String root = System.getProperty("user.dir");

        System.out.println("User.Dir " + root);
        
        String fileName = root + "/vm/" + selected + "_" + type + "_" + mech + ".vm";
        String qSrcFileName = root + "/vm/" + selected + "_" + type + "_" + mech + "_qSrc.xml";
        // Check the the xml file to make sure everything is ok.

        SAXReader reader = new SAXReader(true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setProperty(
                "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", root
                        + "/schema/GsForm.xsd");

        XMLErrorHandler errorHandler = new XMLErrorHandler();
        reader.setErrorHandler(errorHandler);

        // now lets parse the document
        System.out.println("Begin parsing to check schema");
        Document document = reader.read(questionsSrcXml);
        System.out.println("Parsing to check schema - COMPLETE");
        boolean validationPassed = false;
        boolean checksPassed = false;
        if (errorHandler.getErrors().elements().size() == 0) {
            validationPassed = true;
        } else {
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            writer.write(errorHandler.getErrors());
        }

        javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory
                .newInstance();
        javax.xml.transform.Transformer transformer = tFactory
                .newTransformer(new javax.xml.transform.stream.StreamSource(root
                        + "/xslt/checks.xslt"));

        DOMResult domRes = new DOMResult();
        transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml), domRes);

        Node doc = domRes.getNode();
        if (((org.w3c.dom.Document) doc).getElementsByTagName("Error").getLength() <= 0) {
            checksPassed = true;
        } else {
            transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),
                    new javax.xml.transform.stream.StreamResult(System.out));
        }

        // If everything checks out go ahead and transform the xml to a velocity
        // file

        if (validationPassed && checksPassed) {

            System.out.println("Sucessfully passed validation checks");
            transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(root
                    + "/xslt/GsFormTranslator.xslt"));
            transformer.setParameter("paramType", type);
            transformer.setParameter("paramMech", mech);
            transformer.setParameter("paramValidation", new Boolean(validationOff).toString());
            transformer.setParameter("paramGenerateVelocityStrings", "true");
            transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),
                    new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream(
                            fileName)));

            if (loadClob) {

            	transformer = tFactory.newTransformer(new StreamSource(root + "/xslt/GsFormXmlTranslator.xslt"));
                transformer.setParameter("paramType", type);
                transformer.setParameter("paramMech", mech);
                transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),
                        new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream(
                        		qSrcFileName)));                
                TemplateLoader tl = new TemplateLoader(fileName, qSrcFileName, type, mech, selected, p
                        .getProperty(dbProperties));

                if (replace) {
                    tl.replaceTemplate();

                } else {
                    tl.loadNewTemplate();
                }
            }

        }

    }

}