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
 * This class serves as a temporary holder of question attachments for a particular question. Any actions regarding question attachments 
 * are not finalized until the form is saved. Therefore the proxy holds the questionAttachment objetcts whose  actions are not yet 
 * finalized. The proxy is initialized with any attachmets that already exist for the greenesheet question as represeneted by the responseDefId
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class QuestionAttachmentsProxy {

    private String responseDefid;  
    private HashMap qaMap = new HashMap();

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
     * Returns the attachment map.
     * @return String
     */
    public HashMap getAttachmentMap() {
        return this.qaMap;
    }
    
    /**
     * Returns the attachment .
     * @param fileMemoryId 
     * @return QuestionAttachment
     */
    public QuestionAttachment getAttachment(String fileMemoryId) {
        return (QuestionAttachment) this.qaMap.get(fileMemoryId);
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
    
    public void addQuestionAttachment(QuestionAttachment qa) {
        this.qaMap.put(qa.getMemId(), qa);
    }

    public int getAttachmentCount() {
        return this.qaMap.size();
    }

    public void removeAttachment(String memId){
        QuestionAttachment qa = (QuestionAttachment) this.qaMap.get(memId);
        if (qa != null) {
            if(qa.isToBeCreated()) {
                //New attachment, just remove it off  the map.
                this.qaMap.remove(memId);
            }
            else if (qa.isExisting()) {
                qa.setAttachmentStatusToDeleted();
            }
        }        
    }

    public void initWithExistingQuestionAttachments(Object[] attachments) {
        for(int i = 0; i<attachments.length; i++){
            QuestionAttachment qa = (QuestionAttachment) attachments[i];
            qa.setAttachmentStatusToExisting();       
        }
    }


}
