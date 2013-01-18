<%@ page language="java" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
String userNotFoundMsg = (String)session.getAttribute(GreensheetsKeys.USER_NOT_FOUND);
%>

<%@ include file="/jsp/common/GlobalHeader.jsp"%>

<br />
<% if(userNotFoundMsg!=null) {%>
<li><font color="red">User not found. Please try again.</font></li>
<%} %>
<form name="test" id="test" method="post" action="/greensheets/changeusername.do">
<table border="0" cellpadding="0" cellspacing="0" >
	<tr valign="bottom">
	
		<td align="right" valign="bottom" nowrap="1">
				<table>
				
				<tr>
					<td  nowrap="1" valign="bottom">Enter User Name:</td>
					<td valign="bottom"><input type="text" name="newUserName" value=" "/></td>
					<td valign="bottom">
							<table cellspacing="0" width="100%" class="header" border="0">
								<tr class="bottomRow1">
									<td class="globalNav" width="100%" valign="bottom">
										
										<a href="javascript: document.test.submit();">Change</a>
									
									</td>
								</tr>
								</table>
								</td>
				</tr>
			
					
		</td>
	</tr>
	
</table>
</form>

<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html> 











































