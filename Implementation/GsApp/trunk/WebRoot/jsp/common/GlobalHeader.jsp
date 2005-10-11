<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>
<!-- begin default header -->
<link rel="stylesheet" href="./stylesheets/otherappsmenu.css" type="text/css" />
<script language="javascript" src="./scripts/ClientSideMethods.js"></script>

<script>

function openHelp(){


<%
    GsUser user = ((GreensheetUserSession)session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION)).getUser();
    if(user.getRole().equals(GsUserRole.SPEC)){%>
        window.open('./help/GreensheetsSpecGuide.pdf');
    <%}else{%>
         window.open('./help/GreensheetsPgmGuide.pdf');
    <%}%>
}

<%
	String appEnv = DbConnectionHelper.getDbEnvironment();
	String appVersion = application.getInitParameter("appVersion");
	String emailLink = "mailto:" + application.getInitParameter("commentsEmail") + "?subject=Greensheets";
	
	String urlWorkbench = application.getInitParameter("urlWorkbench");
	String urlImpac2 = application.getInitParameter("urlImpac2");
%>

</script>
	<table style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
	<tbody>
		<tr>
			<td nowrap="nowrap" valign="bottom">
				<img src="./images/Logo_Greensheets.gif" alt="Greensheets" border="0" />
			</td>
			<td nowrap="nowrap" valign="bottom">&nbsp;</td>
			<td style="vertical-align: bottom; white-space: nowrap;">
				User: <b><%=userName%></b>
			</td>
			<td nowrap="nowrap" valign="bottom">&nbsp;</td>
			<td style="vertical-align: bottom; white-space: nowrap;">
				Env: <b><%=appEnv%></b>
			</td>
			<td nowrap="nowrap" valign="bottom">&nbsp;</td>
			<td style="vertical-align: bottom; white-space: nowrap;">
				Version: <b><%=appVersion%></b>
			</td>
			<td style="vertical-align: bottom; white-space: nowrap; width: 100%; text-align: right;">
				<img src="./images/spacer.gif" width="60" height="5" />
					<div style="text-align: right;">
						<a href="<%=emailLink%>">SendComments</a>
						<img src="./images/spacer.gif" width="5" height="5">
						<img src="./images/spacer.gif" width="5" height="5">
						<img src="./images/spacer.gif" width="5" height="5"> 
						<a href="javascript: openHelp()">Help</a>
						<img src="./images/spacer.gif" width="5" height="5"> 
				 	</div>
			</td>
		 </tr>
 	</tbody>
</table>

<table width="100%" class="line">
	<tr>
		<td></td>
	</tr>
</table>


<table cellspacing="0" >
	<tbody>
		<tr>
			<td>
				<table cellspacing="0" class="globalNav">
					<tbody>
						<tr>
							<td>
								<span class="otherappsmenu">
									<a class="oambutton" href="<%=urlWorkbench%>">Workbench</a>
								</span>
							</td>
							<td>
								<span class="otherappsmenu">
									<a class="oambutton" href="<%=urlImpac2%>">IMPAC II Applications</a>
								</span>
							</td>
						</tr>
						<tr>
							<td  colspan="2" ></td>
								<table width="100%" class="line" cellpadding="0" cellspacing="0">
									<tr>
										<td></td>
									</tr>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</tbody>
</table>

<!-- end default header -->