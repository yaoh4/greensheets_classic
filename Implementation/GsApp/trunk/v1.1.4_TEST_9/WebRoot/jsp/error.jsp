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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css"
	type="text/css" />
</head>
<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%>

<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
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


<p class="err">We're sorry. An error has occurred.</p>
<p>Please try again, or email the <a
	href="mailto:nci-now-l@list.nih.gov?subject=Greensheets&body=---- User Name: <%=userName%> ---- ">NOW Comments
List</a> Please indicate in the body of the email the error message
below, and describe what you were attempting to do when you received the
error.</p>

<logic:messagesPresent>
	<bean:message key="errors.header" />
	<ul>
		<html:messages id="error">
			<li><bean:write name="error" /></li>
		</html:messages>
	</ul>
	<hr>
</logic:messagesPresent>

</br>


<%if (application.getInitParameter("showDebugOnError").equals("true")) {%>
<%=(String) request.getAttribute("ERROR_STACK_TRACE")%>
<%} else {%>


<a href="javascript:showHideError()"><img src="./images/IconError.gif" />
</a>
Technical Info
<div id="errormessage" style="DISPLAY: none"><pre>

            <%=(String) request.getAttribute("ERROR_STACK_TRACE")%>"
  </pre></div>
<%}%>



<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>
