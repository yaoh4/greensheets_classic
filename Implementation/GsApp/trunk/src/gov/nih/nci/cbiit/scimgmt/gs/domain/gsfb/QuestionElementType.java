
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QuestionElementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuestionElementType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://greensheets.lifedatasystems.com/GsForms}FormElementType">
 *       &lt;sequence>
 *         &lt;element name="question" type="{http://greensheets.lifedatasystems.com/GsForms}QuestionType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestionElementType", propOrder = {
    "question"
})
public class QuestionElementType
    extends FormElementType
{

    @XmlElement(required = true)
    protected QuestionType question;

    /**
     * Gets the value of the question property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionType }
     *     
     */
    public QuestionType getQuestion() {
        return question;
    }

    /**
     * Sets the value of the question property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionType }
     *     
     */
    public void setQuestion(QuestionType value) {
        this.question = value;
    }

}
