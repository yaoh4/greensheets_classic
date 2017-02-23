<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"%>





<html>
<head>
<script language="javascript" src="./scripts/ClientSideMethods.js"></script>
<title>Greensheets</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="./stylesheets/otherappsmenu.css" type="text/css">
<link rel="stylesheet" href="./stylesheets/StyleSheet.css" type="text/css">
</head>
<body>
<table border="0" width="100%">
 
    <tr>
      <td width="100%"><!-- begin default header -->
        
        <link rel="stylesheet" href="./stylesheets/otherappsmenu.css" type="text/css">
        <script language="javascript" src="./scripts/ClientSideMethods.js"></script> 
        <script>
    function openHelp(){
    
         window.open('./help/GreensheetsPgmGuide.pdf');
    
}

function OnSubmitForm()
{

document.reviewDraftGreensheetsForm.action ="reviewDraftGreensheets.do?method=reviewDraft";
document.reviewDraftGreensheetsForm.submit();
}

function OnSubmitMechForm()
{

document.reviewDraftGreensheetsForm.action ="reviewDraftGreensheets.do?method=generateDynamicMechDraft";
document.reviewDraftGreensheetsForm.submit();
}
function rejectViewDraftClose()
{

	var bool = confirm("Are you sure you want to proceed? You are about to REJECT the Module for all Types and Mechanisms.");
        if (bool)
        {
           // document.GreensheetForm.method.value="close";
		   document.reviewDraftGreensheetsForm.action="rejectDraftGreensheets.do";  
    	    document.reviewDraftGreensheetsForm.submit();
   	    }
	
}

function promoteViewDraftClose()
{

	var bool = confirm("Are you sure you want to proceed? You are about to PROMOTE the Module for all Types and Mechanisms.");
        if (bool)
        {
           // document.GreensheetForm.method.value="close";
		   document.reviewDraftGreensheetsForm.action="promoteDraftGreensheets.do";  
    	    document.reviewDraftGreensheetsForm.submit();
   	    }
	
}
</script> 
        <!-- Version: 2.7.2 --> 
        <!-- Build: b06.07.2014 -->
     <%
    String userName = "";
%>	  
        <table width="100%">
          <tr>
				<td width="100%">
					<%@ include file="/jsp/common/GlobalHeader.jsp"%>
				</td>
			</tr>
			
        </table>
        
        <!-- end default header --></td>
    </tr>
    <tr>
      <td><div class="form_display">
	    <logic:notEqual name="isReviewModule" value = "1">
          <h2>Review DRAFT Greensheets</h2>
		  </logic:notEqual>
		  	<%if (request.getAttribute("selectedModule")!=null){%>
		    <logic:equal name="isReviewModule" value = "1">
          <h2><%=(String)request.getAttribute("selectedModule")%> Module Status</h2>
		  </logic:equal>
		  	 <%}%>
		  <!-- Start Error Message -->
<logic:empty name="draftAddDelMsg">
<logic:notEmpty name="draftDeletions">
<html:errors property="deletionList" />
<article>
<logic:iterate name="draftDeletions" id="draftDeletionsId">

<ul class="gs_type"> 
<li>
	<bean:write name="draftDeletionsId"/>
</li>
</ul>
</logic:iterate>
</article> 
</logic:notEmpty>
<logic:notEmpty name="draftAdditions">
<html:errors property="AdditionList" />
<article>
<logic:iterate name="draftAdditions" id="draftAdditionsId">
 <ul class="gs_type">
<li>
	<bean:write name="draftAdditionsId"/>
</li>
</ul>
</logic:iterate>
</article> 
</logic:notEmpty>
</logic:empty>
<logic:notEmpty name="draftAddDelMsg">
<html:errors property="AddList" />
<article>
<logic:iterate name="draftAdditions" id="draftAdditionsId">
 <ul class="gs_type">
<li>
	<bean:write name="draftAdditionsId"/>
</li>
</ul>
</logic:iterate>
</article>
<html:errors property="DelList" />
<article>
<logic:iterate name="draftDeletions" id="draftDeletionsId">

<ul class="gs_type"> 
<li>
	<bean:write name="draftDeletionsId"/>
</li>
</ul>
</logic:iterate>
</article> 

<html:errors property="AddDelMsg" />
</logic:notEmpty>

<logic:empty name="draftAdditions">
<html:errors property="NoAddOrDeletion" />

</logic:empty>
<!-- End Error Message -->
         <html:form action="reviewDraftGreensheetsButtonActions">
             <logic:notEqual name="isReviewModule" value = "1">
			<ul>
			<logic:notEmpty name="moduleNames">
              <li>
                <label style="width: 95px;text-align: right;display: inline-block;margin-right: 3px;">Module:</label>
                <html:select property="moduleName" onchange="OnSubmitForm();">
				 <html:options name="moduleNames" />
				</html:select>
              </li>
			   
			  </logic:notEmpty>
			 <logic:notEmpty name="draftTypes"> 
			 <li>
                <label style="width: 95px;text-align: right;display: inline-block;margin-right: 3px;">Display Options:</label>
                <html:select property="updateOption" onchange="OnSubmitForm();">
				 <html:options name="draftUpdateDropDown" />
				</html:select>
              </li>
              <li>
                <label style="width: 95px;text-align: right;display: inline-block;margin-right: 3px;">Type:</label>
               <html:select property="type" onchange="OnSubmitMechForm();">
			    <html:options name="draftTypes" /> 
				</html:select>
              </li>
			  </logic:notEmpty>
			   <logic:notEmpty name="draftMechs"> 
              <li>
                <label style="width: 95px;text-align: right;display: inline-block;margin-right: 3px;">Mechanism:</label>
               <html:select property="mechanism">
			    <html:options name="draftMechs"/> 
			
				</html:select>
              </li>
			  </logic:notEmpty>
            </ul>
         </logic:notEqual>
        </div>
        <logic:notEmpty name="moduleNames">
        <div class="controls"> 
		<logic:equal name="reviewDraftGreensheetsForm" property="displayViewGreensheetButton" value="true">
		<html:image page="/images/viewDraft.gif" property="image_view" alt="View Draft" onclick='javascript:retreieveDraftGreensheet();'/>
		  </logic:equal>
		  <logic:equal name="reviewDraftGreensheetsForm" property="displayReviewModuleStatusButton" value="true">
		<html:image page="/images/reviewModule.gif" property="image_review" alt="Review Module Status" />
		</logic:equal>
		<logic:equal name="reviewDraftGreensheetsForm" property="displayPromoteButton" value="true">
		<html:link href='javascript:promoteViewDraftClose();'>
	   <html:img page="/images/promoteModule.gif"  alt="promote" />
									</html:link>
		</logic:equal>
		<logic:equal name="reviewDraftGreensheetsForm" property="displayRejectButton" value="true">
		<html:link href='javascript:rejectViewDraftClose();'>
	   <html:img page="/images/rejectModule.gif"  alt="Reject" />
									</html:link>
		</logic:equal>
		</div>
		</logic:notEmpty>

		 <logic:equal name="isReviewModule" value = "1">
		  <div class="controls"> 
		<logic:equal name="reviewDraftGreensheetsForm" property="displayViewGreensheetButton" value="true">
		<html:image page="/images/viewDraft.gif" property="image_view" alt="View Draft" onclick='javascript:retreieveDraftGreensheet();'/>
		  </logic:equal>
		  <logic:equal name="reviewDraftGreensheetsForm" property="displayReviewModuleStatusButton" value="true">
		<html:image page="/images/reviewModule.gif" property="image_review" alt="Review Module Status" />
		</logic:equal>
		<%if((isSuperUser)||(user.getFormBuilderRole().equals(GsUserRole.GS_DA))){%>
		<logic:equal name="reviewDraftGreensheetsForm" property="displayPromoteButton" value="true">
		
		<html:link href='javascript:promoteViewDraftClose();'>
	   <html:img page="/images/promoteModule.gif"  alt="promote" />
									</html:link>
		</logic:equal>
		<%}%>
		<%if((isSuperUser)||(user.getFormBuilderRole().equals(GsUserRole.GS_DA))){%>
		<logic:equal name="reviewDraftGreensheetsForm" property="displayRejectButton" value="true">
		<!--<html:image page="/images/rejectModule.gif" property="image_reject" alt="Reject" />-->
		<html:link href='javascript:rejectViewDraftClose();'>
	   <html:img page="/images/rejectModule.gif"  alt="Reject" />
									</html:link>
		</logic:equal>
		<%}%>
		</div>
        </logic:equal>

        <div class="back">
		 <logic:equal name="isReviewModule" value = "1">
		<a href="javascript:OnSubmitForm();"> << Go Back</a> </div>
		</logic:equal>
		 <logic:notEqual name="isReviewModule" value = "1">
		<a href="<%=request.getContextPath()%>/retrievegrants.do"> << Go Back</a> </div>
		</logic:notEqual>
		<%if (request.getAttribute("selectedModule")!=null){%>
		 <html:hidden name="reviewDraftGreensheetsForm" property="selectedModuleName" value="<%=(String)request.getAttribute(\"selectedModule\")%>" />
		 <%}%>
        </html:form></td>
		
    </tr>
    <tr>
      <td colspan="2"><!-- start default footer include --> 
        
        <br>
        <script language="javascript">
function showAppInfo(act,event)
{
	if(act=='open'){
		m = window.open('/greensheets/jsp/common/AppInfo.jsp','appinfo','width=300,height=200,toolbar=no,titlebar=no', 'screenX='+event.clientX, 'screenY='+event.clientY);
	}
	else{
 		m.close();
	}
}
</script>
        
        <tr>
				<td colspan="2">
					<%@ include file="/jsp/common/GlobalFooter.jsp"%>
				</td>
			</tr>
          
        </table>
        
        <!-- end default footer include --></td>
    </tr>

</table>
</body>
</html>