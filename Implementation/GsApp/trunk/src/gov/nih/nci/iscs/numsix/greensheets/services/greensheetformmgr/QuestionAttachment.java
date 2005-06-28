/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

/**
 * Wrapper class for files attached to greensheet questions.
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class QuestionAttachment {

    private String filename;
    private byte[] docData;
    private boolean toBeDeleted = false;
    private boolean dirty = false;
    private String filePath;
    private int id;

    /**
     * Constructor for QuestionAttachment.
     */
    private QuestionAttachment() {
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
        qa.setFilename(filename);
        qa.setFilePath(path);
        qa.setId(id);
        qa.setDirty(false);
        return qa;
    }

    /**
     * Method createNewAttachment.
     * @return QuestionAttachment
     */
    public static QuestionAttachment createNewAttachment() {
        QuestionAttachment qa = new QuestionAttachment();
        qa.setDirty(true);
        return qa;
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
        return filename;
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
    public void setFilename(String filename) {
        this.filename = filename;
        this.setDirty(true);
    }

    /**
     * Returns the toBeDeleted.
     * @return boolean
     */
    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    /**
     * Sets the toBeDeleted.
     * @param toBeDeleted The toBeDeleted to set
     */
    public void setToBeDeleted(boolean toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }

    /**
     * Returns the dirty.
     * @return boolean
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Sets the dirty.
     * @param dirty The dirty to set
     */
    public void setDirty(boolean dirty) {

        this.dirty = dirty;
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
        this.setDirty(true);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "fileName:: "
            + this.filename
            + "  filePath:: "
            + this.filePath
            + "  isDirty:: "
            + this.isDirty()
            + "  toBeDeleted:: "
            + this.toBeDeleted;

    }

    /**
     * Returns the id.
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     * @param id The id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
