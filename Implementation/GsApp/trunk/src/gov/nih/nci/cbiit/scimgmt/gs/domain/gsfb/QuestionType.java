
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
 * <p>Java class for QuestionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuestionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="shortName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="250"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="answer">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice>
 *                     &lt;element name="description" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType" minOccurs="0"/>
 *                     &lt;element name="answerValue" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;all>
 *                               &lt;element name="label">
 *                                 &lt;simpleType>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                     &lt;maxLength value="250"/>
 *                                   &lt;/restriction>
 *                                 &lt;/simpleType>
 *                               &lt;/element>
 *                             &lt;/all>
 *                             &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
 *                             &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
 *                             &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                             &lt;attribute name="isDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/choice>
 *                   &lt;element name="displayStyle" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="Medium"/>
 *                         &lt;enumeration value="Long"/>
 *                         &lt;enumeration value="Short"/>
 *                         &lt;enumeration value="Horizontal"/>
 *                         &lt;enumeration value="Vertical"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="valueConstraint" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="100"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
 *                 &lt;attribute name="type" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsAnswerType" />
 *                 &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="modifiedBy" type="{http://greensheets.lifedatasystems.com/GsForms}UserNameType"/>
 *         &lt;element name="modifiedOn" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *       &lt;attribute name="qType">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="tableQuestion"/>
 *             &lt;enumeration value="question"/>
 *             &lt;enumeration value=""/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="answerType" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="SINGLE_ANSWER"/>
 *             &lt;enumeration value="MULTI_ANSWER"/>
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
@XmlType(name = "QuestionType", propOrder = {
    "shortName",
    "answer",
    "modifiedBy",
    "modifiedOn"
})
@XmlSeeAlso({
    gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb.TableElementType.Question.class
})
public class QuestionType {

    @XmlElement(required = true)
    protected String shortName;
    @XmlElement(required = true)
    protected QuestionType.Answer answer;
    @XmlElement(required = true)
    protected String modifiedBy;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modifiedOn;
    @XmlAttribute(name = "qType")
    protected String qType;
    @XmlAttribute(name = "answerType", required = true)
    protected String answerType;

    /**
     * Gets the value of the shortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the value of the shortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortName(String value) {
        this.shortName = value;
    }

    /**
     * Gets the value of the answer property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionType.Answer }
     *     
     */
    public QuestionType.Answer getAnswer() {
        return answer;
    }

    /**
     * Sets the value of the answer property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionType.Answer }
     *     
     */
    public void setAnswer(QuestionType.Answer value) {
        this.answer = value;
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
     * Gets the value of the qType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQType() {
        return qType;
    }

    /**
     * Sets the value of the qType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQType(String value) {
        this.qType = value;
    }

    /**
     * Gets the value of the answerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnswerType() {
        return answerType;
    }

    /**
     * Sets the value of the answerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnswerType(String value) {
        this.answerType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;choice>
     *           &lt;element name="description" type="{http://greensheets.lifedatasystems.com/GsForms}LongTextType" minOccurs="0"/>
     *           &lt;element name="answerValue" maxOccurs="unbounded">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;all>
     *                     &lt;element name="label">
     *                       &lt;simpleType>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                           &lt;maxLength value="250"/>
     *                         &lt;/restriction>
     *                       &lt;/simpleType>
     *                     &lt;/element>
     *                   &lt;/all>
     *                   &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
     *                   &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
     *                   &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                   &lt;attribute name="isDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/choice>
     *         &lt;element name="displayStyle" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;enumeration value="Medium"/>
     *               &lt;enumeration value="Long"/>
     *               &lt;enumeration value="Short"/>
     *               &lt;enumeration value="Horizontal"/>
     *               &lt;enumeration value="Vertical"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="valueConstraint" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="100"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
     *       &lt;attribute name="type" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsAnswerType" />
     *       &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "description",
        "answerValue",
        "displayStyle",
        "valueConstraint"
    })
    public static class Answer {

        protected String description;
        protected List<QuestionType.Answer.AnswerValue> answerValue;
        protected String displayStyle;
        protected String valueConstraint;
        @XmlAttribute(name = "gsid", required = true)
        protected String gsid;
        @XmlAttribute(name = "type", required = true)
        protected GsAnswerType type;
        @XmlAttribute(name = "uuid", required = true)
        protected String uuid;

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
         * Gets the value of the answerValue property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the answerValue property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnswerValue().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link QuestionType.Answer.AnswerValue }
         * 
         * 
         */
        public List<QuestionType.Answer.AnswerValue> getAnswerValue() {
            if (answerValue == null) {
                answerValue = new ArrayList<QuestionType.Answer.AnswerValue>();
            }
            return this.answerValue;
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
            return displayStyle;
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

        /**
         * Gets the value of the valueConstraint property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValueConstraint() {
            return valueConstraint;
        }

        /**
         * Sets the value of the valueConstraint property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValueConstraint(String value) {
            this.valueConstraint = value;
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
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link GsAnswerType }
         *     
         */
        public GsAnswerType getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link GsAnswerType }
         *     
         */
        public void setType(GsAnswerType value) {
            this.type = value;
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;all>
         *         &lt;element name="label">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;maxLength value="250"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *       &lt;/all>
         *       &lt;attribute name="gsid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}GsidType" />
         *       &lt;attribute name="uuid" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}UuidType" />
         *       &lt;attribute name="order" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="isDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {

        })
        public static class AnswerValue {

            @XmlElement(required = true)
            protected String label;
            @XmlAttribute(name = "gsid", required = true)
            protected String gsid;
            @XmlAttribute(name = "uuid", required = true)
            protected String uuid;
            @XmlAttribute(name = "order", required = true)
            protected int order;
            @XmlAttribute(name = "isDefault")
            protected Boolean isDefault;

            /**
             * Gets the value of the label property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLabel() {
                return label;
            }

            /**
             * Sets the value of the label property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLabel(String value) {
                this.label = value;
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
             * Gets the value of the isDefault property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isIsDefault() {
                return isDefault;
            }

            /**
             * Sets the value of the isDefault property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setIsDefault(Boolean value) {
                this.isDefault = value;
            }

        }

    }

}
