package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Sep 23, 2016 12:13:02 PM by Hibernate Tools 5.2.0.Beta1

import java.util.HashSet;
import java.util.Set;

/**
 * FormModules generated by hbm2java
 */
@SuppressWarnings("serial")
public class FormModules implements java.io.Serializable {

	private Integer id;
	private String moduleUuid;
	private String name;
	private String description;
	private Boolean showDdEmptyoptionText;
	private Boolean showCheckAll;
	// No need for hashCode() and equal() methods. This is inside a session and Hibernate guarantees equivalence of persistent identity (database row) and Java identity inside a particular session scope.
	private Set<FormTemplates> formTemplates = new HashSet<FormTemplates>(0);

	public FormModules() {
	}

	public FormModules(Integer id) {
		this.id = id;
	}

	public FormModules(Integer id, String moduleUuid, String name, String description, Boolean showDdEmptyoptionText,
			Boolean showCheckAll, Set<FormTemplates> formTemplates) {
		this.id = id;
		this.moduleUuid = moduleUuid;
		this.name = name;
		this.description = description;
		this.showDdEmptyoptionText = showDdEmptyoptionText;
		this.showCheckAll = showCheckAll;
		this.formTemplates = formTemplates;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleUuid() {
		return this.moduleUuid;
	}

	public void setModuleUuid(String moduleUuid) {
		this.moduleUuid = moduleUuid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getShowDdEmptyoptionText() {
		return this.showDdEmptyoptionText;
	}

	public void setShowDdEmptyoptionText(Boolean showDdEmptyoptionText) {
		this.showDdEmptyoptionText = showDdEmptyoptionText;
	}

	public Boolean getShowCheckAll() {
		return this.showCheckAll;
	}

	public void setShowCheckAll(Boolean showCheckAll) {
		this.showCheckAll = showCheckAll;
	}

	public Set<FormTemplates> getFormTemplates() {
		return this.formTemplates;
	}

	public void setFormTemplates(Set<FormTemplates> formTemplates) {
		this.formTemplates = formTemplates;
	}

}
