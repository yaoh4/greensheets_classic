<%@ page language="java" %>

<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/displaytag.tld" prefix="display" %>


<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
%>

<script>

function changePayLineOption(){

		var val = document.frmPayLineOnly.paylineOnly.getAttribute("checked");
		if(val == false || val == "checked")
		{
            document.frmPayLineOnly.paylineOpt.value="NO";
		}else{
            document.frmPayLineOnly.paylineOpt.value="YES";
        }


        document.frmPayLineOnly.submit()

}


function getMyPortfolio(){

		var val = document.frmMyPortfolio.cbxMyPortfolio.getAttribute("checked");
		if(val == false || val == "checked")
		{
            document.frmMyPortfolio.myPortfolio.value="NO";
		}else{
            document.frmMyPortfolio.myPortfolio.value="YES";
        }


        document.frmMyPortfolio.submit()

}





</script>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%@ include file="/jsp/common/GlobalHeader.jsp"%> 
<h1>Program Grants List for <%=userName%></h1>

<%if (request.getAttribute("SEARCH_RESULTS") == null) {%>
<form name="frmPayLineOnly" id="frmPayLineOnly" method="post" action="/greensheets/retrievegrants.do">
<input type="checkbox" name="paylineOnly" id="paylineOnly" value="payLineOnlyChecked" <%=request.getAttribute("payLineOnlyChecked")%> onclick="javascript:changePayLineOption()"/>Show only grants within the payline
<input type="hidden" name="paylineOpt" id="paylineOpt" value="YES"/>
</form>
<%if (request.getAttribute("MY_PORTFOLIO_OPT") != null) {%>
<form name="frmMyPortfolio" id="frmMyPortfolio" method="post" action="/greensheets/retrievegrants.do">
<input type="checkbox" name="cbxMyPortfolio" id="cbxMyPortfolio" value="myPortfolioChecked" <%=request.getAttribute("myPortfolioChecked")%> onclick="javascript:getMyPortfolio()"/>My Portfolio
<input type="hidden" name="myPortfolio" id="myPortfolio" value="YES"/>
</form>
<%}%>

<%}%>
<display:table name="sessionScope.GRANT_LIST" 
	requestURI="/greensheets/no_op.do" 
	class="data" id="row" 
    defaultsort="3"
	sort="list" pagesize="50" cellspacing="0">

		<display:column property="programGreensheet" title=""/>
		<display:column sortable="true" property="grantNumberSort" title="Grant Number" />
		<display:column sortable="true" property="latestBudgetStartDate" title="Budget Start Date" decorator="gov.nih.nci.iscs.numsix.greensheets.application.DateColumnDecorator" />
		<display:column sortable="true" property="pd" title="PD"/>
		<display:column sortable="true" property="pi" title="PI"/>
		<display:column sortable="true" property="cancerActivity" title="CA"/>
		<display:column sortable="true" property="priorityScore" title="Score" />
		<display:column sortable="true" property="percentileScore" title="%"/>
		<display:column sortable="true" property="councilMeetingDate" title="Board Date"/>
		<display:column sortable="true" property="programStatus" title="Greensheet Status"/>
		<display:column sortable="true" property="specialistStatus" title="GMS GS Status"/>


		<display:setProperty name="paging.banner.items_name" value="Grants"/>
        <display:setProperty name="paging.banner.item_name" value="Grant"/>
	    <display:setProperty name="paging.banner.placement" value="both"/>
	  	<display:setProperty name="paging.banner.some_items_found" value="<span>{0} {1} found, displaying Grants {2} to {3}.</span></br>" />
        <display:setProperty name="paging.banner.onepage" value=""/>
        <display:setProperty name="paging.banner.no_items_found" value="No grants found. There are no pending greensheets"/>
       <display:setProperty name="basic.msg.empty_list" value=""/>

		




</display:table>
<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>