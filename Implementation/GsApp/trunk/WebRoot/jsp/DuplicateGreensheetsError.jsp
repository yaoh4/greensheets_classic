<%@ page language="java" %>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>

<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%>

<p><h3>Duplicate greensheets detected: the system determined that the rule that there should be only one greensheet of each type per grant is violated for this particular grant. Please contact support.</h3></p>

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>