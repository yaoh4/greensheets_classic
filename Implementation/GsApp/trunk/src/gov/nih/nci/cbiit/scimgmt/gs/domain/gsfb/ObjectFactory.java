
package gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb package. 
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

    private final static QName _Module_QNAME = new QName("http://greensheets.lifedatasystems.com/GsModule", "module");
    private final static QName _ContentElementTypeDescription_QNAME = new QName("http://greensheets.lifedatasystems.com/GsForms", "description");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.nih.nci.cbiit.scimgmt.gs.domain.gsfb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TableElementType }
     * 
     */
    public TableElementType createTableElementType() {
        return new TableElementType();
    }

    /**
     * Create an instance of {@link QuestionType }
     * 
     */
    public QuestionType createQuestionType() {
        return new QuestionType();
    }

    /**
     * Create an instance of {@link QuestionType.Answer }
     * 
     */
    public QuestionType.Answer createQuestionTypeAnswer() {
        return new QuestionType.Answer();
    }

    /**
     * Create an instance of {@link SkipRuleType }
     * 
     */
    public SkipRuleType createSkipRuleType() {
        return new SkipRuleType();
    }

    /**
     * Create an instance of {@link SkipRuleType.QuestionSkipRule }
     * 
     */
    public SkipRuleType.QuestionSkipRule createSkipRuleTypeQuestionSkipRule() {
        return new SkipRuleType.QuestionSkipRule();
    }

    /**
     * Create an instance of {@link ModuleType }
     * 
     */
    public ModuleType createModuleType() {
        return new ModuleType();
    }

    /**
     * Create an instance of {@link GsForms }
     * 
     */
    public GsForms createGsForms() {
        return new GsForms();
    }

    /**
     * Create an instance of {@link FormType }
     * 
     */
    public FormType createFormType() {
        return new FormType();
    }

    /**
     * Create an instance of {@link ContentElementType }
     * 
     */
    public ContentElementType createContentElementType() {
        return new ContentElementType();
    }

    /**
     * Create an instance of {@link QuestionElementType }
     * 
     */
    public QuestionElementType createQuestionElementType() {
        return new QuestionElementType();
    }

    /**
     * Create an instance of {@link TagType }
     * 
     */
    public TagType createTagType() {
        return new TagType();
    }

    /**
     * Create an instance of {@link TypeMechType }
     * 
     */
    public TypeMechType createTypeMechType() {
        return new TypeMechType();
    }

    /**
     * Create an instance of {@link TableElementType.Question }
     * 
     */
    public TableElementType.Question createTableElementTypeQuestion() {
        return new TableElementType.Question();
    }

    /**
     * Create an instance of {@link QuestionType.Answer.AnswerValue }
     * 
     */
    public QuestionType.Answer.AnswerValue createQuestionTypeAnswerAnswerValue() {
        return new QuestionType.Answer.AnswerValue();
    }

    /**
     * Create an instance of {@link SkipRuleType.QuestionSkipRule.AnswerSkipRule }
     * 
     */
    public SkipRuleType.QuestionSkipRule.AnswerSkipRule createSkipRuleTypeQuestionSkipRuleAnswerSkipRule() {
        return new SkipRuleType.QuestionSkipRule.AnswerSkipRule();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModuleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://greensheets.lifedatasystems.com/GsModule", name = "module")
    public JAXBElement<ModuleType> createModule(ModuleType value) {
        return new JAXBElement<ModuleType>(_Module_QNAME, ModuleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://greensheets.lifedatasystems.com/GsForms", name = "description", scope = ContentElementType.class)
    public JAXBElement<String> createContentElementTypeDescription(String value) {
        return new JAXBElement<String>(_ContentElementTypeDescription_QNAME, String.class, ContentElementType.class, value);
    }

}
