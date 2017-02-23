<%@ page language="java" %>

<!-- begin default header -->
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
	<script language="javascript" src="./scripts/ClientSideMethods.js"></script>
</head>
<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%> 


<div style="width: 1000px; margin: 0px auto;">
	<fieldset class="loginError">		
		<div>	
			<b><font color="#009999">Description</font></b><br/>		
			<br> You do not have permission to access this application. <br>
			If you have any questions, please send an email to List NCI-NOW-L <a
				href="mailto:NCI-NOW-L@LIST.NIH.GOV?subject=Referral Access">&lt;NCI-NOW-L&#64;LIST.NIH.GOV&gt;</a>
		</div>
	</fieldset>
</div>

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>