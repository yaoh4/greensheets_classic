<%@ page language="java"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*"%>
<%@ page import="gov.nih.nci.cbiit.scimgmt.common.bdo.UserRecord"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Greensheets</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css"
	type="text/css" />
</head>
<body>
	<%
	    String userName="";
	    String userNotFoundMsg = (String) session.getAttribute(GreensheetsKeys.USER_NOT_FOUND);
	    List<UserRecord> foundUsers = (List<UserRecord>) session.getAttribute("foundUsers");
	%>

	<%@ include file="/jsp/common/GlobalHeader.jsp"%>

	<br />
	<div class="TitlePrimary">
		Testing / Troubleshooting:
	</div>
	<div class="TitleSecondary">Search for a user whose
            view of the system you would like to experience
    </div>
    <div style="margin: 1em 0;"><!-- Formatting (line spacing) placeholder --></div>
	<%
	    if (userNotFoundMsg != null) {
	%>
	<div class="TitleSecondary"><font color="red">User not found. Please try again.</font></div>
	<%
	    }
	%>
	<form name="searchUser" id="searchUser" method="post"
		action="/greensheets/searchUser.do">
		<input type="hidden" name="method" value="searchUser" />

		<table style="width: 40%;" border="0" cellpadding="0" cellspacing="0">
			<tr valign="bottom">
			<tr>
				<td valign="bottom"><label for="firstName">First Name:</label> <input
					type="text" name="firstName" value="" /></td>
			</tr>
			<tr>
				<td valign="bottom"><label for="lastName">Last Name:</label> <input
					type="text" name="lastName" value="" /></td>
			</tr>
		</table>
		<br> <input name="Search" type="submit" class="button"
			id="Search" value="Search" />

	</form>

	<!-- Showing search results -->
	<form name="setUser" id="setUser" method="post"
		action="/greensheets/setUser.do">
		<input type="hidden" name="method" value="setUser" />

		<%
		    if (foundUsers != null) {
		%>
		<div class="TitleSecondary">Search Results: (Max number of 100 users are displayed.)</div>

		<table style="width: 40%;" class="data" cellspacing="0">

			<thead>
				<tr>
					<th>&nbsp;</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>User name</th>
				</tr>
			</thead>

			<tbody>

				<%
				UserRecord ladpUser = null;

				        for (int i = 0; i < foundUsers.size(); i++) {

				            ladpUser = (UserRecord) foundUsers.get(i);
				%>
				<tr class="even">
					<!-- This could look better with using real DisplayTag odd/even ordering -->
					<td><input type="radio" name="selectedUser"
						value="<%=ladpUser.getCommonName()%>" /></td>
					<td><%=ladpUser.getFirstName()%></td>
					<td><%=ladpUser.getLastName()%></td>

					<td><%=ladpUser.getCommonName()%></td>
				</tr>
				<%
				    }
				%>
			</tbody>
		</table>
		<br> <input name="set" type="submit" class="button" id="set"
			value="Assume the selected user&apos;s view" />


		<%
		    }
		%>

	</form>

	<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>
