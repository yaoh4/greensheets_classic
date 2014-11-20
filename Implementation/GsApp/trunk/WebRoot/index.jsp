<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<%@ page language="java" %>
<html>
<head>
	<title>Welcome To Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/GreensheetsStyleSheet.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/otherappsmenu.css" type="text/css" />

	<script>
		function redirect() {
		    self.location.href="/greensheets/retrievegrants.do" ; // the home page url
		    document.body.style.cursor = "wait";  
		}
	</script>
</head>
<body onload="redirect()">
<%@ include file="/jsp/common/DialogHeader.jsp"%>       

<p>Authenticating your credentials and retrieving the list of grants. Please wait.</p>

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>
