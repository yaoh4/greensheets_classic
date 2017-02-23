
package gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResponseTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResponseTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="COMMENT"/>
 *     &lt;enumeration value="CHECK_BOX"/>
 *     &lt;enumeration value="DATE"/>
 *     &lt;enumeration value="DROP_DOWN"/>
 *     &lt;enumeration value="FILE"/>
 *     &lt;enumeration value="NUMBER"/>
 *     &lt;enumeration value="RADIO"/>
 *     &lt;enumeration value="STRING"/>
 *     &lt;enumeration value="TEXT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResponseTypes")
@XmlEnum
public enum ResponseTypes {

    COMMENT,
    CHECK_BOX,
    DATE,
    DROP_DOWN,
    FILE,
    NUMBER,
    RADIO,
    STRING,
    TEXT;

    public String value() {
        return name();
    }

    public static ResponseTypes fromValue(String v) {
        return valueOf(v);
    }

}
