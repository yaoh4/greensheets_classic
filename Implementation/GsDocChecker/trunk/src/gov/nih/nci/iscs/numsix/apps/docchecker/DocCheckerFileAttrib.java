package gov.nih.nci.iscs.numsix.apps.docchecker;

public class DocCheckerFileAttrib {
	private String fileName;

	private boolean foundOnDisk;

	private boolean recordedInDb;

	private long fileLength;

	private String actionRequired;

	private String actionTaken;

	private String comments;

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean getFoundOnDisk() {
		return this.foundOnDisk;
	}

	public void setFoundOnDisk(boolean foundOnDisk) {
		this.foundOnDisk = foundOnDisk;
	}

	public boolean getRecordedInDb() {
		return this.recordedInDb;
	}

	public void setRecordedInDb(boolean recordedInDb) {
		this.recordedInDb = recordedInDb;
	}

	public long getFileLength() {
		return this.fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getActionRequired() {
		return this.actionRequired;
	}

	public void setActionRequired(String actionRequired) {
		this.actionRequired = actionRequired;
	}

	public String getActionTaken() {
		return this.actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}