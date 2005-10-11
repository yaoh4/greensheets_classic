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
	String emailLink = "mailto" + application.getInitParameter("commentsEmail") + "?subject=Greensheets";
	
	String urlWorkbench = application.getInitParameter("urlWorkbench");
	String urlImpac2 = application.getInitParameter("urlImpac2");
%>

</script>
<!--
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td nowrap><img src="./images/Logo_Greensheets.gif"  alt="Greensheets Logo"></td>
          <td nowrap valign="bottom">&nbsp;</td>
          <td nowrap valign="bottom"><b>User:</b> <%=userName%></td>
          <td nowrap valign="bottom">&nbsp;</td>
          <td nowrap valign="bottom"><b>Env: </b><%=DbConnectionHelper.getDbEnvironment()%></td>
          <td nowrap valign="bottom">&nbsp;</td>
          <td nowrap valign="bottom"><b>Version: </b><%=application.getInitParameter("appVersion")%></td>
          <td nowrap valign="bottom" align="right" width="100%">
          	<img src="./images/spacer.gif" width="60" height="5">
            	<a href="mailto:<%=application.getInitParameter("commentsEmail")%>?subject=Greensheets">Send Comments</a>
            	<img src="images/spacer.gif" width="5" height="5"> 
            	<img src="images/spacer.gif" width="5" height="5"> 
            	<img src="images/spacer.gif" width="5" height="5"> 
            	 <a href="javascript: openHelp()">Help</a>
            <img src="./images/spacer.gif" width="40" height="5"> 
          </td> 
        </tr>
        <tr>
          <td height="5" nowrap><img src="./images/spacer.gif" width="10" height="5"></td>
          <td height="5" nowrap><img src="./images/spacer.gif" width="10" height="5"></td>
          <td height="5" nowrap><img src="./images/spacer.gif" width="10" height="5"></td>
          <td height="5" nowrap><img src="./images/spacer.gif" width="10" height="5"></td>
          <td height="5" nowrap><img src="./images/spacer.gif" width="10" height="5"></td>
        </tr>
      </table>
      
      <table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#99CCFF">
					<span class="otherappsmenu">
						<a  class="oambutton" href="<%=application.getInitParameter("urlWorkbench")%>">Workbench</a> 
					</span>
			</td>		
			<td><img src="./images/spacer.gif" width="5 height="5"> </td>
			<td bgcolor="#99CCFF">
				<span class="otherappsmenu">
					<a class="oambutton"  href="<%=application.getInitParameter("urlImpac2")%>">IMPAC II Web Applications</a>	
				</span>
			</td>			      					
		</tr>
		<tr>
			<td><img src="./images/Spacer.gif" width="10" height="3"></td>
		</tr>
		<tr>
			<td ><img src="./images/Spacer.gif" width="10" height="3"></td>
		</tr>
	</table>
-->
<!--  From Pals -->
<table style="width: 100%;" border="0" cellspacing="0" cellpadding="0">
	<tbody>
		<tr>
			<td nowrap="nowrap" valign="top">
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
							<td colspan="2" bgcolor="#99CCFF"><img src="./images/spacer.gif" width="5" height="2"> </td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</tbody>
</table>




<!-- From Pals end -->




<table cellspacing="0" width="100%" class="header" border="1">
<!--
	<tr class="topRow">
		<td class="logo" colspan="2"><a href="#"><img src="./images/Logo_Greensheets.gif"  border="0" /></a></td>
	</tr>
-->	
	<tr class="bottomRow">
		<td class="globalNav">
			<form name="refreshGrantsList" id="refreshGrantsList" method="post" action="/greensheets/retrievegrants.do">
				<a href="javascript: document.refreshGrantsList.submit();">Refresh Grants List</a>
                <!-- <a href="javascript: openHelp()">Help</a> -->
            </form>
        </td>
		<td class="align1">
			<form name="SimpleSearch" id="SimpleSearch" method="post" action="/greensheets/searchforgrant.do">
				Search for 
				<select name="searchType">
					<option value="SEARCH_GS_NUMBER">Grant Number</option>
					<option value="SEARCH_GS_NAME">PI Name</option>
				</select>:
				<input type="text" name="searchText" value=" "/>
				<a href="javascript: document.SimpleSearch.submit();">Go</a>
			</form>
		</td>
	</tr>
</table>
<br />
<!-- end default header -->