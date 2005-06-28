/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.*;
import java.util.*;

import org.apache.commons.lang.*;

/**
 * This class serves as a temporary holder of question attachments for a particular question. Any actions regarding question
attachments 
 * are not finalized until the form is saved. Therefore the proxy holds the questionAttachment objetcts whose 
 * actions are not yet finalized. The proxy is initialized with any attachmets that already exist for the 
 * greenesheet question as represeneted by the responseDefId
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class QuestionAttachmentsProxy {

    private String responseDefid;
    private List tmpQuestionAttachmentList = new ArrayList();
    private List removedAttachmentFileNames = new ArrayList();

    public QuestionAttachmentsProxy() {
    }

    public QuestionAttachmentsProxy(String responseDefId) {
        this.responseDefid = responseDefId;
    }

    /**
     * Method getQuestionDefId.
     * @return String
     */
    public String getQuestionDefId() {
        return StringUtils.substringBeforeLast(responseDefid, "_RD_");
    }

    /**
     * Returns the responseDefid.
     * @return String
     */
    public String getResponseDefid() {
        return responseDefid;
    }

    /**
     * Sets the responseDefid.
     * @param responseDefid The responseDefid to set
     */
    public void setResponseDefid(String responseDefid) {
        this.responseDefid = responseDefid;
    }

    public void addTmpQuestionAttachment(QuestionAttachment qa) {
        this.tmpQuestionAttachmentList.add(qa);
    }


    public void removeAttachment(String fileName){
        Iterator iter = tmpQuestionAttachmentList.listIterator();
        while(iter.hasNext()){
            QuestionAttachment qa = (QuestionAttachment) iter.next();

            if(qa.getFilename().equalsIgnoreCase(fileName)){
                this.removedAttachmentFileNames.add(qa.getFilename());
             
                iter.remove();
            }               
        }

    }


    public void initWithExistingQuestionAttachments(Object[] attachments) {
        
        //@todo need to type check the array
        
        for(int i = 0; i<attachments.length; i++){
            QuestionAttachment qa = (QuestionAttachment) attachments[i];
            if(!qa.isToBeDeleted()){
                this.tmpQuestionAttachmentList.add(qa);
            }               
        }

    }

    /**
     * Returns the tmpQuestionAttachmentList.
     * @return List
     */
    public List getTmpQuestionAttachmentList() {
        return tmpQuestionAttachmentList;
    }

    /**
     * Returns the removedAttachmentFileNames.
     * @return List
     */
    public List getRemovedAttachmentFileNames() {
        return removedAttachmentFileNames;
    }

}
