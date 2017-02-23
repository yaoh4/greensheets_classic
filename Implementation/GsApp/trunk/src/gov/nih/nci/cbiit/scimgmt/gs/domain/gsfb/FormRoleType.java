
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FormRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FormRoleType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PGM"/>
 *     &lt;enumeration value="SPEC"/>
 *     &lt;enumeration value="REV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FormRoleType")
@XmlEnum
public enum FormRoleType {

    PGM,
    SPEC,
    REV;

    public String value() {
        return name();
    }

    public static FormRoleType fromValue(String v) {
        return valueOf(v);
    }

}
