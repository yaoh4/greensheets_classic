package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.List;

public interface GreensheetsFormGrantsService {
    public List findGrants(GsUser user,
            boolean onControlFlag,
            boolean restrictedToOpenForms,
            boolean restrictedToLoggedinUser);

    public List findGrants(GsUser user,
            String grantsRange,
            String grantType,
            boolean includeOnlyGrantsWithinPayline,
            String grantMechanism,
            String fullGrantNum,
            String piLastName,
            String piFirstName
            );

    public List findGrantsByGrantNum(String fullGrantNum);

    public List retrieveGrantsByFullGrantNum(String fullGrantNum);

    public List findGrantsByPiLastName(String piLastName, String adminPhsOrgCode);

    public List findGrantsByApplId(long applId);
}
