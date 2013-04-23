package gov.nih.nci.cbiit.atsc.dao;

import java.io.Serializable;
import java.util.Date;

public class FormGrant implements Serializable {

	private static final long serialVersionUID = 4610315833175617525L;
	
	private boolean dummy;
    private boolean onControl;
    private boolean electronicallySubmitted;
    private long applId;
    private String fullGrantNum;
    private String rfaPaNumber;
    private String councilMeetingDate;
    private String piFirstName;
    private String piMiddleName;
    private String piLastName;
    private String piName;
    private int irgPercentileNum;
    private int priorityScoreNum;
    private String applTypeCode;
    private String adminPhsOrgCode;
    private String activityCode;
    private int serialNum;
    private int supportYear;
    private String suffixCode;
    private String applStatusCode;
    private String applStatusGroupCode;
    private String formerNum;
    private Date budgetStartDate;
    private Date latestBudgetStartDate;
    private int fy;
    private int ipf;
    private String orgName;
    private String withinPaylineFlag;
    private String cayCode;
    private String roleUsageCode;
    private long pdNpeId;
    private long pdNpnId;
    private String pdLastName;
    private String pdFirstName;
    private String pdMiddleName;
    private String pdUserId;
    private String gmsCode;
    private long gmsNpeId;
    private long gmsNpnId;
    private String gmsLastName;
    private String gmsFirstName;
    private String gmsMiddleName;
    private String gmsUserId;
    private String bkupGmsCode;
    private long bkupGmsNpeId;
    private long bkupGmsNpnId;
    private String bkupGmsLastName;
    private String bkupGmsFirstName;
    private String bkupGmsMiddleName;
    private String bkupGmsUserId;
    private String allGmsUserIds;
    private String pgmFormStatus;
    private Date pgmFormSubmittedDate;
    private String specFormStatus;
    private Date specFormSubmittedDate;
    private String dmFormStatus;
    private boolean minority;

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(String dummyFlag) { // DB column: DUMMY_FLAG
        if (dummyFlag != null && "Y".equalsIgnoreCase(dummyFlag.trim())) {
            this.dummy = true;
        } else {
            this.dummy = false;
        }
    }

    public boolean isOnControl() {
        return onControl;
    }

    public void setOnControl(String onControlFlag) { // DB column: ON_CONTROL_FLAG 
        if (onControlFlag != null && "Y".equalsIgnoreCase(onControlFlag.trim())) {
            this.onControl = true;
        } else {
            this.onControl = false;
        }
    }

    public boolean isElectronicallySubmitted() {
        return electronicallySubmitted;
    }

    public void setElectronicallySubmitted(String electronicSubmissionFlag) { // DB column: ELECTRONIC_SUBMISSION_FLAG
        if (electronicSubmissionFlag != null && "Y".equalsIgnoreCase(electronicSubmissionFlag.trim())) {
            this.electronicallySubmitted = true;
        } else {
            this.electronicallySubmitted = false;
        }
    }

    public long getApplId() {
        return applId;
    }

    public void setApplId(long applId) {
        this.applId = applId;
    }

    public String getFullGrantNum() {
        return fullGrantNum;
    }

    public void setFullGrantNum(String fullGrantNum) {
        this.fullGrantNum = fullGrantNum;
    }

    public String getRfaPaNumber() {
        return rfaPaNumber;
    }

    public void setRfaPaNumber(String rfaPaNumber) {
        this.rfaPaNumber = rfaPaNumber;
    }

    public String getCouncilMeetingDate() {
        return councilMeetingDate;
    }

    public void setCouncilMeetingDate(String councilMeetingDate) {
        this.councilMeetingDate = councilMeetingDate;
    }

    public String getPiFirstName() { // DB column: FIRST_NAME 
        return piFirstName;
    }

    public void setPiFirstName(String piFirstName) {
        this.piFirstName = piFirstName;
    }

    public String getPiMiddleName() {
        return piMiddleName;
    }

    public void setPiMiddleName(String piMiddleName) { // DB column: MI_NAME 
        this.piMiddleName = piMiddleName;
    }

    public String getPiLastName() {
        return piLastName;
    }

    public void setPiLastName(String piLastName) { // DB column: LAST_NAME 
        this.piLastName = piLastName;
    }

    public String getPiName() {
        return piName;
    }

    public void setPiName(String piName) { // DB column: PI_NAME 
        this.piName = piName;
    }

    public int getIrgPercentileNum() {
        return irgPercentileNum;
    }

    public void setIrgPercentileNum(int irgPercentileNum) {
        this.irgPercentileNum = irgPercentileNum;
    }

    public int getPriorityScoreNum() {
        return priorityScoreNum;
    }

    public void setPriorityScoreNum(int priorityScoreNum) {
        this.priorityScoreNum = priorityScoreNum;
    }

    public String getApplTypeCode() {
        return applTypeCode;
    }

    public void setApplTypeCode(String applTypeCode) {
        this.applTypeCode = applTypeCode;
    }

    public String getAdminPhsOrgCode() {
        return adminPhsOrgCode;
    }

    public void setAdminPhsOrgCode(String adminPhsOrgCode) {
        this.adminPhsOrgCode = adminPhsOrgCode;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getSupportYear() {
        return supportYear;
    }

    public void setSupportYear(int supportYear) {
        this.supportYear = supportYear;
    }

    public String getSuffixCode() {
        return suffixCode;
    }

    public void setSuffixCode(String suffixCode) {
        this.suffixCode = suffixCode;
    }

    public String getApplStatusCode() {
        return applStatusCode;
    }

    public void setApplStatusCode(String applStatusCode) {
        this.applStatusCode = applStatusCode;
    }

    public String getApplStatusGroupCode() {
        return applStatusGroupCode;
    }

    public void setApplStatusGroupCode(String applStatusGroupCode) {
        this.applStatusGroupCode = applStatusGroupCode;
    }

    public String getFormerNum() {
        return formerNum;
    }

    public void setFormerNum(String formerNum) {
        this.formerNum = formerNum;
    }

    public Date getBudgetStartDate() {
        return budgetStartDate;
    }

    public void setBudgetStartDate(Date budgetStartDate) {
        this.budgetStartDate = budgetStartDate;
    }

    public Date getLatestBudgetStartDate() {
        return latestBudgetStartDate;
    }

    public void setLatestBudgetStartDate(Date latestBudgetStartDate) {
        this.latestBudgetStartDate = latestBudgetStartDate;
    }

    public int getFy() {
        return fy;
    }

    public void setFy(int fy) {
        this.fy = fy;
    }

    public int getIpf() {
        return ipf;
    }

    public void setIpf(int ipf) {
        this.ipf = ipf;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getWithinPaylineFlag() {
        return withinPaylineFlag;
    }

    public void setWithinPaylineFlag(String withinPaylineFlag) {
        this.withinPaylineFlag = withinPaylineFlag;
    }

    public String getCayCode() {
        return cayCode;
    }

    public void setCayCode(String cayCode) {
        this.cayCode = cayCode;
    }

    public String getRoleUsageCode() {
        return roleUsageCode;
    }

    public void setRoleUsageCode(String roleUsageCode) {
        this.roleUsageCode = roleUsageCode;
    }

    public long getPdNpeId() {
        return pdNpeId;
    }

    public void setPdNpeId(long pdNpeId) {
        this.pdNpeId = pdNpeId;
    }

    public long getPdNpnId() {
        return pdNpnId;
    }

    public void setPdNpnId(long pdNpnId) {
        this.pdNpnId = pdNpnId;
    }

    public String getPdLastName() {
        return pdLastName;
    }

    public void setPdLastName(String pdLastName) {
        this.pdLastName = pdLastName;
    }

    public String getPdFirstName() {
        return pdFirstName;
    }

    public void setPdFirstName(String pdFirstName) {
        this.pdFirstName = pdFirstName;
    }

    public String getPdMiddleName() {
        return pdMiddleName;
    }

    public void setPdMiddleName(String pdMiddleName) {
        this.pdMiddleName = pdMiddleName;
    }

    public String getPdUserId() {
        return pdUserId;
    }

    public void setPdUserId(String pdUserId) {
        this.pdUserId = pdUserId;
    }

    public String getGmsCode() {
        return gmsCode;
    }

    public void setGmsCode(String gmsCode) {
        this.gmsCode = gmsCode;
    }

    public long getGmsNpeId() {
        return gmsNpeId;
    }

    public void setGmsNpeId(long gmsNpeId) {
        this.gmsNpeId = gmsNpeId;
    }

    public long getGmsNpnId() {
        return gmsNpnId;
    }

    public void setGmsNpnId(long gmsNpnId) {
        this.gmsNpnId = gmsNpnId;
    }

    public String getGmsLastName() {
        return gmsLastName;
    }

    public void setGmsLastName(String gmsLastName) {
        this.gmsLastName = gmsLastName;
    }

    public String getGmsFirstName() {
        return gmsFirstName;
    }

    public void setGmsFirstName(String gmsFirstName) {
        this.gmsFirstName = gmsFirstName;
    }

    public String getGmsMiddleName() {
        return gmsMiddleName;
    }

    public void setGmsMiddleName(String gmsMiddleName) {
        this.gmsMiddleName = gmsMiddleName;
    }

    public String getGmsUserId() {
        return gmsUserId;
    }

    public void setGmsUserId(String gmsUserId) {
        this.gmsUserId = gmsUserId;
    }

    public String getBkupGmsCode() {
        return bkupGmsCode;
    }

    public void setBkupGmsCode(String bkupGmsCode) {
        this.bkupGmsCode = bkupGmsCode;
    }

    public long getBkupGmsNpeId() {
        return bkupGmsNpeId;
    }

    public void setBkupGmsNpeId(long bkupGmsNpeId) {
        this.bkupGmsNpeId = bkupGmsNpeId;
    }

    public long getBkupGmsNpnId() {
        return bkupGmsNpnId;
    }

    public void setBkupGmsNpnId(long bkupGmsNpnId) {
        this.bkupGmsNpnId = bkupGmsNpnId;
    }

    public String getBkupGmsLastName() {
        return bkupGmsLastName;
    }

    public void setBkupGmsLastName(String bkupGmsLastName) {
        this.bkupGmsLastName = bkupGmsLastName;
    }

    public String getBkupGmsFirstName() {
        return bkupGmsFirstName;
    }

    public void setBkupGmsFirstName(String bkupGmsFirstName) {
        this.bkupGmsFirstName = bkupGmsFirstName;
    }

    public String getBkupGmsMiddleName() {
        return bkupGmsMiddleName;
    }

    public void setBkupGmsMiddleName(String bkupGmsMiddleName) {
        this.bkupGmsMiddleName = bkupGmsMiddleName;
    }

    public String getBkupGmsUserId() {
        return bkupGmsUserId;
    }

    public void setBkupGmsUserId(String bkupGmsUserId) {
        this.bkupGmsUserId = bkupGmsUserId;
    }

    public String getAllGmsUserIds() {
        return allGmsUserIds;
    }

    public void setAllGmsUserIds(String allGmsUserIds) {
        this.allGmsUserIds = allGmsUserIds;
    }

    public String getPgmFormStatus() {
        return pgmFormStatus;
    }

    public void setPgmFormStatus(String pgmFormStatus) {
        this.pgmFormStatus = pgmFormStatus;
    }

    public Date getPgmFormSubmittedDate() {
        return pgmFormSubmittedDate;
    }

    public void setPgmFormSubmittedDate(Date pgmFormSubmittedDate) {
        this.pgmFormSubmittedDate = pgmFormSubmittedDate;
    }

    public String getSpecFormStatus() {
        return specFormStatus;
    }

    public void setSpecFormStatus(String specFormStatus) {
        this.specFormStatus = specFormStatus;
    }

    public Date getSpecFormSubmittedDate() {
        return specFormSubmittedDate;
    }

    public void setSpecFormSubmittedDate(Date specFormSubmittedDate) {
        this.specFormSubmittedDate = specFormSubmittedDate;
    }

    public String getDmFormStatus() {
        return dmFormStatus;
    }

    public void setDmFormStatus(String dmFormStatus) {
        this.dmFormStatus = dmFormStatus;
    }

    public boolean isMinority() {
        return minority;
    }

    public void setMinority(String mbMinorityFlag) { // DB column: MB_MINORITY_FLAG
        if (mbMinorityFlag != null && "Y".equalsIgnoreCase(mbMinorityFlag.trim())) {
            this.minority = true;
        } else {
            this.minority = false;
        }
    }

    public boolean equals(Object object) {
        // TODO Auto-generated method stub
        return false;
    }

    public int hashCode() {
        // TODO Auto-generated method stub
        return -99999;
    }

    public String toString() {
        return "Grant " + fullGrantNum + " (" + applId + ")" + (dummy ? " [dummy]" : "");
    }
}
