<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>

<script>
//window.resizeTo(850,450);
</script>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%>	

<!--Start Print Options Form-->
<html:form action="getgreensheetpdf">
<input type="hidden" name="FORM_UID" value="<%=request.getParameter("FORM_UID")%>"/>
<h1>PDF Options</h1>	
<p>	
	Grant Number: <%=request.getParameter("GRANT_NUMBER")%><br/>
	Greensheet: <%=request.getParameter("FORM_TITLE")%>	
</p>
<table cellspacing="3">
	<tr>
  	<td class="align1"><strong>Display Questions:</strong></td>
    <td>
			<input type="radio" id="generateAllQuestions"  name="generateAllQuestions" checked="checked" value="NO">
			Main and all Answered Subquestions<br />
			<input type="radio" id="generateAllQuestions" name="generateAllQuestions" value="YES">
			All Questions for the Greensheet Form	
		</td>
	</tr>
	<tr>
		<td class="align1"><strong>Questions/Comments:</strong></td>
		<td> 
			<input type="radio" id="commentsDisplayOption"  name="commentsDisplayOption" checked="checked" value="NO">
			Questions Only (No Comments)<br />
			<input type="radio" id="commentsDisplayOption" name="commentsDisplayOption" value="SHOW_WITH_QUESTION">
			Questions and Comments Together<br />
			<input type="radio" id="commentsDisplayOption" name="commentsDisplayOption" value="SHOW_SEPARATE">
			Questions and Comments Separate
		</td>
	</tr>
</table>
<p>	
	<a class="button" href="javascript:document.pdfOptionsForm.submit();">Print Preview</a>
	<a class="button" href="javascript:window.close()">Close</a>
</p>
</html:form>
<!-- End Print Options Form -->

<%@ include file="/jsp/common/DialogFooter.jsp"%>
</body>
</html>