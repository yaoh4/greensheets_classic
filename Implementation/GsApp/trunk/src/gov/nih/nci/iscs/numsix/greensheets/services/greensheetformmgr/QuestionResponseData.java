/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import java.util.*;

import org.apache.commons.lang.builder.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
/**
 *  Represents an response to an individual question for an individual GreensheetForm instance.
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class QuestionResponseData {

    public static final String DROP_DOWN = "DROP_DOWN";
    public static final String RADIO = "RADIO";
    public static final String CHECK_BOX = "CHECK_BOX";
    public static final String FILE = "FILE";
    public static final String COMMENT = "COMMENT";
    public static final String NUMBER = "NUMBER";
    public static final String TEXT = "TEXT";
    public static final String DATE = "DATE";
    public static final String STRING = "STRING";

    private String responseDefId;
    private String responseDefType;
    private String selectionDefId;
    private String answerValue;
    private String inputValue;
    private String questionDefId;
    private HashMap questionAttachments = new HashMap();
    // kk private boolean dirty = true;
    private int id;


    private static final Logger logger = Logger.getLogger(QuestionResponseData.class);
    public QuestionResponseData(int id){
        this.id = id;
    }

    public QuestionResponseData() {
    }

    /**
     * Method setResponseData set the data for responses that require user selection from a list of options
     * @param questionDefId
     * @param responseDefId
     * @param responseDefType
     * @param selectionDefId
     * @param inputValue
     */
    public void setSelectResponseData(String questionDefId, String responseDefId, String responseDefType, String selectionDefId) {

        this.questionDefId = questionDefId;
        this.responseDefId = responseDefId;
        this.responseDefType = responseDefType;
        this.selectionDefId = selectionDefId;
    }

    /**
     * Method setInputResponseData set the data for responses that require user input
     * @param questionDefId
     * @param responseDefId
     * @param responseDefType
     * @param inputValue
     */
    public void setInputResponseData(String questionDefId, String responseDefId, String responseDefType, String inputValue) {

        this.questionDefId = questionDefId;
        this.responseDefId = responseDefId;
        this.responseDefType = responseDefType;
        this.inputValue = inputValue;

    }

    /**
     * Method setFileResponseData. Add a QuestionAttachment to this questionResponse. The questionResponse is of type FILE
     * @param questionDefId
     * @param responseDefId
     * @param responseDefType
     * @param qa
     */
    public void setFileResponseData(String questionDefId, String responseDefId, String responseDefType, QuestionAttachment qa) {

        this.questionDefId = questionDefId;
        this.responseDefId = responseDefId;
        this.responseDefType = responseDefType;
        this.questionAttachments.put(qa.getMemId(), qa);
    }

    /**
     * Method addQuestionAttachment.
     * @param qa
     */
    public void addQuestionAttachment(QuestionAttachment qa) {
        qa.setAttachmentStatusToNew();
        questionAttachments.put(qa.getMemId(), qa);        
    }

    /**
     * Method removeQuestionAttachment.
     * @param fileName
     * @throws IllegalArgumentException
     */
    public void removeQuestionAttachment(String attachmentMemoryId) throws IllegalArgumentException {
        // Get the attachment corresponding to the attachment.
        QuestionAttachment qa = (QuestionAttachment) this.questionAttachments.get(attachmentMemoryId);
        if(qa != null) {
            // Get the Status of attachment.
            int attachmentStatus = qa.getStatus();
                        
            if(qa.isToBeCreated()) {
                // If the status is NEW, its been recently added to the Map by the user in this session. 
                // Thus, just remove the attachment from the MAP.
                this.questionAttachments.remove(qa);
            }
            else if (qa.isExisting()){
                // If status == EXISTING, it exists in the database. Thus, set the status to DELETED.
            	qa.setAttachmentStatusToDeleted();
        	}
        }
        else {
            throw new IllegalArgumentException("File corresponding to the memory id=" + attachmentMemoryId + " could not be found to be deleted from QuestionResponse ");
        }
    }

    /**
     * Method getQuestionAttachments.
     * @return Map
     */
    public Map getQuestionAttachments() {
        return this.questionAttachments;
    }

    
    /**
     * Method setQuestionAttachments.
     * @param Map
     */
    public HashMap setQuestionAttachments(HashMap qaMap) {
        return this.questionAttachments = qaMap;
    }


    /**
     * Method hasQuestionAttachments. If the type is FILE then are there any attachments.
     * @return boolean
     */
    public boolean hasQuestionAttachments(){
        if(this.questionAttachments.size()>0){
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * Method commentHasValues. If the type id COMMENT then is there any text in the comment
     * @return boolean
     */
    public boolean commentHasValues(){
              
        if(responseDefType.equalsIgnoreCase(this.COMMENT) && !this.inputValue.equals("") && this.inputValue != null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Method getUserSelectId returns the id of a userselect type of response. These are for
     * Check boxes, Selects, and Drop Down type of controls.
     * @return String
     * @throws Exception
     */
    public String getUserSelectId(){
        if (responseDefType.equalsIgnoreCase(this.CHECK_BOX)
            || responseDefType.equalsIgnoreCase(this.RADIO)
            || responseDefType.equalsIgnoreCase(this.DROP_DOWN)) {
            return this.selectionDefId;
        } else {
            return null;
        }
    }



    /**
     * Method getUserInputValue. The the value of the data the user inputted for this question.
     * @return String
     */
    public String getUserInputValue() {
        if (responseDefType.equalsIgnoreCase(this.COMMENT)
            || responseDefType.equalsIgnoreCase(this.STRING)
            || responseDefType.equalsIgnoreCase(this.NUMBER)
            || responseDefType.equalsIgnoreCase(this.TEXT)
            || responseDefType.equalsIgnoreCase(this.DATE)) {
            return this.inputValue;
        } else {
            return null;
        }

    }


    /**
     * Returns the answerValue. This is the value used by the presentation mechanism of 
     * Velocity and HTML.
     * @return String
     */
    public String getAnswerValue() {

        if (responseDefType.equalsIgnoreCase(this.CHECK_BOX) || responseDefType.equalsIgnoreCase(this.RADIO)) {
            answerValue = "checked";
        } else if (responseDefType.equalsIgnoreCase(this.DROP_DOWN)) {
            answerValue = "selected";
        } else {
            answerValue = inputValue;
        }

        return answerValue;
    }

    /**
     * Returns the responseDefId.
     * @return String
     */
    public String getResponseDefId() {
        return responseDefId;
    }

    /**
     * Returns the selectionId.
     * @return String
     */
    public String getselectionInputId() {
        return selectionDefId;
    }

    /**
     * Returns the responseDefType.
     * @return String
     */
    public String getResponseDefType() {
        return responseDefType;
    }

    /**
     * Sets the responseDefType.
     * @param responseDefType The responseDefType to set
     */
    public void setResponseDefType(String responseType) {
        this.responseDefType = responseType;
        //this.dirty = true;
    }

    /**
     * Returns the inputValue.
     * @return String
     */
    public String getInputValue() {
        return inputValue;
    }

    /**
     * Sets the inputValue.
     * @param inputValue The inputValue to set
     */
    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
        //this.dirty = true;
    }

    /**
     * Returns the questionDefId.
     * @return String
     */
    public String getQuestionDefId() {
        return questionDefId;
    }

    /**
     * Sets the questionDefId.
     * @param questionDefId The questionDefId to set
     */
    public void setQuestionDefId(String questionId) {
        this.questionDefId = questionId;
        //this.dirty = true;
    }

    /**
     * Sets the responseDefId.
     * @param responseDefId The responseDefId to set
     */
    public void setResponseDefId(String responseId) {
        this.responseDefId = responseId;
    }

    /**
     * Returns the selectionDefId.
     * @return String
     */
    public String getSelectionDefId() {
        return selectionDefId;
    }

    /**
     * Sets the selectionDefId.
     * @param selectionDefId The selectionDefId to set
     */
    public void setSelectionDefId(String selectionInputId) {
        this.selectionDefId = selectionInputId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Returns the dirty.
     * @return boolean
     */
    /*public boolean isDirty() {
        return dirty;
    }*/

    /**
     * Sets the dirty.
     * @param dirty The dirty to set
     */
    /*public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }*/

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
