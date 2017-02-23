
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TypeMechType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TypeMechType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="grantType" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *             &lt;minInclusive value="1"/>
 *             &lt;maxInclusive value="9"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="grantMechanism" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="3"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TypeMechType")
public class TypeMechType {

    @XmlAttribute(name = "grantType", required = true)
    protected int grantType;
    @XmlAttribute(name = "grantMechanism", required = true)
    protected String grantMechanism;

    /**
     * Gets the value of the grantType property.
     * 
     */
    public int getGrantType() {
        return grantType;
    }

    /**
     * Sets the value of the grantType property.
     * 
     */
    public void setGrantType(int value) {
        this.grantType = value;
    }

    /**
     * Gets the value of the grantMechanism property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrantMechanism() {
        return grantMechanism;
    }

    /**
     * Sets the value of the grantMechanism property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrantMechanism(String value) {
        this.grantMechanism = value;
    }

}
