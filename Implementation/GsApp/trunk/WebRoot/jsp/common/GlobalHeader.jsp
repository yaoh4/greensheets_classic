<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>
<!-- begin default header -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/otherappsmenu.css" type="text/css" />
<script language="javascript" src="<%=request.getContextPath()%>/scripts/ClientSideMethods.js"></script>

<%
    boolean isSuperUser = false;
    String currFYtoAssume = "";
    GsUser user = ((GreensheetUserSession)session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION)).getUser();
    userName = user.getDisplayUserName();

    GreensheetUserSession actualUsersSession = (GreensheetUserSession) 
            (session.getAttribute(GreensheetsKeys.ACTUAL_USER_SESSION));
    if (actualUsersSession!=null) {
    	GsUser actualUser = actualUsersSession.getUser();
    	if (actualUser!=null) {
            Map userPreferences = user.getUserPreferences();
            if (userPreferences!=null) {
               currFYtoAssume = (String) userPreferences.get("FY_TO_ASSUME");
               if (currFYtoAssume==null)  { currFYtoAssume = ""; }
            }    		
    	}
    }
    
    if (user!=null) {
    	isSuperUser = user.getNciPerson().isSuperUser();
    }

    String appEnv = DbConnectionHelper.getDbEnvironment();
    String appVersion = application.getInitParameter("appVersion");
    String appBuild = application.getInitParameter("appBuild");
    String emailLink = "mailto:" + application.getInitParameter("commentsEmail") + "?subject=Greensheets";
    String urlWorkbench = ((Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES)).getProperty("url.Workbench");
    String urlImpac2 = ((Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES)).getProperty("url.Impac2");

%>
    <script>
    function openHelp(){
    <%
    	if(user.getRole().equals(GsUserRole.SPEC)){%>
        window.open('./help/GreensheetsSpecGuide.pdf');
    <%} else {%>
         window.open('./help/GreensheetsPgmGuide.pdf');
    <%}%>
}
</script>
    <!-- Version: <%=appVersion%> -->
    <!-- Build: <%=appBuild%> -->
	<table width="100%">
		<tr>
			<td>
				<table style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td nowrap="nowrap" valign="bottom">
							<img src="<%=request.getContextPath()%>/images/Logo_Greensheets.gif" alt="Greensheets" border="0" />
						</td>
						<td nowrap="nowrap" valign="bottom">&nbsp;</td>
						<td style="vertical-align: bottom; white-space: nowrap;">
							User: <b><%=userName%> </b>
						</td>
						<td style="vertical-align: bottom; white-space: nowrap; padding-left: 1em; padding-right: 2em;">
							<%if(isSuperUser){%>
							 <form name="ChangeUser" id="ChangeUser"
							         style="display: inline;" 
							         method="post" action="<%=request.getContextPath()%>/changeUser.do">
							    [<a href="javascript: document.ChangeUser.submit();">Change User</a>] 
							  </form>
							  <% if (appEnv!=null && !"PROD".equalsIgnoreCase(appEnv)) { %>
							     <span style="padding: 2ex;">
								     <% if (!"".equals(currFYtoAssume)) { %> Current FY: <b><%=currFYtoAssume%></b> <% } %>
								     [<a href="<%=request.getContextPath()%>/editCurrFY.do">Change FY</a>]
								     <%-- If the app is connected to PROD database, we don't want anyone changing the current FY --%>
							     </span>
							  <% } %>
							<% } %>
						    <%-- Now, if we keep on testing as someone else (who is not themselves a "super" user) but we
						         changed the current FY to some other FY, we want it in the page HTML source so we can still see it
						         during testing. --%>
						    <!-- Current FY (if set to anything special): <%=("".equals(currFYtoAssume)?"based on system date":currFYtoAssume)%> -->
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
							<img src="<%=request.getContextPath()%>/images/Spacer.gif" width="60" height="5" />
								<div style="text-align: right;">
									<img src="<%=request.getContextPath()%>/images/Spacer.gif" width="5" height="5">
									<img src="<%=request.getContextPath()%>/images/Spacer.gif" width="5" height="5">					
									<a href="<%=emailLink%>">Send Comments</a>
									<img src="<%=request.getContextPath()%>/images/Spacer.gif" width="5" height="5">
									<img src="<%=request.getContextPath()%>/images/Spacer.gif" width="5" height="5">
									<img src="<%=request.getContextPath()%>/images/Spacer.gif" width="5" height="5"> 
									<a href="javascript: openHelp()">Help</a>
									<img src="<%=request.getContextPath()%>/images/Spacer.gif" width="5" height="5"> 
								</div>
						</td>
				</table>
			</td>
		</tr>
		<tr>
		  <td>
			<table width="100%" class="line">
				<tr>
					<td></td>
				</tr>
			</table>		
		  </td>
		</tr>
		<tr><td>
			<table cellspacing="0" >
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
									<td  colspan="2" >
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
			</table>			
		</td></tr>
	</table>
<!-- end default header -->