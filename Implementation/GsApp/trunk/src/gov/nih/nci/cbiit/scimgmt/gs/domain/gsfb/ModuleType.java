
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ModuleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://greensheets.lifedatasystems.com/GsForms}gsForms"/>
 *         &lt;element name="description">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="moduleName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="showPleaseSelectOptionInDropDown" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="insertCheckAllThatApplyForMultiSelectAnswers" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
@XmlType(name = "ModuleType", namespace = "http://greensheets.lifedatasystems.com/GsModule", propOrder = {
    "gsForms",
    "description",
    "moduleName",
    "showPleaseSelectOptionInDropDown",
    "insertCheckAllThatApplyForMultiSelectAnswers"
})
public class ModuleType {

    @XmlElement(namespace = "http://greensheets.lifedatasystems.com/GsForms", required = true)
    protected GsForms gsForms;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String moduleName;
    protected boolean showPleaseSelectOptionInDropDown;
    protected boolean insertCheckAllThatApplyForMultiSelectAnswers;
    @XmlAttribute(name = "uuid", required = true)
    protected String uuid;

    /**
     * Gets the value of the gsForms property.
     * 
     * @return
     *     possible object is
     *     {@link GsForms }
     *     
     */
    public GsForms getGsForms() {
        return gsForms;
    }

    /**
     * Sets the value of the gsForms property.
     * 
     * @param value
     *     allowed object is
     *     {@link GsForms }
     *     
     */
    public void setGsForms(GsForms value) {
        this.gsForms = value;
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
     * Gets the value of the moduleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Sets the value of the moduleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModuleName(String value) {
        this.moduleName = value;
    }

    /**
     * Gets the value of the showPleaseSelectOptionInDropDown property.
     * 
     */
    public boolean isShowPleaseSelectOptionInDropDown() {
        return showPleaseSelectOptionInDropDown;
    }

    /**
     * Sets the value of the showPleaseSelectOptionInDropDown property.
     * 
     */
    public void setShowPleaseSelectOptionInDropDown(boolean value) {
        this.showPleaseSelectOptionInDropDown = value;
    }

    /**
     * Gets the value of the insertCheckAllThatApplyForMultiSelectAnswers property.
     * 
     */
    public boolean isInsertCheckAllThatApplyForMultiSelectAnswers() {
        return insertCheckAllThatApplyForMultiSelectAnswers;
    }

    /**
     * Sets the value of the insertCheckAllThatApplyForMultiSelectAnswers property.
     * 
     */
    public void setInsertCheckAllThatApplyForMultiSelectAnswers(boolean value) {
        this.insertCheckAllThatApplyForMultiSelectAnswers = value;
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
