package gov.nih.nci.cbiit.atsc.dao;

import java.util.HashSet;
import java.util.List;

public interface ProcessNewQuestionDefsDAO {

    public HashSet<String> getModuleTypeMechListFromDraftDB(String module);
    public HashSet<String> getModuleTypeMechListFromProdDB(String module);
    public HashSet<String> getModuleMechListFromDB(String type, String module);
    public HashSet<String> getModuleListFromDB();
    public HashSet<String> getUpdatedModuleMechListFromDB(String type, String module);
    public HashSet<String> getUpdatedOnlyTypeMechListFromDB(String module);
    public void checkDraftModifedFlag();
    public String getGreensheetDraftTemplateId(String Type, String mech, String moduleName);
    public List<FormGrant> retrieveDraftGrantsByFullGrantNum(String type, String mech, String moduleName);
    public HashSet<String> getInActiveMechTypeByModule(String module);
}
