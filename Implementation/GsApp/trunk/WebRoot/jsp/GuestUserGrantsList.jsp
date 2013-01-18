<%@ page language="java" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
%>
 
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%@ include file="/jsp/common/GlobalHeader.jsp"%>

<table border="0" cellpadding="0" cellspacing="0" >
	<tr valign="bottom">
		<td valign="bottom"  width="100%" nowrap="1"><h1>Grants List for <%=userName%></h1></td>
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
	<tr valign="bottom">
		<td align="right" valign="bottom" nowrap="1" colspan="2" width="100%">
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
			<display:table name="sessionScope.GRANT_LIST" 	requestURI="" class="data" id="row" defaultsort="3" sort="list" pagesize="50" cellspacing="0" style="width: 100%;">
				<display:column sortable="true" property="grantNumberSort" title="Grant Number" />
				<display:column sortable="true" property="cayCode" title="CA"/>
				<display:column sortable="true" property="latestBudgetStartDate" title="Budget Start Date" decorator="gov.nih.nci.iscs.numsix.greensheets.application.DateColumnDecorator" />
				<display:column sortable="true" property="specialist" title="Spec"/>
				<display:column sortable="true" property="bkupGmsUserId" title="Bkup Spec"/>
				<display:column sortable="true" property="pd" title="PD"/>	
				<display:column sortable="true" property="pi" title="PI"/>
				<display:column sortable="true" property="programStatus" title="Pgm GS Status"/>
				<display:column property="programGreensheet" title=""/>
								
				<display:column sortable="true" property="specialistStatus" title="GMS GS Status"/>
				<display:column property="specialistGreensheet" title=""/>				
				
				<display:setProperty name="paging.banner.items_name" value="Grants"/>
				<display:setProperty name="paging.banner.item_name" value="Grant"/>
				<display:setProperty name="paging.banner.placement" value="both"/>
				<display:setProperty name="paging.banner.some_items_found" value="<span>{0} {1} found, displaying Grants {2} to {3}.</span></br>" />
				<display:setProperty name="paging.banner.onepage" value=""/>
			</display:table>
		</td>
	</tr>
</table>
	

<html:form action="changelock">
  <input type="hidden" name="grantId" value=""/>
  <input type="hidden" name="applId" value=""/>
  <input type="hidden" name="groupType" value=""/>
</html:form> 
<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>