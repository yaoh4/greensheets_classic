<%@ page language="java" %>
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

<%@ include file="/jsp/common/DialogHeader.jsp"%>
<!-- begin body content -->
<h1>Greensheet Not Found</h1>
<p>
	A Greensheet was not found for the requested Grant Type and Mechanism<br />
	Type <%=request.getAttribute("MISSING_TYPE")%><br />
	Mechanism <%=request.getAttribute("MISSING_MECH")%><br/>
    Please e-mail the NCI GAB Greensheets e-mail group (in the Global Address List) if you feel that it should be included
</p>
<!-- end body content -->
<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>