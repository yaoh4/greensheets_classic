<%@ page language="java" %>

<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>


<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();

// Get the currently logged in User Preferences.
	GreensheetPreferencesMgr gsPrefMgr = (GreensheetPreferencesMgr) session.getAttribute("USER_PREF_MGR");
	
%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/otherappsmenu.css" type="text/css" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />	
	<script language="javascript">
		function performSaveCancel(bSave) {
			if (bSave) 
			{
				// save the preferences and go to the next screen
				document.savePreferencesForm.method.value="savePreferencesToDB";
  				document.savePreferencesForm.submit();
			}
			else 
			{
				var cancelOp = confirm("Are you sure you want to Cancel and lose any changes?");
    			if (cancelOp) 
    			{
    				document.savePreferencesForm.reset();
    			}
			}
		
		}
	</script>
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
						<form id="savePreferencesForm" name="savePreferencesForm" action="savePreferencesForm">
							<div id="divId1" name="divId1" style="display:block">
								 <table cellpadding="5" bgcolor="#CCCCCC">
									<tr>
										<td valign="bottom"><strong>Grants From</strong></td>
										<td valign="bottom">
											<label> 
												<select id="prefGrantSource" name="prefGrantSource"> 
													<option value="<%=gsPrefMgr.getGrantSourcePortfolio()%>" <%=gsPrefMgr.isGrantSourceOptionSelected(gsPrefMgr.getGrantSourcePortfolio(), true)%>>My Portfolio</option>
													<option value="<%=gsPrefMgr.getGrantSourceMyActivities()%>" <%=gsPrefMgr.isGrantSourceOptionSelected(gsPrefMgr.getGrantSourceMyActivities(), true)%>>My Cancer Activities</option>
													<option value="<%=gsPrefMgr.getGrantSourceAll()%>" <%=gsPrefMgr.isGrantSourceOptionSelected(gsPrefMgr.getGrantSourceAll(), true)%>>All NCI Grants</option>
												</select>
											</label>
										</td>
										<td valign="bottom"><strong>Grant Type</strong></td>
										<td>
											<select id="prefGrantType" name="prefGrantType">
												<option value="<%=gsPrefMgr.getGrantTypeCompeting()%>" <%=gsPrefMgr.isGrantTypeOptionSelected(gsPrefMgr.getGrantTypeCompeting(), true)%>>Competing Grants</option>
												<option value="<%=gsPrefMgr.getGrantTypeNonCompeting()%>" <%=gsPrefMgr.isGrantTypeOptionSelected(gsPrefMgr.getGrantTypeNonCompeting(), true)%>>Non-Competing Grants</option>
												<option value="<%=gsPrefMgr.getGrantTypeBoth()%>" <%=gsPrefMgr.isGrantTypeOptionSelected(gsPrefMgr.getGrantTypeBoth(), true)%>>Both Competing and Non-Competing Grants</option>
											</select>
										</td>
									</tr>
									<tr>
										<td valign="bottom">
											<div align="left"><strong>Mechanism</strong></div>
										</td>
										<td>
											<input name="txtBudgetMechanism" id="txtBudgetMechanism" type="text" value="<%=gsPrefMgr.getGrantMechanism()%>" /> Example: R01 <br />Leaving Mechanism blank will return all Mechanisms.
										</td>
										<td valign="bottom"><strong>Only Grants within the Payline</strong></td>
										<td>
											<input type="radio" id="prefGrantWithinPayline" name="prefGrantWithinPayline" value="<%=gsPrefMgr.getGrantPaylineYes()%>" <%=gsPrefMgr.isGrantPaylineOptionSelected(gsPrefMgr.getGrantPaylineYes(), false)%> />Yes
											<input type="radio" id="prefGrantWithinPayline" name="prefGrantWithinPayline" value="<%=gsPrefMgr.getGrantPaylineNo()%>" <%=gsPrefMgr.isGrantPaylineOptionSelected(gsPrefMgr.getGrantPaylineNo(), false)%> />No
										</td>
									</tr>			
									<tr>
										<td colspan="4" align="left" valign="bottom">
											<table cellspacing="0" class="header" border="0">
												<tr class="bottomRow2">
													<td width="100%" valign="bottom">
														<a href="javascript:performSaveCancel(true)">Save Preferences</a>&nbsp;
														<a href="javascript:performSaveCancel(false)">Cancel</a>&nbsp;
													</td>
												</tr>
											</table>
										</td>
									</tr>				
								</table>
							</div>
						</form>							
					</td>
				</tr>
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