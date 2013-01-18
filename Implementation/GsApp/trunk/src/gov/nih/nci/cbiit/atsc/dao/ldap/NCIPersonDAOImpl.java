package gov.nih.nci.cbiit.atsc.dao.ldap;

import gov.nih.nci.cbiit.atsc.dao.NCIPersonDAO;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.NciPerson;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

public class NCIPersonDAOImpl implements NCIPersonDAO {

    private static final Logger logger = Logger
            .getLogger(NCIPersonDAOImpl.class);
    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public NciPerson getUserByUserName(String userName) {
        NciPerson nciPerson = null;
        String distinguishedName = null;
        //   userName = "cn=AyyalapuNR,ou=6116,ou=NCI,o=NIH";

        logger.debug("@userName " + userName);
        // Abdul: Copied over from existing. No good. Redo this work
        // if (userName.indexOf("ou=") == -1) {
        // // distinguishedName = "cn=" + userName + ",ou=EXT,ou=NCI,o=NIH";
        // } else {
        // distinguishedName = userName;
        // }

        if (userName.indexOf(",") != -1) {
            String firstToken = userName.substring(0, userName.indexOf(","));
            userName = firstToken.substring(firstToken.indexOf("=") + 1);
        }

        try {
            //  nciPerson = new NciPerson();
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("objectclass", "person"));
            filter.and(new EqualsFilter("cn", userName));
            List list = ldapTemplate.search("", filter.encode(),
                    new NciPersonAttributesMapper());

            if (list.size() == 1) {
                nciPerson = (NciPerson) list.get(0);
                distinguishedName = "cn=" + userName + ",ou="
                        + nciPerson.getExternalOrganization() + ",o=NIH";
            } else {
                throw new GreensheetBaseException("No record found.");

            }

        } catch (Exception e) {
            logger.debug("@Exception " + e.getMessage());
        }

        if (nciPerson != null) {

            Properties appProperties = (Properties) AppConfigProperties
                    .getInstance().getProperty(
                            GreensheetsKeys.KEY_CONFIG_PROPERTIES);
            String superUsers = appProperties.getProperty("superUsers");

            if (superUsers != null) {
                if (StringUtils.containsIgnoreCase(superUsers, userName)) {
                    nciPerson.setSuperUser(true);
                }
            }
        }

        return nciPerson;
    }

}