/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.pdfengine;

import java.io.*;

import org.apache.avalon.framework.logger.*;
import org.apache.fop.apps.*;
import org.xml.sax.*;
import org.apache.log4j.Logger;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import org.dom4j.*;
import org.dom4j.io.*;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
/**
 * Class that generates the PDF file using Apache FO
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class FoEngine {
    private static final Logger logger = Logger.getLogger(FoEngine.class);

    /**
     * Constructor for FoEngine.
     */
    FoEngine() {
    }

    /**
     * Given an FO formatted XML document returns a byte[] containing the
     * PDF representation of the document
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

            inputStream = new ByteArrayInputStream(foDoc.asXML().getBytes());
            outputStream = new ByteArrayOutputStream();

            //Setup FOP
            Log4JLogger l4Jlogger = new Log4JLogger(logger);
            InputSource inputSource = new InputSource(inputStream);
            Driver driver = new Driver(inputSource, outputStream);
            driver.setLogger(l4Jlogger);
            driver.setRenderer(Driver.RENDER_PDF);

            driver.run();

            result = outputStream.toByteArray();

        } catch (Exception e) {
            throw new GreensheetBaseException("error rendering PDF",e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            	throw new GreensheetBaseException("error rendering PDF",e);
            }
        }
        return result;
    }

    private Document generateFo(Document formXmlDoc) throws Exception {
        File xsltSrc = (File) AppConfigProperties.getInstance().getProperty("FORM_FO_TRANSLATOR");

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xsltSrc));

        // now lets style the given document
        DocumentSource source = new DocumentSource(formXmlDoc);
        DocumentResult result = new DocumentResult();
        transformer.transform(source, result);

        // return the transformed document
        
        Document transformedDoc = result.getDocument();

        return transformedDoc;

    }

}
