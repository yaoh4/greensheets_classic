package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.cbiit.atsc.dao.NCIPersonDAO;
import gov.nih.nci.cbiit.atsc.dao.UserRoleDataDAO;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.NciPerson;

import org.apache.log4j.Logger;

public class GreensheetsUserServicesImpl implements GreensheetsUserServices {

    private static final Logger logger = Logger
            .getLogger(GreensheetsUserServicesImpl.class);

    private NCIPersonDAO nciPersonDAO;
    private UserRoleDataDAO userRoleDataDAO;
    private GreensheetsUserPreferencesServices greensheetsUserPreferencesServices;

    public NCIPersonDAO getNciPersonDAO() {
        return nciPersonDAO;
    }

    public void setNciPersonDAO(NCIPersonDAO nciPersonDAO) {
        this.nciPersonDAO = nciPersonDAO;
    }

    public UserRoleDataDAO getUserRoleDataDAO() {
        return userRoleDataDAO;
    }

    public void setUserRoleDataDAO(UserRoleDataDAO userRoleDataDAO) {
        this.userRoleDataDAO = userRoleDataDAO;
    }

    public GreensheetsUserPreferencesServices getGreensheetsUserPreferencesServices() {
        return greensheetsUserPreferencesServices;
    }

    public void setGreensheetsUserPreferencesServices(GreensheetsUserPreferencesServices greensheetsUserPreferencesServices) {
        this.greensheetsUserPreferencesServices = greensheetsUserPreferencesServices;
    }

    public GsUser getUserByUserName(String userName) {
        GsUser gsUser = null;
        NciPerson nciPerson = null;
        String userID = null;

        nciPerson = nciPersonDAO.getUserByUserName(userName);
        if (nciPerson == null)
            return null;
        if (nciPerson.getOracleId() != null) {

            gsUser = userRoleDataDAO.getUserRolesActivitiesNPEIDs(nciPerson
                    .getOracleId());
            gsUser.setNciPerson(nciPerson);
            gsUser.setOracleId(nciPerson.getOracleId());
        } else {

            gsUser = new GsUser(nciPerson);
            gsUser.setRole(GsUserRole.GS_GUEST);
        }

        if (gsUser != null) {
            gsUser.setRemoteUserName(userName);

            userID = gsUser.getNciPerson().getCommonName().toUpperCase();
            if (userID != null) {
                gsUser.setUserName(userID);
            }

            gsUser.setUserPreferences(this.greensheetsUserPreferencesServices.readUserPrefernces(gsUser.getUserName(), gsUser.getRoleAsString())); // enum is a feature available in v5 and later.
        }

        return gsUser;
    }

    public GsUser getUserRolesActivitiesNPEIDs(String nciOracleID) {
        return userRoleDataDAO.getUserRolesActivitiesNPEIDs(nciOracleID);
    }

    public static void main(String args[]) {

        GreensheetsUserServices gsClient = new GreensheetsUserServicesImpl();

        GsUser gsUser = gsClient.getUserByUserName(args[0]);
        System.out.println("nciOracleID of the user is "
                + gsUser.getNciPerson().getOracleId());
    }
}

/*
 * cd C:\ALateef\Practice\Spring\SpringLDAP\NCILDAP set
 * SPRING_LDAP=C:\dev\tools\spring-ldap-1.3.0.RELEASE SET
 * SPRING_LDAP_JARs=%SPRING_LDAP%\dist\modules\spring-ldap-core-1.3.0.RELEASE.jar
 * 
 * set SPRING_CORE=C:\dev\tools\spring-framework-2.5.6.SEC01 set
 * SPRING_CORE_JARs=%SPRING_CORE%\dist\spring.jar set
 * MISC_JARs=%SPRING_CORE%\lib\jakarta-commons\commons-lang.jar;%SPRING_CORE%\lib\jakarta-commons\commons-logging.jar;%SPRING_CORE%\lib\jakarta-commons\commons-beanutils.jar;%SPRING_CORE%\lib\jakarta-commons\commons-collections.jar
 * 
 * REM if you use LDAP Pooling, use the following set
 * MISC_JARs=%SPRING_CORE%\lib\jakarta-commons\commons-lang.jar;%SPRING_CORE%\lib\jakarta-commons\commons-logging.jar;%SPRING_CORE%\lib\jakarta-commons\commons-beanutils.jar;%SPRING_CORE%\lib\jakarta-commons\commons-collections.jar;%SPRING_CORE%\lib\jakarta-commons\commons-pool.jar
 * 
 * echo %SPRING_LDAP_JARs%;%SPRING_CORE_JARs%;%MISC_JARs%;. javac -d bin
 * -classpath %SPRING_LDAP_JARs%;%SPRING_CORE_JARs%;%MISC_JARs%;. src\*.java
 * 
 * REM copy src\com\javaworld\sample\springldap.xml
 * bin\com\javaworld\sample\springldap.xml /Y /V
 * 
 * java -classpath %SPRING_LDAP_JARs%;%SPRING_CORE_JARs%;%MISC_JARs%;bin;.
 * GreensheetsUserServicesImpl gs_pd
 * 
 */