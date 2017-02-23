
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TableElementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TableElementType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://greensheets.lifedatasystems.com/GsForms}FormElementType">
 *       &lt;sequence>
 *         &lt;element name="tableShortName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="250"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="question" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://greensheets.lifedatasystems.com/GsForms}QuestionType">
 *                 &lt;sequence>
 *                   &lt;element name="description" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
 *                 &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
 *                 &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="isIdentifying" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="tableType" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="SIMPLE"/>
 *             &lt;enumeration value="DYNAMIC"/>
 *             &lt;enumeration value="STATIC"/>
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
@XmlType(name = "TableElementType", propOrder = {
    "tableShortName",
    "question"
})
public class TableElementType
    extends FormElementType
{

    @XmlElement(required = true)
    protected String tableShortName;
    @XmlElement(required = true)
    protected List<TableElementType.Question> question;
    @XmlAttribute(name = "tableType", required = true)
    protected String tableType;

    /**
     * Gets the value of the tableShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableShortName() {
        return tableShortName;
    }

    /**
     * Sets the value of the tableShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableShortName(String value) {
        this.tableShortName = value;
    }

    /**
     * Gets the value of the question property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the question property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuestion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TableElementType.Question }
     * 
     * 
     */
    public List<TableElementType.Question> getQuestion() {
        if (question == null) {
            question = new ArrayList<TableElementType.Question>();
        }
        return this.question;
    }

    /**
     * Gets the value of the tableType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableType() {
        return tableType;
    }

    /**
     * Sets the value of the tableType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableType(String value) {
        this.tableType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{http://greensheets.lifedatasystems.com/GsForms}QuestionType">
     *       &lt;sequence>
     *         &lt;element name="description" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType"/>
     *       &lt;/sequence>
     *       &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
     *       &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
     *       &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="isIdentifying" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "description"
    })
    public static class Question
        extends QuestionType
    {

        @XmlElement(required = true)
        protected String description;
        @XmlAttribute(name = "gsid", required = true)
        protected String gsid;
        @XmlAttribute(name = "uuid", required = true)
        protected String uuid;
        @XmlAttribute(name = "order", required = true)
        protected int order;
        @XmlAttribute(name = "isIdentifying")
        protected Boolean isIdentifying;

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
         * Gets the value of the isIdentifying property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isIsIdentifying() {
            return isIdentifying;
        }

        /**
         * Sets the value of the isIdentifying property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setIsIdentifying(Boolean value) {
            this.isIdentifying = value;
        }

    }

}
