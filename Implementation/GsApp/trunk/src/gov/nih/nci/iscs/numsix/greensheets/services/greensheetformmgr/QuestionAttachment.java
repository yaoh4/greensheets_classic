/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import java.rmi.server.UID;
/**
 * Wrapper class for files attached to greensheet questions.
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class QuestionAttachment {    
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
        this.setMemId();
    }

    /**
     * Method createExistingAttachment.
     * @param filename
     * @param path
     * @param id
     * @return QuestionAttachment
     */
    public static QuestionAttachment createExistingAttachment(String filename, String path, int id) {
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
    public String getMemId() {
        return memId;
    }

    /**
     * Sets the Memory id.
     */
    private void setMemId() {
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
                
        return "fileName:: " + this.fileName + "  filePath:: " + this.filePath + "  File Status:: " + fileStatus;           
    }

    
}
