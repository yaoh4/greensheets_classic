<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<%@ page language="java"%>
<%@ page isErrorPage="true"%>
<%@ page import="java.io.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*"%>
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
<%@ include file="/jsp/common/DialogHeader.jsp"%>

<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = "";
if (gus!=null && gus.getUser()!=null) {
	userName = gus.getUser().getDisplayUserName();
}
if (userName == null) { userName = ""; }
%>
<script>
function showHideError(){

            if(document.getElementById("errormessage").style.display=="none"){

                                document.getElementById("errormessage").style.display="block";

                } else{
                                document.getElementById("errormessage").style.display="none";
                }
}

</script>

<div>
	<p class="TitlePrimary">We're sorry. An error has occurred.</p>
	<p>Please try again, or email the <a
		href="mailto:nci-now-l@list.nih.gov?subject=Greensheets&body=---- User Name: <%=userName%> ---- ">NOW Comments
	List</a>.</p> 
	<p>Please include in the body of the e-mail the error message shown
	below, and describe what you were attempting to do when you received 
	the	error. Please include the grant number if applicable.</p>
<!--  
	<logic:messagesPresent>
		<bean:message key="errors.header" />
		<ul>
			<html:messages id="error">
				<li><bean:write name="error" /></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	<logic:messagesNotPresent>
	   <div style="margin-left: 4em;">The system is unable to provide any additional information 
	   about the error.</div>
	</logic:messagesNotPresent>
-->
</div>

<%-- And this below allows for the full stack trace to still be included in the page,
     if needed, but only as a comment - visible if one looks at page source in the 
     browser.
--%>
<!--  
<%
String debugOnErrorInitParam = application.getInitParameter("showDebugOnError");
if (debugOnErrorInitParam!=null && "TRUE".equals(debugOnErrorInitParam.toUpperCase())) {
%>

BELOW IS THE DETAILED STACK TRACE INFORMATION RELATED TO THE ERROR MESSAGE:  

<%=(String) request.getAttribute("ERROR_STACK_TRACE")%>
<%}%>
-->

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>
