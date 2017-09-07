
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GsAnswerType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GsAnswerType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="POSITIVE_FLOAT"/>
 *     &lt;enumeration value="POSITIVE_INTEGER"/>
 *     &lt;enumeration value="INTEGER"/>
 *     &lt;enumeration value="RADIO"/>
 *     &lt;enumeration value="NUMBER"/>
 *     &lt;enumeration value="STRING"/>
 *     &lt;enumeration value="DROP_DOWN"/>
 *     &lt;enumeration value="CHECK_BOX"/>
 *     &lt;enumeration value="YEAR"/>
 *     &lt;enumeration value="MONTH_YEAR"/>
 *     &lt;enumeration value="DATE"/>
 *     &lt;enumeration value="TEXT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GsAnswerType")
@XmlEnum
public enum GsAnswerType {

    POSITIVE_FLOAT,
    POSITIVE_INTEGER,
    INTEGER,
    RADIO,
    NUMBER,
    STRING,
    DROP_DOWN,
    CHECK_BOX,
    YEAR,
    MONTH_YEAR,
    DATE,
    TEXT;

    public String value() {
        return name();
    }

    public static GsAnswerType fromValue(String v) {
        return valueOf(v);
    }

}
