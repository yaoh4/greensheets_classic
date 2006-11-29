package gov.nih.nci.iscs.numsix.apps.docchecker;

import java.util.HashMap;
import java.util.Map;

public class DocCheckerResults {
	private Map docCheckerResultsMap;
	private long totalFileCount;
	private long filesToBeReconciled;
	private long filesToBeLocated;
	private long filesRequiringNoAction;
	private long emptyFilesToBeReconciled;
	private long emptyFilesRequiringNoAction;
	
	public DocCheckerResults() {
		this.docCheckerResultsMap = new HashMap();
		this.totalFileCount = 0;
		this.filesToBeReconciled = 0;
		this.filesToBeLocated = 0;
		this.filesRequiringNoAction = 0;
		this.emptyFilesRequiringNoAction = 0;
		this.emptyFilesToBeReconciled = 0;
	}
	
	public Map getDocCheckerResultsMap() {
		return this.docCheckerResultsMap;
	}
	
	public void setDocCheckerResultsMap(Map docCheckerResultsMap) {
		this.docCheckerResultsMap = docCheckerResultsMap;
	}
	
	public long getTotalFileCount() {
		return this.totalFileCount;
	}
	public void setTotalFileCount(long totalFileCount) {
		this.totalFileCount = totalFileCount;
	}
	
	public long getFilesToBeReconciled() {
		return this.filesToBeReconciled;
	}
	public void setFilesToBeReconciled(long filesToBeReconciled) {
		this.filesToBeReconciled = filesToBeReconciled;
	}		
	
	public long getFilesToBeLocated() {
		return this.filesToBeLocated;
	}
	public void setFilesToBeLocated(long filesToBeLocated) {
		this.filesToBeLocated = filesToBeLocated;
	}		
	
	public long getFilesRequiringNoAction() {
		return this.filesRequiringNoAction;
	}
	public void setFilesRequiringNoAction(long filesRequiringNoAction) {
		this.filesRequiringNoAction = filesRequiringNoAction;
	}

	public long getEmptyFilesRequiringNoAction() {
		return emptyFilesRequiringNoAction;
	}

	public void setEmptyFilesRequiringNoAction(long emptyFilesRequiringNoAction) {
		this.emptyFilesRequiringNoAction = emptyFilesRequiringNoAction;
	}

	public long getEmptyFilesToBeReconciled() {
		return emptyFilesToBeReconciled;
	}

	public void setEmptyFilesToBeReconciled(long emptyFilesToBeReconciled) {
		this.emptyFilesToBeReconciled = emptyFilesToBeReconciled;
	}			
}
