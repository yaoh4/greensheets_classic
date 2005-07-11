<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean" %>


<script>

    var numAttachments = <%=request.getAttribute("NUM_OF_ATTACHMENTS")%>;


  function addAttach() {
  	var filename = document.attachmentsForm.fileAttachment.value;
  	//filename = filename .replace(/^\s+|\s+$/g, "");
	//document.attachmentsForm.fileAttachment.value = filename;
	
  	var fnArray = filename.split("\<%=java.io.File.separator%>");
  	var fileNameOnly = fnArray[fnArray.length-1];
  	fileNameOnly = fileNameOnly.replace(/^\s+|\s+$/g, "");

  	// Check for duplicate file names.
  	var bIsDuplicateFileName = false;
  	var allFileNames = document.attachmentsForm.allValidFileNames.value;
  	if ((allFileNames != null) && (allFileNames != ""))  
  	{
  		var arrFilenames = allFileNames.split("$");
  		if (arrFilenames != null) 
  		{
  			var countFiles = arrFilenames.length;
  			var index = 0;
  			for (index=0; index<countFiles; index++)
  			{
  				var existingFileName = arrFilenames[index];
  				existingFileName = existingFileName.replace(/^\s+|\s+$/g, "");
  				if(fileNameOnly == existingFileName)
  				{
  					bIsDuplicateFileName = true;
  				}  				
  			}  		
  		}  	
  	}
  	
  	if(bIsDuplicateFileName)
  	{
  		alert("A file with the specified name already exists. Please rename the file before attempting to attach.");
  	}
  	else
  	{
  		if(filename != '')
  		{
  			//var fileNameOnly = filename;
			//var fnArray = filename.split("\<%=java.io.File.separator%>");
  			//fileNameOnly = fnArray[fnArray.length-1];
			if (fileNameOnly.length >= 100)
			{
				alert("The name of the file you are attaching is exceeds 100 characters. Please rename the file and attach again");
			}
			else
			{
				document.attachmentsForm.method.value="attachFile";
  				document.attachmentsForm.submit();
				numAttachments ++;
			}
  		}
		else
		{
  			alert("Please select a file to attach.");
  		}
  	}  	
  }

/*
if(filename != ''){
				var fileNameOnly = filename;
  			var fnArray = filename.split("\<%=java.io.File.separator%>");
  			fileNameOnly = fnArray[fnArray.length-1];
				if (fileNameOnly.length >= 100) {	
					alert("The name of the file you are attaching is exceeds 100 characters. Please rename the file and attach again");
				}else{
  				document.attachmentsForm.method.value="attachFile";
  				document.attachmentsForm.submit();
        	numAttachments ++;
        }
  	}
  	else{
  		alert("Please select a file to attach.");
  	}

*/

  function removeAttach(fileMemId, filename) {

    var bool = confirm("Are you sure you want to delete the attachment '" + filename + "'?");
    if (bool) {
  		document.attachmentsForm.fileMemoryId.value = fileMemId;
  		document.attachmentsForm.method.value="deleteFile";
  		document.attachmentsForm.submit();
        numAttachments --;
     }	  	
	  	
  }
  
  function viewAttach(fileMemId) {
  		document.attachmentsForm.fileMemoryId.value = filename;
  		document.attachmentsForm.method.value="viewFile";
  		document.attachmentsForm.submit();
  }  

  function cancel() {
  		document.attachmentsForm.method.value="cancel";
  		document.attachmentsForm.submit();
  }

  function save() {
  		document.attachmentsForm.method.value="save";
  		document.attachmentsForm.submit();
        window.opener.setAttachmentsIcon('<%=(String)request.getAttribute("RESP_DEF_ID")%>',numAttachments);
  }
</script> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Greensheets</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="./stylesheets/GreensheetsStyleSheet.css" type="text/css" />
</head>
<body>
<%@ include file="/jsp/common/DialogHeader.jsp"%> 
<!-- begin body content -->
<html:form action="questionattachments" enctype="multipart/form-data">
	<html:hidden name="attachmentsForm" property="method"/>
	<html:hidden name="attachmentsForm" property="fileMemoryId"/>
	<html:hidden name="attachmentsForm" property="allValidFileNames" value="<%=(String)request.getAttribute(\"VALID_FILE_NAMES\")%>"/> 
	<html:hidden name="attachmentsForm" property="respDefId" value="<%=(String)request.getAttribute(\"RESP_DEF_ID\")%>"/> 
	<html:hidden name="attachmentsForm" property="formUid" value="<%=(String)request.getAttribute(\"FORM_UID\")%>"/>
    <logic:equal name="canEdit" value="true">
    <h2>New Attachment</h2>
	<p>
		File: <html:file property="fileAttachment"/>
	</p>
	<p>
		<a class="button" href="javascript:addAttach()">Add Attachment</a>
	</p>
    </logic:equal>
    <%if(request.getAttribute("FILE_SIZE_ERROR") != null){%>
    <h4><font color="#FF0000">The File you are attaching is too large. The file must be less than <%=request.getAttribute("FILE_SIZE_LIMIT")%></font></h4>
    <%}%>
	<br />
	<h2>Attachment File List</h2>
	<table width="100%" cellspacing="0" class="data">
		<tr> 
	    <th scope="col">&nbsp;</th>
	    <th scope="col">File Name</th>
	  </tr>
		<logic:iterate id="attachment" name="QA_PROXY" property="attachmentCollection">            
			<logic:equal name="attachment" property="statusValue" value="NEW">
				<tr>
					<td>
						<a href="javascript:viewAttach('<bean:write name="attachment" property="fileMemoryId"/>')"><img src="./images/IconView.gif" alt="View" width="15" height="15" border="0" /></a>
						<logic:equal name="canEdit" value="true">
	            				<a href="javascript:removeAttach('<bean:write name="attachment" property="fileMemoryId"/>', '<bean:write name="attachment" property="filename"/>')" ><img src="./images/IconDelete.gif" alt="Delete" width="15" height="15" border="0" /></a> 
	        				</logic:equal>	    
					</td>
					<td>
			  		  	<bean:write name="attachment" property="filename"/>
			  		  </td>
		<!--	  		  <td>
			  		  	<bean:write name="attachment" property="statusValue"/>
			  		  </td>
			  		  -->
				</tr>
			</logic:equal>    		
			<logic:equal name="attachment" property="statusValue" value="EXISTING">
				<tr>
					<td>
						<a href="javascript:viewAttach('<bean:write name="attachment" property="fileMemoryId"/>')"><img src="./images/IconView.gif" alt="View" width="15" height="15" border="0" /></a>
						<logic:equal name="canEdit" value="true">
	            				<a href="javascript:removeAttach('<bean:write name="attachment" property="fileMemoryId"/>', '<bean:write name="attachment" property="filename"/>')" ><img src="./images/IconDelete.gif" alt="Delete" width="15" height="15" border="0" /></a> 
	        				</logic:equal>	    
					</td>
					<td>
			  		  	<bean:write name="attachment" property="filename"/>
			  		  </td>
			  	<!--	  <td>
			  		  	<bean:write name="attachment" property="statusValue"/>
			  		  </td>-->
				</tr>
			</logic:equal>    			
	 	</logic:iterate> 
	</table>
	<p>
        <logic:equal name="canEdit" value="true">
		<a class="button" href="javascript:save()">Save</a>
		<a class="button" href="javascript:cancel()">Cancel</a>
        </logic:equal>
        <logic:equal name="canEdit" value="false">
        <a class="button" href="javascript:window.close()">Close</a>
        </logic:equal>
	</p>
</html:form>
<!-- end body content -->
<%@ include file="/jsp/common/GlobalFooter.jsp"%>
</body>
</html>