import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
            public void actionPerformed(ActionEvent e) {

                try{


                  System.out.println(srcMap.get(questionSrcList.getSelectedItem()));
                  System.out.println(txtType.getText());
                  System.out.println(txtMech.getText());



                  String selected = (String) srcMap.get(questionSrcList.getSelectedItem());

                  String fileName = root + "/html/" + selected + "_" + txtType.getText() + "_" + txtMech.getText() +".html";

                  String questionsSrcXml = root + "/xml/" + selected + "_Questions.xml";

                  javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

                  javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(root + "/xslt/GsFormTranslator.xslt"));
                  transformer.setParameter("paramType", txtType.getText());
                  transformer.setParameter("paramMech", txtMech.getText());

                  transformer.setParameter("paramGenerateVelocityStrings", "false");
                  transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),new javax.xml.transform.stream.StreamResult(
                                        new java.io.FileOutputStream(fileName)));

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