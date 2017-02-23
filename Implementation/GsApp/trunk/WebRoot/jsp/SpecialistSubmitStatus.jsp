<%@ page import="java.util.*" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%>
<table cellspacing="0" width="100%" class="header">
	<tr class="topRow">
		<td class="logo"><a href="#"><img src="./images/LogoGreensheets.gif" alt="Greensheets logo" border="0" /></a></td>
		<td class="align1 align4">&nbsp;<%=application.getInitParameter("buildinfo")%></td>
	</tr>
	<tr class="bottomRow">
		<td class="globalNav">
			<form name="refreshGrantsList" id="refreshGrantsList" method="post" action="/greensheets/specialistSubmittedStatus.do">
				<a href="javascript: document.refreshGrantsList.submit();">Refresh Submitted Grants List</a>
                <input type="hidden" name="refreshList" id="refreshList" value="refresh_list"/>
            </form>
        </td>
        <td class="align1"/>
	</tr>
</table>
<br/>
 

<%

if(session.getAttribute("list") == null || request.getParameter("refreshList")!= null){
}
%>

<display:table name="sessionScope.list" 
	class="data" id="row" 
    requestURI="/greensheets/specialistSubmittedStatus.do"
    defaultsort="1"
    export="true"
	sort="list" pagesize="50" cellspacing="2">

		<display:column sortable="true" property="submitterId" title="Submitter" group="1"/>
		<display:column sortable="true" property="fullGrantNum" title="Grant Number" />

		<display:column sortable="true" property="applId" title="Appl Id" />
		<display:column sortable="true" property="submittedDate" title="Date Submitted" decorator="gov.nih.nci.iscs.numsix.greensheets.application.DateColumnDecorator" />

        <display:setProperty name="export.amount" value="list" />
        <display:setProperty name="export.xml" value="false" />
        <display:setProperty name="export.csv" value="false" />
        <display:setProperty name="export.excel.include_header" value="true" />

		<display:setProperty name="paging.banner.items_name" value="Submitted Grants"/>
        <display:setProperty name="paging.banner.item_name" value="Submitted Grant"/>
	    <display:setProperty name="paging.banner.placement" value="both"/>
	  	<display:setProperty name="paging.banner.some_items_found" value="<span class='pagebanner'>{0} {1} found, displaying Submitted Grants {2} to {3}.</span>" />
        <display:setProperty name="paging.banner.onepage" value=""/>
        <display:setProperty name="paging.banner.no_items_found" value="No Submitted Grants found."/>
        <display:setProperty name="basic.msg.empty_list" value=""/>

</display:table>

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>