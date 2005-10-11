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
<%@ include file="/jsp/common/GlobalHeader1.jsp"%> 

<table cellspacing="0" width="100%" class="header">
	<tr class="bottomRow">
		<td class="globalNav">
			<form name="refreshGrantsList" id="refreshGrantsList" method="post" action="/greensheets">
				<a href="javascript: document.refreshGrantsList.submit();">Reload Greensheets</a>
            </form>
        </td>
	</tr>
</table>
<br />

Your user session has timed out. Please select the Reload Greensheets button.

<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>