<%@ page language="java" %>

<!-- begin default header -->
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
	<script language="javascript" src="./scripts/ClientSideMethods.js"></script>
</head>
<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%> 

<table cellspacing="0" width="100%" class="header">
	<tr class="bottomRow">
		<td class="globalNav">
			<form name="refreshGrantsList" id="refreshGrantsList" method="post" action="/greensheets/"> <!-- Abdul Latheef: Changed the action attribute to /greensheets/ from /greensheets -->
				<a href="javascript: document.refreshGrantsList.submit();">Reload Greensheets</a>
            </form>
        </td>
	</tr>
</table>
<br />

Your user session has timed out. Please select the Reload Greensheets button.

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>