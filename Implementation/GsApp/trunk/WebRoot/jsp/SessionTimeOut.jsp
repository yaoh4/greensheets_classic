<%@ page language="java" %>

<!-- begin default header -->
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
	<script language="javascript" src="./scripts/ClientSideMethods.js"></script>
</head>
<body>
<table cellspacing="0" width="100%" class="header">
	<tr class="topRow">
		<td class="logo"><a href="#"><img src="./images/LogoGreensheets.gif" alt="Greensheets logo" border="0" /></a></td>
		<td class="align1 align4">&nbsp;<%=application.getInitParameter("buildinfo")%></td>
	</tr>
	<tr class="bottomRow">
		<td class="globalNav">
			<form name="refreshGrantsList" id="refreshGrantsList" method="post" action="/greensheets">
				<a href="javascript: document.refreshGrantsList.submit();">Reload Greensheets</a>
                <a href="javascript: openHelp()">Help</a>
            </form>
        </td>
	</tr>
</table>
<br />

Your user session has timed out. Please select the Reload Greensheets button.

<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>