<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>
<!-- begin default header -->
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

</script>

<table cellspacing="0" width="100%" class="header">
	<tr class="topRow">
		<td class="logo"><a href="#"><img src="./images/LogoGreensheets.gif" title="Info <%=((Properties)AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_DB_PROPERTIES)).getProperty("oracle.url")%> <%=application.getInitParameter("buildinfo")%>" border="0" /></a></td>
		<td class="align1 align4">&nbsp;<%=application.getInitParameter("buildinfo")%>
		ENV: <%=DbConnectionHelper.getDbEnvironment()%>
		</td>
	</tr>
	<tr class="bottomRow">
		<td class="globalNav">
			<form name="refreshGrantsList" id="refreshGrantsList" method="post" action="/greensheets/retrievegrants.do">
				<a href="javascript: document.refreshGrantsList.submit();">Refresh Grants List</a>
                <a href="javascript: openHelp()">Help</a>
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