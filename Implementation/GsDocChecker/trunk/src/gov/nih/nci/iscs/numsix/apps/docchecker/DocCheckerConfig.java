package gov.nih.nci.iscs.numsix.apps.docchecker;

import java.util.Date;

public class DocCheckerConfig {
	private String buildVersion = null;

	private Date buildDate = null;

	private String runningEnv = null;

	private String diskLocation = null;

	private String dbPrefix = null;

	private String reportLoc = null;

	private String reportType = null;

	private Date reportLastRun = null;

	public Date getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public String getDbPrefix() {
		return dbPrefix;
	}

	public void setDbPrefix(String dbPrefix) {
		this.dbPrefix = dbPrefix;
	}

	public String getDiskLocation() {
		return diskLocation;
	}

	public void setDiskLocation(String diskLocation) {
		this.diskLocation = diskLocation;
	}

	public Date getReportLastRun() {
		return reportLastRun;
	}

	public void setReportLastRun(Date reportLastRun) {
		this.reportLastRun = reportLastRun;
	}

	public String getReportLoc() {
		return reportLoc;
	}

	public void setReportLoc(String reportLoc) {
		this.reportLoc = reportLoc;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getRunningEnv() {
		return runningEnv;
	}

	public void setRunningEnv(String runningEnv) {
		this.runningEnv = runningEnv;
	}
}
