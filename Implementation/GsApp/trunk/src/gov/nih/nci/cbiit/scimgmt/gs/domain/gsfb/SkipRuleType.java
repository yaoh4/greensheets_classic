
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Skip rule defines all aspects of a ruyles that make a parent(skip target) element visible or hidden
 * 
 * <p>Java class for SkipRuleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SkipRuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="questionSkipRule" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="answerSkipRule" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="answerValueUUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ruleValue" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="logicalOp" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}SkipLogicalOpType" />
 *                 &lt;attribute name="triggerQuestionUUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="triggerFormUUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="logicalOp" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}SkipLogicalOpType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkipRuleType", propOrder = {
    "questionSkipRule"
})
public class SkipRuleType {

    @XmlElement(required = true)
    protected List<SkipRuleType.QuestionSkipRule> questionSkipRule;
    @XmlAttribute(name = "logicalOp", required = true)
    protected SkipLogicalOpType logicalOp;

    /**
     * Gets the value of the questionSkipRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the questionSkipRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuestionSkipRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SkipRuleType.QuestionSkipRule }
     * 
     * 
     */
    public List<SkipRuleType.QuestionSkipRule> getQuestionSkipRule() {
        if (questionSkipRule == null) {
            questionSkipRule = new ArrayList<SkipRuleType.QuestionSkipRule>();
        }
        return this.questionSkipRule;
    }

    /**
     * Gets the value of the logicalOp property.
     * 
     * @return
     *     possible object is
     *     {@link SkipLogicalOpType }
     *     
     */
    public SkipLogicalOpType getLogicalOp() {
        return logicalOp;
    }

    /**
     * Sets the value of the logicalOp property.
     * 
     * @param value
     *     allowed object is
     *     {@link SkipLogicalOpType }
     *     
     */
    public void setLogicalOp(SkipLogicalOpType value) {
        this.logicalOp = value;
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
     *         &lt;element name="answerSkipRule" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="answerValueUUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="ruleValue" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="logicalOp" use="required" type="{http://greensheets.lifedatasystems.com/GsForms}SkipLogicalOpType" />
     *       &lt;attribute name="triggerQuestionUUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="triggerFormUUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "answerSkipRule"
    })
    public static class QuestionSkipRule {

        @XmlElement(required = true)
        protected List<SkipRuleType.QuestionSkipRule.AnswerSkipRule> answerSkipRule;
        @XmlAttribute(name = "ruleValue")
        @XmlSchemaType(name = "anySimpleType")
        protected String ruleValue;
        @XmlAttribute(name = "logicalOp", required = true)
        protected SkipLogicalOpType logicalOp;
        @XmlAttribute(name = "triggerQuestionUUID", required = true)
        protected String triggerQuestionUUID;
        @XmlAttribute(name = "triggerFormUUID", required = true)
        protected String triggerFormUUID;

        /**
         * Gets the value of the answerSkipRule property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the answerSkipRule property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnswerSkipRule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SkipRuleType.QuestionSkipRule.AnswerSkipRule }
         * 
         * 
         */
        public List<SkipRuleType.QuestionSkipRule.AnswerSkipRule> getAnswerSkipRule() {
            if (answerSkipRule == null) {
                answerSkipRule = new ArrayList<SkipRuleType.QuestionSkipRule.AnswerSkipRule>();
            }
            return this.answerSkipRule;
        }

        /**
         * Gets the value of the ruleValue property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRuleValue() {
            return ruleValue;
        }

        /**
         * Sets the value of the ruleValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRuleValue(String value) {
            this.ruleValue = value;
        }

        /**
         * Gets the value of the logicalOp property.
         * 
         * @return
         *     possible object is
         *     {@link SkipLogicalOpType }
         *     
         */
        public SkipLogicalOpType getLogicalOp() {
            return logicalOp;
        }

        /**
         * Sets the value of the logicalOp property.
         * 
         * @param value
         *     allowed object is
         *     {@link SkipLogicalOpType }
         *     
         */
        public void setLogicalOp(SkipLogicalOpType value) {
            this.logicalOp = value;
        }

        /**
         * Gets the value of the triggerQuestionUUID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTriggerQuestionUUID() {
            return triggerQuestionUUID;
        }

        /**
         * Sets the value of the triggerQuestionUUID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTriggerQuestionUUID(String value) {
            this.triggerQuestionUUID = value;
        }

        /**
         * Gets the value of the triggerFormUUID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTriggerFormUUID() {
            return triggerFormUUID;
        }

        /**
         * Sets the value of the triggerFormUUID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTriggerFormUUID(String value) {
            this.triggerFormUUID = value;
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
         *       &lt;attribute name="answerValueUUID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class AnswerSkipRule {

            @XmlAttribute(name = "answerValueUUID", required = true)
            protected String answerValueUUID;

            /**
             * Gets the value of the answerValueUUID property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAnswerValueUUID() {
                return answerValueUUID;
            }

            /**
             * Sets the value of the answerValueUUID property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAnswerValueUUID(String value) {
                this.answerValueUUID = value;
            }

        }

    }

}
