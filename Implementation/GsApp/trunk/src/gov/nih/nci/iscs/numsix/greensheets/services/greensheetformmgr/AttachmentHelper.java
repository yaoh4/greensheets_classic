/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.application.QuestionAttachmentsAction;
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
     * @param qrd
     * @param grant
     * @param conn
     * @throws SQLException
     */
    // Updated by KKanchinadam.
    
    void saveAttachments(QuestionResponseData qrd, GsGrant grant, GreensheetForm form, Connection conn) throws GreensheetBaseException {
        // Get the file path
        Properties p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        String repositoryPath = p.getProperty("attachemts.repository.path");
        String rootDir = repositoryPath + File.separator + grant.getFullGrantNumber() + File.separator + form.getGroupTypeAsString() + File.separator + qrd.getQuestionDefId();

        PreparedStatement pstmt = null;
        boolean makeNewQuestionEntry = false;
        
        File f = null;
        
        try {
            // Create the folder structure in the file system.
            File dir = new File(rootDir);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            
            //If  this is a new question, make an entry in the Database in the Form Question Answers table.
            if(qrd.getId() == 0) {
                qrd.setId(DbUtils.getNewRowId(conn, "fqa_seq.nextval"));
                this.makeNewQuestionEntry(qrd,form,conn);
            }
            
            // Now that the parent record is taken care of... take care of the file attachments.
            // Also, folder structure is present. create or delete (not a good idea in this situation....!!! )  the file attachments.
            
            boolean fileAttachmentsPresentInDb = false; //flag to track whether the question response data will eventually contain any file or not.
            if (qrd.getQuestionAttachments() != null) {
                Set entries = qrd.getQuestionAttachments().entrySet();
                Iterator iter = entries.iterator();
                // Iterate thru the entries. Delete if status is deleted, else add it.
                
                while (iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    // get the status
                    int qaStatus = qa.getStatus();
                    
                    // If status = NEW or EXISTING, then eventually at least one file attachment will exist. So no need to deleted the Form Question Answer from the database.
                    if (qa.isExisting() || qa.isToBeCreated()) {
                        fileAttachmentsPresentInDb = true;
                    }
                    
                    /*
                    *	Status = NEW, create the attachment in the database.
                    *	Status = DELETED, delete the attachment
                    *	Status = EXISTING, no modification required.
                    */
                    
                    // Get the file
                    f = new File(rootDir + File.separator + qa.getFilename());
                    
                    String sqlAction = null;
                    if (qa.isToBeDeleted()) {
                        sqlAction = "delete from form_answer_attachments_t where id=?";
                        pstmt = conn.prepareStatement(sqlAction);
                        pstmt.setInt(1,qa.getDbId());
                        
                        // Delete the file from the file system.
                        // Commented out ....KK to discuss with Kevin. KK does not like this idea.
                        
                        //f.delete();
                    }
                    else if (qa.isToBeCreated()) {
                        sqlAction = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?)";
                        pstmt = conn.prepareStatement(sqlAction);
                        pstmt.setInt(1,qrd.getId());
                        pstmt.setString(2,qa.getFilename());
                        pstmt.setString(3,qa.getFilePath());
                        
                        // Create the file on the file system.
                        FileOutputStream fs = new FileOutputStream(f);
                        fs.write(qa.getDocData());
                        fs.close();
                        qa.setFilePath(rootDir + File.separator + qa.getFilename());
                    }
                   
                    pstmt.execute();
                    pstmt.close();
                }
                // Check the fileAttachmentsPresentInDb flag. If this is true, it means there is at least one file attachment in the database for the qrd.
                // If this flag is false, there are no file attachments in the db for this QRD. Hence, delete the parent record.
                if (!fileAttachmentsPresentInDb) {
                    String sqlDelete = "delete from form_question_answers_t where id =?";
                    pstmt = conn.prepareStatement(sqlDelete);
                    pstmt.setInt(1,qrd.getId());
                
                    pstmt.execute();
                    pstmt.close();                  
                }
            }
        }
        catch (SQLException se) {
            throw new GreensheetBaseException("error.savefile.sql", se);
        } 
        catch (FileNotFoundException fe) {
            throw new GreensheetBaseException("error.savefile.fe", fe);
        }
        catch (IOException ie) {
            throw new GreensheetBaseException("error.savefile.io", ie);
        }
        finally {            
            try {

                if (pstmt != null)
                    pstmt.close();
            } 
            catch (SQLException se) {
                throw new GreensheetBaseException("error.savefile.sql", se);
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

   /*
    void saveAttachments1(QuestionResponseData qrd, GsGrant grant, GreensheetForm form, Connection conn) throws GreensheetBaseException {

        // First, create/delete db entries.
        boolean dbActionsPerformedSuccessfully = true;
        try {
            registerQuestionAttachment(qrd, form, conn);
        }
        catch (SQLException se) {
            dbActionsPerformedSuccessfully = false;
            se.printStackTrace();
        }
        
        // If all db entries are fine,
        File f = null;

        Properties p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        String repositoryPath = p.getProperty("attachemts.repository.path");
        String rootDir = repositoryPath + File.separator + grant.getFullGrantNumber() + File.separator + form.getGroupTypeAsString() + File.separator + qrd.getQuestionDefId();

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
                
                // if qa status = Existing , do nothing
                // if qa status = New, create the file
                // if qa status = Deleted, delete the file.
                if(qa.isToBeCreated()) {
                    FileOutputStream fs = new FileOutputStream(f);
                    fs.write(qa.getDocData());
                    fs.close();
                    qa.setFilePath(rootDir + File.separator + qa.getFilename());
                }
                else if (qa.isToBeDeleted()) {
                    f.delete();
                }            
            }

            //registerQuestionAttachment(qrd, form, conn);

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
    */
    /**
     * Method registerQuestionAttachment.
     * @param qrd
     * @param form
     * @param conn
     * @throws SQLException
     */
    /*
    private void registerQuestionAttachmentkkkkk(QuestionResponseData qrd, GreensheetForm form, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        boolean makeNewQuestionEntry = false;
        
        try {
            // If  this is a new question, make an entry in the Database in the Form Question Answers table.
            if(qrd.getId() == 0) {
                qrd.setId(DbUtils.getNewRowId(conn, "fqa_seq.nextval"));
                this.makeNewQuestionEntry(qrd,form,conn);
            }
            
            // Now that the parent record is taken care of... take care of the file attachments.
            boolean fileAttachmentsPresentInDb = false; //flag to track whether the question response data will eventually contain any file or not.
            if (qrd.getQuestionAttachments() != null) {
                Set entries = qrd.getQuestionAttachments().entrySet();
                Iterator iter = entries.iterator();
                // Iterate thru the entries. Delete if status is deleted, else add it.
                
                while (iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    // get the status
                    int qaStatus = qa.getStatus();
                    
                    // If status = NEW or EXISTING, then eventually at least one file attachment will exist. So no need to deleted the Form Question Answer from the database.
                    if (qa.isExisting() || qa.isToBeCreated()) {
                        fileAttachmentsPresentInDb = true;
                    }
                    
                   String sqlAction = null;
                   if (qa.isToBeDeleted()) {
                       sqlAction = "delete from form_answer_attachments_t where id=?";
                       pstmt = conn.prepareStatement(sqlAction);
                       pstmt.setInt(1,qa.getDbId());
                   }
                   else if (qa.isToBeCreated()) {
                       sqlAction = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?)";
                       pstmt = conn.prepareStatement(sqlAction);
                       pstmt.setInt(1,qrd.getId());
                       pstmt.setString(2,qa.getFilename());
                       pstmt.setString(3,qa.getFilePath());
                   }
                   
                   pstmt.execute();
                   pstmt.close();
                }
                
                // Check the fileAttachmentsPresentInDb flag. If this is true, it means there is at least one file attachment in the database for the qrd.
                // If this flag is false, there are no file attachments in the db for this QRD. Hence, delete the parent record.
                if (!fileAttachmentsPresentInDb) {
                    String sqlDelete = "delete from form_question_answers_t where id =?";
                    pstmt = conn.prepareStatement(sqlDelete);
                    pstmt.setInt(1,qrd.getId());
                
                    pstmt.execute();
                    pstmt.close();                  
                }
            }
        }
        catch (SQLException se) {
            throw se;
        } 
        finally {            
            try {

                if (pstmt != null)
                    pstmt.close();
            } 
            catch (SQLException se) {
                throw se;
            }
        }
    }
*/
    
}
