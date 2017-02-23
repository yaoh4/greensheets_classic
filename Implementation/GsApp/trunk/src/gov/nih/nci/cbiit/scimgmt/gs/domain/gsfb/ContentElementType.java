
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContentElementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContentElementType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://greensheets.lifedatasystems.com/GsForms}FormElementType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="displayStyle" default="HEADING">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="CUSTOM"/>
 *             &lt;enumeration value="REGULAR"/>
 *             &lt;enumeration value="SUBHEADING"/>
 *             &lt;enumeration value="HEADING"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentElementType", propOrder = {
    "rest"
})
public class ContentElementType
    extends FormElementType
{

    @XmlElementRef(name = "description", namespace = "http://greensheets.lifedatasystems.com/GsForms", type = JAXBElement.class, required = false)
    protected List<JAXBElement<String>> rest;
    @XmlAttribute(name = "displayStyle")
    protected String displayStyle;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Description" is used by two different parts of a schema. See: 
     * line 159 of file:/Users/polonskyy/Work/Project%20-%20Greensheets/gsnew-trunk/src/gov/nih/nci/cbiit/scimgmt/gs/domain/gsfb/GsForm2.xsd
     * line 124 of file:/Users/polonskyy/Work/Project%20-%20Greensheets/gsnew-trunk/src/gov/nih/nci/cbiit/scimgmt/gs/domain/gsfb/GsForm2.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<String>>();
        }
        return this.rest;
    }

    /**
     * Gets the value of the displayStyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayStyle() {
        if (displayStyle == null) {
            return "HEADING";
        } else {
            return displayStyle;
        }
    }

    /**
     * Sets the value of the displayStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayStyle(String value) {
        this.displayStyle = value;
    }

}
