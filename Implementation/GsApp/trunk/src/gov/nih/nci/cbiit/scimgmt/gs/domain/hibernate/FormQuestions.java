package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Sep 23, 2016 12:13:02 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FormQuestions generated by hbm2java
 */
@SuppressWarnings("serial")
public class FormQuestions implements java.io.Serializable {

	private Integer id;
	private String gsQuestionId;
	private String gsResponseId;
	private String questionUuid;
	private String shortName;
	private String text;
	private String answerText;
	private String displayStyle;
	private String answerConstraint;
	private String answerType;
	private String gsfbChangedUserId;
	private Date gsfbChangeDate;
	// No need for hashCode() and equal() methods. This is inside a session and Hibernate guarantees equivalence of persistent identity (database row) and Java identity inside a particular session scope.
	private Set<FormAnswers> formAnswers = new HashSet<FormAnswers>(0);
	private Set<FormElements> formElements = new HashSet<FormElements>(0);

	public FormQuestions() {
	}

	public FormQuestions(Integer id) {
		this.id = id;
	}

	public FormQuestions(Integer id, String gsQuestionId, String gsResponseId, String questionUuid, String shortName,
			String text, String answerText, String displayStyle, String answerConstraint, String answerType,
			String gsfbChangedUserId, Date gsfbChangeDate, Set<FormAnswers> formAnswerses, Set<FormElements> formElementses) {
		this.id = id;
		this.gsQuestionId = gsQuestionId;
		this.gsResponseId = gsResponseId;
		this.questionUuid = questionUuid;
		this.shortName = shortName;
		this.text = text;
		this.answerText = answerText;
		this.displayStyle = displayStyle;
		this.answerConstraint = answerConstraint;
		this.answerType = answerType;
		this.gsfbChangedUserId = gsfbChangedUserId;
		this.gsfbChangeDate = gsfbChangeDate;
		this.formAnswers = formAnswerses;
		this.formElements = formElementses;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGsQuestionId() {
		return this.gsQuestionId;
	}

	public void setGsQuestionId(String gsQuestionId) {
		this.gsQuestionId = gsQuestionId;
	}

	public String getGsResponseId() {
		return this.gsResponseId;
	}

	public void setGsResponseId(String gsResponseId) {
		this.gsResponseId = gsResponseId;
	}

	public String getQuestionUuid() {
		return this.questionUuid;
	}

	public void setQuestionUuid(String questionUuid) {
		this.questionUuid = questionUuid;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAnswerText() {
		return this.answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public String getDisplayStyle() {
		return this.displayStyle;
	}

	public void setDisplayStyle(String displayStyle) {
		this.displayStyle = displayStyle;
	}

	public String getAnswerConstraint() {
		return this.answerConstraint;
	}

	public void setAnswerConstraint(String answerConstraint) {
		this.answerConstraint = answerConstraint;
	}

	public String getAnswerType() {
		return this.answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
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

	public Set<FormAnswers> getFormAnswers() {
		return this.formAnswers;
	}

	public void setFormAnswers(Set<FormAnswers> formAnswers) {
		this.formAnswers = formAnswers;
	}

	public Set<FormElements> getFormElements() {
		return this.formElements;
	}

	public void setFormElements(Set<FormElements> formElements) {
		this.formElements = formElements;
	}

}
