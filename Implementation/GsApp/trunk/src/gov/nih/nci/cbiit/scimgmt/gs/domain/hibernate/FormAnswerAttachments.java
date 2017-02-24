package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Sep 23, 2016 12:13:02 PM by Hibernate Tools 5.2.0.Beta1

/**
 * FormAnswerAttachments generated by hbm2java
 */
@SuppressWarnings("serial")
public class FormAnswerAttachments implements java.io.Serializable {

	private Integer id;
	private FormQuestionAnswers formQuestionAnswers;
	private String name;
	private String title;
	private String fileLocation;
	private Integer updateStamp;

	public FormAnswerAttachments() {
	}

	public FormAnswerAttachments(Integer id, FormQuestionAnswers formQuestionAnswers) {
		this.id = id;
		this.formQuestionAnswers = formQuestionAnswers;
	}

	public FormAnswerAttachments(Integer id, FormQuestionAnswers formQuestionAnswers, String name, String title,
			String fileLocation, Integer updateStamp) {
		this.id = id;
		this.formQuestionAnswers = formQuestionAnswers;
		this.name = name;
		this.title = title;
		this.fileLocation = fileLocation;
		this.updateStamp = updateStamp;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FormQuestionAnswers getFormQuestionAnswers() {
		return this.formQuestionAnswers;
	}

	public void setFormQuestionAnswers(FormQuestionAnswers formQuestionAnswers) {
		this.formQuestionAnswers = formQuestionAnswers;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileLocation() {
		return this.fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Integer getUpdateStamp() {
		return this.updateStamp;
	}

	public void setUpdateStamp(Integer updateStamp) {
		this.updateStamp = updateStamp;
	}

}