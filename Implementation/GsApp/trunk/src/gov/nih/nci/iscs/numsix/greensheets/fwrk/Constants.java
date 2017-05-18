package gov.nih.nci.iscs.numsix.greensheets.fwrk;

public interface Constants {

	// generic keys
	public static final String SUCCESS_KEY = "success";

	public static final String FAILURE_KEY = "failure";

	public static final String UNDERCONSTRUCTION_KEY = "underconstruction";

	// preferences bean keys
	public static final String PREFERENCES_KEY = "preferences";

	public static final String GRANT_SOURCES_KEY = "grantSources";

	public static final String GRANT_SOURCE_KEY = "grantSource";

	public static final String GRANT_TYPES_KEY = "grantTypes";

	public static final String GRANT_TYPE_KEY = "grantType";

	public static final String GRANT_MECHANISM_KEY = "mechanism";

	public static final String GRANTS_WITHIN_PAYLINE_CHOICE = "onlyGrantsWithinPayline";

	// preferences field value keys
	public static final String PREFERENCES_MYPORTFOLIO = "myportfolio";

	public static final String PREFERENCES_MYCANCERACTIVITY = "mycanceractivity";

	public static final String PREFERENCES_ALLNCIGRANTS = "allncigrants";

	public static final String PREFERENCES_COMPETINGGRANTS = "competinggrants";

	public static final String PREFERENCES_NONCOMPETINGGRANTS = "noncompetinggrants";

	public static final String PREFERENCES_BOTH = "both";

	public static final String PREFERENCES_YES = "yes";

	public static final String PREFERENCES_NO = "no";

	// preferences map keys
	public static final String PREFERENCES_GRANT_SOURCE_KEY = "preferences.grantSource";

	public static final String PREFERENCES_GRANT_SOURCE_PD_KEY = "preferences.grantSourcePd";

	public static final String PREFERENCES_GRANT_SOURCE_PA_KEY = "preferences.grantSourcePa";

	public static final String PREFERENCES_GRANT_TYPE_KEY = "preferences.grantType";

	public static final String PREFERENCES_GRANT_MECHANISM_KEY = "preferences.mechanism";

	public static final String PREFERENCES_GRANT_PAYLINE_KEY = "preferences.onlyGrantsWithinPayline";

	public static final String PREFERENCES_GRANT_NUMBER_KEY = "preferences.grantNumber";

//	public static final String PREFERENCES_GRANT_PI_KEY = "preferences.piName";	// Bug#4204 Abdul: Commented out this for the new fields lastName and firstName 
	public static final String PREFERENCES_GRANT_PI_LAST_NAME_KEY = "preferences.lastName";	// Bug#4204 Abdul: Added the new field lastName
	public static final String PREFERENCES_GRANT_PI_FIRST_NAME_KEY = "preferences.firstName";	// Bug#4204 Abdul: Added the new field firstName

	public static final String SESS_MAINT_FLTR_INIT_PARAM_NAME = "resources-not-needing-user-info";
	public static final String SESS_MAINT_FLTR_INIT_PARAM_PRELOAD_AND_SVC = "resources-to-preload-user-info-and-then-service";
	public static final String SESSION_EXPIRED_PAGE = "/jsp/SessionExpired.jsp";
	
	//Draft Greensheets Keys
	
	public static final String DRAFT_MODULE_NAME = "moduleNames";
	public static final String DRAFT_TYPE = "draftTypes";
	public static final String DRAFT_MECHANISM = "draftMechs";
	public static final String DRAFT_UPDATE = "draftUpdateDropDown";
	public static final String DRAFT_ADDITION_LIST = "draftAdditions";
	public static final String DRAFT_DELETION_LIST = "draftDeletions";
	public static final String DRAFT_ADDITION_DELETION_LIST = "draftAddDelMsg";
	public static final String DRAFT_DELETION_ADDITION_MSG = "draftAddDeletions";
	public static final String DRAFT_IS_REVIEW_MODULE = "isReviewModule";
	public static final String SELECTED_DRAFT_MODULE = "selectedModule";
	public static final String SELECTED_DRAFT_TYPE = "selectedType";
	public static final String SELECTED_DRAFT_MECHANISM = "selectedMechanism";
	public static final String UPDATE_DROP_DOWN_OPTION_ALL= "View All";
	public static final String UPDATE_DROP_DOWN_OPTION_UPDATE = "Updated Only";
	public static final String REVISION_TYPE = "REV";

	public static final int OK_STATUS = 200;
	public static final int VALIDATION_FAILED_STATUS = 301;
	public static final int BAD_REQUEST_STATUS = 400;
	public static final int UNAUTHORIZED_STATUS = 401;
	public static final int INTERNAL_SERVER_ERROR_STATUS = 500;
	
	

}
