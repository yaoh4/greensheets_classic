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



function DoNothing()
{
}

function fileAttachments(respDefId)
{
	document.AttachFileForm.RESP_DEF_ID.value = respDefId;
	document.AttachFileForm.submit();

}

function saveSubmit(selection)
{

	var poc = document.frmGrantInfo.pointOfContact.value;

	if(poc != "" && poc != null)
	{

		document.GreensheetForm.POC = poc;
	}
	if(selection == "SAVE")
	{
		document.GreensheetForm.method.value="save";
	}
	if(selection == "SUMBIT")
	{
		document.GreensheetForm.method.value="submit";
	}
	document.GreensheetForm.submit();

}

function PrintGreensheetForm2()
{
	openNewWindow("/greensheets/jsp/PrintOptionsForm2.jsp");
	
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
					if (bViewAllComments)
						showComment(divElement);
					else
						hideComment(divElement);
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
	if (htmlElement != null)
		htmlElement.value = "";
}

function processInputType_Radio(htmlElement)
{
	if (htmlElement != null)
	{
		var radioName = getHtmlElementAttribute(htmlElement,attrName_Id);
		var controlArray = document.forms[0].elements[radioName];
		var controlRadio = null;
		if (controlArray != null)
		{
			var nIndex = 0;
			var nLength = controlArray.length;
			for (nIndex=0; nIndex<nLength; nIndex++)
			{
				controlRadio = controlArray.item(nIndex);
				if (controlRadio != null)
					controlRadio.checked = false;
			}
		}
	}
}

function processInputType_CheckBox(htmlElement)
{
	if (htmlElement != null)
	{
		var checkboxName = getHtmlElementAttribute(htmlElement,attrName_Id);
		var controlArray = document.forms[0].elements[checkboxName];
		var controlCheckBox = null;
		if (controlArray != null)
		{
			var nIndex = 0;
			var nLength = controlArray.length;
			for (nIndex=0; nIndex<nLength; nIndex++)
			{
				controlCheckBox = controlArray.item(nIndex);
				if (controlCheckBox != null)
					controlCheckBox.checked = false;
			}
		}
	}
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
					case tagName_Div		:	processHtmlElement_Div(childElement, bHideElement); break;
					case tagName_Select		:	processHtmlElement_Select(childElement); break;
					case tagName_Input		:	processHtmlElement_Input(childElement); break;
					case tagName_Textarea	:	processHtmlElement_Textarea(childElement); break;
					default					:	break;
				}
				clearHtmlElementValue(childElement, !bHideElement);
			}
		}
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
					else
						hideDivElement(divElement);
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
}

function hideDivElement(divElement)
{
	showHideDivElement(divElement, false);
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
	var objTextarea = document.forms[0].elements[commentId];
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
	clearHtmlElementValue(document.forms[0]);
	setViewHideAllSubQuestions(false);
	setViewHideAllComments(false);
}
