<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="html" uri="/WEB-INF/tlds/struts-html.tld" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
        <title>Change Current Fiscal Year : Greensheets</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/GreensheetsStyleSheet.css" type="text/css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/otherappsmenu.css" type="text/css" />
</head>

<%
    String appEnv = DbConnectionHelper.getDbEnvironment();
    String appVersion = application.getInitParameter("appVersion");
    String emailLink = "mailto:" + application.getInitParameter("commentsEmail") + "?subject=Greensheets";
    String urlWorkbench = ((Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES)).getProperty("url.Workbench");
    String urlImpac2 = ((Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES)).getProperty("url.Impac2");
%>

<body>
    <div style="float: right;">
        <p>
            <span style="padding: 0 3ex;">Env: <b><%=appEnv%></b></span>
            <span style="padding: 0 3ex;">Version: <b><%=appVersion%></b></span>
            <span style="padding: 0 0 0 3ex;"><a href="<%=emailLink%>">Send Comments</a></span>
        </p>
    </div>
    <img src="<%=request.getContextPath()%>/images/Logo_Greensheets.gif" alt="Greensheets" border="0" />
    <table width="100%" class="line">
		<tr>
		    <td></td>
		</tr>
    </table>        
    
    
    <div style="margin: 2em 1em;"> <!-- DIV just for line spacing -->
        <h1 class="TitlePrimary">Changing the current Fiscal Year</h1>
        
        <p style="font-size: 10pt;">For testing purposes, you can make the system act as if the current fiscal year is different
           than the actual fiscal year in progress right now. This can be helpful if the data in the 
           Development or Test database has not been refreshed from the Production database for a while. 
        </p>
        
        <html:form action="changeCurrFy">
            <label for="newFY" class="TitleTertiary">Specify the fiscal year:</label> <html:text property="newFY"/>
            
            <div style="margin-top: 1em;">
                <html:submit property="okButton" value="OK"/>
                <html:submit property="cancelButton" value="Cancel"/>
            </div>
        </html:form>
        
    </div>
    
    <%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>