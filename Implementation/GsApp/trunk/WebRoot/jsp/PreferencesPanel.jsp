<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*"%>
<%@ page import="java.util.*"%>

<%
	// Get the currently logged in User Preferences.
	GreensheetPreferencesMgr gsPrefMgr = (GreensheetPreferencesMgr) session.getAttribute("USER_PREF_MGR");
	
	
%>

<!-- Start of Preferences Code -->

<table cellpadding="3" bgcolor="#CCCCCC">
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
		<td valign="bottom">
			<div align="left"><strong>Search For</strong></div>
		</td>
		<td colspan="3">
			<select id="searchType" name="searchType">
				<option value="SEARCH_GS_NUMBER">Grant Number</option>
				<option value="SEARCH_GS_NAME">PI Name</option>
			</select>&nbsp; 
			<input name="searchText" id="searchText" type="text" value="" />
		</td>		
	</tr>
	<tr>
		<td colspan="4" align="right" valign="bottom">
			<table cellspacing="0" class="header" border="0">
				<tr class="bottomRow2">
					<td width="100%" valign="bottom">
						<a href="#">Restore Preferences</a>&nbsp;
						<a href="#">Search</a>&nbsp; 
						<a href="#">Cancel</a>&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<!--
	<tr>
		<td valign="bottom">
			<div align="left">
				<strong>Grant Number</strong>
			</div>
		</td>
		<td>
			<input name="txtGrantNumber" id="txtGrantNumber" type="text" value=""/>    -or-
		</td>
		<td valign="bottom">
			<div align="left">
				<strong>PI Name</strong>
			</div>
		</td>
		<td>
			<input name="txtPIName" id="txtPIName" type="text" value=""/>
		</td>
	</tr>
<!--
<table border="0" cellpadding="0" cellspacing="0">
	<tr valign="bottom">
		<td valign="bottom"  width="100%" nowrap="1"><h1>Program Grants List for <%=userName%></h1></td>
		<td align="right" valign="bottom" nowrap="1">
			<form name="SimpleSearch" id="SimpleSearch" method="post" action="/greensheets/searchforgrant.do">
				<table>
					<tr>
						<td  nowrap="1" valign="bottom">						
								Search for 
								<select name="searchType">
									<option value="SEARCH_GS_NUMBER">Grant Number</option>
									<option value="SEARCH_GS_NAME">PI Name</option>
								</select>:
						</td>
						<td valign="bottom"><input type="text" name="searchText" value=" "/></td>
						<td valign="bottom">
							<table cellspacing="0" width="100%" class="header" border="0">
								<tr class="bottomRow1">
									<td class="globalNav" width="100%" valign="bottom">
										<a href="javascript: document.SimpleSearch.submit();">Go</a>
									</td>
								</tr>
							</table>
						</td>					
					</tr>
				</table>			
			</form>
		</td>
	</tr>
	</table>

-->
<!-- End of Preferences -->
