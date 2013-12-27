package gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer.xmlxform;

import gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer.GSForm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSParserFilter;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.traversal.NodeFilter;

public class XmlProcessor {

	private static Logger logger = Logger.getLogger(XmlProcessor.class);
	private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	private String inputFilesDir = "";
	private String outputDir = "";
	private String postProcessDir = "";
	private String xsltFileName = "";
	private String summaryFileName = "";
	
	private TransformerFactory xformFactory = null;
	private Transformer transformer = null;
	
	private DocumentBuilder xmlDocBuilder = null;
	private DOMImplementationLS xmlMachinery = null;
	private LSParser xmlParser = null;
	private LSInput xmlInput = null;
	private LSSerializer xmlWriter = null;
	private LSOutput xmlOutput = null;
	
	public void init() {
		logger.info("Custom Greensheets forms definition XML processor is being initialized.");
		xformFactory = TransformerFactory.newInstance();
		try {
			Source xsltSource = new StreamSource(new FileInputStream(xsltFileName));
			transformer = xformFactory.newTransformer(xsltSource);

			DocumentBuilderFactory docBldrFactory = DocumentBuilderFactory.newInstance();
//			String id    = "http://apache.org/xml/properties/dom/document-class-name";
//			Object value = "org.apache.xerces.dom.DocumentImpl";
//			try {
//			    docBldrFactory.setAttribute(id, value);
//			} 
//			catch (IllegalArgumentException e) {
//			    logger.error("!!! Could not set parser property of DocumentBuilderFactory to use Xerses "
//			    		+ "from a JAR file that's part of this application instead of the standard implementation "
//			    		+ "bundled into the JDK !");
//			}
			xmlDocBuilder = docBldrFactory.newDocumentBuilder();
			DOMImplementation impl = xmlDocBuilder.getDOMImplementation();
			logger.debug("DOM impl through DocBuilderFactory: " + impl.getClass().getName());
			boolean hasLSfeature = impl.hasFeature("LS", "3.0");
			if (!hasLSfeature) {
				logger.debug("This DOM implementation does not seem to support LS (load+save) feature 3.0 ,"
						+ "so those operations might not work.");
			}			
			xmlMachinery = (DOMImplementationLS) impl;
			xmlParser = xmlMachinery.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, "http://www.w3.org/2001/XMLSchema");
			xmlParser.setFilter(new WhitespaceFilter());  // defined below - filters out blank text nodes
			xmlInput = xmlMachinery.createLSInput();
			xmlWriter = xmlMachinery.createLSSerializer();
			xmlWriter.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // tried String "true" first: nuh-uh!
			// xmlWriter.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
			xmlOutput = xmlMachinery.createLSOutput();
		} 
		catch (FileNotFoundException e) {
			logger.error("Unable to load the XSLT file: " + e);
		}
		catch (TransformerConfigurationException tce) {
			logger.error("Problem configuring the XSLT transformer: \n\t" + tce);
		} catch (ParserConfigurationException pce) {
			logger.error("Problem configuring XML paser and serializer: \n\t" + pce);
		}
	}
	
	
	public void writeOneFormsQuestions(String filenamePrefix, String applType, 
			String fundingMech) {
		logger.debug("  > >  Generating custom question file for " + applType + fundingMech);
		String sourceFilePath = inputFilesDir + System.getProperty("file.separator") +
				filenamePrefix + "_Questions.xml";
		String outFilePath = generateOutputFilename(filenamePrefix, applType, fundingMech);
		try {
			Source xmlSource = new StreamSource(new FileInputStream(sourceFilePath));
			Result xformResult = new StreamResult(new BufferedOutputStream(
					new FileOutputStream(outFilePath)));
			if (transformer != null) {
				transformer.setParameter("appltype", applType);
				transformer.setParameter("fundingmech", fundingMech);
				transformer.transform(xmlSource, xformResult);
			}
		} 
		catch (IOException ioe) {
			logger.error("Input/Output problem when constructing XML Source for transformation from\n"
					+ sourceFilePath);
		}
		catch (TransformerException xfme) {
			logger.error(" > > >  * * *  XML transformation error with " + filenamePrefix + 
					" questions for " + applType + fundingMech + ": \n\t" + xfme);
		}
	}
	
	
	public void writeFormDefXml (String filenamePrefix, List<GSForm> formList) {
		if (xmlMachinery == null) {
			logger.error(" ** ** **  There is no DOM Implementation defined - we won't be able to \n\t"
					+ "read/write form-definition files.");
			return;
		}
		logger.info("  >>  Starting to write individual XML files for distinct forms.");
		BufferedInputStream stream = null;
		PrintWriter writer = null;
		Document xmlDoc = null;
		int counter = 0;
		for (GSForm oneForm : formList) {
			Document newDoc = xmlDocBuilder.newDocument();
			Element newrootElem = newDoc.createElement("GreensheetForm");
			newDoc.appendChild(newrootElem);
			String sourceFilePath = outputDir + File.separator + oneForm.getInitialFilename();
			try {
				stream = new BufferedInputStream(new FileInputStream(sourceFilePath));
				xmlInput.setByteStream(stream);
				xmlDoc = xmlParser.parse(xmlInput);
				Node hostOfImport = newDoc.importNode(xmlDoc.getFirstChild(), true);
				newrootElem.appendChild(hostOfImport);
				counter++;
				addInfoElements(newDoc, oneForm, counter);
				newDoc.normalizeDocument();
				String destFilePath = outputDir + File.separator + oneForm.getGeneratedName() + ".xml";
				writer = new PrintWriter(new BufferedWriter(new FileWriter(destFilePath)));
				xmlOutput.setCharacterStream(writer);
				xmlWriter.write(newDoc, xmlOutput);
			}
			catch (IOException ioe) {
				logger.error("  ** **   Error parsing XML file " + oneForm.getInitialFilename() + ":\n\t" + ioe);
			}
			finally {
				try {
					if (stream!=null)  { stream.close(); }
					if (writer!=null)  { writer.close(); }
				}
				catch (IOException ioe) {
					logger.warn("UNABLE to close XML reading/writing streams: \n\t" + ioe);
				}
			}
		}
		logger.info("  >>  Done with writing individual XML files for distinct forms.");
	}


	
	private String generateOutputFilename (String filenamePrefix, String applType, 
			String fundingMech) {
		return outputDir + System.getProperty("file.separator") + filenamePrefix
				+ "_" + applType + "_" + fundingMech + "_qstns.xml";
	}
	
	
	private void addInfoElements(Document xmlDoc, GSForm gsForm, int formNumber) {
		Node rootNode = xmlDoc.getFirstChild();
		
		Attr formIdAttr = xmlDoc.createAttribute("form_id");
		String revisedFormId = gsForm.getGeneratedName();
		String prefix = revisedFormId.substring(0, revisedFormId.lastIndexOf('_')+1);
		revisedFormId = prefix + formNumber;
		gsForm.setGeneratedName(revisedFormId);
		// this ^ changes form number to be based on frequency of use (most-used form will be numbered 1)
		formIdAttr.setValue(revisedFormId);
		rootNode.getAttributes().setNamedItem(formIdAttr);
		
		Element applicabilityElem = xmlDoc.createElement("Applicability");
		applicabilityElem.setTextContent(gsForm.getApplicabilityAsString());
		rootNode.insertBefore(applicabilityElem, rootNode.getFirstChild());

		Element useFreqElem = xmlDoc.createElement("UseFrequency");
		useFreqElem.setTextContent(Long.toString(gsForm.getOccurrences()));
		rootNode.insertBefore(useFreqElem, rootNode.getFirstChild());
	}
	
	
	private class WhitespaceFilter implements LSParserFilter {
		@Override
		public short acceptNode(Node nodeArg) {
			if ((nodeArg.getNodeType() == Node.TEXT_NODE || nodeArg.getNodeType() == Node.CDATA_SECTION_NODE)
					&& shouldBeRejectedAsWhitespace(nodeArg.getTextContent())) {
				return NodeFilter.FILTER_REJECT;
			}
			else {
				return NodeFilter.FILTER_ACCEPT;
			}
		}
		@Override
		public int getWhatToShow() {
			return NodeFilter.SHOW_ALL;
		}
		@Override
		public short startElement(Element elementArg) {
			return LSParserFilter.FILTER_ACCEPT;
		}
		
		private boolean shouldBeRejectedAsWhitespace(String s) {
			StringBuilder sb = new StringBuilder(s);
			String code13 =  String.copyValueOf(Character.toChars(13));
			while (sb.indexOf("\n")!=-1 || sb.indexOf(code13)!=-1 || sb.indexOf("\t")!=-1 || sb.indexOf(" ")!=-1) {
				int pos = Math.max(sb.indexOf("\n"), sb.indexOf(code13));
				pos = Math.max(pos, sb.indexOf("\t"));
				pos = Math.max(pos, sb.indexOf(" "));
				if (pos > -1) {
					sb.deleteCharAt(pos);
				}
			}
			return (sb.length() == 0);
		}
	}
	
	
	/** has a side effect of writing 'post-processed' XML files where the declaration is on its own line */
	public void writeFormSummaryFile() throws IOException {
		String summFilePath = postProcessDir + File.separator + summaryFileName; 
		PrintWriter summaryWriter = new PrintWriter(new BufferedWriter(new FileWriter(summFilePath)));
		summaryWriter.println("Form" + '\t' + "Use Freq" + '\t' + "Category (approx.)" + '\t' + "Applicability"); // header line
		File postProcDir = new File(postProcessDir);
		File[] formDefFiles = new File[1];
		BufferedReader oneFormReader = null;
		PrintWriter oneFormWriter = null;
		try {
			if (postProcDir != null && postProcDir.exists() && postProcDir.isDirectory()) {
				formDefFiles = postProcDir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File arg0, String arg1) { return arg1.matches("[^_]*_form_[0-9]+.xml"); }
				});
				for (File f : formDefFiles) {
					logger.debug("   > > >  Reading " + f.getName());
					String modName = f.getName().substring(0, f.getName().lastIndexOf(".xml")) + "_ppd.xml"; // ppd for 'post-processed'
					// just re-writing the file with XML declaration on a separate line - that's it 
					long lineCounter = 0;
					oneFormReader = new BufferedReader(new FileReader(f));
					oneFormWriter = new PrintWriter(new BufferedWriter(new FileWriter(postProcDir + File.separator + modName)));
					String lineRead = "";
					do {
						lineRead = oneFormReader.readLine();
						if (lineCounter == 0) {
							oneFormWriter.println(XML_DECLARATION);
							oneFormWriter.println(lineRead.substring(XML_DECLARATION.length()));
						}
						else {
							if (lineRead != null)  { oneFormWriter.println(lineRead); }
						}
						lineCounter++;
					}
					while (lineRead != null);
					oneFormWriter.flush(); oneFormWriter.close();
					oneFormReader.close();
					// now reading a DOM XML document from the file to write grant category, use count, and applicability
					// to the summary file
					xmlInput.setByteStream(new BufferedInputStream(new FileInputStream(f)));
					Document xmlDoc = xmlParser.parse(xmlInput);
					Element docElem = xmlDoc.getDocumentElement();
					Element useFreqElem = (Element) docElem.getFirstChild();
					Element applicabilityElem = (Element) useFreqElem.getNextSibling();
					Element approxCategElem = (Element) applicabilityElem.getNextSibling();
					summaryWriter.println(docElem.getAttribute("form_id") + '\t' + useFreqElem.getTextContent() + '\t' + 
							approxCategElem.getTextContent() + '\t' + applicabilityElem.getTextContent());
				}
			}
			else {
				logger.error(" =-=-=  Trouble with post-processing directory. =-=-=");
			}
		}
		finally {
			if (oneFormReader != null)  { oneFormReader.close(); }
			if (oneFormWriter != null)  { oneFormWriter.flush(); oneFormWriter.close(); }
			if (summaryWriter != null) {
				summaryWriter.flush();
				summaryWriter.close();
			}
		}
	}
	
	
	public String getInputFilesDir() {
		return inputFilesDir;
	}
	public void setInputFilesDir(String inputFilesDir) {
		this.inputFilesDir = inputFilesDir;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public String getPostProcessDir() {
		return postProcessDir;
	}
	public void setPostProcessDir(String postProcessDir) {
		this.postProcessDir = postProcessDir;
	}
	public String getXsltFileName() {
		return xsltFileName;
	}
	public void setXsltFileName(String xsltFileName) {
		this.xsltFileName = xsltFileName;
	}
	public String getSummaryFileName() {
		return summaryFileName;
	}
	public void setSummaryFileName(String summaryFileName) {
		this.summaryFileName = summaryFileName;
	}
}
