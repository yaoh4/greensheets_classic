package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Sep 23, 2016 12:13:02 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FormQuestionsDraft generated by hbm2java
 */
@SuppressWarnings("serial")
public class FormQuestionsDraft implements java.io.Serializable {

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
	private Set<FormElementsDraft> formElementsDrafts = new HashSet<FormElementsDraft>(0);
	private Set<FormAnswersDraft> formAnswersDrafts = new HashSet<FormAnswersDraft>(0);

	public FormQuestionsDraft() {
	}

	public FormQuestionsDraft(Integer id) {
		this.id = id;
	}

	public FormQuestionsDraft(Integer id, String gsQuestionId, String gsResponseId, String questionUuid,
			String shortName, String text, String answerText, String displayStyle, String answerConstraint,
			String answerType, String gsfbChangedUserId, Date gsfbChangeDate, Set<FormElementsDraft> formElementsDrafts, Set<FormAnswersDraft> formAnswersDrafts) {
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
		this.formElementsDrafts = formElementsDrafts;
		this.formAnswersDrafts = formAnswersDrafts;
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

	public Set<FormElementsDraft> getFormElementsDrafts() {
		return this.formElementsDrafts;
	}

	public void setFormElementsDrafts(Set<FormElementsDraft> formElementsDrafts) {
		this.formElementsDrafts = formElementsDrafts;
	}

	public Set<FormAnswersDraft> getFormAnswersDrafts() {
		return this.formAnswersDrafts;
	}

	public void setFormAnswersDrafts(Set<FormAnswersDraft> formAnswersDrafts) {
		this.formAnswersDrafts = formAnswersDrafts;
	}

	public FormQuestions toFormQuestion(){
		FormQuestions fq = new FormQuestions();
		fq.setGsQuestionId(this.gsQuestionId);
		fq.setGsResponseId(this.gsResponseId);
		fq.setQuestionUuid(this.questionUuid);
		fq.setShortName(this.shortName);
		fq.setText(this.text);
		fq.setAnswerText(this.answerText);
		fq.setDisplayStyle(this.displayStyle);
		fq.setAnswerConstraint(this.answerConstraint);
		fq.setAnswerType(this.answerType);
		fq.setGsfbChangedUserId(this.gsfbChangedUserId);
		fq.setGsfbChangeDate(this.gsfbChangeDate);

		Set<FormAnswers> fas = new HashSet<FormAnswers>();
		fq.setFormAnswerses(fas);
		for (FormAnswersDraft fad : this.formAnswersDrafts) {
			FormAnswers fa = new FormAnswers();
			fas.add(fa);
			fa.setFormQuestions(fq);
			fa.setGsAnswerId(fad.getGsAnswerId());
			fa.setAnswerUuid(fad.getAnswerUuid());
			fa.setOrder(fad.getOrder());
			fa.setText(fad.getText());
			fa.setValue(fad.getValue());
			fa.setDefault_(fad.getDefault_());
			fa.setFormQuestions(fq);
		}

		return fq;
	}
}