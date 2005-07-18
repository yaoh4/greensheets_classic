/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.application.QuestionAttachmentsAction;

import java.rmi.server.UID;

import org.apache.log4j.Logger;
/**
 * Wrapper class for files attached to greensheet questions.
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class QuestionAttachment {    
    private static final Logger logger = Logger.getLogger(QuestionAttachmentsAction.class);
    
    private String fileName; 
    private String filePath;
    private byte[] docData;
    private int dbId;
    private String memId;
    private int status;
    
    private static final int EXISTING = 0; // indicates that the attachment exists in the database.
    private static final int NEW = 1; // indicates a new attachment.
    private static final int DELETED = 2; // indicates that the attachment has been deleted by the user. 

    /**
     * Constructor for QuestionAttachment.
     */
    private QuestionAttachment() {
        this.setAttachmentStatusToNew();
        this.setFileMemoryId();
    }

    /**
     * Method createExistingAttachment.
     * @param filename
     * @param path
     * @param id
     * @return QuestionAttachment
     */
    public static QuestionAttachment createExistingAttachment(String filename, String path, int id) {
        logger.debug("In Method QuestionAttachment:createExistingAttachment().");
        
        QuestionAttachment qa = new QuestionAttachment();
        qa.setAttachmentStatusToExisting();
        qa.setFilename(filename);
        qa.setFilePath(path);
        qa.setDbId(id);
        return qa;
    }

    /**
     * Method createNewAttachment.
     * @return QuestionAttachment
     */
    public static QuestionAttachment createNewAttachment() {
       QuestionAttachment qa = new QuestionAttachment();
       qa.setAttachmentStatusToNew();
       return qa;
    }
    
    /**
     * Method createCopy.
     * @return QuestionAttachment
     */
    public  QuestionAttachment createCopy() {
       logger.debug("In Method QuestionAttachment:createCopy().");
       QuestionAttachment qa = new QuestionAttachment();
       qa.fileName = this.fileName;
       qa.filePath = this.filePath;
       qa.docData = this.docData;
       qa.dbId = this.dbId;
       qa.memId = this.memId;
       qa.status = this.status;
       
       return qa;
    }

    /**
     * Returns the Database ID.
     * @return int
     */
    public int getDbId() {
        return dbId;
    }

    /**
     * Sets the Database id.
     * @param id The id to set
     */
    
    public void setDbId(int id) {
        this.dbId = id;
    }
    
    /**
     * Returns the Memory ID.
     * @return String
     */
    public String getFileMemoryId() {
        return memId;
    }

    /**
     * Sets the Memory id.
     */
    private void setFileMemoryId() {
       // Get a unique id.
        UID uniqueId = new UID();
        long time = System.currentTimeMillis();
        this.memId = uniqueId.toString() + time;
    }

    /**
     * Sets the status of the attachment to NEW
     */
    public void setAttachmentStatusToNew() {
        this.status = QuestionAttachment.NEW;
    }

    /**
     * Sets the status of the attachment to DELETED
     */
    public void setAttachmentStatusToDeleted() {
        this.status = QuestionAttachment.DELETED;
    }
    
    /**
     * Sets the status of the attachment to EXISTING
     */
    public void setAttachmentStatusToExisting() {
        this.status = QuestionAttachment.EXISTING;
    }
    
    /**
     * Gets the status of the attachment.
     */
    public int getStatus() {
        return this.status;
    }
    
    /**
     * Gets the status value of the attachment.
     */
    public String getStatusValue() {
        String fileStatus = "";
        if (this.status == 0) {
            fileStatus = "EXISTING";
        }
        
        if (this.status == 1) {
            fileStatus = "NEW";
        }
        
        if (this.status == 2) {
            fileStatus = "DELETED";
        }
        
        return fileStatus;
    }
    
    /**
     * Determines if the attachment needs to be Created
     */
    public boolean isToBeCreated() {
        return (this.status == QuestionAttachment.NEW);
    }
    
    /**
     * Determines if the attachment is existing
     */
    public boolean isExisting() {
        return (this.status == QuestionAttachment.EXISTING);
    }
    
    /**
     * Determines if the attachment needs to be deleted
     */
    public boolean isToBeDeleted() {
        return (this.status == QuestionAttachment.DELETED);
    }
    
  
    /**
     * Gets the value of New
     */
    public String getAttachmentNewValue() {
        return "NEW";
    }
    
    
    /**
     * Gets the value of Existing
     */
    public String getAttachmentExistingValue() {
        return "EXISTING";
    }
    
    /**
     * Gets the value of Deleted
     */
    public String getAttachmentDeletedValue() {
        return "DELETED";
    }
    
    
    /**
     * Returns the docData.
     * @return byte[]
     */
    public byte[] getDocData() {
        return docData;
    }

    /**
     * Returns the filename.
     * @return String
     */
    public String getFilename() {
        return fileName;
    }

    /**
     * Sets the docData.
     * @param docData The docData to set
     */
    public void setDocData(byte[] docData) {
        this.docData = docData;
    }

    /**
     * Sets the filename.
     * @param filename The filename to set
     */
    public void setFilename(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Returns the filePath.
     * @return String
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the filePath.
     * @param filePath The filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String fileStatus = this.getStatusValue();
                
        return "fileName:: " + this.fileName + "  filePath:: " + this.filePath + "  File Status:: " + fileStatus;           
    }

    
}
