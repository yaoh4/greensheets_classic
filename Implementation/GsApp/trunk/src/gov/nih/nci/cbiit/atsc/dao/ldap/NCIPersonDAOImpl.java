package gov.nih.nci.cbiit.atsc.dao.ldap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import gov.nih.nci.cbiit.atsc.dao.NCIPersonDAO;
import gov.nih.nci.cbiit.atsc.dao.NciPeopleVw;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.NciPerson;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class NCIPersonDAOImpl implements NCIPersonDAO {

    private static final Logger logger = Logger
            .getLogger(NCIPersonDAOImpl.class);

    public NciPerson getUserByUserName(String userName) {

        NciPerson nciPerson = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String selectNciUser = "SELECT DISTINCT ORACLE_ID, FIRST_NAME, LAST_NAME, EMAIL_ADDRESS FROM NCI_PEOPLE_VW WHERE NIH_NETWORK_ID =? ";

        try {
            conn = DbConnectionHelper.getInstance().getConnection();
            stmt = conn.prepareStatement(selectNciUser);
            stmt.setString(1, userName.trim().toUpperCase());
            rs = stmt.executeQuery();
            if (rs != null && rs.next()) {
                if (rs.getRow() > 1) {
                    throw new Exception("Duplicate accounts exist for same NIH NETWORK ID : " + userName);
            }
                nciPerson = new NciPerson();
                nciPerson.setOracleId(rs.getString("ORACLE_ID"));
                nciPerson.setFirstName(rs.getString("FIRST_NAME"));
                nciPerson.setLastName(rs.getString("LAST_NAME"));
                nciPerson.setEmail(rs.getString("EMAIL_ADDRESS"));
                String fullName = nciPerson.getFirstName() + " " + nciPerson.getLastName();
                nciPerson.setFullName(fullName);
                nciPerson.setCommonName(userName);
                nciPerson.setDistinguishedName(userName);
        }

        if (nciPerson != null) {

            Properties appProperties = (Properties) AppConfigProperties
                    .getInstance().getProperty(
                            GreensheetsKeys.KEY_CONFIG_PROPERTIES);
            String superUsers = appProperties.getProperty("superUsers");
            logger.debug("Super Users: " + superUsers);

            if (superUsers != null) {
                if (StringUtils.containsIgnoreCase(superUsers, userName)) {
                	logger.debug(userName + " is a superUser");
                    nciPerson.setSuperUser(true);
                }
            }
        }
        } catch (Exception ex) {
            logger.error("Error occurred while getting user oracle_id of NCI User with NIH NETWORK ID : " + userName, ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                DbConnectionHelper.getInstance().freeConnection(conn);
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }

        return nciPerson;
    }
    
    public boolean isI2eAccountValid(String nciOracleId){      
        boolean isI2eAccountValid = true; 
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;       
        NciPeopleVw nciUser = null;
        String nciUserSelect = "SELECT DISTINCT * FROM NCI_PEOPLE_VW WHERE ORACLE_ID =? ";
        try{
            conn = DbConnectionHelper.getInstance().getConnection();
            logger.debug("Using connection: " + conn);
            stmt = conn.prepareStatement(nciUserSelect);
            stmt.setString(1, nciOracleId.toUpperCase());
            rs = stmt.executeQuery();

            if(rs != null && rs.next()){
                if(rs.getRow() > 1){
                    throw new Exception("Duplicate accounts exist for same oracleId : "+nciOracleId);
                }
                nciUser = new NciPeopleVw();
                nciUser.setNpnId(Long.parseLong(rs.getString("NPN_ID")));
                nciUser.setNihNetworkId(rs.getString("NIH_NETWORK_ID"));
                nciUser.setOracleId(rs.getString("ORACLE_ID"));
                nciUser.setFirstName(rs.getString("FIRST_NAME"));
                nciUser.setLastName(rs.getString("LAST_NAME"));
                nciUser.setEmailAddress(rs.getString("EMAIL_ADDRESS"));
             /*   if(rs.getString("INACTIVE_DATE") != null && !"".equals(rs.getString("INACTIVE_DATE"))){
                   nciUser.setInactiveDate(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(rs.getString("INACTIVE_DATE")));
                   
                }   */        
                nciUser.setActiveFlag(rs.getString("ACTIVE_FLAG"));               
            }            

            if(nciUser == null || "N".equalsIgnoreCase((String)nciUser.getActiveFlag())){
                isI2eAccountValid = false;
                if(nciUser == null){
                    logger.info("I2E Account with oracleId : "+nciOracleId + " is not Valid. No record found in NCI_PEOPLE_VW.");
                } else if ("N".equalsIgnoreCase((String) nciUser.getActiveFlag())) {
                    logger.info("I2E Account with oracleId : "+nciOracleId + " is not Valid. I2e account is Inactive.");
                }           
            }
            
            if(isRestrictedUser(nciOracleId)){
                isI2eAccountValid = false;        
            }
        } catch (Exception ex) {
            logger.error("Error occurred while validating I2E Account of NCI User with oracleid: "+nciOracleId, ex);
            
        } finally {
            try {
                if (rs != null) {
                    rs.close();
        }
                if (stmt != null) {
                    stmt.close();
                }
                DbConnectionHelper.getInstance().freeConnection(conn);
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return isI2eAccountValid;
    }
    
    
    public boolean isRestrictedUser(String oracleId){ 
        
        boolean isRestrictedUser = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String restrictedUserCheck = "SELECT GRANTED_ROLE FROM DBA_ROLE_PRIVS WHERE UPPER(GRANTEE)= ? ";
        
        try{
            
            conn = DbConnectionHelper.getInstance().getConnection();
            stmt = conn.prepareStatement(restrictedUserCheck);
            stmt.setString(1, oracleId.toUpperCase());
            rs = stmt.executeQuery();
            while (rs.next()) {
               String role = rs.getString("GRANTED_ROLE");
               if(role.toUpperCase().equals("I2E_RESTRICTED_USER")){
                   isRestrictedUser = true;
                   logger.info("I2E Account with oracleId : "+oracleId + " is Restricted.");
                   break;
               }
            }
           

        } catch (Throwable ex) {
            logger.error("Error occurred while retrieving NCI User Roles with oracleId: "+oracleId);         
        }
        finally {
            try{
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            DbConnectionHelper.getInstance().freeConnection(conn);
            }catch(Exception e){
            	logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }     
        return isRestrictedUser;
    }


}