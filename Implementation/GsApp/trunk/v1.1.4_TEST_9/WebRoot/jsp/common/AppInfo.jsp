<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>

<HTML>
<HEAD>
<TITLE>Paylist Info</TITLE>
<META NAME="Generator" CONTENT="TextPad 4.6">
<META NAME="Author" CONTENT="?">
<META NAME="Keywords" CONTENT="?">
<META NAME="Description" CONTENT="?">
</HEAD>


<BODY BGCOLOR="#FFFFFF" TEXT="#000000" LINK="#FF0000" VLINK="#800000" ALINK="#FF00FF" BACKGROUND="gray">
	<TABLE ALIGN="left" BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH="100%">
	<tr>
		<td>Version:</td>
		<td><%=application.getInitParameter("appVersion")%></td>
	</tr>
	<tr>
		<td>Build:</td>
		<td><%=application.getInitParameter("appBuild")%></td>
	</tr>
	<tr>
		<td>Environment:</td>
		<td><%=DbConnectionHelper.getDbEnvironment()%></td>
	</tr>	
</table>
</BODY>
</HTML>