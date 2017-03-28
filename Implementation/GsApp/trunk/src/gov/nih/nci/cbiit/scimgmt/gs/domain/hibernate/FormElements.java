package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Sep 23, 2016 12:13:02 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FormElements generated by hbm2java
 */
@SuppressWarnings("serial")
public class FormElements implements java.io.Serializable {

	private Integer id;
	private FormTemplates formTemplates;
	private FormAnswers formAnswers;
	private FormQuestions formQuestions;
	private FormElements formElements;
	private String elementUuid;
	private Integer order;
	private Integer hierarchicalOrder;
	private Boolean required;
	private Boolean readonly;
	private String learnMore;
	private String text;
	private String gsfbChangedUserId;
	private Date gsfbChangeDate;
	private Integer updateStamp;
	// No need for hashCode() and equal() methods. This is inside a session and Hibernate guarantees equivalence of persistent identity (database row) and Java identity inside a particular session scope.
	private Set<FormElements> formElementSet = new HashSet<FormElements>(0);

	public FormElements() {
	}

	public FormElements(Integer id) {
		this.id = id;
	}

	public FormElements(Integer id, FormTemplates formTemplates, FormAnswers formAnswers, FormQuestions formQuestions,
			FormElements formElements, String elementUuid, Integer order, Integer hierarchicalOrder, Boolean required,
			Boolean readonly, String learnMore, String text, String gsfbChangedUserId,
			Date gsfbChangeDate, Integer updateStamp, Set<FormElements> formElementSet) {
		this.id = id;
		this.formTemplates = formTemplates;
		this.formAnswers = formAnswers;
		this.formQuestions = formQuestions;
		this.formElements = formElements;
		this.elementUuid = elementUuid;
		this.order = order;
		this.hierarchicalOrder = hierarchicalOrder;
		this.required = required;
		this.readonly = readonly;
		this.learnMore = learnMore;
		this.text = text;
		this.gsfbChangedUserId = gsfbChangedUserId;
		this.gsfbChangeDate = gsfbChangeDate;
		this.updateStamp = updateStamp;
		this.formElementSet = formElementSet;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FormTemplates getFormTemplates() {
		return this.formTemplates;
	}

	public void setFormTemplates(FormTemplates formTemplates) {
		this.formTemplates = formTemplates;
	}

	public FormAnswers getFormAnswers() {
		return this.formAnswers;
	}

	public void setFormAnswers(FormAnswers formAnswers) {
		this.formAnswers = formAnswers;
	}

	public FormQuestions getFormQuestions() {
		return this.formQuestions;
	}

	public void setFormQuestions(FormQuestions formQuestions) {
		this.formQuestions = formQuestions;
	}

	public FormElements getFormElements() {
		return this.formElements;
	}

	public void setFormElements(FormElements formElements) {
		this.formElements = formElements;
	}

	public String getElementUuid() {
		return this.elementUuid;
	}

	public void setElementUuid(String elementUuid) {
		this.elementUuid = elementUuid;
	}

	public Integer getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getHierarchicalOrder() {
		return this.hierarchicalOrder;
	}

	public void setHierarchicalOrder(Integer hierarchicalOrder) {
		this.hierarchicalOrder = hierarchicalOrder;
	}

	public Boolean getRequired() {
		return this.required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getReadonly() {
		return this.readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public String getLearnMore() {
		return this.learnMore;
	}

	public void setLearnMore(String learnMore) {
		this.learnMore = learnMore;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getGsfbChangedUserId() {
		return this.gsfbChangedUserId;
	}

	public void setGsfbChangedUserId(String gsfbChangedUserId) {
		this.gsfbChangedUserId = gsfbChangedUserId;
	}

	public Date getGsfbChangeDate() {
		return this.gsfbChangeDate;
	}

	public void setGsfbChangeDate(Date gsfbChangeDate) {
		this.gsfbChangeDate = gsfbChangeDate;
	}

	public Integer getUpdateStamp() {
		return this.updateStamp;
	}

	public void setUpdateStamp(Integer updateStamp) {
		this.updateStamp = updateStamp;
	}

	public Set<FormElements> getFormElementSet() {
		return this.formElementSet;
	}

	public void setFormElementSet(Set<FormElements> formElementSet) {
		this.formElementSet = formElementSet;
	}

}
