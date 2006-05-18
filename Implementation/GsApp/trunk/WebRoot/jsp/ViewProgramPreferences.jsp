<%@ page language="java"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*"%>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/displaytag.tld" prefix="display"%>
<script language="javascript" src="<%=request.getContextPath()%>/scripts/ClientSideMethods.js"></script>
<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html>
	<head>
		<title>Greensheets</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="./stylesheets/otherappsmenu.css" type="text/css" />
		<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
	</head>
	<body>
		<table border="0" width="100%">
			<tr>
				<td width="100%">
					<%@ include file="/jsp/common/GlobalHeader.jsp"%>
				</td>
			</tr>
			<tr>
				<td>
					<H3>
						Your current preferences are:
					</H3>
				</td>
			</tr>
			<tr>
				<td>
					<div>
						<STRONG>Grants From:</STRONG> My Portfolio
					</div>
					<div>
						<STRONG>Grant Type:</STRONG> Competing Grants
					</div>
					<div>
						<STRONG>Budget Mechanism:</STRONG> K12
					</div>
					<div>
						<STRONG>Only Grants within Payline:</STRONG> Yes
					</div>
					<div>
						<table width="100%" class="line">
							<tr>
								<td></td>
							</tr>
						</table>
					</div>
					<div>
					    <img src="/images/Spacer.gif" width="5" height="5">	
					 <div>					
					<div>
						<a href="retrievegrants.do"> <img src="./images/GreensheetsHome.gif" border="0"> </a>
					</div>
				<td>
			<tr>
			<tr>
				<td colspan="2">
					<%@ include file="/jsp/common/GlobalFooter.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
