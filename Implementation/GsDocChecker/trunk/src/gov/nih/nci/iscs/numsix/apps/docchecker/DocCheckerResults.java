package gov.nih.nci.iscs.numsix.apps.docchecker;

import java.util.HashMap;
import java.util.Map;

public class DocCheckerResults {
	private Map docCheckerResultsMapActionNone;
	private Map docCheckerResultsMapActionReconcile;
	private Map docCheckerResultsMapActionLocate;
	private long totalFileCount;
	private long filesToBeReconciled;
	private long filesToBeLocated;
	private long filesRequiringNoAction;
	private long emptyFilesToBeReconciled;
	private long emptyFilesRequiringNoAction;
	
	public DocCheckerResults() {
		this.docCheckerResultsMapActionNone = new HashMap();
		this.docCheckerResultsMapActionReconcile = new HashMap();
		this.docCheckerResultsMapActionLocate = new HashMap();
		this.totalFileCount = 0;
		this.filesToBeReconciled = 0;
		this.filesToBeLocated = 0;
		this.filesRequiringNoAction = 0;
		this.emptyFilesRequiringNoAction = 0;
		this.emptyFilesToBeReconciled = 0;
	}
	
	public Map getDocCheckerResultsMapActionNone() {
		return this.docCheckerResultsMapActionNone;
	}

	public void setDocCheckerResultsMapActionNone(Map docCheckerResultsMap) {
		this.docCheckerResultsMapActionNone = docCheckerResultsMap;
	}

	public Map getDocCheckerResultsMapActionReconcile() {
		return this.docCheckerResultsMapActionReconcile;
	}

	public void setDocCheckerResultsMapActionReconcile(Map docCheckerResultsMap) {
		this.docCheckerResultsMapActionReconcile = docCheckerResultsMap;
	}

	public Map getDocCheckerResultsMapActionLocate() {
		return this.docCheckerResultsMapActionLocate;
	}

	public void setDocCheckerResultsMapActionLocate(Map docCheckerResultsMap) {
		this.docCheckerResultsMapActionLocate = docCheckerResultsMap;
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
