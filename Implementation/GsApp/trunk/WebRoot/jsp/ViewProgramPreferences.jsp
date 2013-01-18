<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
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
						<STRONG>Grants From:</STRONG>
                        <bean:write name="grantSource" />
					</div>
					<div>
						<STRONG>Grant Type:</STRONG>
						<bean:write name="grantType" />
					</div>
					<div>
						<STRONG>Mechanism:</STRONG>
						<bean:write name="mechanism" />
					</div>
					<div>
						<STRONG>Only Competing Grants within Payline:</STRONG> 
						<bean:write name="onlyGrantsWithinPayline" />
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
						<a href="retrievegrants.do"> <img src="./images/GreensheetsHome.gif" border="0" alt="Greensheets Home"> </a>						
						&nbsp;&nbsp;&nbsp;
						<a href="editprogrampreferences.do"> <img src="./images/EditPref.gif" border="0" alt="Edit Preferences"> </a>
					</div>
					<div>
						<STRONG>NOTE: Selection of ALL NCI Grants, Both Competing and Non-Competing Grants, and leaving the Payline checkbox unchecked will result in all grants being returned within the multi-page list. 
						This will slow system response.</STRONG>
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
