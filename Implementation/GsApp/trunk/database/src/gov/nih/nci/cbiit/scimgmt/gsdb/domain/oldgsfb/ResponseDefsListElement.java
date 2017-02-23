
package gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResponseDefsListElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseDefsListElement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponseDef" type="{}ResponseDefElement" maxOccurs="3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseDefsListElement", propOrder = {
    "responseDef"
})
public class ResponseDefsListElement {

    @XmlElement(name = "ResponseDef", required = true)
    protected List<ResponseDefElement> responseDef;

    /**
     * Gets the value of the responseDef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the responseDef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResponseDef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResponseDefElement }
     * 
     * 
     */
    public List<ResponseDefElement> getResponseDef() {
        if (responseDef == null) {
            responseDef = new ArrayList<ResponseDefElement>();
        }
        return this.responseDef;
    }

}
