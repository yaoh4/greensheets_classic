package gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator;

import javax.swing.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import java.awt.*;
import java.awt.event.*;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.*;
import java.io.*;

import oracle.sql.BLOB;

import org.w3c.dom.*;
//import org.apache.xerces.dom.*;
import org.apache.xerces.parsers.*;
//import org.apache.xml.serialize.*;
//import org.dom4j.*;
////import org.dom4j.DocumentException;
////import org.dom4j.DocumentHelper;
//import org.dom4j.io.*;
////import org.xml.sax.InputSource;
////import org.dom4j.Document;
////import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;

//import javax.xml.parsers.*;
import org.apache.log4j.Logger;

import gov.nih.nci.iscs.numsix.greensheets.gsTools.gsgenerator.FilesysUtils;

public class GenerateReportsPanel {

	private static Properties p;
	private static String fileLocation = "";
	private static String typeMechComboListFileLocation = ""; 
	private static Logger logger = Logger.getLogger(GenerateReportsPanel.class);;
	
    public GenerateReportsPanel() {

    	p = FilesysUtils.loadProperties(GenerateReportsPanel.class.getName(), "gengsapp.properties");
    	
    	typeMechComboListFileLocation = p.getProperty("typemech.list.files.dir");
    	if ( typeMechComboListFileLocation!=null && !typeMechComboListFileLocation.endsWith(System.getProperty("file.separator")) ) {
    		typeMechComboListFileLocation += System.getProperty("file.separator");
    	}
    	fileLocation = FilesysUtils.getBaseDirectory(this.getClass().getName()).getParent();
    }

    public static Component createReportsPanel(final Map<String, String> srcMap) {
     	
        JButton btnGenenerate = new JButton("Generate Reports");
        btnGenenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    performValidation();               
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        });
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        
        JPanel dbOptionsPanel = new JPanel();
        dbOptionsPanel.setLayout(new BorderLayout());

        JPanel container = new JPanel();
        JPanel pane = new JPanel();

        JTextArea label = new JTextArea("Click the button to genereate some report. \n" +
        		"What report will run is currently hard-coded in the Java source code. \n" +
        		"You'll have to edit the code and recompile the software to change it.");
        label.setBackground(pane.getBackground());
        label.setEditable(false);
        label.setMargin(new Insets(5,5,5,5));
        label.setLineWrap(true); label.setWrapStyleWord(true);
        
        pane.setLayout(new GridLayout(0, 1));
        pane.add(btnGenenerate);
        pane.add(btnClose);

        container.setLayout(new BorderLayout());
        container.add(label, BorderLayout.NORTH);
        container.add(pane, BorderLayout.SOUTH);

        return container;
    }
    private static void performValidation() throws Exception { 
        

//        System.out.println("User.Dir " + root);
        logger.info("Starting the Task ....");
        
        // createNewQuestionSrc();
 
        //createQuestionCountReport();
        //System.out.println(replace ("ABCDEFGHIJKLMNOPQRSTUVWXYZ" , 'a', 'a'));
        // System.out.println("Original word : " + "Bill Me Later");
        // System.out.println("New Password : " + randomPassword("Bill Me Later"));
        //createReportOfQuestionsDisplayed();

        /*                      
        String xsdName = root + "/schema/GsForm.xsd";
        System.out.println("XSD Name " + xsdName);*/
        
     
        logger.info("Finished the Task.");
    }
    
    
    static void createReportOfQuestionsDisplayed() throws Exception {
//    	System.out.println("root =" + root);	// GPMATS
    	    	
    	String pcqFileName = typeMechComboListFileLocation + "qlPC.txt";
        String pncqFileName = typeMechComboListFileLocation + "qlPNC.txt";
        String scqFileName = typeMechComboListFileLocation + "qlSC.txt";
        String sncqFileName = typeMechComboListFileLocation + "qlSNC.txt";
        
        System.out.println("Program Competing");
        createReportOfQuestionsDisplayed(pcqFileName,"PC");
		System.out.println("Program Non Competing");
		createReportOfQuestionsDisplayed(pncqFileName,"PNC");
		System.out.println("Specialist Competing");
		createReportOfQuestionsDisplayed(scqFileName,"SC");
		System.out.println("Specialist Non Competing");
		createReportOfQuestionsDisplayed(sncqFileName,"SNC");
        
    }
    static void createReportOfQuestionsDisplayed(String fileName, String selected) throws Exception {
    	//String newSrcFileName = root + "/xml/" + "NewQuestionSrc" + "_"+ selected +".xml";
    	String newSrcFileName = fileLocation + "xml/" + selected +"_Questions.xml";
//    	System.out.println(questionsSrcXml + "  " + newSrcFileName);
    	
    	FileReader fr = new FileReader(new File(fileName));
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        int count = 0;
        
        System.out.println("Type;Mech;QuestionsDeclared;QuestionsDisplayedUsingOLD;QuestionsDisplayedUsingNew");
        

       while ((line = br.readLine()) != null) {
            String[] temp = line.split(",");
            String qType = temp[0];
            String qMech = temp[1];
           
                //System.out.println(qType + "," + qMech);
                
                int origSrcCount = countQOSrc(newSrcFileName,qType,qMech);
                //System.out.println("The following questions in " + qType + " and " + qMech + " is missing the GrantTypemech/TypeMech." );
                javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
                javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(fileLocation + "xslt/Transform_oldQuery.xslt"));
                String oldQueryFileName = fileLocation + "oldQ/" + selected + "_" + qType + "_" + qMech +".xml"; 
                transformer.setParameter("paramType", qType);
                transformer.setParameter("paramMech", qMech);
                transformer.transform(new javax.xml.transform.stream.StreamSource(newSrcFileName),new javax.xml.transform.stream.StreamResult(
                		new java.io.FileOutputStream(oldQueryFileName)));
                
     
                int oldQCount = countQuestionsXML(oldQueryFileName);
                
                javax.xml.transform.TransformerFactory tFactoryN = javax.xml.transform.TransformerFactory.newInstance();
                javax.xml.transform.Transformer transformerN = tFactoryN.newTransformer (new javax.xml.transform.stream.StreamSource(fileLocation + "xslt/Transform_newQuery.xslt"));
                String newQueryFileName = fileLocation + "newQ/" + selected + "_" + qType + "_" + qMech +".xml"; 
                transformerN.setParameter("paramType", qType);
                transformerN.setParameter("paramMech", qMech);
                transformerN.transform(new javax.xml.transform.stream.StreamSource(newSrcFileName),new javax.xml.transform.stream.StreamResult(
                		new java.io.FileOutputStream(newQueryFileName)));
              
                int newQCount = countQuestionsXML(newQueryFileName); 
            
            System.out.println(qType+";"+qMech+";"+origSrcCount+";"+oldQCount+";"+newQCount+";");
            
            count++;
        }                      
        br.close();
    }
    static void createQuestionCountReport () throws Exception {
    	String pcqFileName = typeMechComboListFileLocation + "qlPC.txt";
        String pncqFileName = typeMechComboListFileLocation + "qlPNC.txt";
        String scqFileName = typeMechComboListFileLocation + "qlSC.txt";
        String sncqFileName = typeMechComboListFileLocation + "qlSNC.txt";
        
		System.out.println("Program Competing");
		createQuestionCountReport(pcqFileName,"PC");
		System.out.println("Program Non Competing");
		createQuestionCountReport(pncqFileName,"PNC");
		System.out.println("Specialist Competing");
		createQuestionCountReport(scqFileName,"SC");
		System.out.println("Specialist Non Competing");
		createQuestionCountReport(sncqFileName,"SNC");
    	
    }
    
    static void createNewQuestionSrc ()throws Exception {
    	String pcqFileName = typeMechComboListFileLocation + "qlPC.txt";
        String pncqFileName = typeMechComboListFileLocation + "qlPNC.txt";
        String scqFileName = typeMechComboListFileLocation + "qlSC.txt";
        String sncqFileName = typeMechComboListFileLocation + "qlSNC.txt";
        
		System.out.println("Program Competing");
		createNewQuestionSrc(pcqFileName,"PC");
		System.out.println("Program Non Competing");
		createNewQuestionSrc(pncqFileName,"PNC");
		 System.out.println("Specialist Competing");
		 createNewQuestionSrc(scqFileName,"SC");
		System.out.println("Specialist Non Competing");
		createNewQuestionSrc(sncqFileName,"SNC");
		 

    }
    static void createNewQuestionSrc(String fileName, String selected) throws Exception {
    	System.out.println(selected + fileName);
    	String questionsSrcXml = fileLocation + "xml/" + selected + "_Questions.xml";
    	FileReader fr = new FileReader(new File(fileName));
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        
        String newSrcFileName = fileLocation + "xml/" + "NewQuestionSrc" + "_"+ selected +".xml";
        System.out.println("Creating New Src file"+ newSrcFileName +" for " + selected + " Starts.....");
        String tempFileName = "";
        byte[] srcfile =  getBytesFromFile(new File(questionsSrcXml));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ByteArrayInputStream in = new ByteArrayInputStream(srcfile);

        while ((line = br.readLine()) != null) {
        	String[] temp = line.split(",");
            String qType = temp[0];
            String qMech = temp[1];
            System.out.println(qType + "," + qMech);
            out = new ByteArrayOutputStream();
            tempFileName = fileLocation + "temp/" + qType + "_" + qMech + "_T"+ selected +".xml";
            javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(fileLocation + "xslt/AddMissingTypeMech.xslt"));
            
            transformer.setParameter("paramType", qType);
            transformer.setParameter("paramMech", qMech);
            System.out.println("Starting Transformation");
            transformer.transform(new javax.xml.transform.stream.StreamSource(in),
            		new javax.xml.transform.stream.StreamResult(
                    		new java.io.FileOutputStream(tempFileName)));
            
            
            
           in = new ByteArrayInputStream(getBytesFromFile(new File(tempFileName)));
            //in = new ByteArrayInputStream(out.toByteArray());
           
            System.out.println("Finished Transformation");
        }
        
//        FileWriter fw = new FileWriter(new File (newSrcFileName) );
//        
//        fw.write(out.toString());
        copy (new File(tempFileName), new File(newSrcFileName));
        System.out.println("Finished Temp Writing ");
        
    }
	static void copy (File fromFile, File toFile) throws Exception {
		FileInputStream from = null;
	    FileOutputStream to = null;
	    try {
	      from = new FileInputStream(fromFile);
	      to = new FileOutputStream(toFile);
	      byte[] buffer = new byte[4096];
	      int bytesRead;

	      while ((bytesRead = from.read(buffer)) != -1)
	        to.write(buffer, 0, bytesRead); // write
	    } finally {
	      if (from != null)
	        try {
	          from.close();
	        } catch (IOException e) {
	          ;
	        }
	      if (to != null)
	        try {
	          to.close();
	        } catch (IOException e) {
	          ;
	        }
	    }
		
	}
	
    static void writeTempFile (String tempFileName, ByteArrayOutputStream out ) throws Exception {
    	FileWriter fw = new FileWriter(new File (tempFileName) );
        
        fw.write(out.toString());
        System.out.println("Finished Temp Writing ");
        
    }
  
    /* 
     * Convert File to Bytes
     * */
     static byte[] getBytesFromFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);
      // Get the size of the file
       long length = file.length();
        // You cannot create an array using a long type.
       // It needs to be an int type.
       // Before converting to an int type, check
       // to ensure that file is not larger than Integer.MAX_VALUE.
       if (length > Integer.MAX_VALUE) {

            // File is too large

        }
       // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
        // Read in the bytes
       int offset = 0;
       int numRead = 0;

        while (offset < bytes.length

               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {

            offset += numRead;
        }
       // Ensure all the bytes have been read in
        if (offset < bytes.length) {
           throw new IOException("Could not completely read file "+ file.getName());
        }
        // Close the input stream and return bytes
       is.close();
  
       return bytes;
   }
    
    static void createOrphanQuestionReport(String fileName, String selected) throws Exception {
    	
//    	System.out.println(selected + fileName);
    	String questionsSrcXml = fileLocation + "xml/" + selected + "_Questions.xml";
    	
    	FileReader fr = new FileReader(new File(fileName));
        BufferedReader br = new BufferedReader(fr);
 
        
        String line;

        System.out.println("Reports for " + selected + " Starts.....");
        
        byte[] srcfile = getBytesFromFile(new File(questionsSrcXml));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ByteArrayInputStream in = new ByteArrayInputStream(srcfile);
        
        while ((line = br.readLine()) != null) {
        	
            String[] temp = line.split(",");
            String qType = temp[0];
            String qMech = temp[1];
            System.out.println(qType + "," + qMech);
            
            //System.out.println("The following questions in " + qType + " and " + qMech + " is missing the GrantTypemech/TypeMech." );
            javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(fileLocation + "xslt/CheckMissingQuestions.xslt"));
            String tempFileName = fileLocation + "qtemp/" + selected + "_" + qType + "_" + qMech +".xml"; 
            transformer.setParameter("paramType", qType);
            transformer.setParameter("paramMech", qMech);
            transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),new javax.xml.transform.stream.StreamResult(
            		new java.io.FileOutputStream(tempFileName)));
            
 
            //printQuestions(tempFileName);
            
            System.out.println(qType + "," + qMech);
            System.out.println("");
            
        }                      
        br.close();
        System.out.println("\nFinished Creating Reports for " + selected + ".");
//        System.out.println("Number of Files Generated = "+ count);
    }
    static void createQuestionCountReport(String fileName, String selected) throws Exception {
    	
//    	System.out.println(selected + fileName);
    	String questionsSrcXml = fileLocation + "xml/" + selected + "_Questions.xml";
    	String newSrcFileName = fileLocation + "xml/" + "NewQuestionSrc" + "_"+ selected +".xml";
    	
//    	System.out.println(questionsSrcXml + "  " + newSrcFileName);
    	
    	FileReader fr = new FileReader(new File(fileName));
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        int count = 0;
        
        System.out.println("Type;Mech;OriginalSrcFile;QuestionsDisplayed;NewSrcFile;QuestionsDisplayed");
        

       while ((line = br.readLine()) != null) {
            String[] temp = line.split(",");
            String qType = temp[0];
            String qMech = temp[1];
            
//            System.out.println("Get DOCS");
            String origFile = getSrcCovertedtoDBformat(questionsSrcXml,selected,qType,qMech, "oldQ");
            String newFile = getSrcCovertedtoDBformat(newSrcFileName,selected,qType,qMech,"newQ");
            
//            System.out.println("Got Docs");
            int origSrcCount,noQinOrigSrc,newSrcCount,noQinNewSrc = 0;
            
//            DOMParser parser = new DOMParser();
//            parser.parse(fileName);
//            org.w3c.dom.Document document = (org.w3c.dom.Document )parser.getDocument();
            
//            System.out.println("Get The Count Displayed in Orig File");
            origSrcCount = countQOSrc(questionsSrcXml,qType,qMech);
            
//            System.out.println("Get The Count in Orig File");
            noQinOrigSrc = countQ(origFile,selected,qType,qMech,"oldQ","New.xslt");
           
         
//            System.out.println("Get The Count Displayed in New File");
            newSrcCount = countQOSrc(newSrcFileName,qType,qMech);
            
//            System.out.println("Get The Count in New File");
            noQinNewSrc = countQ(newFile,selected,qType,qMech,"newQ","New.xslt");
            
            
            
            
            System.out.println(qType+";"+qMech+";"+origSrcCount+";"+noQinOrigSrc+";"+newSrcCount+";"+noQinNewSrc);
            
            count++;
        }                      
        br.close();
//        System.out.println("Number of Files Generated = "+ count);
    }
    static String getSrcCovertedtoDBformat( String file, String selected,String type, String mech, String Dir) throws Exception {
//    	 Convert the src file to format that is in database..
    	//String questionsSrcXml = root + "/xml/" + selected + "_Questions.xml";
    	String xsltSrc = fileLocation + "xslt/GsFormXmlTranslator.xslt";
//    	System.out.println("xslt");
    	
        String newFileName = fileLocation + Dir +"/" + selected + "QDisplayed_" + type + "_" + mech +".xml";
    	
//        System.out.println("got the Doc");
//       System.out.println("Create Factory and Transformer");
    	javax.xml.transform.TransformerFactory factory = (javax.xml.transform.TransformerFactory)javax.xml.transform.TransformerFactory.newInstance();
    	javax.xml.transform.Transformer transformer = ((javax.xml.transform.TransformerFactory)factory).newTransformer(new javax.xml.transform.stream.StreamSource(xsltSrc));
//    	System.out.println("got the source and result");
		// now lets style the given document
    	transformer.transform(new javax.xml.transform.stream.StreamSource(file),new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream(newFileName)));
//    	System.out.println("transform the doce");
		// return the transformed document
		
		
		return newFileName;
    	
    }
    
    static int countQOSrc(String questionsSrcXml, String type, String mech)throws Exception {
    
    	return letsCount(questionsSrcXml,type,mech);
    }
    
    static int countQ(org.dom4j.Document doc, String selected,String type, String mech,String xslt)throws Exception {
//    	System.out.println("Begin : countQNewQuery");
    	javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(fileLocation + "xslt/"+xslt));//new.xslt
        String newFileName = fileLocation + "newQ/" + selected + "_" + type + "_" + mech +".xml"; 
        transformer.setParameter("paramType", type);
        transformer.setParameter("paramMech", mech);
//        transformer.setParameter("paramGenerateVelocityStrings", "false");
//        System.out.println("Transforming");
        
        transformer.transform(new org.dom4j.io.DocumentSource(doc),new javax.xml.transform.stream.StreamResult(new java.io.FileOutputStream(newFileName)));
//        transformer.transform(new javax.xml.transform.stream.StreamSource(questionsSrcXml),new javax.xml.transform.stream.StreamResult(       new java.io.FileOutputStream(newFileName)));
    	
//        System.out.println("End : countQNewQuery");
    	return letsCount(newFileName,type,mech);
    }
    static int countQ(String SrcXml2bCounted,String selected,String type, String mech, String Dir , String xslt)throws Exception {
    	
//   	System.out.println("Begin : countQ " + SrcXml2bCounted);
    	javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = tFactory.newTransformer (new javax.xml.transform.stream.StreamSource(fileLocation + "xslt/"+xslt));//old.xslt
        String tempFileName = fileLocation + Dir+"/" + selected + "_" + type + "_" + mech +".xml"; 
        transformer.setParameter("paramType", type);
        transformer.setParameter("paramMech", mech);
//        transformer.setParameter("paramGenerateVelocityStrings", "false");
/*        transformer.transform(new org.dom4j.io.DocumentSource(doc),new javax.xml.transform.stream.StreamResult(
                new java.io.FileOutputStream(tempFileName)));*/
        transformer.transform(new javax.xml.transform.stream.StreamSource(SrcXml2bCounted),new javax.xml.transform.stream.StreamResult(
        		new java.io.FileOutputStream(tempFileName)));

    	
//        System.out.println("End : countQ " + SrcXml2bCounted);
    	return letsCount(tempFileName,type,mech);
    }
    
    static int letsCount(String fName, String type, String mech) throws Exception {
        
        
        //printElements(doc);
        int count = countQuestions(fName, type, mech);
        //int count = countTypeMechs(fName,type,mech);
       //System.out.println("Total Count of type and Mech in file( "+ fName + " ) = " + count);
        
        
        return count;
    }
/*    static void printElements(Document doc)
    {
       NodeList nl = doc.getElementsByTagName("*");
       Node n;
          
       for (int i=0; i<nl.getLength(); i++)
       {
          n = nl.item(i);
          System.out.print(n.getNodeName() + " ");
       }

       System.out.println();
    }*/
    static void printQuestions(String fileName) throws Exception
    {
    	DOMParser parser = new DOMParser();
        parser.parse(fileName);
        org.w3c.dom.Document document = (org.w3c.dom.Document )parser.getDocument();
       //Document doc = (org.dom4j.Document)document;
        org.w3c.dom.NodeList nl = document.getElementsByTagName("*");
       org.w3c.dom.Element e;
       org.w3c.dom.Node n;
       org.w3c.dom.NamedNodeMap nnm;

       String attrname;
       String attrval;
       int i, len ;
       
       System.out.println("");
       len = nl.getLength();
       for (int j=0; j < len; j++)
       {
		  e = (org.w3c.dom.Element)nl.item(j);
		  System.out.println(e.getTagName() + ": ");
		  nnm = e.getAttributes();
		  if (nnm != null)
		  {
	         for (i=0; i<nnm.getLength(); i++)
		         {
		            n = nnm.item(i);
		            attrname = n.getNodeName();
		            attrval = n.getNodeValue();
		            System.out.print(" " + attrname + " = " + attrval);
			                
			      }
	         //System.out.println("");
		   }
		  System.out.println("");
       }
       //System.out.println("");
    }
    static int countQuestionsXML (String fName) throws Exception {
    	DOMParser parser = new DOMParser();

        parser.parse(fName);
        //System.out.println(fName);
        org.w3c.dom.Document doc = (org.w3c.dom.Document)parser.getDocument();
        
        NodeList list = doc.getElementsByTagName("questionID");
       // System.out.println("//questionID");
        //System.out.println("Number of Question Id: " + list.getLength());
        
        return list.getLength();
    }
    
    static int countQuestions(String fName, String type, String Mech) throws Exception
    {
    	DOMParser parser = new DOMParser();

        parser.parse(fName);
        
        org.w3c.dom.Document doc = (org.w3c.dom.Document)parser.getDocument();
        
        org.w3c.dom.NodeList nl = doc.getElementsByTagName("*");
    	org.w3c.dom.Element e;
    	org.w3c.dom.Node n;
    	org.w3c.dom.NamedNodeMap nnm;

       String attrname;
       String attrval;
       int i, len , tc=0;
       
      
       len = nl.getLength();
       for (int j=0; j < len; j++)
       {
		  e = (org.w3c.dom.Element)nl.item(j);
		  if (e.getTagName().equals("GrantTypeMechs"))
		  {
//		      System.out.println(e.getTagName() + ":");
		      tc=tc + checkCriteria(type, Mech, e);
		      nnm = e.getAttributes();
		      if (nnm != null)
		      {
		         for (i=0; i<nnm.getLength(); i++)
		         {
		            n = nnm.item(i);
		            attrname = n.getNodeName();
		            attrval = n.getNodeValue();
//		            System.out.print(" " + attrname + " = " + attrval);
			                
			      }
		      }
		  }
       }
       parser.reset();
       return tc;
    }
    public static int checkCriteria(String _type, String _mech, org.w3c.dom.Element e){
    	org.w3c.dom.Node n;
    	String attrname;
    	int count=0;
    	NodeList tmech = e.getChildNodes();
    	//System.out.println(e.getTagName());
    	for (int k = 0; k < tmech.getLength() ; k++){
    		org.w3c.dom.Node tmeche = tmech.item(k);
    		  NamedNodeMap nmntmech = tmeche.getAttributes();
    	   if (nmntmech != null)
    	   {
    		   
    	 	 //System.out.println("Tmech Attr : " +nmntmech.getLength());
    	      //for (i=0; i<nmntmech.getLength(); i++)
    	      //{
    	         n = nmntmech.item(0);
    	         
    	         attrname = n.getNodeName();
    	         String mech = n.getNodeValue();
//    	         //if (  attrname.equalsIgnoreCase("type") &&  attrval.equalsIgnoreCase("3")){
//    	         System.out.print(" " + attrname + " = " + mech);
//    	         //}
    	         n = nmntmech.item(1);
    	         attrname = n.getNodeName();
    	         String type = n.getNodeValue();
//    	         System.out.println(" " + attrname + " = " + type);
    	         if (type.equals(_type) && mech.equals(_mech)){
    	         	++count;
//    	         	System.out.println(" " + type + " & " + mech);
    	         }
    	         
    	         
    	     //}
    	   }
    	 
    	}
    	return count;
    }
    public static String replace(String string, char character, char replacement) {

    	       String returnValue;

    	       if (string.length() > 0) {

    	           StringBuffer buffer = new StringBuffer();

    	           for (int i = 0; i < string.length(); i++) {

    	               char tempChar = string.charAt(i);

    	               if (tempChar != character) {

    	                  buffer.append(tempChar);

    	              } else {

    	                  buffer.append(replacement);

    	              }

    	          }

    	         returnValue = buffer.toString();

    	      } else {

    	          returnValue = string;

    	      }

    	      return returnValue;

    	  }
    
    public static String randomPassword(String s)
    {
       //chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
       String pass = "";

       while (pass.length() < s.length()){
      
    	   Double d = new Double(Math.random());
    	   double j = (d.doubleValue() * 9.0 * s.length());
    	   //System.out.println("Random : " + j);
    	   
          //double i = Math.abs(j);
          //System.out.println("Absolute : " + i);
          int a = new Double(j).intValue();
         
          if (a < s.length()){
        	  
        	  char c = s.charAt(a)   ;  
        	  if (letsCheck (pass , s, c)){	  pass += c;}
        	  //pass+=c;
          //System.out.println("Word : " +pass);
       }
       }
       return pass;
    }
    public static boolean letsCheck (String newPass , String OrigString, char ch){
        char c[] = newPass.toCharArray();
          
        int count = instancesofChar(OrigString, ch);
        
        int countNew = instancesofChar(newPass,ch);
        
        for (int i = 0 ; i < c.length; i++ ){
        	//System.out.println("Character to be printed"+ ch + " Char checked" + c[i]);
        	if (c[i] == ch ) {
        		if ( countNew < count ) {return true;} 
        		else
        		return false;
        	}
        }
    	
    	return true;
    }
    public static int instancesofChar ( String OrigString, char ch){
    	char o[] = OrigString.toCharArray();
    	int count = 0;
    	for (int i = 0 ; i < o.length; i++ ){
        	//System.out.println("Character to be printed"+ ch + " Char checked" + o[i]);
        	if (o[i] == ch ) {
        		count+=1;
        	}
        }
    	return count;
    	
    }
    
//    private void printXmlFile(String fileName, Document document)
//	throws Exception {
//		OutputFormat format = OutputFormat.createPrettyPrint();
//		XMLWriter writer = new XMLWriter(new FileWriter(fileName), format);
//		writer.write(document);
//		writer.close();
//		}
}
/*          
*/ 