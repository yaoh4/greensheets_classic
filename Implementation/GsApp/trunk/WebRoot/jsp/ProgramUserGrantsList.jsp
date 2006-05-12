<%@ page language="java" %>

<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/displaytag.tld" prefix="display" %>


<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();

%>

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


</script>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
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
		<td width="100%">
			<%@ include file="/jsp/common/GlobalHeader.jsp"%> 
		</td>
	</tr>
	<tr>
		<td width="100%">
			<table width="100%" cellpadding="3">
				<tr>
					<td>
						<form id="frmCriteria" name="frmCriteria">
							<a href="javascript:toggleCriteriaPanelDisplay('divId1', 'imgDivId1')">
								<img border="0" id="imgDivId1" name="imgDivId1" src="images/IconOpen.gif" onclick=""/>
							</a>
							<div id="divId1" name="divId1" style="display:block">
								<%@ include file="/jsp/PreferencesPanel.jsp"%> 			
							</div>
						</form>							
					</td>
					<td align="right" nowrap="nowrap">
						<table cellspacing="0" class="header" border="0">
							<tr class="bottomRow2">
								<td class="globalNav" width="100%" valign="bottom">
									<form name="frmEditPrefs" id="frmEditPrefs" action="editpreferences">
										<a href="/greensheets/editpreferences.do">Edit Preferences</a>
									</form>
									
								</td>
							</tr>
						</table>
					</td>					
				</tr>
			</table>
		</td>
	<tr>
		<td width="100%" >
			<display:table name="sessionScope.GRANT_LIST" 	requestURI="/greensheets/no_op.do" class="data" id="row" defaultsort="3" sort="list" pagesize="50" cellspacing="0" width="100%">
				<display:column property="programGreensheet" title=""/>
				<display:column sortable="true" property="grantNumberSort" title="Grant Number" />
				<display:column sortable="true" property="latestBudgetStartDate" title="Budget Start Date" decorator="gov.nih.nci.iscs.numsix.greensheets.application.DateColumnDecorator" />
				<display:column sortable="true" property="pd" title="PD"/>
				<display:column sortable="true" property="pi" title="PI"/>
				<display:column sortable="true" property="cancerActivity" title="CA"/>
				<display:column sortable="true" property="priorityScore" title="Score" />
				<display:column sortable="true" property="percentileScore" title="%"/>
				<display:column sortable="true" property="councilMeetingDate" title="Board Date"/>
				<display:column sortable="true" property="programStatus" title="Greensheet Status"/>
				<display:column sortable="true" property="specialistStatus" title="GMS GS Status"/>
								
				<display:setProperty name="paging.banner.items_name" value="Grants"/>
				<display:setProperty name="paging.banner.item_name" value="Grant"/>
				<display:setProperty name="paging.banner.placement" value="both"/>
				<display:setProperty name="paging.banner.some_items_found" value="<span>{0} {1} found, displaying Grants {2} to {3}.</span></br>" />
				<display:setProperty name="paging.banner.onepage" value=""/>
				<display:setProperty name="paging.banner.no_items_found" value="No grants found. There are no pending greensheets"/>
				<display:setProperty name="basic.msg.empty_list" value=""/>
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