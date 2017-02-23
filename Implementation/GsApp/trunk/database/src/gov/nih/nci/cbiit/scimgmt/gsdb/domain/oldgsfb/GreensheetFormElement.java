
package gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GreensheetFormElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GreensheetFormElement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GreensheetQuestions" type="{}GreensheetQuestionsElement"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GreensheetFormElement", propOrder = {
    "greensheetQuestions"
})
public class GreensheetFormElement {

    @XmlElement(name = "GreensheetQuestions", required = true)
    protected GreensheetQuestionsElement greensheetQuestions;

    /**
     * Gets the value of the greensheetQuestions property.
     * 
     * @return
     *     possible object is
     *     {@link GreensheetQuestionsElement }
     *     
     */
    public GreensheetQuestionsElement getGreensheetQuestions() {
        return greensheetQuestions;
    }

    /**
     * Sets the value of the greensheetQuestions property.
     * 
     * @param value
     *     allowed object is
     *     {@link GreensheetQuestionsElement }
     *     
     */
    public void setGreensheetQuestions(GreensheetQuestionsElement value) {
        this.greensheetQuestions = value;
    }

}
