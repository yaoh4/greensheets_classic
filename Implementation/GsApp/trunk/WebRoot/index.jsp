<%@ page language="java" %>
<html>
<head>
	<title>Welcome To Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>


<script>
function redirect() {
	self.location.href="/greensheets/retrievegrants.do" ; // the home page url
	document.body.style.cursor = "wait";  
}
</script>

<body onload="redirect()">

<%@ include file="/jsp/common/DialogHeader.jsp"%>
<p>Retrieving your list of grants. Please wait.</p>
<%@ include file="/jsp/common/GlobalFooter.jsp"%>