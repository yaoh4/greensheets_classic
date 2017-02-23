<%@ page language="java" %>

<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>


<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();

// Get the currently logged in User Preferences.
	
%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="./stylesheets/otherappsmenu.css" type="text/css" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />	
</head>
<body>

<table border="0" width="100%">
	<tr>
		<td width="100%">
			<%@ include file="/jsp/common/GlobalHeader.jsp"%> 
		</td>
	</tr>
	<tr>
		<td width="100%">
			<table width="100%" cellpadding="3">
				<tr>
					<td>
						<form id="frmPreferences" name="savePreferencesForm" action="savePreferencesForm">
							 <table cellpadding="5" bgcolor="#CCCCCC">
								<tr>
									<td valign="bottom"><strong>Grants From</strong></td>
									<td valign="bottom">
										Value
									</td>
									<td valign="bottom"><strong>Grant Type</strong></td>
									<td>
										Value
									</td>
								</tr>
								<tr>
									<td valign="bottom"><strong>Mechanism</strong></td>
									<td valign="bottom">
										Value
									</td>
									<td valign="bottom"><strong>Only Grants within the Payline</strong></td>
									<td>
										Value
									</td>
								</tr>
							</table>
						</form>							
					</td>
			</table>
		</td>
	<tr>
	<tr>
		<td colspan="2">
			<%@ include file="/jsp/common/GlobalFooter.jsp"%>
		</td>
	</tr>
</table>


</body>
</html>