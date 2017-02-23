
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for FormType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FormType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType"/>
 *         &lt;element name="description" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType" minOccurs="0"/>
 *         &lt;element name="createdBy" type="{http://greensheets.lifedatasystems.com/GsForms}UserNameType"/>
 *         &lt;element name="modifiedBy" type="{http://greensheets.lifedatasystems.com/GsForms}UserNameType"/>
 *         &lt;element name="modifiedOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="formRole" type="{http://greensheets.lifedatasystems.com/GsForms}FormRoleType" minOccurs="0"/>
 *         &lt;element name="typeMechs" type="{http://greensheets.lifedatasystems.com/GsForms}TypeMechType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="questionElement" type="{http://greensheets.lifedatasystems.com/GsForms}QuestionElementType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tableElement" type="{http://greensheets.lifedatasystems.com/GsForms}TableElementType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contentElement" type="{http://greensheets.lifedatasystems.com/GsForms}ContentElementType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormType", propOrder = {
    "name",
    "description",
    "createdBy",
    "modifiedBy",
    "modifiedOn",
    "formRole",
    "typeMechs",
    "questionElement",
    "tableElement",
    "contentElement"
})
public class FormType {

    @XmlElement(required = true)
    protected String name;
    protected String description;
    @XmlElement(required = true)
    protected String createdBy;
    @XmlElement(required = true)
    protected String modifiedBy;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modifiedOn;
    @XmlSchemaType(name = "string")
    protected FormRoleType formRole;
    protected List<TypeMechType> typeMechs;
    protected List<QuestionElementType> questionElement;
    protected List<TableElementType> tableElement;
    protected List<ContentElementType> contentElement;
    @XmlAttribute(name = "uuid", required = true)
    protected String uuid;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

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
     * Gets the value of the formRole property.
     * 
     * @return
     *     possible object is
     *     {@link FormRoleType }
     *     
     */
    public FormRoleType getFormRole() {
        return formRole;
    }

    /**
     * Sets the value of the formRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormRoleType }
     *     
     */
    public void setFormRole(FormRoleType value) {
        this.formRole = value;
    }

    /**
     * Gets the value of the typeMechs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the typeMechs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTypeMechs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeMechType }
     * 
     * 
     */
    public List<TypeMechType> getTypeMechs() {
        if (typeMechs == null) {
            typeMechs = new ArrayList<TypeMechType>();
        }
        return this.typeMechs;
    }

    /**
     * Gets the value of the questionElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the questionElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuestionElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QuestionElementType }
     * 
     * 
     */
    public List<QuestionElementType> getQuestionElement() {
        if (questionElement == null) {
            questionElement = new ArrayList<QuestionElementType>();
        }
        return this.questionElement;
    }

    /**
     * Gets the value of the tableElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tableElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTableElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TableElementType }
     * 
     * 
     */
    public List<TableElementType> getTableElement() {
        if (tableElement == null) {
            tableElement = new ArrayList<TableElementType>();
        }
        return this.tableElement;
    }

    /**
     * Gets the value of the contentElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contentElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContentElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContentElementType }
     * 
     * 
     */
    public List<ContentElementType> getContentElement() {
        if (contentElement == null) {
            contentElement = new ArrayList<ContentElementType>();
        }
        return this.contentElement;
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

}
