
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for FormElementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FormElementType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="modifiedBy" type="{http://greensheets.lifedatasystems.com/GsForms}UserNameType"/>
 *         &lt;element name="modifiedOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="skipRule" type="{http://greensheets.lifedatasystems.com/GsForms}SkipRuleType" minOccurs="0"/>
 *         &lt;element name="learnMore" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType" minOccurs="0"/>
 *         &lt;element name="description" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType"/>
 *         &lt;element name="tags" type="{http://greensheets.lifedatasystems.com/GsForms}TagType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
 *       &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
 *       &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="isRequired" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="isReadonly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="isVisible" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormElementType", propOrder = {
    "modifiedBy",
    "modifiedOn",
    "skipRule",
    "learnMore",
    "description",
    "tags"
})
@XmlSeeAlso({
    ContentElementType.class,
    QuestionElementType.class,
    TableElementType.class
})
public abstract class FormElementType {

    @XmlElement(required = true)
    protected String modifiedBy;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modifiedOn;
    protected SkipRuleType skipRule;
    protected String learnMore;
    @XmlElement(required = true)
    protected String description;
    protected List<TagType> tags;
    @XmlAttribute(name = "gsid", required = true)
    protected String gsid;
    @XmlAttribute(name = "uuid", required = true)
    protected String uuid;
    @XmlAttribute(name = "order", required = true)
    protected int order;
    @XmlAttribute(name = "isRequired", required = true)
    protected boolean isRequired;
    @XmlAttribute(name = "isReadonly")
    protected Boolean isReadonly;
    @XmlAttribute(name = "isVisible")
    protected Boolean isVisible;

    /**
     * Gets the value of the modifiedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the value of the modifiedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedBy(String value) {
        this.modifiedBy = value;
    }

    /**
     * Gets the value of the modifiedOn property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the value of the modifiedOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModifiedOn(XMLGregorianCalendar value) {
        this.modifiedOn = value;
    }

    /**
     * Gets the value of the skipRule property.
     * 
     * @return
     *     possible object is
     *     {@link SkipRuleType }
     *     
     */
    public SkipRuleType getSkipRule() {
        return skipRule;
    }

    /**
     * Sets the value of the skipRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link SkipRuleType }
     *     
     */
    public void setSkipRule(SkipRuleType value) {
        this.skipRule = value;
    }

    /**
     * Gets the value of the learnMore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLearnMore() {
        return learnMore;
    }

    /**
     * Sets the value of the learnMore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLearnMore(String value) {
        this.learnMore = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the tags property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tags property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTags().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TagType }
     * 
     * 
     */
    public List<TagType> getTags() {
        if (tags == null) {
            tags = new ArrayList<TagType>();
        }
        return this.tags;
    }

    /**
     * Gets the value of the gsid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGsid() {
        return gsid;
    }

    /**
     * Sets the value of the gsid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGsid(String value) {
        this.gsid = value;
    }

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the order property.
     * 
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     */
    public void setOrder(int value) {
        this.order = value;
    }

    /**
     * Gets the value of the isRequired property.
     * 
     */
    public boolean isIsRequired() {
        return isRequired;
    }

    /**
     * Sets the value of the isRequired property.
     * 
     */
    public void setIsRequired(boolean value) {
        this.isRequired = value;
    }

    /**
     * Gets the value of the isReadonly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsReadonly() {
        if (isReadonly == null) {
            return false;
        } else {
            return isReadonly;
        }
    }

    /**
     * Sets the value of the isReadonly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsReadonly(Boolean value) {
        this.isReadonly = value;
    }

    /**
     * Gets the value of the isVisible property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVisible() {
        return isVisible;
    }

    /**
     * Sets the value of the isVisible property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVisible(Boolean value) {
        this.isVisible = value;
    }

}
