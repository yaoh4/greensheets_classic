<%@ page language="java" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/displaytag.tld" prefix="display" %>

<%
GreensheetUserSession gus = (GreensheetUserSession) session.getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
String userName = gus.getUser().getDisplayUserName();
%>
 
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%@ include file="/jsp/common/GlobalHeader.jsp"%>
<h1>Specialist Grants List for <%=userName%></h1>
<display:table name="sessionScope.GRANT_LIST" 
	requestURI="/greensheets/no_op.do" 
	class="data" id="row" 
	defaultsort="3"
	sort="list" pagesize="50" cellspacing="0">
		<display:column sortable="true" property="grantNumberSort" title="Grant Number" />
		<display:column sortable="true" property="cancerActivity" title="CA"/>
		<display:column sortable="true" property="budgetStartDate" title="Budget Start Date" decorator="gov.nih.nci.iscs.numsix.greensheets.application.DateColumnDecorator" />
        <display:column sortable="true" property="specialist" title="Spec"/>
        <display:column sortable="true" property="backUpSpecialist" title="Bkup Spec"/>
        <display:column sortable="true" property="pd" title="PD"/>	
		<display:column sortable="true" property="pi" title="PI"/>
		<display:column sortable="true" property="programStatus" title="Pgm GS Status"/>
		<display:column property="programGreensheet" title=""/>
        <display:column property="programLockIcon" title="Pgm GS Lock"/>

		<display:column sortable="true" property="specialistStatus" title="GMS GS Status"/>
		<display:column property="specialistGreensheet" title=""/>
        <display:column property="specialistLockIcon" title="GMS GS Lock"/>

		<display:setProperty name="paging.banner.items_name" value="Grants"/>
        <display:setProperty name="paging.banner.item_name" value="Grant"/>
	    <display:setProperty name="paging.banner.placement" value="both"/>
	  	<display:setProperty name="paging.banner.some_items_found" value="<span>{0} {1} found, displaying Grants {2} to {3}.</span></br>" />
        <display:setProperty name="paging.banner.onepage" value=""/>
</display:table>
<html:form action="changelock">
  <input type="hidden" name="grantId" value=""/>
  <input type="hidden" name="applId" value=""/>
  <input type="hidden" name="groupType" value=""/>
</html:form> 
<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>