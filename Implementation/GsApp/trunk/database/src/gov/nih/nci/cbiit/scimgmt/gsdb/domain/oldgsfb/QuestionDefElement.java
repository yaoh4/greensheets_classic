
package gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QuestionDefElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuestionDefElement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GrantTypeMechs" type="{}GrantTypeMechsElement"/>
 *         &lt;element name="QText" type="{}QuestionText"/>
 *         &lt;element name="QInstructions" type="{}QuestionInstructions"/>
 *         &lt;element name="ResponseDefsList" type="{}ResponseDefsListElement"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{}QuestionDefId" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestionDefElement", propOrder = {
    "grantTypeMechs",
    "qText",
    "qInstructions",
    "responseDefsList"
})
public class QuestionDefElement {

    @XmlElement(name = "GrantTypeMechs", required = true)
    protected GrantTypeMechsElement grantTypeMechs;
    @XmlElement(name = "QText", required = true)
    protected String qText;
    @XmlElement(name = "QInstructions", required = true)
    protected String qInstructions;
    @XmlElement(name = "ResponseDefsList", required = true)
    protected ResponseDefsListElement responseDefsList;
    @XmlAttribute(name = "id", required = true)
    protected String id;

    /**
     * Gets the value of the grantTypeMechs property.
     * 
     * @return
     *     possible object is
     *     {@link GrantTypeMechsElement }
     *     
     */
    public GrantTypeMechsElement getGrantTypeMechs() {
        return grantTypeMechs;
    }

    /**
     * Sets the value of the grantTypeMechs property.
     * 
     * @param value
     *     allowed object is
     *     {@link GrantTypeMechsElement }
     *     
     */
    public void setGrantTypeMechs(GrantTypeMechsElement value) {
        this.grantTypeMechs = value;
    }

    /**
     * Gets the value of the qText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQText() {
        return qText;
    }

    /**
     * Sets the value of the qText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQText(String value) {
        this.qText = value;
    }

    /**
     * Gets the value of the qInstructions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQInstructions() {
        return qInstructions;
    }

    /**
     * Sets the value of the qInstructions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQInstructions(String value) {
        this.qInstructions = value;
    }

    /**
     * Gets the value of the responseDefsList property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseDefsListElement }
     *     
     */
    public ResponseDefsListElement getResponseDefsList() {
        return responseDefsList;
    }

    /**
     * Sets the value of the responseDefsList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseDefsListElement }
     *     
     */
    public void setResponseDefsList(ResponseDefsListElement value) {
        this.responseDefsList = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
