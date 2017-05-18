package gov.nih.nci.cbiit.atsc.dao;

import java.util.Calendar;
import java.util.List;

import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

public interface GrantDAO {
    public List findGrants(Calendar latestBudgetStartDate,
            Calendar latestBudgetEndDate,
            boolean restrictedToOpenForms,
            String applStatusGroupCode,
            boolean minorityGrantsIncluded,
            List userPortfolioIds,
            List userCancerActivities,
            String grantType,
            boolean includeOnlyGrantsWithinPayline,
            String grantMechanism,
            String fullGrantNum,
            String piLastName,
            String piFirstName,
            boolean filterCancelled
            );

    public List findGrants(String nciOracleId,
            Calendar latestBudgetStartDate,
            Calendar latestBudgetEndDate,
            boolean onControlFlag,
            boolean restrictedToOpenForms,
            boolean restrictedToLoggedinUser,
            boolean filterCancelled);

    public List findGrantsByGrantNum(String fullGrantNum);

    public List retrieveGrantsByFullGrantNum(String fullGrantNum);

    public List findGrantsByPiLastName(String piLastName, String adminPhsOrgCode);

    public List findGrantsByApplId(long applId);
    
    public FormGrantProxy findGSGrantInfo(Long actionId, Long applId, String grantNum, GsUser userId);

}

/*

desc nci.form_grant_vw
Name                           Null     Type                                                                                                                                                                                          
------------------------------ -------- --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
DUMMY_FLAG                              CHAR(1)                                                                                                                                                                                       
ON_CONTROL_FLAG                         CHAR(1)                                                                                                                                                                                       
ELECTRONIC_SUBMISSION_FLAG              CHAR(1)                                                                                                                                                                                       
APPL_ID                                 NUMBER(10)                                                                                                                                                                                    
FULL_GRANT_NUM                          VARCHAR2(19)                                                                                                                                                                                  
RFA_PA_NUMBER                           VARCHAR2(10)                                                                                                                                                                                  
COUNCIL_MEETING_DATE                    VARCHAR2(6)                                                                                                                                                                                   
FIRST_NAME                              VARCHAR2(30)                                                                                                                                                                                  
MI_NAME                                 VARCHAR2(30)                                                                                                                                                                                  
LAST_NAME                               VARCHAR2(30)                                                                                                                                                                                  
PI_NAME                                 VARCHAR2(93)                                                                                                                                                                                  
IRG_PERCENTILE_NUM                      NUMBER                                                                                                                                                                                        
PRIORITY_SCORE_NUM                      NUMBER                                                                                                                                                                                        
APPL_TYPE_CODE                          VARCHAR2(1)                                                                                                                                                                                   
ADMIN_PHS_ORG_CODE                      VARCHAR2(2)                                                                                                                                                                                   
ACTIVITY_CODE                           VARCHAR2(3)                                                                                                                                                                                   
SERIAL_NUM                              NUMBER(6)                                                                                                                                                                                     
SUPPORT_YEAR                            NUMBER(2)                                                                                                                                                                                     
SUFFIX_CODE                             VARCHAR2(4)                                                                                                                                                                                   
APPL_STATUS_CODE                        VARCHAR2(2)                                                                                                                                                                                   
APPL_STATUS_GROUP_CODE                  VARCHAR2(2)                                                                                                                                                                                   
FORMER_NUM                              VARCHAR2(19)                                                                                                                                                                                  
BUDGET_START_DATE                       DATE                                                                                                                                                                                          
LATEST_BUDGET_START_DATE                DATE                                                                                                                                                                                          
FY                                      NUMBER(4)                                                                                                                                                                                     
IPF                                     NUMBER                                                                                                                                                                                        
ORG_NAME                                VARCHAR2(40)                                                                                                                                                                                  
WITHIN_PAYLINE_FLAG                     VARCHAR2(4000)                                                                                                                                                                                
CAY_CODE                                VARCHAR2(8)                                                                                                                                                                                   
ROLE_USAGE_CODE                         VARCHAR2(2)                                                                                                                                                                                   
PD_NPE_ID                               NUMBER(10)                                                                                                                                                                                    
PD_NPN_ID                               NUMBER(10)                                                                                                                                                                                    
PD_LAST_NAME                            VARCHAR2(30)                                                                                                                                                                                  
PD_FIRST_NAME                           VARCHAR2(30)                                                                                                                                                                                  
PD_MI_NAME                              VARCHAR2(30)                                                                                                                                                                                  
PD_USER_ID                              VARCHAR2(30)                                                                                                                                                                                  
GMS_CODE                                VARCHAR2(2)                                                                                                                                                                                   
GMS_NPE_ID                              NUMBER(10)                                                                                                                                                                                    
GMS_NPN_ID                              NUMBER(10)                                                                                                                                                                                    
GMS_LAST_NAME                           VARCHAR2(30)                                                                                                                                                                                  
GMS_FIRST_NAME                          VARCHAR2(30)                                                                                                                                                                                  
GMS_MI_NAME                             VARCHAR2(30)                                                                                                                                                                                  
GMS_USER_ID                             VARCHAR2(30)                                                                                                                                                                                  
BKUP_GMS_CODE                           VARCHAR2(2)                                                                                                                                                                                   
BKUP_GMS_NPE_ID                         NUMBER(10)                                                                                                                                                                                    
BKUP_GMS_NPN_ID                         NUMBER(10)                                                                                                                                                                                    
BKUP_GMS_LAST_NAME                      VARCHAR2(30)                                                                                                                                                                                  
BKUP_GMS_FIRST_NAME                     VARCHAR2(30)                                                                                                                                                                                  
BKUP_GMS_MI_NAME                        VARCHAR2(30)                                                                                                                                                                                  
BKUP_GMS_USER_ID                        VARCHAR2(30)                                                                                                                                                                                  
ALL_GMS_USERIDS                         VARCHAR2(61)                                                                                                                                                                                  
PGM_FORM_STATUS                         VARCHAR2(50)                                                                                                                                                                                  
PGM_FORM_SUBMITTED_DATE                 DATE                                                                                                                                                                                          
SPEC_FORM_STATUS                        VARCHAR2(50)                                                                                                                                                                                  
SPEC_FORM_SUBMITTED_DATE                DATE                                                                                                                                                                                          
DM_FORM_STATUS                          VARCHAR2(50)                                                                                                                                                                                  
MB_MINORITY_FLAG                        CHAR(1)                                                                                                                                                                                       

57 rows selected

*/