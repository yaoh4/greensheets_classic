/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.log4j.*;
/**
 * Helper class for working with question file attachments
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
class AttachmentHelper {

    private static final Logger logger = Logger.getLogger(AttachmentHelper.class);
    /**
     * Constructor for AttachmentHelper.
     */
    AttachmentHelper() {
    }

    /**
     * Method saveAttachments.
     * @param form
     * @param grant
     * @throws GreensheetBaseException
     */

    private void registerQuestionAttachment(QuestionResponseData qrd, GreensheetForm form, Connection conn) throws SQLException {

        PreparedStatement pstmt = null;
        boolean makeNewQuestionEntry = false;

        try {
            if(qrd.getId() == 0){
                qrd.setId(DbUtils.getNewRowId(conn, "fqa_seq.nextval"));
                makeNewQuestionEntry = true;
            }

            List tmp = new ArrayList();

            int numOfAttachments = qrd.getQuestionAttachments().size();
            int counter = 0;
            Set entries = qrd.getQuestionAttachments().entrySet();

            Iterator iter = entries.iterator();

            while (iter.hasNext()) {

                logger.debug("counter " + counter);

                Map.Entry me = (Map.Entry) iter.next();
                QuestionAttachment qa = (QuestionAttachment) me.getValue();

                if (qa.isToBeDeleted()) {
                    counter++;
                    logger.debug(qa.toString());

                    String sqlDelete = "delete from form_answer_attachments_t where id=?";

                    logger.debug("file delete " + sqlDelete);
                    pstmt = conn.prepareStatement(sqlDelete);
                    pstmt.setInt(1,qa.getId());
                    
                    pstmt.execute();
                    pstmt.close();
                    tmp.add(qa.getFilename());

                    // Delete the file entry in the question_answer table
                    if (counter == numOfAttachments) {

                        String sqlDelete2 = "delete from form_question_answers_t where id =?";
                        logger.debug("file delete 2 " + sqlDelete2);
                        pstmt = conn.prepareStatement(sqlDelete2);
                        pstmt.setInt(1,qrd.getId());
                    
                        pstmt.execute();
                        pstmt.close();
                    }
                    qa.setDirty(false);

                }

            }

            Iterator iter2 = entries.iterator();
            logger.debug("Num of entries" + entries.size());
            
            if(makeNewQuestionEntry){
                this.makeNewQuestionEntry(qrd,form,conn);
            }
                       
            while (iter2.hasNext()) {
                Map.Entry me = (Map.Entry) iter2.next();
                QuestionAttachment qa = (QuestionAttachment) me.getValue();
                logger.debug(qa.toString());
                if (qa.isDirty() && !qa.isToBeDeleted()) {

                    String sqlInsert =
                        "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?)";

                    logger.debug("insert attachment " + sqlInsert);
                    
                    pstmt = conn.prepareStatement(sqlInsert);
                    pstmt.setInt(1,qrd.getId());
                    pstmt.setString(2,qa.getFilename());
                    pstmt.setString(3,qa.getFilePath());

                    pstmt.execute();
                    pstmt.close();
                }
            }

            // Remove qa from qd attachment list

            for (Iterator i = tmp.iterator(); i.hasNext();) {

                String fName = (String) i.next();
                logger.debug("--------------> removing " + fName);
                qrd.getQuestionAttachments().remove(fName);

            }

        } catch (SQLException se) {
            throw se;
        } finally {
            try {

                if (pstmt != null)
                    pstmt.close();

            } catch (SQLException se) {
                throw se;
            }
        }

    }

    private void makeNewQuestionEntry(QuestionResponseData qrd,GreensheetForm form,Connection conn) throws SQLException{
        PreparedStatement pstmt = null;
        try{
	        String fqaInsert =  "insert into form_question_answers_t "  
				+ "(id, frm_id, extrnl_question_id, extrnl_resp_def_id)"
				+ " values(?,?,?,?)";                    
	        
	        logger.debug("Inserting into FQA " + qrd.getId());
	        
	        pstmt = conn.prepareStatement(fqaInsert);
	        pstmt.setInt(1,qrd.getId());
	        pstmt.setInt(2,form.getFormId());
	        pstmt.setString(3,qrd.getQuestionDefId());
	        pstmt.setString(4,qrd.getResponseDefId());
	        
	        pstmt.execute();
	        pstmt.close();  
        } catch (SQLException se) {
            throw se;
        } finally {
            try {

                if (pstmt != null)
                    pstmt.close();

            } catch (SQLException se) {
                throw se;
            }
        }
    }
    
    void saveAttachments(QuestionResponseData qrd, GsGrant grant, GreensheetForm form, Connection conn)
        throws GreensheetBaseException {

        File f = null;

        Properties p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        String repositoryPath = p.getProperty("attachemts.repository.path");
        String rootDir =
            repositoryPath
                + File.separator
                + grant.getFullGrantNumber()
                + File.separator
                + form.getGroupTypeAsString()
                + File.separator
                + qrd.getQuestionDefId();

        try {
            File dir = new File(rootDir);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            Object[] list = qrd.getQuestionAttachments().values().toArray();
            int size = list.length;
            for (int i = 0; i < size; i++) {
                QuestionAttachment qa = (QuestionAttachment) list[i];

                f = new File(rootDir + File.separator + qa.getFilename());

                logger.debug("file is " + f.getAbsolutePath());

                if (!qa.isToBeDeleted() && qa.isDirty()) {
                    FileOutputStream fs = new FileOutputStream(f);
                    fs.write(qa.getDocData());
                    fs.close();
                    qa.setFilePath(rootDir + File.separator + qa.getFilename());
                } else if (qa.isToBeDeleted()) {
                    f.delete();
                }

            }

            registerQuestionAttachment(qrd, form, conn);

        } catch (SQLException e) {
            e.printStackTrace();
            f.delete();
            throw new GreensheetBaseException("error.savefile.sql", e);
        } catch (FileNotFoundException fe) {
            throw new GreensheetBaseException("error.savefile.fe", fe);
        } catch (IOException ie) {
            throw new GreensheetBaseException("error.savefile.io", ie);
        }

    }

    void getQuestionAttachmentData(QuestionAttachment qa) throws GreensheetBaseException {

        String fullpath = qa.getFilePath();
        File file = new File(fullpath);

        byte[] bytes;
        try {

            InputStream is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();

            // You cannot create an array using a long type.
            // It needs to be an int type.
            // Before converting to an int type, check
            // to ensure that file is not larger than Integer.MAX_VALUE.
            if (length > Integer.MAX_VALUE) {
                throw new IOException("File too large :: " + file.getName());
            }

            // Create the byte array to hold the data
            bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }

            // Close the input stream and return bytes
            is.close();
        } catch (FileNotFoundException e) {
            throw new GreensheetBaseException(e);
        } catch (IOException e) {
            throw new GreensheetBaseException(e);
        }
        qa.setDocData(bytes);

    }

}
