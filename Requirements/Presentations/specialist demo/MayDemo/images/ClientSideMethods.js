var strCollapseSubQuestions	=	"COLLAPSE_SUBQUESTIONS";
var strViewSubQuestions		=	"VIEW_SUBQUESTIONS";
var strViewSelectSubQuestions = "VIEW_SELECT_SUBQUESTIONS";
var strViewComment			=	"VIEW_COMMENT";
var strShowAllComments		=	"VIEW_ALL_COMMENTS";
var strHideAllComments		=	"HIDE_ALL_COMMENTS";
var strShowAllSubQuestions	=	"VIEW_ALL_SUBQUESTIONS";
var strHideAllSubQuestions	=	"HIDE_ALL_SUBQUESTIONS";
var strViewActionIcon		=	"VIEW_ACTION_ICON";
var strTriggerActionSubQuestion = "TRIGGER_ACTION_SUBQUESTION";

var strFormName					=	"document.GreensheetForm";

var ICON_COLLAPSED = 0;
var ICON_EXPANDED  = 1;
var ICON_COMMENT_CHECKED = 2;
var ICON_COMMENT_UNCHECKED = 3;

var altTextIconCollapsed	=	"View Sub Questions";
var altTextIconExpanded		=	"Collapse Sub Questions";

var idViewHideAllSubQuestions	=	"a_onViewHideAllSubQuestions";
var textViewAllSubQuestions		=	"View All Subquestions";
var textHideAllSubQuestions		=	"Hide All Subquestions";
var scriptViewAllSubQuestions	=	"javascript:onViewAllSubQuestions()";
var scriptHideAllSubQuestions	=	"javascript:onHideAllSubQuestions()";

var idViewHideAllComments	=	"a_onViewHideAllComments";
var textViewAllComments		=	"View All Comments";
var textHideAllComments		=	"Hide All Comments";
var scriptViewAllComments	=	"javascript:onViewAllComments()";
var scriptHideAllComments	=	"javascript:onHideAllComments()";

var idViewHideSubQuestions	=	"a_onViewHideSubQuestion";
var scriptViewSubQuestions	=	"javascript:onViewSubquestions()";
var scriptHideSubQuestions	=	"javascript:onHideSubquestions()";


var txtElementTypeComment = "comment";
var txtElementTypeSubquestion =	"subquestion";
var txtElementTypeQuestion = "question";


var tagName_Div			=	"div";
var tagName_Select		=	"select";
var tagName_Input		=	"input";
var tagName_Textarea	=	"textarea";
var tagName_Option		=	"option";

var attrName_Id			=	"id";
var attrName_Value		=	"value";
var attrName_Type		=	"type";
var attrName_Checked	=	"checked";
var attrName_Selected	=	"selected";

var attrName_DivParentId	=	"div_parent_id";
var attrName_DivType		=	"div_type";

var attrInputTypeValue_Text		=	"text";
var attrInputTypeValue_Radio	=	"radio";
var attrInputTypeValue_CheckBox	=	"checkbox";



function getGrantDetail(grantNum, applId, url){
    var urlString = url + "=" + applId;
    
    //var urlString = "https://cii-server5.nci.nih.gov:6443/yourgrants/jsp/GrantDetails.jsp?applId=" + applId;
    
    //var urlString = "<%=((java.util.Property)gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties.getProperty("CONFIG_PROPERTIES")).getProperty("url.grantdetailviewer")%>=" + applId;
    
    var toolbarDisplay = "toolbar=no";
    var statusbarDisplay = "status=yes";
    var scrollbarDisplay = "scrollbars=yes";
    var locationbarDisplay = "location=no";
    var menubarDisplay = "menubar=no";
    var directoryDisplay = "directories=no";
    var showMaximizeButton = "resizable=yes";
    var windowName = "_blank";  // display a new unnamed window every time.


    var displayString = toolbarDisplay + "," + showMaximizeButton + "," + statusbarDisplay + "," + scrollbarDisplay + "," + locationbarDisplay + "," + menubarDisplay + "," + directoryDisplay;
    window.open(urlString, windowName , displayString);
  }


function CancelAndRefresh() {
	var bool = confirm("You are leaving the current form. Unsaved changes will be lost. Click OK to continue");
    if (bool){
    	document.GreensheetForm.submit();
   	}
}

function fileAttachments(formUid,respDefId)
{

	var params = "?FORM_UID="+ formUid + "&RESP_DEF_ID=" + respDefId + "&method=findAttachments";
	var windowName = "Attachemtn"+ formUid + respDefId;
	var message = "You already have attachments for this greensheet open in another window. Please switch windows to that other window to continue to work on attachments for this greensheet.";

	myOpenWindow("/greensheets/questionattachments.do" + params,windowName,'',message);

}

var myOpenWindow = function(URL, windowName,displayString, message) {
    var playerUrl = URL;
	var myOpenedWindow= window.open("", windowName,displayString) ;
  
    if(myOpenedWindow.location == 'about:blank' ){
    	
    	myOpenedWindow.location = playerUrl ;
    }else{
    	alert(message);
    }
    myOpenedWindow.focus();
}

/*
function saveSubmitClose(selection)
{
    var pocCtrl = document.frmGrantInfo.pointOfContact;
    if(pocCtrl != null){
	    var poc = document.frmGrantInfo.pointOfContact.value;

	    if(poc != "" && poc != null)
	    {
		    document.GreensheetForm.POC.value = poc;
	    }
	}
	if(selection == "SAVE")
	{
        if(allDataFormatsValidated){
		  document.GreensheetForm.method.value="save";
		  document.GreensheetForm.submit();
        }else{
            alert("There are errors in the data format of one or more questions.");
        }

	}
	if(selection == "SUBMIT")
	{
		document.GreensheetForm.method.value="submit";
		var val = ValidateFormValues('GreensheetForm');
		if (val){
		    document.GreensheetForm.submit();
		}else{
		    alert("The form cannot be submitted because there are questions that require answers. The exclamation marks indicate questions that need answers"); 
		}
		return;
	}
	if(selection == "CLOSE")
	{
		var bool = confirm("You are leaving the current form. Unsaved changes will be lost. Click OK to continue");
        if (bool){
            document.GreensheetForm.method.value="close";
    	    document.GreensheetForm.submit();
   	    }
	}
}
*/
function disableAllLinks (){


	var links = document.getElementsByTagName("a");
	for(var i = 0; i<links.length; i++) {

	if(links[i].href== "javascript:saveSubmitClose('SAVE')" ){
	   links[i].href = "#";
	   links[i].style.background ="white";
	  }else if (links[i].href== "javascript:saveSubmitClose('SUBMIT')"){
	   links[i].href = "##";
	   links[i].style.background ="white";
	  }
	}
}

function saveSubmitClose(selection)
{
    var pocCtrl = document.frmGrantInfo.pointOfContact;
    if(pocCtrl != null){
	    var poc = document.frmGrantInfo.pointOfContact.value;

	    if(poc != "" && poc != null)
	    {
		    document.GreensheetForm.POC.value = poc;
	    }
	}
	
	if (selection == "SAVE")
	{
		var val = ValidateFormValues('GreensheetForm', false); // second parameter signifies whether the operation is SUBMIT or not. 
		if(val)
		{
			disableAllLinks();
			document.GreensheetForm.method.value="save";
			document.GreensheetForm.submit();
		}
		else
		{
			// Only reason is that the data format for some question is not right. 
			var strMessage = "The form cannot be saved since the data format for some answer(s) may not be correct. The exclamation marks indcate the answer(s) that are not correct. Please check the answer(s) and try saving the form again.\n";
			strMessage += "NOTE: Individual error messages are displayed as pop-up text when hovering over the exclamation marks with the mouse."; 
			alert(strMessage);
		}
	}
	
	if (selection == "SUBMIT")
	{
		var val = ValidateFormValues('GreensheetForm', true); // second parameter signifies whether the operation is SUBMIT or not. 
		if(val)
		{
			disableAllLinks();
			document.GreensheetForm.method.value="submit";
			document.GreensheetForm.submit();
			//alert("Submitting the form..actual code commented out.");
		}
		else
		{
			// Reasons - Data format may not be correct OR Not all questions have been answered.
			var strMessage = "The form cannot be submitted due to the following reason(s):\n";
			strMessage += "1. The data format for some answer(s) may not be correct.\n";
			strMessage += "2. Some questions require answers.\n";
			strMessage += "The exclamation marks indicate the answer(s) that are not correct. Please check the answer(s) and try submitting the form again.\n";
			strMessage += "NOTE: Individual error messages are displayed as pop-up text when hovering over the exclamation marks with the mouse.";
			alert(strMessage);
		}
	}
		
	if(selection == "CLOSE")
	{
		var bool = confirm("You are leaving the current form. Unsaved changes will be lost. Click OK to continue.");
        if (bool)
        {
            document.GreensheetForm.method.value="close";
    	    document.GreensheetForm.submit();
   	    }
	}
}

function changeLock(grantNum, applId, group){

	var bool = confirm("You are about to change the Greensheet Lock for this Grant. Click OK to continue");
    if (bool){
        document.changeLockForm.grantId.value=grantNum;
        document.changeLockForm.applId.value=applId; 
        document.changeLockForm.groupType.value=group;
        document.changeLockForm.submit();
    }  
    
}




function retreieveGreensheet(grantNum, applId, group, oracleId){

    var params = "?GRANT_ID="+ grantNum + "&APPL_ID=" + applId + "&GS_GROUP_TYPE=" + group + "&ORACLE_ID=" + oracleId;   
    var url = "/greensheets/retrievegreensheet.do" + params
    //var toolbarDisplay = "width=900, height=500, toolbar=no";
    var toolbarDisplay = "toolbar=no";
	var statusbarDisplay = "status=yes";
	var scrollbarDisplay = "scrollbars=yes";
	var locationbarDisplay = "location=no";
	var menubarDisplay = "menubar=no";
	var directoryDisplay = "directories=no";
	var showMaximizeButton = "resizable=yes";
	var windowName ="RetreieveGreensheet" + applId +group + oracleId;
	var message = "You already have this greensheet open in another window. Please switch windows to that other window to continue to work on this greensheet."; 

	var displayString = toolbarDisplay + "," + showMaximizeButton + "," + statusbarDisplay + "," + scrollbarDisplay + "," + locationbarDisplay + "," + menubarDisplay + "," + directoryDisplay;

	
	myOpenWindow(url,windowName,displayString,message);
	

}

function retreieveDraftGreensheet(){
	var type = document.reviewDraftGreensheetsForm.type.value;
	var mech = document.reviewDraftGreensheetsForm.mechanism.value;
	var moduleName = document.reviewDraftGreensheetsForm.moduleName.value;
    var params = "?TYPE="+ type + "&MECH=" + mech + "&MODULE_NAME=" + moduleName;   
    var url = "/greensheets/previewDraftGreensheets.do" + params
     //var toolbarDisplay = "width=900, height=500, toolbar=no";
    var toolbarDisplay = "toolbar=no";
	var statusbarDisplay = "status=yes";
	var scrollbarDisplay = "scrollbars=yes";
	var locationbarDisplay = "location=no";
	var menubarDisplay = "menubar=no";
	var directoryDisplay = "directories=no";
	var showMaximizeButton = "resizable=yes";
	var windowName ="RetreieveDraftGreensheet";
	var message = "You already have this greensheet open in another window. Please switch windows to that other window to continue to work on this greensheet."; 

	var displayString = toolbarDisplay + "," + showMaximizeButton + "," + statusbarDisplay + "," + scrollbarDisplay + "," + locationbarDisplay + "," + menubarDisplay + "," + directoryDisplay;

	
	myOpenWindow(url,windowName,displayString,message);
	

}

function printGreensheetForm(formUid, formName, grantNum)
{
        var params = "?FORM_UID="+ formUid + "&GRANT_NUMBER=" + grantNum + "&FORM_TITLE=" + formName;     
        openNewWindow("/greensheets/pdfgeneration.do" + params);
}

function openNewWindow(urlString) 
{
	var toolbarDisplay = "width=900, height=500, toolbar=no";
	var statusbarDisplay = "status=yes";
	var scrollbarDisplay = "scrollbars=yes";
	var locationbarDisplay = "location=no";
	var menubarDisplay = "menubar=no";
	var directoryDisplay = "directories=no";
	var showMaximizeButton = "resizable=yes";
	var windowName = "_blank";  // display a new unnamed window every time.


	var displayString = toolbarDisplay + "," + showMaximizeButton + "," + statusbarDisplay + "," + scrollbarDisplay + "," + locationbarDisplay + "," + menubarDisplay + "," + directoryDisplay;
	window.open(urlString, windowName , displayString);
}


function onViewAllSubQuestions()
{
	showHideAllSubQuestions(true);
}

function onHideAllSubQuestions()
{
	showHideAllSubQuestions(false);
}

function showHideAllSubQuestions(bViewAllSubQuestions)
{
	var lstAllDivElems	=	getChildDivTags(document);
	if (lstAllDivElems != null)
	{
		var lengthDivElements = getListLength(lstAllDivElems);
		if (lengthDivElements != 0)
		{
			var divElement		= null;
			var divElementType	= null;
			var nIndex = 0;

			for (nIndex=0; nIndex<lengthDivElements; nIndex++)
			{
				divElement		=	getListElement(lstAllDivElems, nIndex);
				divElementType	=	getDivElementType(divElement);
				
				if ( (divElement != null) && !isDivElementTypeComment(divElementType))
				{
					if (bViewAllSubQuestions)
						showQuestion(divElement);
					else
					{						
						if (isDivElementTypeSubquestion(divElementType))
							hideQuestion(divElement);
						else
							showQuestion(divElement);
							
						setIcon(getHtmlElementAttribute(divElement,attrName_Id), ICON_COLLAPSED);
					}
				}
			}
		}
		
		// Set the Text and new method call for the Option.
		setViewHideAllSubQuestions(bViewAllSubQuestions);
	}
}



function onViewAllComments()
{
	showHideAllComments(true);
}

function onHideAllComments()
{
	showHideAllComments(false);
}
 
function showHideAllComments(bViewAllComments)
{
	var lstAllDivElems	=	getChildDivTags(document);
	if (lstAllDivElems != null)
	{
		var nIndex = 0;
		var lengthDivElements = getListLength(lstAllDivElems);
		if (lengthDivElements != 0)
		{
			var divElement = null;
			var divElementType = null;
			for (nIndex=0; nIndex<lengthDivElements; nIndex++)
			{
				divElement		=	getListElement(lstAllDivElems, nIndex);
				divElementType	=	getDivElementType(divElement);
				
				if ( (divElement != null) && isDivElementTypeComment(divElementType))
				{
				
				    var cntrlName = divElement.getAttribute("name").split("div_")[1];
				
                    var elmComment = document.getElementById(cntrlName);
                    
					if (bViewAllComments){
					    if((elmComment.firstChild != null) && (elmComment.firstChild.nodeValue != "")){
						    showComment(divElement);
					    }
					}else{
						hideComment(divElement);
					}
				}
			}
			
		}
		
		// Set the Text and new method call for the Option.
		setViewHideAllComments(bViewAllComments);
	}
} 

function onViewSubQuestions(divElementId)
{
	onViewHideSubQuestions(divElementId, true);
}

function onHideSubQuestions(divElementId)
{
	onViewHideSubQuestions(divElementId, false);
}

function onViewHideSubQuestions(divElementId, bViewSubquestion)
{
	var divQuestionElement = getFormElement(divElementId);
	// This is the parent question div id. Get the Child divs for this Div element and set the display attribute to true.
	if (divQuestionElement != null)
	{
		var lstChildDivElements = getChildDivTags(divQuestionElement);
		var lengthQuestionElements = getListLength(lstChildDivElements);
		if (lengthQuestionElements != 0)
		{
			var divElement = null;
			var divElementType = null;
			var divParentId = null;
			var nIndex = 0;
			
			for (nIndex=0; nIndex<lengthQuestionElements; nIndex++)
			{
				divElement		=	getListElement(lstChildDivElements, nIndex);
				divElementType	=	getDivElementType(divElement);
				divParentId		=	getDivElementParentId(divElement);
				
				if ( (divElement != null) && isDivElementTypeSubquestion(divElementType) && (divParentId == divElementId))
				{
					if (bViewSubquestion)
					{
						showQuestion(divElement);
						setIcon(divElementId, ICON_EXPANDED);
					}
					else
					{
						hideQuestion(divElement);
						setIcon(divElementId, ICON_COLLAPSED);
					}
						
					var anchorQuestion = getFormElement("a_" + divElementId);
					if (anchorQuestion != null)
					{
						if (bViewSubquestion)
							anchorQuestion.href = "javascript:onHideSubQuestions('" + divElementId + "')";
						else
							anchorQuestion.href = "javascript:onViewSubQuestions('" + divElementId + "')";
					}
					
				}
			}
		}
	}
}

function processHtmlElement_Select(htmlElement)
{
	if (htmlElement != null)
		htmlElement.selectedIndex = 0;
}

function getHtmlElementAttribute(htmlElement, attributeName)
{
	var retValue = null;
	if (htmlElement != null)
	{
		try
		{
			retValue = htmlElement.getAttribute(attributeName); 
		}
		catch (e)
		{
		}
	}
	
	return retValue;
}

function processInputType_Text(htmlElement)
{
	if (htmlElement != null){
		htmlElement.value = "";	    					
	}
}

function processInputType_Radio(htmlElement)
{
	if (htmlElement != null)
	{
		var radioName = getHtmlElementAttribute(htmlElement,attrName_Id);


    	var inputElementList = document.getElementsByName(radioName);
    	var nLength = inputElementList.length;
    	   	
    	var nIndex = 0;
    
    	for (nIndex = 0; nIndex<nLength; nIndex++)
    	{
    	
    	    //if(inputElementList[nIndex].getAttribute("name") == radioName){
    	        
    	        var val = inputElementList[nIndex].checked;   	       
    	        if (val == true || val == "checked"){
    	            inputElementList[nIndex].checked=false;
    	        }

    	    //}
    
    	}

    }
}



function processInputType_CheckBox(htmlElement)
{
    processInputType_Radio(htmlElement);

}

function processHtmlElement_Input(htmlElement)
{
	if (htmlElement != null)
	{
		var inputType = getHtmlElementAttribute(htmlElement,attrName_Type);

		if (inputType != null)
		{
			switch(inputType)
			{

				case attrInputTypeValue_Text	:	processInputType_Text(htmlElement); break;
				case attrInputTypeValue_Radio	:	processInputType_Radio(htmlElement); break;
				case attrInputTypeValue_CheckBox:	processInputType_CheckBox(htmlElement); break;	
			}
		}
	}
}

function processHtmlElement_Textarea(htmlElement)
{
	if (htmlElement != null)
	{
		htmlElement.value = "";
		
		// TO-DO : If this textarea is a comment, reset the icon too. 
		displayCommentIcon(htmlElement);
		
	}
}

function processHtmlElement_Div(htmlElement, bHideElement)
{
	if (htmlElement != null)
	{
		var htmlElementId = getHtmlElementAttribute(htmlElement,attrName_Id);
		divElementType	=	getDivElementType(htmlElement);
		if (isDivElementTypeSubquestion(divElementType))
		{
			if (bHideElement)
			{
				htmlElement.style.display = "none";
				setIcon(htmlElementId, ICON_COLLAPSED);
			}
			else
			{
				htmlElement.style.display = "block";
				setIcon(htmlElementId, ICON_COLLAPSED);
			}
		}

		
		if (isDivElementTypeComment(divElementType))
		{
			htmlElement.style.display = "none";
			setCommentIcon("img_" + htmlElementId, ICON_COMMENT_UNCHECKED);
		}
	}
}

function clearHtmlElementValue(htmlElement, bHideElement)
{


	if (htmlElement.hasChildNodes())
	{
		var nIndex = 0;
		var childElement = null;
		var tagName = null;
		var nListCount = 0;
		var lstChildNodes = htmlElement.childNodes;
		if (lstChildNodes != null)
		{
			nListCount = lstChildNodes.length;
			for (nIndex=0; nIndex<nListCount; nIndex++)
			{
				childElement = lstChildNodes.item(nIndex);
				
				tagName = childElement.tagName;
				if ((tagName != null) && (tagName != "undefined") && (tagName != ""))
					tagName = tagName.toLowerCase();
					
				switch(tagName)
				{
					case tagName_Div		:	processHtmlElement_Div(childElement, bHideElement); 
                                                
                                                break;
					case tagName_Select	    :	processHtmlElement_Select(childElement);    
					                            break;
					case tagName_Input		:	processHtmlElement_Input(childElement); break;
					case tagName_Textarea	:	processHtmlElement_Textarea(childElement); break;
					case "span"				:	processSpanTag(childElement);break;
												
					default					:	break;
				}
				clearHtmlElementValue(childElement, !bHideElement);
                
			}
		}
	}
}

function processSpanTag(elm){
	var myRE = new RegExp("span_div_error_", "i");
	var strx = new String(elm.id);
	var res = strx.match(myRE);
	if(res != null && res.length > 0){
		hideDivElement(elm);
	}

}


function onViewSelectSubQuestions(divQuestionId, divSelectionId)
{
	var divQuestionElement = getFormElement(divQuestionId);
	if (divQuestionElement != null)
	{
		var lstChildDivElements = getChildDivTags(divQuestionElement);
		var lengthDivElements = getListLength(lstChildDivElements);
		if (lengthDivElements != 0)
		{
			var divElement = null;
			var divElementType = null;
			var divElementId = null;
			var divParentId = null;
			var nIndex = 0;
			var bSelectionFound = false;
			for (nIndex=0; nIndex<lengthDivElements; nIndex++)
			{
				divElement		=	getListElement(lstChildDivElements, nIndex);
				divElementType	=	getDivElementType(divElement);
				divParentId		=	getDivElementParentId(divElement);
				divElementId = getHtmlElementAttribute(divElement,attrName_Id);
				
				if ( (divElement!=null) && isDivElementTypeSubquestion(divElementType) && (divParentId == divQuestionId) )
				{
					// We are either showing or hiding the div element corresponding to a sub question. 
					// Perfect spot to clear the user response values.

						clearHtmlElementValue(divElement, false);					
					
					// If selection, display. Else hide.					
					if (divElementId == divSelectionId)
					{
						bSelectionFound = true;
						showDivElement(divElement);
					}
					else{
						hideDivElement(divElement);

					}

				}	
			}
			var divQuestionElementId = getHtmlElementAttribute(divQuestionElement,attrName_Id);
			
			if (!isDivElementTypeComment(divElementType))
			{
				if (bSelectionFound)
					setIcon(divQuestionElementId, ICON_EXPANDED);
				else
					setIcon(divQuestionElementId, ICON_COLLAPSED);
			}
		}
	}
}

function onSelectDropdownOption(questionId, controlId)
{

	var control = getFormElement(controlId);
	var optArray = control.options;
	var selopt = control.options[control.selectedIndex];
	var showId = selopt.getAttribute("target_selection_id");
	var bSelectionFound = false;
	var divElement = null;
	for(var i = 0; i<optArray.length; i++)
	{
		var tmpId = optArray[i].getAttribute("target_selection_id");
		if(tmpId != showId && tmpId != '')
		{
			divElement = getFormElement(tmpId);
			hideDivElement(divElement);
			
			// We are either showing or hiding the div element corresponding to a sub question. Perfect spot to clear the user response values.
			clearHtmlElementValue(divElement, false);
		}
		else 
		{
			bSelectionFound = true;
		}
	}
	
	showDivElement(getFormElement(showId));
	if (bSelectionFound)
		setIcon(questionId, ICON_EXPANDED);		
	else
		setIcon(questionId, ICON_COLLAPSED);
}

function getChildDivTags(object)
{
	var lstDivElements = null;
	if (object != null)
		lstDivElements = getElementList(object, tagName_Div);
		
	return lstDivElements;
}

function getElementList(object, tagName)
{
	var lstElements = null;
	if (object != null)
	{
		lstElements = object.getElementsByTagName(tagName);
	}
	return lstElements;
}

function getListLength(lst)
{
	var nLength = 0;
	if (lst != null)
		nLength = lst.length;
		
	return nLength;
}

function getListElement(lst, index)
{
	var objReturnValue = null;
	if (lst != null)
		objReturnValue = lst.item(index);
		
	return objReturnValue;
}

function getDivElementType(divElement)
{
	var objRetVal = getHtmlElementAttribute(divElement, attrName_DivType);
	return objRetVal;
}

function getDivElementParentId(divElement)
{
	var objRetVal = getHtmlElementAttribute(divElement, attrName_DivParentId);
	return objRetVal;
}


function isDivElementTypeComment(divElementType)
{
	var bRetVal = false;
	if (divElementType == txtElementTypeComment)
		bRetVal = true;
		
	return bRetVal;
}


function isDivElementTypeSubquestion(divElementType)
{
	var bRetVal = false;
	if (divElementType == txtElementTypeSubquestion)
		bRetVal = true;
		
	return bRetVal;
}


function isDivElementTypeQuestion(divElementType)
{
	var bRetVal = false;
	if (divElementType == txtElementTypeQuestion)
		bRetVal = true;
		
	return bRetVal;
}

function showQuestion(divElement)
{
	if (divElement != null)
	{
		setIcon(getHtmlElementAttribute(divElement,attrName_Id), ICON_EXPANDED);
		showDivElement(divElement);
	}
}

function hideQuestion(divElement)
{
	if (divElement != null)
	{
		setIcon(getHtmlElementAttribute(divElement,attrName_Id), ICON_COLLAPSED);
		hideDivElement(divElement);
	}
}

function showComment(divElement)
{
	showDivElement(divElement);
}

function hideComment(divElement)
{
	hideDivElement(divElement);
}

function onClickComment(divElementId)
{
	var divElement = getFormElement(divElementId);
	toggleDivElementDisplay(divElement);
}



function setMandatory(divElement, value){
	
	var lstChildDivElements = getChildDivTags(divElement);
	var lengthQuestionElements = getListLength(lstChildDivElements);
	
	if (lengthQuestionElements != 0)
	{
		var divChildElement = null;
		var attrQRespDef = null;
		
		for (nIndex=0; nIndex<lengthQuestionElements; nIndex++){
		    divChildElement = getListElement(lstChildDivElements,nIndex);
		    
		    if(isDivElementTypeQuestion(getDivElementType(divChildElement))){
		    
		        attrQRespDef = getHtmlElementAttribute(divElement,"ques_resp_def");
		        
		        var lstQRespDefsSelect = divChildElement.getElementsByTagName("SELECT");
		        var lstQRespDefsInput = divChildElement.getElementsByTagName("INPUT");
		        var lstQRespDefsRadio = divChildElement.getElementsByTagName("RADIO");
		        var lstQRespDefsTxtArea = divChildElement.getElementsByTagName("TEXTAREA");
		        
		        setMandatoryForControls(lstQRespDefsSelect,value);
                setMandatoryForControls(lstQRespDefsInput,value);
		        setMandatoryForControls(lstQRespDefsRadio,value);
		        setMandatoryForControls(lstQRespDefsTxtArea,value);
		        
		    }    
		
		}
	}
}


function setMandatoryForControls(controlList, value){

    var size = getListLength(controlList);
    var i = 0;
    for(i=0; i<size; i++){
        var controlElement = getListElement(controlList,i);
        var attrMandatory = getHtmlElementAttribute(controlElement, "valMandatory");
        controlElement.setAttribute("valMandatory",value);
    }
}



function toggleDivElementDisplay(divElement)
{
	var bShowElement = true;
	if (divElement.style.display == "block")
	{
		bShowElement = false;
	}
	showHideDivElement(divElement, bShowElement);
}

function showDivElement(divElement)
{
	showHideDivElement(divElement, true);
	// get all elementes under the div and for the controls set to valMand true
	setMandatory(divElement,"true");
}




function hideDivElement(divElement)
{
	showHideDivElement(divElement, false);
	// get all elements under the div and for the control set to valMand=sfalse
		setMandatory(divElement,"false");
}

function showHideDivElement(divElement, bShowElement)
{
	var displayText = "none";
	if (bShowElement)
	{
		displayText = "block";
	}
	if (divElement != null)
	{
		divElement.style.display = displayText;
	}
}

function getFormElement(elementId)
{
	var formElement = document.getElementById(elementId);
	return formElement;
}


function setIcon(elementId, iconType)
{
	var srcIcon = null;
	var altText = null;

	switch (iconType)
	{
		case ICON_COLLAPSED	:	srcIcon = imgSrcIconCollapsed;
								altText = altTextIconCollapsed;
								break;
								
		case ICON_EXPANDED	:	srcIcon	= imgSrcIconExpanded;
								altText	= altTextIconExpanded;
								break;
	}
	
	if (srcIcon != null)
	{
		var idImage = "img_" + elementId;
				
		if (document != null)
		{
			var imgElement = getFormElement(idImage);
			if ((imgElement != null) && (imgElement != 'undefined'))  
			{
				imgElement.src = srcIcon;
				imgElement.alt = altText;				
			}
		}		
	}
}

function displayCommentIcon(commentId)
{

	var srcIcon = ICON_COMMENT_UNCHECKED;
	var objTextarea = document.GreensheetForm.elements[commentId];
	if (objTextarea != null)
	{
		var textAreaValue = objTextarea.value;
	//	textAreaValue = textAreaValue.trim();
		if ((textAreaValue != null) && (textAreaValue != ""))
			srcIcon	=	ICON_COMMENT_CHECKED;
	}
	
	
	setCommentIcon(commentId, srcIcon);
}


function setCommentIcon(commentId, iconType)
{
	var srcIcon = null;
	switch (iconType)
	{
		case ICON_COMMENT_CHECKED	:	srcIcon = imgCommentIconChecked;
										break;
								
		case ICON_COMMENT_UNCHECKED	:	srcIcon	= imgCommentIconUnchecked;
										break;
	}
	
	if (srcIcon != null)
	{
		var imgElement = null;
		if (document != null)
		{
			imgElement = getFormElement("img_div_" + commentId);
			if ((imgElement != null) && (imgElement != 'undefined'))  
				imgElement.src = srcIcon;
			else
			{
				imgElement = getFormElement(commentId);
				if ((imgElement != null) && (imgElement != 'undefined'))  
					imgElement.src = srcIcon;
			}
		}		
	}
}



function setAttachmentsIcon(id, numOfAttachments)
{
    
    if(Number(numOfAttachments) > 0){
        imgElement = getFormElement("img_div_" + id);
		if ((imgElement != null) && (imgElement != 'undefined')){  
		    imgElement.src = "./images/IconAttachmentChecked.gif";
		}
	}
	if (Number(numOfAttachments) == 0) 
	{
        imgElement = getFormElement("img_div_" + id);
		if ((imgElement != null) && (imgElement != 'undefined')){  
		    imgElement.src = "./images/IconAttachment.gif";
		}	    
	}
}


function setViewHideAllSubQuestions(bViewAllSubQuestions)
{
	var scriptMethod = scriptViewAllSubQuestions;
	var displayText = textViewAllSubQuestions;
	
	if (bViewAllSubQuestions)
	{
		scriptMethod = scriptHideAllSubQuestions;
		displayText = textHideAllSubQuestions;
	}
	
	var anchorViewHideAllSubQuestions = getFormElement(idViewHideAllSubQuestions);
	if (anchorViewHideAllSubQuestions != null)
	{
		anchorViewHideAllSubQuestions.href = scriptMethod;
		anchorViewHideAllSubQuestions.innerHTML = displayText;		
	}
}

function setViewHideAllComments(bViewAllComments)
{
	var scriptMethod = scriptViewAllComments;
	var displayText = textViewAllComments;
	
	if (bViewAllComments)
	{
		scriptMethod = scriptHideAllComments;
		displayText = textHideAllComments;
	}
	
	var anchorViewHideAllComments = getFormElement(idViewHideAllComments);
	if (anchorViewHideAllComments != null)
	{
		anchorViewHideAllComments.href = scriptMethod;
		anchorViewHideAllComments.innerHTML = displayText;
		
	}
}

//-----------------------------------------------------------------------------------

function OnClickClearAll()
{

	var bool = confirm("Are you sure you want to clear all questions? Click OK to continue");
    if (bool){
	    clearHtmlElementValue(document.GreensheetForm);
	    setViewHideAllSubQuestions(false);
	    setViewHideAllComments(false);
        allDataFormatsValidated = true;
	}
	

function toggleDivAreaDisplay(divId, imgId)
	{		
		// Get the div element. 
		var divElement = document.getElementById(divId);	
		// If element is not null, toggle the display of the element.
		if( (divElement != null) && (divElement != 'undefined') )
		{
			//set up the variables.
			var displayText = "block";
			var imgSrc = "Open.gif";
			
			if(divElement.style.display == "block")
			{
				displayText = "none";
				imgSrc = "Closed.gif";			
			}

			// Show or hide the Div Area
			divElement.style.display = displayText;			
			
			// Change the icon 
			// First get the image element
			var imgElement = document.getElementById(imgId);
			if( (imgElement != null) && (imgElement != 'undefined') )
			{
				imgElement.src = imgSrc;	
			}
		
		}		
	}

	
}

