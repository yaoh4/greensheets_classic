package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseActionForm;

public class ReviewDraftGreensheetsForm extends GsBaseActionForm {

	private static final long serialVersionUID = 1L;

	public String moduleName;
	public String type;
	public String mechanism;
	public String updateOption;
	public boolean displayRejectButton;
	public boolean displayPromoteButton;
	public boolean displayViewGreensheetButton;
	public boolean displayReviewModuleStatusButton;
	public String selectedModuleName;

	public ReviewDraftGreensheetsForm() {
	}

	public String getMechanism() {
		return mechanism;
	}

	public void setMechanism(String mechanism) {
		this.mechanism = mechanism;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isDisplayRejectButton() {
		return displayRejectButton;
	}

	public void setDisplayRejectButton(boolean displayRejectButton) {
		this.displayRejectButton = displayRejectButton;
	}

	public boolean isDisplayPromoteButton() {
		return displayPromoteButton;
	}

	public void setDisplayPromoteButton(boolean displayPromoteButton) {
		this.displayPromoteButton = displayPromoteButton;
	}

	public boolean isDisplayViewGreensheetButton() {
		return displayViewGreensheetButton;
	}

	public void setDisplayViewGreensheetButton(boolean displayViewGreensheetButton) {
		this.displayViewGreensheetButton = displayViewGreensheetButton;
	}

	public boolean isDisplayReviewModuleStatusButton() {
		return displayReviewModuleStatusButton;
	}

	public void setDisplayReviewModuleStatusButton(boolean displayReviewModuleStatusButton) {
		this.displayReviewModuleStatusButton = displayReviewModuleStatusButton;
	}

	public String getSelectedModuleName() {
		return selectedModuleName;
	}

	public void setSelectedModuleName(String selectedModuleName) {
		this.selectedModuleName = selectedModuleName;
	}

	public String getUpdateOption() {
		return updateOption;
	}

	public void setUpdateOption(String updateOption) {
		this.updateOption = updateOption;
	}
}
