package gov.nih.nci.cbiit.atsc.dao.ldap;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.NciPerson;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.NamingEnumeration;
import java.util.ArrayList;
import org.springframework.ldap.core.AttributesMapper;

public class NciPersonAttributesMapper implements AttributesMapper {

	public Object mapFromAttributes(Attributes personAttributes) throws NamingException {
		NciPerson person = new NciPerson();

		Attribute attribute = personAttributes.get("uid");
		if (attribute != null) {
			person.setUid((String) attribute.get());
		}

		attribute = personAttributes.get("mail");
		if (attribute != null) {
			person.setEmail((String) attribute.get());
		}

		attribute = personAttributes.get("cn");
		if (attribute != null) {
			person.setCommonName((String) attribute.get());
		}

		attribute = personAttributes.get("fullname");
		if (attribute != null) {
			person.setFullName((String) attribute.get());
		}

		attribute = personAttributes.get("manager");
		if (attribute != null) {
			person.setManager((String) attribute.get());
		}

		attribute = personAttributes.get("givenName");
		if (attribute != null) {
			person.setFirstName((String) attribute.get());
		}

		attribute = personAttributes.get("initials");
		if (attribute != null) {
			person.setMiddleInitial((String) attribute.get());
		}

		attribute = personAttributes.get("sn");
		if (attribute != null) {
			person.setLastName((String) attribute.get());
		}

		attribute = personAttributes.get("title");
		if (attribute != null) {
			person.setTitle((String) attribute.get());
		}

		attribute = personAttributes.get("nciFunctionalRole");
		if (attribute != null) {
			person.setFunctionalRole((String) attribute.get());
		}

		attribute = personAttributes.get("groupMembership");
		if (attribute != null) {
			// the groups are an enumeration, a list of groups the user is a member of
			NamingEnumeration enums = attribute.getAll();
			ArrayList<String> groupList = new ArrayList<String>();
			try {
				while ((enums != null) && (enums.hasMore())) {
					groupList.add((String) enums.next());
				}
			} catch (Exception e) {
				// throw new GreensheetBaseException("error.ldap", e); //Abdul: Redo this work
				throw new NamingException("Some error ocuurred. - Abdul");// Abdul
			}
			person.setGroupMembership(groupList);
		}

		attribute = personAttributes.get("nciaumembership");
		if (attribute != null) {
			person.setOrganization((String) attribute.get());
		}

		attribute = personAttributes.get("ou");
		if (attribute != null) {
			person.setExternalOrganization((String) attribute.get());
		} else {
			person.setExternalOrganization("NCI");
		}

		attribute = personAttributes.get("nciau");
		if (attribute != null) {
			person.setAdminUnit((String) attribute.get());
		}

		attribute = personAttributes.get("member");
		if (attribute != null) {
			person.setMemeber((String) attribute.get());
		}

		attribute = personAttributes.get("nciTfsId");
		if (attribute != null) {
			person.setTfsId((String) attribute.get());
		}

		attribute = personAttributes.get("nciOracleID");
		if (attribute != null) {

			person.setOracleId((String) attribute.get());
		}

		return person;
	}
}