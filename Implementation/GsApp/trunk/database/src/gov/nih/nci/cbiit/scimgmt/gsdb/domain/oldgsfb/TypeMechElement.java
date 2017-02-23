
package gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TypeMechElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TypeMechElement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="type" use="required" type="{}GrantType" />
 *       &lt;attribute name="mech" use="required" type="{}GrantMech" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TypeMechElement")
public class TypeMechElement {

    @XmlAttribute(name = "type", required = true)
    protected String type;
    @XmlAttribute(name = "mech", required = true)
    protected String mech;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the mech property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMech() {
        return mech;
    }

    /**
     * Sets the value of the mech property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMech(String value) {
        this.mech = value;
    }

}
