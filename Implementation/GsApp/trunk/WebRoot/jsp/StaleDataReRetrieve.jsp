<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>

<html>
<head>
    <title>Greensheets</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>

<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%>

<div style="padding: 2ex;">
	<h1 class="TitlePrimary">Greensheet could not be saved</h1>
	<h2 class="TitleSecondary">Conflicting modifications problem</h2>

	<p>The system has detected that between when you retrieved this greensheet for editing and 
	now, it was saved by someone else (or perhaps by you in a different window); therefore, if 
	the system proceeded with the Save operation you are attempting, it could overwrite those 
	more recent changes saved.
	</p>  
	<logic:present name="<%=GreensheetsKeys.KEY_STALEDATA_TIMESTAMP%>" scope="request">
	    <logic:notEmpty name="<%=GreensheetsKeys.KEY_STALEDATA_TIMESTAMP%>" scope="request">
	        <p>This greensheet was saved most recently at
	        <bean:write name="<%=GreensheetsKeys.KEY_STALEDATA_TIMESTAMP%>" scope="request"/>
				<logic:present name="<%=GreensheetsKeys.KEY_STALEDATA_USERNAME%>" scope="request">
				    <logic:notEmpty name="<%=GreensheetsKeys.KEY_STALEDATA_USERNAME%>" scope="request">
				        by user
				        '<bean:write name="<%=GreensheetsKeys.KEY_STALEDATA_USERNAME%>" scope="request"/>'.
				    </logic:notEmpty>
				</logic:present>
			</p>
	    </logic:notEmpty>
	</logic:present>
	<p>You should close this window and <strong>re-retrieve this greensheet</strong> to be sure you are 
	modifying the most recent saved version of it.</p>
</div>

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>