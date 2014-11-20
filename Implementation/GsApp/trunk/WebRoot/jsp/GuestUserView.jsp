<%@ page language="java" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
%>

<%@ include file="/jsp/common/GlobalHeader.jsp"%>

<br />

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
		<td colspan="2" width="100%" >
			<p>There are no pending greensheets assigned to you. According to the system you are a 'guest user'. If you think this is an error, please send an e-mail to the <a href="mailto:nci-now-l@list.nih.gov?subject=Greensheets">NOW Comments List</a>. You may view a read-only version of a greensheet for a particular grant by using the search field above.</p>
		</td>
	</tr>
</table>


<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>