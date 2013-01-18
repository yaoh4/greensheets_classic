<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>
<script language="javascript" src="./scripts/ClientSideMethods.js"></script>

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
					<html:form action="editprogrampreferencesdispatch">
						<TABLE border="0" width="80%" cellpadding="3">
							<TR>
								<TD>
									<P align="left">
										<STRONG>Grants From:</STRONG>
										<html:select property="grantSource">
											<html:optionsCollection name="grantSources" value="value" label="label" />
										</html:select>
									</P>
								<TD>
									<DIV align="left">
										<STRONG>Grant Type:</STRONG>
										<html:select property="grantType">
											<html:optionsCollection name="grantTypes" value="value" label="label" />
										</html:select>
									</DIV>
								</TD>
							</TR>
							<TR>
								<TD align="bottom">
									<DIV align="left">
										<STRONG>Mechanism:</STRONG>
										<html:text property="mechanism" />
										Example: R01
									</DIV>
									<DIV align="left">
										Leaving Mechanisms blank will return all Mechanisms
									</DIV>
								</TD>
								<TD>
									<DIV align="left">
										<STRONG>Show only Competing Grants within the Payline:</STRONG>
									    <html:checkbox property="onlyGrantsWithinPayline" value="yes"/>
									</DIV>
								</TD>
							</TR>
							<TR>
								<TD colspan="3">
									<P align="left">
									    <html:image page="/images/SavePref.gif" property="image_savePreferences" alt="Save Preferencces" />
										<html:image page="/images/Cancel.gif" property="image_cancel" alt="Cancel" />
									</P>
								</TD>
							</TR>
							<TR>
								<TD colspan="3">
									<STRONG>NOTE: Selection of ALL NCI Grants, Both Competing and Non-Competing Grants, and leaving the Payline checkbox unchecked will result in all grants being returned within the multi-page list. 
									This will slow system response.</STRONG>
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
