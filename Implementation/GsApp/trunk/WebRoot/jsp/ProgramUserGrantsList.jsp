<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<script language="javascript" src="./scripts/ClientSideMethods.js"></script>

<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html:hidden property="method" value="error" />

<script>
function toggleCriteriaPanelDisplay(divId, imgId)
	{		
		// Get the div element. 
		var divElement = document.getElementById(divId);	
		// If element is not null, toggle the display of the element.
		if( (divElement != null) && (divElement != 'undefined') )
		{
			//set up the variables.
			var displayText = "block";
			var imgSrc = "./images/IconOpen.gif";
			
			if(divElement.style.display == "block")
			{
				displayText = "none";
				imgSrc = "./images/IconClosed.gif";			
			}

			// Show or hide the Div Area
			divElement.style.display = displayText;			
			
			// Change the icon 
			// First get the image element
			var imgElement = document.getElementById(imgId);
			if( (imgElement != null) && (imgElement != 'undefined') )
			{
				imgElement.src = imgSrc;	
			}		
		}		
	}	
	
function setMethod(target) {
   document.forms[0].method.value=target;
}
</script>

<html>
	<head>
		<title>Greensheets</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="./stylesheets/otherappsmenu.css" type="text/css" />
		<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
	</head>
	<body>
		<table border="0" width="100%">
			<tr>
				<td border="0" width="100%">
					<%@ include file="/jsp/common/GlobalHeader.jsp"%>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" width="100%">
						<tr>
							<td>
								<a href="javascript:toggleCriteriaPanelDisplay('divId1', 'imgDivId1')"> <img border="0" id="imgDivId1" name="imgDivId1" src="./images/IconOpen.gif" onclick="" /> </a>
								<div id="divId1" name="divId1" style="display:block">
									<html:form action="searchprogramgrantsdispatch">
										<TABLE bgcolor="#CCCCCC" border="0" width="100%" cellpadding="3">
											<TR>
												<TD>
													<P align="left">
														<STRONG>Grants From:</STRONG>
														<html:select property="grantSource">
															<html:optionsCollection name="grantSources" value="value" label="label" />
														</html:select>
													</P>
												<TD>
													<DIV align="right">
														<STRONG>Grant Type:</STRONG>
														<html:select property="grantType">
															<html:optionsCollection name="grantTypes" value="value" label="label" />
														</html:select>
													</DIV>
												</TD>
											</TR>
											<TR>
												<TD align="bottom">
													<DIV align="left">
														<STRONG>Mechanism:</STRONG>
														<html:text property="mechanism" />
														Example: R01
													</DIV>
													<DIV align="left">
														Leaving Mechanism blank will return all Mechanisms
													</DIV>
												</TD>
												<TD>
													<DIV align="right">
														<STRONG>Show only Competing Grants within the Payline:</STRONG>
														<html:checkbox property="onlyGrantsWithinPayline" value="yes"/>
													</DIV>
												</TD>
											</TR>
											<TR>
												<TD colspan="2">
													<DIV align="left">
														<STRONG>Grant Number:</STRONG>
														<html:text property="grantNumber" />
														&nbsp;&nbsp;&nbsp; -or- &nbsp;&nbsp;&nbsp; <STRONG>PI's Last Name:</STRONG>
														<html:text property="lastName" />
														&nbsp;&nbsp;&nbsp; <STRONG>First Name:</STRONG>
														<html:text property="firstName" />														
													</DIV>
												</TD>
											</TR>
											<TR>
												<TD colspan="3" valign="bottom">
													<DIV align="right">
														<html:image page="/images/Search.gif" property="image_search" alt="Search" />
														<html:image page="/images/Cancel.gif" property="image_cancel" alt="Cancel" />
														<html:image page="/images/RestorePref.gif" property="image_restorePreferences" alt="Restore Preferences" />														
													</DIV>
												<TD>
											</TR>
											<TR>
												<TD colspan="3" valign="bottom">
													<DIV align="left">
														<STRONG>NOTE: Selection of ALL NCI Grants, Both Competing and Non-Competing Grants, and leaving the Payline checkbox unchecked will result in all grants being returned within the multi-page list. 
														This will slow system response.</STRONG>
													</DIV>
												</TD>
											</TR>
										</TABLE>
									</html:form>
								</div>
							</td>
							<td>
								<p align="right">
									<html:link page="/editprogrampreferences.do">
										<html:img page="/images/EditPref.gif" border="0" alt="Edit Preferencces" />
									</html:link>
								</p>
							<td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="100%">
					<display:table name="sessionScope.GRANT_LIST" requestURI="" class="data" id="row" defaultsort="3" sort="list" pagesize="50" cellspacing="0" width="100%">
						<display:column sortable="true" property="programGreensheet" title="Pgm GS" />
						<display:column sortable="true" property="grantNumberSort" title="Grant Number" />
						<display:column sortable="true" property="latestBudgetStartDate" title="Budget Start Date" decorator="gov.nih.nci.iscs.numsix.greensheets.application.DateColumnDecorator" />
						<display:column sortable="true" property="pd" title="PD" />
						<display:column sortable="true" property="pi" title="PI" />
						<display:column sortable="true" property="cancerActivity" title="CA" />
						<display:column sortable="true" property="priorityScore" title="Score" />
						<display:column sortable="true" property="percentileScore" title="%" />
						<display:column sortable="true" property="councilMeetingDate" title="Board Date" />
						<display:column sortable="true" property="programStatus" title="Greensheet Status" />
						<display:column sortable="true" property="specialistStatus" title="GMS GS Status" />
						<display:setProperty name="paging.banner.items_name" value="Grants" />
						<display:setProperty name="paging.banner.item_name" value="Grant" />
						<display:setProperty name="paging.banner.placement" value="both" />
						<display:setProperty name="paging.banner.some_items_found" value="<span>{0} {1} found, displaying Grants {2} to {3}.</span></br>" />
						<display:setProperty name="paging.banner.onepage" value="" />
					</display:table>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<%@ include file="/jsp/common/GlobalFooter.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
