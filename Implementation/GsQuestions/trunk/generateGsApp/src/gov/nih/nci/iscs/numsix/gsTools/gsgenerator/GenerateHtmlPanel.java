package gov.nih.nci.iscs.numsix.gsTools.gsgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


public class GenerateHtmlPanel {


    public GenerateHtmlPanel(){
    }

    public static Component createHtmlPanel(final Map srcMap) {

        final String root = System.getProperty("user.dir");

        final JComboBox questionSrcList = new JComboBox(srcMap.keySet().toArray());

        final JTextField txtType = new JTextField(4);
        final JTextField txtMech = new JTextField(4);


        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
              System.exit(0);
            }

        });

        JButton button = new JButton("Generate Html");
        button.addActionListener(new ActionListener() {
            private void generateHtmlFiles(String root, String selected, String fileName) throws Exception {
                System.out.println("Type Mech Source File name is = " + fileName);
                String questionsSrcXml = root + "/xml/" + selected + "_Questions.xml";
                
                FileReader fr = new FileReader(new File(fileName));
                BufferedReader br = new BufferedReader(fr);
                String line;
                
                int count = 0;
                
                javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
                javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(root + "/xslt/HtmlGenerator.xslt"));

                while ((line = br.readLine()) != null) {
                    String[] temp = line.split(",");
                    String qType = temp[0];
                    String qMech = temp[1];

                    String htmlFileName = root + "/html/" + selected + "_" + qType + "_" + qMech +".html"; 
                    transformer.setParameter("paramType", qType);
                    transformer.setParameter("paramMech", qMech);
                    transformer.setParameter("paramGenerateVelocityStrings", "false");
                    transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),new javax.xml.transform.stream.StreamResult(
                            new java.io.FileOutputStream(htmlFileName)));
                    count++;
                }                      
                br.close();
                System.out.println("Number of Files Generated = "+ count);                
            }
            
            public void actionPerformed(ActionEvent e) {

                try{
                  //System.out.println(srcMap.get(questionSrcList.getSelectedItem()));
                  System.out.println(txtType.getText());
                  System.out.println(txtMech.getText());

                  String selected = (String) srcMap.get(questionSrcList.getSelectedItem());

                  String fileName = root + "/html/" + selected + "_" + txtType.getText() + "_" + txtMech.getText() +".html";

                  String questionsSrcXml = root + "/xml/" + selected + "_Questions.xml";

                  javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

                  javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(root + "/xslt/HtmlGenerator.xslt"));
                  String qType = txtType.getText();
                  String qMech = txtMech.getText();
                  transformer.setParameter("paramType", qType);
                  transformer.setParameter("paramMech", qMech);

                  transformer.setParameter("paramGenerateVelocityStrings", "false");
                  if ((qType.equalsIgnoreCase("")) && (qMech.equalsIgnoreCase(""))) {
                      String pcqFileName = root + "/qlPC.txt";
                      String pncqFileName = root + "/qlPNC.txt";
                      String scqFileName = root + "/qlSC.txt";
                      String sncqFileName = root + "/qlSNC.txt";
                      
                      System.out.println("Generating all HTML Files....START");
                      this.generateHtmlFiles(root, "PC", pcqFileName); 
                      this.generateHtmlFiles(root, "PNC", pncqFileName); 
                      this.generateHtmlFiles(root, "SC", scqFileName); 
                      this.generateHtmlFiles(root, "SNC", sncqFileName); 
                      System.out.println("Generation of HTML files -- END");
                  }
                  else {
                      transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),new javax.xml.transform.stream.StreamResult(
                                        new java.io.FileOutputStream(fileName)));
                  }

                }catch(Exception ex){
                  ex.printStackTrace();
                }

              }
        });

        JPanel container = new JPanel();

        JPanel labels = new JPanel();
        labels.setLayout(new GridLayout(5,0));
        labels.add(new JLabel("Question Source"));
        labels.add(new JLabel("Select Type"));
        labels.add(new JLabel("Select Mech"));
        labels.add(new JLabel("Hit the Button"));
        labels.add(new JLabel("I'm Done"));

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(5, 0));
        pane.add(questionSrcList);
        pane.add(txtType);
        pane.add(txtMech);
        pane.add(button);
        pane.add(btnClose);

        container.setLayout(new BorderLayout());
        container.add(labels,BorderLayout.WEST);
        container.add(pane, BorderLayout.CENTER);

        return container;
    }


   




}