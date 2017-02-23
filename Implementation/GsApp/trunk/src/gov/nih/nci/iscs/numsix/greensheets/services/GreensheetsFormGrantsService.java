package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.List;

public interface GreensheetsFormGrantsService {
    public List findGrants(GsUser user,
            boolean onControlFlag,
            boolean restrictedToOpenForms,
            boolean restrictedToLoggedinUser,
            boolean filterCancelled);

    public List findGrants(GsUser user,
            String grantsRange,
            String grantType,
            boolean includeOnlyGrantsWithinPayline,
            String grantMechanism,
            String fullGrantNum,
            String piLastName,
            String piFirstName,
            boolean filterCancelled
            );

    public List findGrantsByGrantNum(String fullGrantNum);

    public List retrieveGrantsByFullGrantNum(String fullGrantNum);

    public List findGrantsByPiLastName(String piLastName, String adminPhsOrgCode);

    public List findGrantsByApplId(long applId);
    
    public boolean checkActionStatusByApplId(String applId) throws GreensheetBaseException;

    public boolean checkActionStatusByGrantId(String grantId) throws GreensheetBaseException;

    /**
     * GREENSHEET-495
     * 
     * Is a given APPL_ID a Type 6 Grant with Award Type 1, 2, 4, 5,8 9 in same FY?
     */
	public boolean isValidGrantType(String parameter, StringBuffer gn) throws GreensheetBaseException;
	
}
