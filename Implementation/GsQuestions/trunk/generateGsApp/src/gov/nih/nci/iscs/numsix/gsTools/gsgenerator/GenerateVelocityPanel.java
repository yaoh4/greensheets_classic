package gov.nih.nci.iscs.numsix.gsTools.gsgenerator;

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

import org.xml.sax.helpers.*;

public class GenerateVelocityPanel {

    final static Properties p = new Properties();

    public GenerateVelocityPanel() {

    }

    public static Component createVelocityPanel(final Map srcMap) {

        final String root = System.getProperty("user.dir");

        try {
            p.load(new FileInputStream("gengsapp.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JComboBox questionSrcList = new JComboBox(srcMap.keySet().toArray());
        final JComboBox targetDatabaseList = new JComboBox();
        targetDatabaseList.addItem("NONE");
        targetDatabaseList.addItem("DEV");
        targetDatabaseList.addItem("TEST");
        targetDatabaseList.addItem("TRAINING");
        targetDatabaseList.addItem("PROD");

        final JRadioButton rbLoadClob = new JRadioButton("Load Single Template");
        rbLoadClob.setEnabled(false);
        final JCheckBox cbxReplace = new JCheckBox("Replace Existing Clob");
        cbxReplace.setEnabled(false);
        final JRadioButton rbLoadAll = new JRadioButton("Load All Templates for Source");
        rbLoadAll.setEnabled(false);
        final JCheckBox cbxValidationOn = new JCheckBox("Question Validation On");
        cbxValidationOn.setEnabled(false);

        final JTextField txtType = new JTextField(4);
        final JTextField txtMech = new JTextField(4);

        JButton btnClose = new JButton("Close");
        JButton btnGenenerate = new JButton("Generate Velocity");

        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });


        targetDatabaseList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JComboBox cb = (JComboBox)e.getSource();
                String sel = (String)cb.getSelectedItem();
                System.out.print("---- " + sel);
                if(!sel.equalsIgnoreCase("NONE")){
                    rbLoadClob.setEnabled(true);
                    rbLoadAll.setEnabled(true);
                    cbxReplace.setEnabled(true);
                    cbxValidationOn.setEnabled(true);
                }else{
                    rbLoadClob.setEnabled(false);
                    rbLoadAll.setEnabled(false);
                    cbxReplace.setEnabled(false);
                    cbxValidationOn.setEnabled(false);

                }

            }
        } );

        btnGenenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {

                    System.out.println(srcMap.get(questionSrcList.getSelectedItem()));
                    System.out.println(txtType.getText());
                    System.out.println(txtMech.getText());
                    String type = txtType.getText().trim();
                    String mech = txtMech.getText().trim();

                    String questSrc = (String) srcMap.get(questionSrcList.getSelectedItem());
                    String dbProperties = (String) targetDatabaseList.getSelectedItem();

                    String questionsSrcXml = System.getProperty("user.dir") + "/xml/" + questSrc
                            + "_Questions.xml";

                    if (rbLoadAll.isSelected()) {
                        System.out.println("Bulk Load " + p.getProperty(questSrc));

                        FileReader fr = new FileReader(p.getProperty(questSrc));
                        BufferedReader br = new BufferedReader(fr);
                        String s = null;
                        while ((s = br.readLine()) != null) {
                            type = s.substring(0, 1);
                            mech = s.substring(2);
                            System.out.println("typemech " + type + mech);
                            System.out.println(rbLoadClob.isSelected());
                            generateTemplates(type, mech, questionsSrcXml, dbProperties,
                                    true, cbxReplace.isSelected(), questSrc, cbxValidationOn.isSelected());

                        }
                        br.close();
                        fr.close();
                    } else if (rbLoadClob.isSelected()){

                        generateTemplates(type, mech, questionsSrcXml, dbProperties, true, cbxReplace.isSelected(),
                                questSrc, cbxValidationOn.isSelected());
                    }else{
                        generateTemplates(type, mech, questionsSrcXml, dbProperties, false, false,
                                questSrc, cbxValidationOn.isSelected());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbLoadClob);
        bg.add(rbLoadAll);

        JPanel dbOptionsPanel = new JPanel();
        dbOptionsPanel.setLayout(new BorderLayout());

        JPanel dpOptPanLeft = new JPanel();
        dpOptPanLeft.setLayout(new GridLayout(2, 0));
        dpOptPanLeft.add(rbLoadClob);
        dpOptPanLeft.add(rbLoadAll);


        JPanel dbOptPanRight = new JPanel();
        dbOptPanRight.setLayout(new GridLayout(2, 0));
        dbOptPanRight.add(cbxValidationOn);
        dbOptPanRight.add(cbxReplace);


        dbOptionsPanel.add(dpOptPanLeft,BorderLayout.WEST);
        dbOptionsPanel.add(dbOptPanRight,BorderLayout.EAST);

        JPanel container = new JPanel();

        JPanel labels = new JPanel();

        labels.setLayout(new GridLayout(7, 0));
        labels.add(new JLabel("Question Source"));
        labels.add(new JLabel("Specify Type"));
        labels.add(new JLabel("Specify Mech"));
        labels.add(new JLabel("Select DB"));
        labels.add(new JLabel("Load Options"));
        labels.add(new JLabel("Hit the Button"));
        labels.add(new JLabel("I'm Done"));

        JPanel pane = new JPanel();

        pane.setLayout(new GridLayout(7, 0));
        pane.add(questionSrcList);
        pane.add(txtType);
        pane.add(txtMech);
        pane.add(targetDatabaseList);
        pane.add(dbOptionsPanel);
        pane.add(btnGenenerate);
        pane.add(btnClose);

        container.setLayout(new BorderLayout());
        container.add(labels, BorderLayout.WEST);
        container.add(pane, BorderLayout.CENTER);

        return container;
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
                    new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream( fileName)));

            System.out.println("Generating Questions from " + type + " " + mech);
        	transformer = tFactory.newTransformer(new StreamSource(root + "/xslt/GsFormXmlTranslator.xslt"));
            transformer.setParameter("paramType", type);
            transformer.setParameter("paramMech", mech);
            transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),
                    new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream(
                    		qSrcFileName)));

            if (loadClob) {
                TemplateLoader tl = new TemplateLoader(fileName, qSrcFileName, type, mech, selected, p.getProperty(dbProperties));
                if (replace) {
                    tl.replaceTemplate();
                } else {
                    tl.loadNewTemplate();
                }
            }
        }
    }
}