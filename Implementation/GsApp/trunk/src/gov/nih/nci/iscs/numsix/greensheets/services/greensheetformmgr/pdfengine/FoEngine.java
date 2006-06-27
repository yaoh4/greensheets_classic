/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.pdfengine;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.logger.Log4JLogger;
import org.apache.fop.apps.Driver;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.xml.sax.InputSource;

/**
 * Class that generates the PDF file using Apache FO
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class FoEngine {
	private static final Logger logger = Logger.getLogger(FoEngine.class);

	/**
	 * Constructor for FoEngine.
	 */
	FoEngine() {
	}

	/**
	 * Given an FO formatted XML document returns a byte[] containing the PDF
	 * representation of the document
	 * 
	 * @param srcDoc
	 * @return byte[]
	 * @throws GreensheetBaseException
	 */
	byte[] renderFormAsPDF(Document srcDoc) throws GreensheetBaseException {

		byte[] result = null;

		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;

		try {

			Document foDoc = this.generateFo(srcDoc);
			String docXml = foDoc.asXML();
			// Replace the default UTF-8 encoding with ISO-8859-1 to take care
			// of the special/extended character set.
			// unfortunately, dom4j 1.4 has a bug whereby .asXML() method
			// returns the encoding as UTF-8 only. Hence this approach.
			// using dom4j 1.6.1 has its own set of problems.

			docXml = this.Replace(docXml, "UTF-8", "ISO-8859-1");
			inputStream = new ByteArrayInputStream(docXml.getBytes());
			outputStream = new ByteArrayOutputStream();

			// Setup FOP
			Log4JLogger l4Jlogger = new Log4JLogger(logger);
			InputSource inputSource = new InputSource(inputStream);
			Driver driver = new Driver(inputSource, outputStream);
			driver.setLogger(l4Jlogger);
			driver.setRenderer(Driver.RENDER_PDF);

			driver.run();

			result = outputStream.toByteArray();

		} catch (Exception e) {
			throw new GreensheetBaseException("error rendering PDF", e);
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				throw new GreensheetBaseException("error rendering PDF", e);
			}
		}
		return result;
	}

	private String Replace(String psWord, String psReplace, String psNewSeg) {
		StringBuffer lsNewStr = new StringBuffer();
		// Tested by DR 03/23/98 and modified
		int liFound = 0;
		int liLastPointer = 0;

		do {

			liFound = psWord.indexOf(psReplace, liLastPointer);

			if (liFound < 0)
				lsNewStr.append(psWord
						.substring(liLastPointer, psWord.length()));

			else {

				if (liFound > liLastPointer)
					lsNewStr.append(psWord.substring(liLastPointer, liFound));

				lsNewStr.append(psNewSeg);
				liLastPointer = liFound + psReplace.length();
			}

		} while (liFound > -1);

		return lsNewStr.toString();
	}

	private Document generateFo(Document formXmlDoc) throws Exception {
		File xsltSrc = (File) AppConfigProperties.getInstance().getProperty(
				"FORM_FO_TRANSLATOR");

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(
				xsltSrc));

		// now lets style the given document
		DocumentSource source = new DocumentSource(formXmlDoc);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);

		// return the transformed document
		Document transformedDoc = result.getDocument();

		return transformedDoc;

	}

}
