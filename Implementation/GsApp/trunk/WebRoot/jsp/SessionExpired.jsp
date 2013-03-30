<%@ page language="java" %>

<%-- This is just like SessionTimeOut.jsp, but with paths not starting with ./  - Anatoli
     How SessionTimeOut.jsp works with all paths starting with ./ is because even when 
     control is redirected to JSPs from Struts mappings, users' browsers don't know that - 
     to them, all requests they submit are for /someAction.do starting at context root.
--%>

<!-- begin default header -->
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/GreensheetsStyleSheet.css" type="text/css" />
	<script language="javascript" src="<%=request.getContextPath()%>/scripts/ClientSideMethods.js"></script>
</head>
<body>

<!-- Header -->
<table cellspacing="0" width="100%" class="header">
    <tr class="topRow">
        <td class="logo" colspan="2"><a href="#"><img src="<%=request.getContextPath()%>/images/Logo_Greensheets.gif" alt="Greensheets" border="0" /></a></td>
    </tr>
    <tr class="bottomRow">
        <td class="globalNav">&nbsp;</td>
        <td class="align1">&nbsp;</td>
    </tr>
</table>
<br />
<!-- End of header -->

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

<div style="margin: 0 10em 1em 2em">
	<div class="TitleSecondary">
	We are sorry, it appears that your user session has timed out. <br>
	Please select the Reload Greensheets button, or close any pop-up Greensheets windows,
	return to the window where you have the Greensheets home page open, and reload it.
	</div>
</div>

<!--  Footer  -->
<table width="100%" cellspacing="0"  class="header">
    <tr class="bottomRow">
        <td class="globalNav">&nbsp;</td>
        <td class="align1">&nbsp;</td>
    </tr>

    <tr>
        <td>
            &nbsp;
        </td>
        <td class="align1">
            <img src="<%=request.getContextPath()%>/images/LogoNCI.gif" alt="National Cancer Institute" title="National Cancer Institute">
        </td>
    </tr>
</table>
<!-- End of footer -->

</body>
</html>