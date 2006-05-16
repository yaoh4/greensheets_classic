<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>



<%
	// Names of the Keys
	String keyGrantsFrom = "GrantsFrom";
	String keyGrantType = "GrantType";
	String keyBudgetMechanism = "BudgetMechanism";
	String keyWithinPayline = "WithinPayline";

	// Grants From 
	String prefGrantsFromDefault = "ALL";
	String prefGrantsFromP = "P";
	String prefGrantsFromCA = "CA";
	String prefGrantsFromAll = "ALL";

	// Grant Type
	String prefGrantTypeDefault = "BOTH";
	String prefGrantTypeC = "C";
	String prefGrantTypeNC = "NC";
	String prefGrantTypeBoth = "BOTH";

	// Budget Mechanism
	String prefBudgetMechanism = "";

	// Within Payline
	String prefWithinPaylineDefault = "YES";
	String prefWithinPaylineYes = "YES";
	String prefWithinPaylineNo = "NO";

	Properties props = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_USER_PREFERENCES);
	
	if (props != null) {
		// Key Names
		keyGrantsFrom				=	props.getProperty("preferences.GrantsFromName");
		keyGrantType				=	props.getProperty("preferences.GrantTypeName");
		keyBudgetMechanism	=	props.getProperty("preferences.BudgetMechanismName");
		keyWithinPayline			=	props.getProperty("preferences.WithinPaylineName");

		// Default values
		prefGrantsFromDefault		=	props.getProperty("preferences.GrantsFromDefault");
		prefGrantTypeDefault			=	props.getProperty("preferences.GrantTypeDefault");
		prefBudgetMechanism		=	props.getProperty("preferences.BudgetMechanismDefault");
		prefWithinPaylineDefault		=	props.getProperty("preferences.WithinPaylineDefault");

		// Grants From values
		prefGrantsFromP			=	props.getProperty("preferences.GrantsFromP");
		prefGrantsFromCA			=	props.getProperty("preferences.GrantsFromCA");
		prefGrantsFromAll			=	props.getProperty("preferences.GrantsFromAll");
	
		// Grant Type
		prefGrantTypeC					=	props.getProperty("preferences.GrantTypeC");
		prefGrantTypeNC				=	props.getProperty("preferences.GrantTypeNC");
		prefGrantTypeBoth				=	props.getProperty("preferences.GrantTypeBoth");

		// Within Payline
		prefWithinPaylineYes			=	props.getProperty("preferences.WithinPaylineYes");
		prefWithinPaylineNo			=	props.getProperty("preferences.WithinPaylineNo");
	}

	// Get the currently logged in User Preferences.
	GreensheetPreferencesMgr gsPrefMgr = new GreensheetPreferencesMgrImpl();
	HashMap userPrefs = gsPrefMgr.getUserPreferences();

	String grantsFrom 				= 	userPrefs.containsKey(keyGrantsFrom)	?	(String) userPrefs.get(keyGrantsFrom)	:	prefGrantsFromDefault;
	String grantType				=	userPrefs.containsKey(keyGrantType)	?	(String) userPrefs.get(keyGrantType)	:	prefGrantTypeDefault;
	String budgetMechanism	=	userPrefs.containsKey(keyBudgetMechanism)	?	(String) userPrefs.get(keyBudgetMechanism)	:	prefBudgetMechanism;
	String withinPayline			=	userPrefs.containsKey(keyWithinPayline)	?	(String) userPrefs.get(keyWithinPayline)	:	prefWithinPaylineDefault;

System.out.println(grantsFrom + " ---- " + grantType + " ------- " + budgetMechanism + " ------- " + withinPayline);

	// For Radio selections

	// Grants From radio buttons
	String selGrantsFromP = grantsFrom.equals(prefGrantsFromP)	?	"checked='checked'" : "";
	String selGrantsFromCA = grantsFrom.equals(prefGrantsFromCA)	?	"checked='checked'" : "";
	String selGrantsFromAll = grantsFrom.equals(prefGrantsFromAll)	?	"checked='checked'" : "";
	
	// Grant Type radio buttons
	String selGrantTypeC = grantType.equals(prefGrantTypeC)	?	"checked='checked'" : "";
	String selGrantTypeNC = grantType.equals(prefGrantTypeNC)	?	"checked='checked'" : "";
	String selGrantTypeBoth = grantType.equals(prefGrantTypeBoth)	?	"checked='checked'" : "";

	// Within Payline radio buttons
	String selWithinPaylineYes = withinPayline.equals(prefWithinPaylineYes)	?	"checked='checked'" : "";
	String selWithinPaylineNo = withinPayline.equals(prefWithinPaylineNo)	?	"checked='checked'" : "";
%>

<!-- Start of Preferences Code -->

<table cellpadding="3" bgcolor="#CCCCCC">
	<tr>
		<td valign="bottom">
			<strong>Grants From</strong>
		</td>
		<td valign="bottom">
			<label>
				<input type="radio" name="rbGrantsFrom" id="rbGrantsFrom" value="<%=prefGrantsFromP%>"  <%=selGrantsFromP%> />My Portfolio</label>
		</td>
		<td valign="bottom">
			<label>
				<input type="radio" name="rbGrantsFrom" id="rbGrantsFrom" value="<%=prefGrantsFromCA%>" <%=selGrantsFromCA%>/>My Cancer Activites</label>
		</td>
		<td valign="bottom">
			<label>
				<input type="radio" name="rbGrantsFrom" id="rbGrantsFrom" value="<%=prefGrantsFromAll%>" <%=selGrantsFromAll%> />All NCI Grants</label>
		</td>
	</tr>
	<tr>
		<td valign="bottom">
			<strong>Grant Type</strong>
		</td>
		<td valign="bottom">
			<label>
				<input type="radio" name="rbGrantType" id="rbGrantType" value="<%=prefGrantTypeC%>" <%=selGrantTypeC%> />Competing Grants</label>
		</td>
		<td valign="bottom">
			<label>
				<input type="radio" name="rbGrantType" id="rbGrantType" value="<%=prefGrantTypeNC%>" <%=selGrantTypeNC%> />Non-Competing Grants</label>
		</td>
		<td colspan="2" valign="bottom">
			<label>
				<input type="radio" name="rbGrantType" id="rbGrantType" value="<%=prefGrantTypeBoth%>" <%=selGrantTypeBoth%> />Both Competing and Non-Competing Grants</label>
		</td>
	</tr>
	<tr>
		<td valign="bottom">
			<div align="left">
				<strong>Budget Mechanism</strong>
			</div>
		</td>
		<td>
			<input name="txtBudgetMechanism" id="txtBudgetMechanism" type="text" value="<%=budgetMechanism%>"/>
		</td>
	</tr>
	<tr>
		<td valign="bottom">
			<strong>Only Grants within the Payline</strong>
		</td>
		<td valign="bottom">
			<label>
				<input type="radio" name="rbPayline" id="rbPayline" value="<%=prefWithinPaylineYes%>" <%=selWithinPaylineYes%>/>Yes</label>
			<label>
				<input type="radio" name="rbPayline" id="rbPayline" value="<%=prefWithinPaylineNo%>" <%=selWithinPaylineNo%>/>No</label>
		</td>
	</tr>
	<tr>
		<td colspan="4" align="right" valign="bottom">
			<img src="images/RefreshList.gif"/>
			<img src="images/RestorePref.gif" border="0"/>
		</td>
	</tr>
</table>



<!-- End of Preferences -->