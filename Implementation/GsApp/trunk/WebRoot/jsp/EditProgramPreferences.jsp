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
					<html:form action="saveprogrampreferences">
						<TABLE border="0" width="80%" cellpadding="3">
							<TR>
								<TD>
									<P align="left">
										<STRONG>Grants From:</STRONG>
										<SELECT name="grantsFrom">
											<OPTION value="myportfolio">
												My Porfolio
											</option>
											<OPTION value="mycanceractivity">
												My Cancer Activity
											</option>
											<OPTION value="allncigrants">
												All NCI Grants
											</option>
										</SELECT>
									</P>
								<TD>
									<DIV align="right">
										<STRONG>Grant Type:</STRONG>
										<SELECT name="grantType">
											<OPTION value="competinggrants">
												Competing Grants
											</OPTION>
											<OPTION value="noncompetinggrants">
												Non-Competing Grants
											</OPTION>
											<OPTION value="both" selected="true">
												Both
											</OPTION>
										</SELECT>
									</DIV>
								</TD>
							</TR>
							<TR>
								<TD align="bottom">
									<DIV align="left">
										<STRONG>Mechanism:</STRONG>
										<INPUT type="text" name="mechanism">
										Example: R01
									</DIV>
									<DIV align="left">
										Leaving Mechanisms blank will return all Mechanisms
									</DIV>
								</TD>
								<TD>
									<DIV align="right">
										<STRONG>Only Grants within Payline:</STRONG>
										<LABEL>
											<INPUT type="radio" name="onlyGrantsWithinPayline" value="yes">
											Yes
										</LABEL>
										<LABEL>
											<INPUT type="radio" name="onlyGrantsWithinPayline" value="no">
											No
										</LABEL>
									</DIV>
								</TD>
							</TR>
							<TR>
								<TD colspan="2">
									<DIV align="left">
										<STRONG>Grant Number:</STRONG>
										<INPUT type="text" name="grantNumber">
										    &nbsp;&nbsp;&nbsp; -or- &nbsp;&nbsp;&nbsp;
										    <STRONG>PI Name:</STRONG>
										<INPUT type="text" name="piName" />
									</DIV>
								</TD>
							</TR>
							<TR>
								<TD colspan="3">
									<P align="left">
										<a href="javascript:document.programPreferencesForm.submit();">
										   <img src="./images/SavePref.gif" border="0">
										</a>
										<a href="retrievegrants.do">
										  <img src="./images/Cancel.gif" border="0">
										</a>
									</P>
								</TD>
							</TR>
						</TABLE>
					</html:form>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<%@ include file="/jsp/common/GlobalFooter.jsp"%>
				</td>
			</tr>
		</table>
	</body>
</html>
