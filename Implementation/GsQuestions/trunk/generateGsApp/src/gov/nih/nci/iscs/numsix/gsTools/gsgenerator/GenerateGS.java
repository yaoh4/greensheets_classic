package gov.nih.nci.iscs.numsix.gsTools.gsgenerator;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GenerateGS {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }



        HashMap srcMap = new HashMap();
        srcMap.put("Program Competing", "PC");
        srcMap.put("Program Non-Competing", "PNC");
        srcMap.put("Specialist Competing", "SC");
        srcMap.put("Specialist Non-Competing", "SNC");
        srcMap.put("TEST","TEST");




        JFrame frame = new JFrame("GenerateGS");
        
 
        
        final JTabbedPane tabbedPane = new JTabbedPane();
        
        Component htmlPanel = GenerateHtmlPanel.createHtmlPanel(srcMap);
        Component velocityPanel = GenerateVelocityPanel.createVelocityPanel(srcMap);
        Component validationPanel = GenerateValidationPanel.createValidationPanel(srcMap);
        Component xmlPanel = GenerateReportsPanel.createValidationPanel(srcMap);

        tabbedPane.addTab("Generate HTML", htmlPanel);
        tabbedPane.add("Generate Velocity", velocityPanel);    
        tabbedPane.addTab("Validate XML", validationPanel);
        tabbedPane.addTab("Generate Reports", xmlPanel);


        GenerateGS app = new GenerateGS();



        frame.getContentPane().add(tabbedPane);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
               frame.setSize(500,340);
               frame.setResizable(false);
    }
}

