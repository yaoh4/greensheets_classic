//<script language="javascript">

function DisplayAlert(strMsg)
{
	if (strMsg == null)
		strMsg = "";
	alert(strMsg);
}

function IsObjectNull(obj)
{
	var bRetVal = true;
	if (obj != null)
	{
		bRetVal = false;

	}
	
	return bRetVal;
}

function IsStringEqual(strSource, strValue)
{
	var bRetVal = false;
	if (strSource == strValue)
	{
		bRetVal = true;
	}
	return bRetVal;
}

function IsBlankString(strSource)
{
	var bRetVal = false;
	if (strSource == "")
	{
		bRetVal = true;
	}
	return bRetVal;
}

function TrimString (str) 
{
	str = this != window? this : str;
	return str.replace(/^\s*/, '').replace(/\s*$/, ''); 
}

function GetFormElementValue(elElement)
{
	var strReturnValue = "";
	if (elElement != null)
	{
		strReturnValue = TrimString(elElement.value);
	}
	
	return strReturnValue;
}


function GetFormElementAttributeValue(frmForm, elElement, attributeName, strDefaultValue)
{
	var strReturnValue = "";
	if (elElement != null)
	{
		var strAtt = "document." + frmForm.name + "." + elElement.name + ".getAttribute('" + attributeName + "')";
		
		var strElementType = elElement.type;			
			
		if ( (strElementType != "radio") && (strElementType != "RADIO") && 
		            (strElementType != "checkbox") && (strElementType != "CHECKBOX"))
		{
			var objAttribute = eval(strAtt);	
			if (objAttribute != null)
			{			
				strReturnValue = TrimString(String(objAttribute));
			}		
			else
			{
				strReturnValue = strDefaultValue;
			}
		}
		else
		{		
			if (attributeName == "valType")
				strReturnValue = elElement.getAttribute('valType');
			if (attributeName == "valMandatory")
				strReturnValue = elElement.getAttribute('valMandatory');
			if (attributeName == "valContainsSpaces")
				strReturnValue = elElement.getAttribute('valContainsSpaces');
			if (attributeName == "valMinLength")
				strReturnValue = elElement.getAttribute('valMinLength');
			if (attributeName == "valMaxLength")
				strReturnValue = elElement.getAttribute('valMaxLength');
			if (attributeName == "displayName")
				strReturnValue = elElement.getAttribute('displayName');				
		}
	}

	return strReturnValue;
}


function SetFormElementValue(elElement, strValue)
{
	if (elElement != null)
	{
		elElement.value = strValue;
	}
}


function FormatSSNString(strSource)
{   
	var strReturnValue = FormatStringValue(strSource, "", 3, "-", 2, "-", 4);
	return strReturnValue;
}

/*
	The method below is a handy one for arbitrarily inserting formatting characters or delimiters of various kinds with the TargetString.
	It takes rone named argument, a string s, and any number of other arguments.  The other arguments must be integers or strings.  
	These other arguments specify how string s is to be reformatted and how and where other strings are to be inserted into it.
	It processes the other arguments in order one by one. 
		1. If the argument is an integer, reformat appends that number of sequential characters from s to the resultString.
		2. If the argument is a string, reformat appends the string to the resultString.
		
	NOTE:
		1.	The first argument after TARGETSTRING must be a string. (It can be empty.)  The second argument must be an integer. Thereafter, 
			integers and strings must alternate.  This is to provide backward compatibility to Navigator 2.0.2 JavaScript by avoiding use of 
			the typeof operator. 
		2.	It is the caller's responsibility to make sure that we do not try to copy more characters from s than s.length.
		
	EXAMPLE:
		1.  To reformat a 10-digit U.S. phone number from "1234567890" to "(123) 456-7890" make this function call:
			FormatStringValue("1234567890", "(", 3, ") ", 3, "-", 4)
			
		2.	To reformat a 9-digit U.S. Social Security number from "123456789" to "123-45-6789" make this function call:
			FormatStringValue("123456789", "", 3, "-", 2, "-", 4)
*/


function FormatStringValue(strString)
{   
	var arg;
	var sPos = 0;
	var resultString = "";

	for (var i = 1; i < FormatStringValue.arguments.length; i++) 
	{
		arg = FormatStringValue.arguments[i];
		if (i % 2 == 1) 
	 		resultString += arg;
		else 
		{
			resultString += strString.substring(sPos, sPos + arg);
			sPos += arg;
		}
	}
	
	return resultString;
}


// Methods used for the HTML help files.

var IMAGE_ARROW_DOWN = "ArrowDown.gif";
var IMAGE_ARROW_RIGHT = "ArrowRight.gif";

function doExpand(paraNum,arrowNum){
	//expand the paragraph and rotate the arrow; collapse and rotate it back
	if (paraNum.style.display=="none")
		{
			paraNum.style.display="";
			arrowNum.src= IMAGE_ARROW_DOWN;
		}
	else
	{
		paraNum.style.display="none";
		arrowNum.src= IMAGE_ARROW_RIGHT;
	}
}
