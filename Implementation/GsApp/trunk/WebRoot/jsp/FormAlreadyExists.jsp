<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>

<html>
<head>
    <title>Greensheets</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>

<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%>

<div style="padding: 2ex;">
	<h1 class="TitlePrimary">Greensheet could not be saved</h1>
	<h2 class="TitleSecondary">Only one greensheet of each type is allowed per grant</h2>

	<p>You tried to save or submit a greensheet that, when you retrieved it from the database,
	was in the NOT STARTED status. However, the system determined that by now a saved greensheet 
	of this type for this grant is already present in the database.  
	</p>  
	<p>Because only one greensheet of each type is allowed per grant, you should close this window 
	and <strong>re-retrieve this greensheet</strong> to be sure you are working with the most 
	recent saved version of it.</p>
</div>

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>