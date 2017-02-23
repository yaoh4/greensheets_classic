package gov.nih.nci.iscs.numsix.greensheets.services;

import java.util.HashSet;
import java.util.List;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;

public interface ProcessNewQuestionDefsService {
    
    public boolean validateXml(String module) throws Exception;
   // public int processNewQuestionDefs(String module, String user, String comments, EmailNotification emailHelper);
    public void setTypeMechList(String module);
    public void setTypeMechUpdatedList(String module);
    public void setDynamicMechList(String type, String module);
    public void setUpdatedDynamicMechList(String type, String module);
    public HashSet<String> getDraftList();
    public HashSet<String> getMechDynamicList();
    public HashSet<String> getUpdatedMechDynamicList();
    public HashSet<String> getModuleMechListFromDB(String type, String module);
    public HashSet<String> getUpdatedOnlyTypeMechListFromDB(String module);
    public HashSet<String> getModuleListFromDB();
    public void checkDraftModifedFlag();
    public HashSet<String> getAdditionList(HashSet<String> inActiveMechTypeByModuleList, String module);
    public HashSet<String> getDeletionList(HashSet<String> inActiveMechTypeByModuleList, String module);
    public HashSet<String> getInActiveMechTypeByModule(String module);
    public String getGreensheetDraftTemplateId(String type, String mech, String moduleName);
    public List<FormGrant> retrieveDraftGrantsByFullGrantNum(String type, String mech, String moduleName);
}
