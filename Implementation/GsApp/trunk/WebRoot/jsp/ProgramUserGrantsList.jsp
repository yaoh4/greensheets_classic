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

function changePayLineOption()
{
	var isPayLineOptionChecked = document.frmPayLineOnly.paylineOnly.checked;
	
	if(isPayLineOptionChecked )
	{
		document.frmPayLineOnly.paylineOpt.value="YES";
	}
	else
	{
		document.frmPayLineOnly.paylineOpt.value="NO";
	}
	
	document.frmPayLineOnly.submit();

	//alert(document.frmPayLineOnly.paylineOpt.value);
}

function getMyPortfolio()
{
	var isMyPortfolioChecked = document.frmMyPortfolio.cbxMyPortfolio.checked;
	
	if(isMyPortfolioChecked )
	{
		document.frmMyPortfolio.myPortfolio.value="YES";
	}
	else
	{
		document.frmMyPortfolio.myPortfolio.value="NO";
	}
	
	document.frmMyPortfolio.submit();

	//alert(document.frmMyPortfolio.myPortfolio.value);
}


/*
function changePayLineOption(){

		var val = document.frmPayLineOnly.paylineOnly.getAttribute("checked");
		if(val == false || val == "checked")
		{
            document.frmPayLineOnly.paylineOpt.value="NO";
		}else{
            document.frmPayLineOnly.paylineOpt.value="YES";
        }


        document.frmPayLineOnly.submit()

}


function getMyPortfolio(){

		var val = document.frmMyPortfolio.cbxMyPortfolio.getAttribute("checked");
		if(val == false || val == "checked")
		{
            document.frmMyPortfolio.myPortfolio.value="NO";
		}else{
            document.frmMyPortfolio.myPortfolio.value="YES";
        }


        document.frmMyPortfolio.submit()

}

*/


function toggleDivAreaDisplay(divId, imgId)
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
<%@ include file="/jsp/common/GlobalHeader.jsp"%> 

<br />
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
	<!-- Show/Hide Criteria -->


	<tr valign="bottom">
		<td valign="bottom"  width="100%" nowrap="1" colspan="2">
			<%if (request.getAttribute("SEARCH_RESULTS") == null) {%>
				<form name="frmPayLineOnly" id="frmPayLineOnly" method="post" action="/greensheets/retrievegrants.do">
					<input type="checkbox" name="paylineOnly" id="paylineOnly" value="payLineOnlyChecked" <%=request.getAttribute("payLineOnlyChecked")%> onclick="javascript:changePayLineOption()"/>Show only grants within the payline
					<input type="hidden" name="paylineOpt" id="paylineOpt" value="YES"/>
				</form>
			<%}%>
		</td>
	</tr>
	<tr valign="bottom">
		<td>
			<%if ( (request.getAttribute("SEARCH_RESULTS") == null) && (request.getAttribute("MY_PORTFOLIO_OPT") != null) ) {%>
					<form name="frmMyPortfolio" id="frmMyPortfolio" method="post" action="/greensheets/retrievegrants.do">
						<input type="checkbox" name="cbxMyPortfolio" id="cbxMyPortfolio" value="myPortfolioChecked" <%=request.getAttribute("myPortfolioChecked")%> onclick="javascript:getMyPortfolio()"/>My Portfolio
						<input type="hidden" name="myPortfolio" id="myPortfolio" value="YES"/>
					</form>
			<%}%>
		</td>
		<td align="right" valign="bottom" nowrap="1">
			<table cellspacing="0" width="100%" align="right" border="0">
				<tr class="bottomRow1">
					<td width="100%">&nbsp;</td>
					<td  rowspan="2" valign="bottom" align="right" nowrap="1">
						<table cellpadding="0" cellspacing="0" class="header">
							<tr class="bottomRow1">
								<td nowrap="1" class="globalNav">
									<form name="refreshGrantsList" id="refreshGrantsList" method="post" action="/greensheets/retrievegrants.do">
										<a href="javascript: document.refreshGrantsList.submit();">Refresh Grants List</a>
									</form>
								</td>
							</tr>
						</table>						
			        </td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td colspan="2" width="100%">
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
</table>

<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>