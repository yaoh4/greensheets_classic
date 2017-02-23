
package gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GreensheetForm_QNAME = new QName("", "GreensheetForm");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.nih.nci.cbiit.scimgmt.gsdb.domain.oldgsfb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GreensheetFormElement }
     * 
     */
    public GreensheetFormElement createGreensheetFormElement() {
        return new GreensheetFormElement();
    }

    /**
     * Create an instance of {@link TypeMechElement }
     * 
     */
    public TypeMechElement createTypeMechElement() {
        return new TypeMechElement();
    }

    /**
     * Create an instance of {@link GreensheetQuestionsElement }
     * 
     */
    public GreensheetQuestionsElement createGreensheetQuestionsElement() {
        return new GreensheetQuestionsElement();
    }

    /**
     * Create an instance of {@link QuestionDefElement }
     * 
     */
    public QuestionDefElement createQuestionDefElement() {
        return new QuestionDefElement();
    }

    /**
     * Create an instance of {@link GrantTypeMechsElement }
     * 
     */
    public GrantTypeMechsElement createGrantTypeMechsElement() {
        return new GrantTypeMechsElement();
    }

    /**
     * Create an instance of {@link ResponseDefElement }
     * 
     */
    public ResponseDefElement createResponseDefElement() {
        return new ResponseDefElement();
    }

    /**
     * Create an instance of {@link ResponseDefsListElement }
     * 
     */
    public ResponseDefsListElement createResponseDefsListElement() {
        return new ResponseDefsListElement();
    }

    /**
     * Create an instance of {@link SelectionDefElement }
     * 
     */
    public SelectionDefElement createSelectionDefElement() {
        return new SelectionDefElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GreensheetFormElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "GreensheetForm")
    public JAXBElement<GreensheetFormElement> createGreensheetForm(GreensheetFormElement value) {
        return new JAXBElement<GreensheetFormElement>(_GreensheetForm_QNAME, GreensheetFormElement.class, null, value);
    }

}
