<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>
<!-- begin pop up window header -->
<script language="javascript" src="./scripts/ClientSideMethods.js"></script>
<table cellspacing="0" width="100%" class="header">
	<tr class="topRow">
		<td class="logo"><a href="#"><img src="./images/LogoGreensheets.gif" title="Info <%=((Properties)AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_DB_PROPERTIES)).getProperty("oracle.url")%> <%=application.getInitParameter("buildinfo")%>" border="0" /></a></td>
		<td class="align1 align4">&nbsp;<%=application.getInitParameter("buildinfo")%></td>
	</tr>
	<tr class="bottomRow">
		<td class="globalNav">
			<a href="javascript:window.close();">Close</a>
		</td>
		<td class="align1">&nbsp;
			
		</td>
	</tr>
</table>
<br />
<!-- end pop up window header -->