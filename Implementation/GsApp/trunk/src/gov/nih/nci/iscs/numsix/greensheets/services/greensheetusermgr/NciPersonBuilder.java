/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute.
 */
package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;


import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;
import org.apache.log4j.*;
/**
 * Creates a NciPerson object user the NCI LDAP. @see NciPerson.
 *
 * @author kpuscas, Number Six Software
 *
 */

public class NciPersonBuilder {

 

    public static final String DISTINGUISHED_NAME = "distinguishedName";



    private String ADMIN_UNIT = "nciau";
    private String CN = "cn";
    private String EMAIL = "mail";
    private String EXTERNAL_ORG = "ou";
    private String FIRST_NAME = "givenName";
    private String FULL_NAME = "fullname";
    private String FUNCTIONAL_ROLE = "nciFunctionalRole";
    private String GROUP_MEMBERSHIP = "groupMembership";
    private String LAST_NAME = "sn";
    private String MANAGER = "manager";
    private String MEMBER = "member";
    private String MIDDLE_INITIAL = "initials";
    private String ORACLE_ID = "nciOracleID";
    private String ORGANIZATION = "nciaumembership";
    private String TFSID = "nciTfsId";
    private String TITLE = "title";
    private String UID = "uid";
    private String[] PERSON_ATTRS_DETAILS =
        {
            UID,
            EMAIL,
            CN,
            FULL_NAME,
            MANAGER,
            FIRST_NAME,
            MIDDLE_INITIAL,
            LAST_NAME,
            TITLE,
            FUNCTIONAL_ROLE,
            GROUP_MEMBERSHIP,
            ORGANIZATION,
            EXTERNAL_ORG,
            ADMIN_UNIT,
            MEMBER,
            TFSID,
            ORACLE_ID };


    private static final Logger logger = Logger.getLogger(NciPersonBuilder.class);
    public NciPersonBuilder() {
    }

 
    /**
     * Method getPersonByRemoteUserName.
     * @param name
     * @return NciPerson
     * @throws GreensheetBaseException
     */
    public NciPerson getPersonByUserName(String name) throws GreensheetBaseException {

        String distinguishedName = name;


        DirContext context = null;
        NciPerson person = null;
        try {
            
            Properties p = (Properties)AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_LDAP_PROPERTIES);
            
            LdapConnectionHelper helper = new LdapConnectionHelper(p);
            context = helper.getConnection();
            person = new NciPerson();
            if (distinguishedName.indexOf("ou=")== -1) {
                person.setDistinguishedName("cn=" + distinguishedName + ",ou=EXT,ou=NCI,o=NIH");
            } else {
                person.setDistinguishedName(distinguishedName);
            }
            
            
                logger.debug("distinguished name " + person.getDistinguishedName());

            
            Attributes attributes =
                context.getAttributes(person.getDistinguishedName(), PERSON_ATTRS_DETAILS);

            populatePersonDetails(person, attributes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GreensheetBaseException("error.ldap",e);
        } finally {
            try {

                // close the context
                if (context != null) {

                    context.close();
                }
            } catch (javax.naming.NamingException e) {
                throw new GreensheetBaseException("error.ldap",e);
            }
        }

        return person;
    }

    private void populatePersonDetails(NciPerson person, Attributes personAttributes)
        throws GreensheetBaseException {
        try {

            // Detailed attributes
            Attribute attribute = personAttributes.get(UID);
            if (attribute != null) {
                person.setUid((String) attribute.get());
            }

            attribute = personAttributes.get(EMAIL);
            if (attribute != null) {
                person.setEmail((String) attribute.get());
            }

            attribute = personAttributes.get(CN);
            if (attribute != null) {
                person.setCommonName((String) attribute.get());
            }

            attribute = personAttributes.get(FULL_NAME);
            if (attribute != null) {
                person.setFullName((String) attribute.get());
            }

            attribute = personAttributes.get(MANAGER);
            if (attribute != null) {
                person.setManager((String) attribute.get());
            }

            attribute = personAttributes.get(FIRST_NAME);
            if (attribute != null) {
                person.setFirstName((String) attribute.get());
            }

            attribute = personAttributes.get(MIDDLE_INITIAL);
            if (attribute != null) {
                person.setMiddleInitial((String) attribute.get());
            }

            attribute = personAttributes.get(LAST_NAME);
            if (attribute != null) {
                person.setLastName((String) attribute.get());
            }

            attribute = personAttributes.get(TITLE);
            if (attribute != null) {
                person.setTitle((String) attribute.get());
            }

            attribute = personAttributes.get(FUNCTIONAL_ROLE);
            if (attribute != null) {
                person.setFunctionalRole((String) attribute.get());
            }

            attribute = personAttributes.get(GROUP_MEMBERSHIP);
            if (attribute != null) {

                // the groups are an enumeration, a list of groups the user is a member of
                NamingEnumeration enum = attribute.getAll();
                ArrayList groupList = new ArrayList();
                try {
                    while ((enum != null) && (enum.hasMore())) {
                        groupList.add((String) enum.next());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                person.setGroupMembership(groupList);
            }

            attribute = personAttributes.get(ORGANIZATION);
            if (attribute != null) {
                person.setOrganization((String) attribute.get());
            }

            attribute = personAttributes.get(EXTERNAL_ORG);
            if (attribute != null) {
                person.setExternalOrganization((String) attribute.get());
            } else {
                person.setExternalOrganization("NCI");
            }

            attribute = personAttributes.get(ADMIN_UNIT);
            if (attribute != null) {
                person.setAdminUnit((String) attribute.get());
            }

            attribute = personAttributes.get(MEMBER);
            if (attribute != null) {
                person.setMemeber((String) attribute.get());
            }

            attribute = personAttributes.get(TFSID);
            if (attribute != null) {
                person.setTfsId((String) attribute.get());
            }

            attribute = personAttributes.get(ORACLE_ID);
            if (attribute != null) {

                person.setOracleId((String) attribute.get());
            }
        } catch (Exception e) {
            throw new GreensheetBaseException("error.ldap",e);
        }
    }
}
