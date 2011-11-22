package gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator.FilesysUtils;

public class GenerateGS {
	
	public static final String CONFIG_DIR_NAME = "appconfig";
	
    public static void main(String[] args) {
    	
    	// System.out.println("Here is the class path:\n\t");
    	String loggerConfigLocation = FilesysUtils.getBaseDirectory(GenerateGS.class.getName()).getParent() +
    		"/" + CONFIG_DIR_NAME + "/log4j.properties"; 
    	System.out.println(" = = = = About to try to load logger configuration from " + loggerConfigLocation);
    	PropertyConfigurator.configure(loggerConfigLocation);
    	System.out.println(" = = = = Logger configuration loading complete.");
    	System.out.println("OPERATIONS WILL BE LOGGED - check " + loggerConfigLocation + 
    			" to see where the log file will be located.");
    	
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        HashMap<String, String> srcMap = new HashMap<String, String>();
        srcMap.put("Program Competing", "PC");
        srcMap.put("Program Non-Competing", "PNC");
        srcMap.put("Specialist Competing", "SC");
        srcMap.put("Specialist Non-Competing", "SNC");
        srcMap.put("DM Competing", "DC");	// Added for DM Checklist - GPMATS Enhancements
        srcMap.put("DM Non-Competing", "DNC");	// Added for DM Checklist - GPMATS Enhancements
        srcMap.put("TEST","TEST");

        JFrame frame = new JFrame("GenerateGS");

        final JTabbedPane tabbedPane = new JTabbedPane();

        Component htmlPanel = GenerateHtmlPanel.createHtmlPanel(srcMap);
        Component velocityPanel = GenerateVelocityPanel.createVelocityPanel(srcMap);
        Component validationPanel = GenerateValidationPanel.createValidationPanel(srcMap);
        Component xmlPanel = GenerateReportsPanel.createReportsPanel(srcMap);

        tabbedPane.addTab("Generate Previews", htmlPanel);
        tabbedPane.add("Upload the Templates", velocityPanel);
        tabbedPane.addTab("Validate XML", validationPanel);
        tabbedPane.addTab("Generate Reports", xmlPanel);

        // GenerateGS app = new GenerateGS();

        frame.getContentPane().add(tabbedPane);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setSize(640,340);
        frame.setLocation(80, 80);
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
