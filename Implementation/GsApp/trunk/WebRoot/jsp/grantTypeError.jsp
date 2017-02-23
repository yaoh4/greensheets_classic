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


<div>
	<p class="TitleSecondary">The grant award (<%=request.getAttribute("gnFull")%>) was an administrative action to reflect a change in the recipient organization's status. It did not require preparation or submission of a greensheet.</p>
</div>

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>
