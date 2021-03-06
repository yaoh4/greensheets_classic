<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body> 

<%@ include file="/jsp/common/DialogHeader.jsp"%> 
<!-- begin body content -->
<h1>Greensheet Questionnaire Not Found</h1>
<%
String mech = (String) request.getAttribute("MISSING_MECH");
if(mech != null){
%>
<p>
	Greensheet questionnaire definition was not found for 
	type <strong><%=request.getAttribute("MISSING_TYPE")%></strong>
	<strong><%=request.getAttribute("MISSING_MECH")%></strong> grants.
</p>
<%} else {%>
<p>
	Greensheet questionnaire definition was not found for Revision action.
</p>
<%} %>
<p>
    Please contact support if you feel that greensheet questionnaires for such grants should be defined.
</p>
<!-- end body content -->
<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>