/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.utils;

/**
 * Keys used throughout the application to identify certain specific objects
 * stored in different contexts or collections.
 * 
 * @author kpuscas, Number Six Software
 */
public final class GreensheetsKeys {

	public static final String KEY_GRANT_ID = "GRANT_ID";

	public static final String KEY_APPL_ID = "APPL_ID";

	public static final String KEY_USER_ID = "USER_ID";

	public static final String KEY_GS_GROUP_TYPE = "GS_GROUP_TYPE";

	// Test user parameter to bypass apache
	public static final String KEY_TEST_USER = "TEST_USER";

	// UID for a form in a users session
	public static final String KEY_FORM_UID = "FORM_UID";

	public static final String KEY_CURRENT_USER_SESSION = "CURRENT_USER_SESSION";

	public static final String KEY_NEW_ATTACHMENTS_MAP = "NEW_ATTACHMENTS_MAP";

	// Template as a String
	public static final String KEY_TEMPLATE_ID = "TEMPLATE_ID";

	// Database Properties Object
	public static final String KEY_DB_PROPERTIES = "DB_PROPERTIES";

	// Ldap Properties Object
	public static final String KEY_LDAP_PROPERTIES = "LDAP_PROPERTIES";

	// Greensheet App Config Properties
	public static final String KEY_CONFIG_PROPERTIES = "CONFIG_PROPERTIES";

	// Error Messages
	public static final String KEY_ERROR_MESSAGES = "ERROR_MESSAGES";

	// Preferences
	public static final String KEY_USER_PREFERENCES = "KEY_USER_PREFERENCES";

	// GrantsList Search Types
	public static final String SEARCH_GS_NUMBER = "SEARCH_GS_NUMBER";

	public static final String SEARCH_GS_NAME = "SEARCH_GS_NAME";

	// Message for Action Confirmation
	public static final String KEY_ACTION_CONFIRM_MESSAGE = "ACTION_CONFIRM_MESSAGE";

	// Key for map containing cached greensheet form templates
	public static final String KEY_TEMPLATE_RESOURCE_MAP = "TEMPLATE_RESOURCE_MAP";

}
