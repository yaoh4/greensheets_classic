
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SkipLogicalOpType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SkipLogicalOpType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AND"/>
 *     &lt;enumeration value="OR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SkipLogicalOpType")
@XmlEnum
public enum SkipLogicalOpType {

    AND,
    OR;

    public String value() {
        return name();
    }

    public static SkipLogicalOpType fromValue(String v) {
        return valueOf(v);
    }

}
