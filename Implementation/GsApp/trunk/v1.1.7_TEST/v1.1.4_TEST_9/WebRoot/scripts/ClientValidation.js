//<script language="javascript">

var strDisplayMessage = "";
var strDisplayName = "";
var MSG_INVALID_FORM = "Invalid form name specified. Please check the Form name and try again.";
var STRING_TRUE = "true";
var STRING_FALSE = "false";
var STRING_ZERO = "0";

var VAL_TYPE_STRING = "typeString";
var VAL_TYPE_NAME = "typeName";
var VAL_TYPE_ALPHA = "typeAlpha";
var VAL_TYPE_ALPHA_SPACE = "typeAlphaSpace";
var VAL_TYPE_NUMBER = "typeNumber";
var VAL_TYPE_NUMBER_SPACE = "typeNumberSpace";
var VAL_TYPE_ALPHANUM = "typeAlphaNumeric";
var VAL_TYPE_ALPHANUM_SPACE = "typeAlphaNumericSpace";
var VAL_TYPE_DATE = "typeDate";
var VAL_TYPE_DOB = "typeDob";
var VAL_TYPE_URL = "typeUrl";
var VAL_TYPE_EMAIL = "typeEmail";
var VAL_TYPE_SSN = "typeSsn";
var VAL_TYPE_RADIO = "typeRadio";
var VAL_TYPE_CHECKBOX = "typeCheckBox";
var VAL_TYPE_AT_LEAST_ONE = "typeAtLeastOne";

var STRING_DEFAULT_DISPLAY_NAME = "Question";
var MSG_NOT_BLANK = " may not be blank. Please specify a value.";
var MSG_CANNOT_CONTAIN_SPACES = " may not contain spaces.";
var MSG_VALIDATE_AS_NAME = " may only contain alphabets, digits, and the characters - period(.), underscore(_), single quotes.";
var MSG_ALPHABETS_ONLY = " can only contain alphabets.";
var MSG_ALPHABETS_SPACE_ONLY = " can only contain alphabets and spaces.";
var MSG_NUMBER_ONLY = " can only contain numbers and the symbols {'-', '+', '.' }. \nNote:\n1. Either '-' or '+' can appear only at the beginning of the value.\n";
var MSG_NUMBER_SPACE_ONLY = " can only contain numeric digits and spaces.";
var MSG_ALPHANUM_ONLY = " can only contain alphabets and numeric digits.";
var MSG_ALPHANUM_SPACE_ONLY = " can only contain alphabets, numeric digits and spaces.";

var MSG_DATE_INCORRECT_FORMAT = " must have the fomat - mm/dd/yyyy";
var MSG_DATE_MONTHS_1TO12 = " can only contain months between 1 and 12.";
var MSG_DATE_DATE_1TO31 = " can only contain days between 1 and 31.";
var MSG_DATE_MONTH_DOES_NOT_HAVE_31_DAYS = " does not have 31 days.";
var MSG_DATE_LEAP_YEAR = " refers to a Leap Year. Hence, February can contain a maximum of 29 days only.";
var MSG_DATE_FEBRUARY_CANNOT_CONTAIN_DAYS = ": February can not contain more than 28 days.";
var MSG_YEAR_CANNOT_BE_GREATER = ": Year cannot be greater than Current Year.";
var MSG_MONTH_CANNOT_BE_GREATER = ": Month cannot be greater than the Current Month.";
var MSG_DAY_CANNOT_BE_GREATER = ": Day cannot be greater than the Current Day.";

var MSG_INCORRECT_URL = " should begin with either 'http://' or 'https://' and end with a period (.) followed by 2 to 4 alphabets.";
var MSG_INCORRECT_EMAIL = " can only contain alphanumeric characters, @ and period(.) and should be of the form myname@mycompany.xx(x)."; 
var MSG_INCORRECT_SSN = " is not a valid SSN Number. It should be in the format - xxx-xx-xxxx or xxxxxxxxx .";

var MSG_SELECT_RADIO = " is mandatory. Please select an option."; 
var MSG_SELECT_DROPDOWN = " is mandatory. Please select an option.";
var MSG_SELECT_CHECKBOX = " is mandatory. Please select an option.";

var MSG_AT_LEAST_ONE_SHOULD_BE_FILLED = "At least one of the following fields should be filled:\n";

var allDataFormatsValidated = true;

///////////////////////////////////////////////////////////////////////////////////////
					

function ValidateFormValues(strFormName, bSubmitAction)
{
	var bRetVal = false;

	var frmForm = eval("document." + strFormName);	
	if (IsObjectNull(frmForm))
	{
		strDisplayMessage = MSG_INVALID_FORM;
		DisplayAlert(strDisplayMessage);
		return bRetVal;	
	}
	 
	var arrElements = frmForm.elements;
	var intErrorCounter = 0;
	var nIndex = 0;
	var nElementsLength = arrElements.length;
	var foundFalse = false;
	
	var elFirstElement = null;
	strDisplayMessage = "";
			
	var attValidate = null;
	
	for (nIndex = 0;nIndex < nElementsLength; nIndex++)
	{
	    strDisplayMessage = "";
		with (arrElements[nIndex])
		{
			attValidate	=	arrElements[nIndex].getAttribute("performValidation");

			if (attValidate != null)
			{
				if (IsStringEqual(attValidate, STRING_TRUE))
				{				
				    //get the span element that displays the quesion validate error icon
				    var spanElement = getFormElement("span_div_error_" + arrElements[nIndex].id);
					var imgElement = null;		    
					
					var bValidationResult = Validate(frmForm, arrElements[nIndex], false, bSubmitAction);	// Third parameter = true from the html page when checking for values on the fly. 

					if (!bValidationResult)
					{
						showDivElement(spanElement);	
					    imgElement = getFormElement("img_div_error_" + arrElements[nIndex].id);
						imgElement.setAttribute("title", strDisplayMessage);
						foundFalse = true;
										
					}
					else
					{
					    hideDivElement(spanElement);
					    imgElement = getFormElement("img_div_error_" + arrElements[nIndex].id);
					    imgElement.setAttribute("title","nothing here");
					    bRetVal = true;
					}

				}
			}
		}
	}
	
	//if a single element validateion is found to be false then return false
	
	if(foundFalse)
	{
	    bRetVal = false;
	}
	else
	{
	    bRetVal = true;
	}
	return bRetVal;
}



function Validate(frmForm, elElement,valueCheck, bSubmitAction)
{
    var bReturnValue = false;	
	
	elElement.value	=	GetFormElementValue(elElement);

	var strMessage = CheckValue(frmForm, elElement, bSubmitAction);
	
	if (strMessage == "")
	{
		bReturnValue = true;
        //allDataFormatsValidated = true;
	}
	else
	{
		strDisplayMessage += strMessage + "\n";
		if(valueCheck==true && elElement.value != ""){
		    alert("Error in Answer Format: " + strMessage);
		    bReturnValue = false;
		    
            //allDataFormatsValidated = false;
		    //elElement.value="";
		}
	}
	
	
	return bReturnValue;	
}

function CheckValue(frmForm, elElement, bSubmitAction)
{
	var strReturnValue = "";
	
	var strType			=	GetFormElementAttributeValue(frmForm, elElement, "valType",VAL_TYPE_STRING);
	var strMandatory		=	GetFormElementAttributeValue(frmForm, elElement, "valMandatory",STRING_FALSE);
		
	var strSpace			=	GetFormElementAttributeValue(frmForm, elElement, "valContainsSpaces",STRING_TRUE);
	var strMinLength		=	GetFormElementAttributeValue(frmForm, elElement, "valMinLength",STRING_ZERO);
	var strMaxLength		=	GetFormElementAttributeValue(frmForm, elElement, "valMaxLength",STRING_ZERO);
	
	strDisplayName			=	GetFormElementAttributeValue(frmForm, elElement, "displayName", STRING_DEFAULT_DISPLAY_NAME);
	
	
	if (IsBlankString(strDisplayName))
	{
		strDisplayName = STRING_DEFAULT_DISPLAY_NAME ;
	}
	else
	{
		if (!IsStringEqual(strType, VAL_TYPE_AT_LEAST_ONE))
			strDisplayName = "'" + strDisplayName + "'" ;
	}
	
	if (IsStringEqual(strMandatory, STRING_TRUE) &&  !IsStringEqual(strType, VAL_TYPE_RADIO) && !IsStringEqual(strType, VAL_TYPE_AT_LEAST_ONE))
	{
		if (bSubmitAction) // Check for an empty response ONLY if submitting the Form
		{		
			strReturnValue = IsMandatory(elElement);
		}
		else
		{
			strReturnValue = "";
		}
	}
	else
	{
		var strElementValue = GetFormElementValue(elElement);
		if ( !IsStringEqual(strType, VAL_TYPE_AT_LEAST_ONE) && !IsStringEqual(strType, VAL_TYPE_RADIO) )
		{
			if (IsBlankString(strElementValue))
			{
				return strReturnValue;
			}
		}		
	}
	
	
	if (IsBlankString(strReturnValue) && IsStringEqual(strMandatory, STRING_TRUE))
	{

		switch(strType)
		{
			case VAL_TYPE_STRING			:	strReturnValue = IsString(elElement, bSubmitAction);
												break;
										
			case VAL_TYPE_NAME				:	strReturnValue = IsValidName(elElement);
												break;
			
			case VAL_TYPE_ALPHA				:	strReturnValue = IsAlphabet(elElement);
												break;
			
			case VAL_TYPE_ALPHA_SPACE		:	strReturnValue = IsAlphabetWithSpace(elElement);
												break;

			case VAL_TYPE_NUMBER			:	strReturnValue = IsValidNumber(elElement);
												break;
			
			case VAL_TYPE_NUMBER_SPACE		:	strReturnValue = IsValidNumberWithSpace(elElement);
												break;

			case VAL_TYPE_ALPHANUM			:	strReturnValue = IsValidAlphaNumeric(elElement);
												break;

			case VAL_TYPE_ALPHANUM_SPACE	:	strReturnValue = IsValidAlphaNumericWithSpace(elElement);
												break;
			
			case VAL_TYPE_DATE				:	strReturnValue = IsValidDate(elElement);
												break;
			
			case VAL_TYPE_DOB				:	strReturnValue = IsValidDob(elElement);
												break;
			
			case VAL_TYPE_URL				:	strReturnValue = IsValidUrl(elElement);
												break;

			case VAL_TYPE_EMAIL				:	strReturnValue = IsValidEMail(elElement);
												break;

			case VAL_TYPE_SSN				:	strReturnValue = IsValidSsn(elElement);
												break; 

			case VAL_TYPE_RADIO				:	strReturnValue = IsRadioSelected(frmForm, elElement, bSubmitAction);												
												break;
												
			case VAL_TYPE_CHECKBOX			:	strReturnValue = IsCheckBoxSelected(frmForm, elElement, bSubmitAction);
												break;												
												

			case VAL_TYPE_AT_LEAST_ONE		:	strReturnValue = AtLeastOne(frmForm, elElement, bSubmitAction);
												break;
		}
	}
	
	// Check for Spaces
	if (IsBlankString(strReturnValue))
	{
		strReturnValue = CheckForSpaces(elElement, strSpace);
	}
	
	//Check for Min/Max. 
	if (IsBlankString(strReturnValue))
	{
		strReturnValue = IsWithinLimits(elElement, strMinLength, strMaxLength);
	}

	return strReturnValue;
}

function AtLeastOne(frmForm, elElement, bSubmitAction)
{
	var strReturnValue = "";
	var elementNamesValue = GetFormElementAttributeValue(frmForm, elElement, "elementNames",VAL_TYPE_STRING);
	var strValue = elElement.value;
	var bFilled = false;
	if (strValue != "")
	{
		bFilled = true;
		return strReturnValue;
	}

	var arrElementNames = null; 
	var nLength = null; 
	var nIndex = 0;
		
	if (!bFilled && (elementNamesValue != ""))
	{
		arrElementNames = elementNamesValue.split("-");
		nLength = arrElementNames.length;
		for(nIndex=0; nIndex<nLength; nIndex++)
		{
			var strElement = "document." + frmForm.name + "." + arrElementNames[nIndex];
			element = eval(strElement);
			if (element != null)
			{
				strValue = element.value;
				if (strValue != "")
				{
					bFilled = true;
					break;
				}
			}
		}
	}
	
	if (!bFilled & bSubmitAction)
	{
		strReturnValue = MSG_AT_LEAST_ONE_SHOULD_BE_FILLED;
		var displayNames = strDisplayName;		
		arrElementNames = displayNames.split("-");
		nLength = arrElementNames.length;
			
		for (nIndex=0; nIndex<nLength; nIndex++)
		{
			strReturnValue += "\n" + String(nIndex + 1) + ".   " + arrElementNames[nIndex];
		}		
	}
	
	return strReturnValue;
}



function IsCheckBoxSelected(frmForm, elElement, bSubmitAction)  
{ 
	var strReturnValue = ""; 
	var elName = elElement.getAttribute("name");
	
	var inputElementList = document.getElementsByTagName("INPUT");
	var nLength = inputElementList.length;
		
	var nIndex = 0;
	var bSelected = false;	
	
	for (nIndex = 0; nIndex<nLength; nIndex++)
	{	
	    if(inputElementList[nIndex].getAttribute("name") == elName)
	    {	        
	        //var val = inputElementList[nIndex].getAttribute("checked");
	        var val = inputElementList[nIndex].checked;
			if(val == true || val == "checked")
			{
				bSelected = true;
				break;
			}	        
	    }
	}
	
	if (!bSelected && bSubmitAction)
	{
		strReturnValue = strDisplayName + MSG_SELECT_CHECKBOX; 
	}
			
	return strReturnValue;
} 




function IsRadioSelected(frmForm, elElement, bSubmitAction)  
{ 
	var strReturnValue = ""; 
	var elName = elElement.getAttribute("name");
	

	var inputElementList = document.getElementsByTagName("INPUT");
	var nLength = inputElementList.length;
	
	
	
	var nIndex = 0;
	var bSelected = false;

	for (nIndex = 0; nIndex<nLength; nIndex++)
	{
	
	    if(inputElementList[nIndex].getAttribute("name") == elName)
	    {	        
	        //var val = inputElementList[nIndex].getAttribute("checked");
	        var val = inputElementList[nIndex].checked;
			if(val == true || val == "checked")
			{
				bSelected = true;
				break;
			}	        
	    }
	}
	
	// For single radio button responses
    if(nLength == undefined)
    {
	    //var val = elElement.getAttribute("checked");
	    var val = inputElementList[nIndex].checked;
		if(val == true || val == "checked")
		{
			bSelected = true;
		}	
	}
	
	if (!bSelected && bSubmitAction)
	{
		strReturnValue = strDisplayName + MSG_SELECT_RADIO; 
	}
		
	return strReturnValue;
} 

function IsValidNumber(elElement)
{
	var strReturnValue = "";
	var nValue = Number(GetFormElementValue(elElement));
	if (isNaN(nValue))
		strReturnValue = strDisplayName +  MSG_NUMBER_ONLY;
	
	return strReturnValue;
}

function IsValidSsn(elElement)
{
	var strReturnValue = "";
	
	var strValue = GetFormElementValue(elElement)
	var nValueLength = Number(strValue.length);
	var bValidSsn = true;
	
	var strExp = /\d{3}\-\d{2}\-\d{4}/;

	if (strValue.match(strExp) == null)
	{
		strValue = FormatSSNString(strValue);
		SetFormElementValue(elElement, strValue);
		return strReturnValue;
	}
	
	if (nValueLength > 9)
	{
		if (strValue.match(strExp) == null)
		{
			bValidSsn = false;
		}
	}
	else
	{
		if ( (nValueLength < 9) || ((nValueLength == 9)&&(IsValidNumber(elElement) != "")) )
		{
			bValidSsn = false;
		}
	}
	
	if (!bValidSsn)
	{		
		strReturnValue = strDisplayName + MSG_INCORRECT_SSN;
	}

	return strReturnValue;
}

function IsValidNumberWithSpace(elElement)
{
	var strReturnValue = "";
	var strValue = GetFormElementValue(elElement)
	var strExp = /[^0-9\s]/;	
	
	if (strValue.search(strExp) >= 0)
		strReturnValue = strDisplayName + MSG_NUMBER_SPACE_ONLY;
	
	return strReturnValue;
}

function IsValidAlphaNumeric(elElement)
{
	var strReturnValue = "";
	var strValue = GetFormElementValue(elElement)
	var strExp = /[^A-Za-z0-9]/;	
	
	if (strValue.search(strExp) >= 0)
		strReturnValue = strDisplayName + MSG_ALPHANUM_ONLY;
	
	return strReturnValue;
}

function IsValidAlphaNumericWithSpace(elElement)
{
	var strReturnValue = "";
	var strValue = GetFormElementValue(elElement)
	var strExp = /[^A-Za-z0-9\s]/;	
	
	if (strValue.search(strExp) >= 0)
		strReturnValue = strDisplayName + MSG_ALPHANUM_SPACE_ONLY;
	
	return strReturnValue;
}

function IsAlphabet(elElement)
{
	var strReturnValue = "";
	var strValue = GetFormElementValue(elElement);
	var strExp = /[^A-Za-z]/;	
	
	if (strValue.search(strExp) >= 0)
		strReturnValue = strDisplayName + MSG_ALPHABETS_ONLY;
	
	return strReturnValue;
}

function IsAlphabetWithSpace(elElement)
{
	var strReturnValue = "";
	var strValue = GetFormElementValue(elElement);
	var strExp = /[^A-Za-z\s]/;	
	
	if (strValue.search(strExp) >= 0)
		strReturnValue = strDisplayName + MSG_ALPHABETS_SPACE_ONLY;
	
	return strReturnValue;
}


function IsMandatory(elElement)  
{ 
	var strReturnValue = ""; 
	var strValue = GetFormElementValue(elElement);

	var strExp = /[\S]/; 
	if (strValue.search(strExp) < 0) 
	     strValue = ""; 
	     
	if (strValue == "") 
	{
		strReturnValue = strDisplayName + MSG_NOT_BLANK;
	}
	return strReturnValue; 
} 

function IsString(elElement, bSubmitAction)
{
	var strReturnValue = "";
	if( bSubmitAction && (elElement.tagName == "SELECT") && (elElement.value.indexOf("SEL_X_") > -1))
	{	    
	    strReturnValue = strDisplayName + MSG_SELECT_DROPDOWN; 
	}
	return strReturnValue;
}

function IsValidName(elElement)
{
	var strReturnValue = "";
	var strValue = GetFormElementValue(elElement);
	var strExp = /[^A-Za-z0-9_.'\s]/;	
	
	if (strValue.search(strExp) >= 0)
	{
		strReturnValue = strDisplayName + MSG_VALIDATE_AS_NAME; 
	}
	return strReturnValue;
}

function CheckForSpaces(elElement, strSpace)
{
	var strReturnValue = "";	
	if (!IsStringEqual(strSpace, STRING_TRUE))
	{
		if (IsSpacePresent(elElement))
		{
			strReturnValue = strDisplayName + MSG_CANNOT_CONTAIN_SPACES;
		}
	}
	return strReturnValue;
}

function IsSpacePresent(elElement)
{
	var bRetVal = false;
	
	var strValue = GetFormElementValue(elElement);
	var strExp = /[\s]/;
	if (strValue.search(strExp) >= 0)
	{
		// Space is present
		bRetVal = true;
	}
	return bRetVal;
}

function IsValidEMail(elElement)
{
	var strReturnValue	=	"";
	var strValue	= GetFormElementValue(elElement);
	var emailExp = /[a-z0-9_\-\.]+@[a-z0-9_\-\.]+\.[a-z]{2,3}$/i;
	
	var emailExp1 = /@/i;
		
	var bIncorrectFormat = false;
	if (strValue.search(emailExp1) == 0)
	{
		bIncorrectFormat = true;
	}

	if (strValue.search(emailExp) < 0)
	{
		bIncorrectFormat = true;
	}
	
	if (bIncorrectFormat)
	{
		strReturnValue	=	strDisplayName + MSG_INCORRECT_EMAIL;
	}
		
	return strReturnValue;
}	
		

function IsValidUrl(elElement)
{
  
	var strReturnValue	=	"";
	var strValue	= GetFormElementValue(elElement);

	var urlExp = /^(https|http):\/\/\S+\.+[A-Z]{2,4}$/i ; 
				
	if (!urlExp.test(strValue))
		strReturnValue = strDisplayName + MSG_INCORRECT_URL;
	
	return strReturnValue;
	
}	

function IsWithinLimits(elElement, strMinLength, strMaxLength)
{

	var strReturnValue = "";
	
	if (!IsBlankString(strMinLength) && !IsBlankString(strMaxLength))
	{
		var strValue		=	GetFormElementValue(elElement);
		var nLength			=	strValue.length;
		var intLowerLimit	=	Number(strMinLength);
		var intUpperLimit	=	Number(strMaxLength);
		
		if (intLowerLimit < 0)
		{
			intLowerLimit = 0;
		}
		
		if (intUpperLimit < 0)
		{
			intUpperLimit = 0;
		}
		
		if ( (intLowerLimit == 0) && (intUpperLimit == 0) )
		{
			return strReturnValue;
		}
		
		if (intLowerLimit > intUpperLimit)
		{
			var tempLimit = intUpperLimit;
			intUpperLimit = intLowerLimit;
			intLowerLimit = tempLimit;
		}
			
		if ((nLength < intLowerLimit) || (nLength > intUpperLimit))
		{
			if (intLowerLimit == intUpperLimit)
			{
				strReturnValue = strDisplayName + " should be " + String(intLowerLimit) + " characters in length.";
			}
			else
			{
				strReturnValue = strDisplayName + " should be within " + String(intLowerLimit) + " to " + String(intUpperLimit) + " characters in length.";
			}
		}
	}
	return strReturnValue;
}

function IsValidDate(elElement)
{
	var strReturnValue = IsDate(elElement, false);
	return strReturnValue;
}

function IsValidDob(elElement)
{
	var strReturnValue = IsDate(elElement, true);	
	return strReturnValue;	
}

function IsDate(elElement, bPerformDOBCheck)
{
	var strReturnValue = "";
	var dateStr = GetFormElementValue(elElement);
	//alert(dateStr);
	//allDataFormatsValidated = true;
    if(dateStr != ""){
		var datePat = /^(\d{2})(\/|-)(\d{2})\2(\d{4})$/;
		var matchArray = dateStr.match(datePat);
		if (matchArray == null) 
		{
			strReturnValue = strDisplayName + MSG_DATE_INCORRECT_FORMAT;
			return strReturnValue;
		}
	
	
	    month = matchArray[1]; 
		day = matchArray[3];
		year = matchArray[4];
	
		
		if (month < 1 || month > 12) 
		{
			strReturnValue = strDisplayName + MSG_DATE_MONTHS_1TO12;
			return strReturnValue;
		}
		if (day < 1 || day > 31) 
		{
			strReturnValue = strDisplayName + MSG_DATE_DATE_1TO31;
			return strReturnValue;
		}
		if ((month==4 || month==6 || month==9 || month==11) && day==31) 
		{
			strReturnValue = month + MSG_DATE_MONTH_DOES_NOT_HAVE_31_DAYS;
			return strReturnValue;
		}
		if (month == 2) 
		{ 
			var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
			if (!isleap && (day > 28) )
			{
				strReturnValue = strDisplayName + MSG_DATE_FEBRUARY_CANNOT_CONTAIN_DAYS;
				return strReturnValue;
				
			}
			
			if (isleap && (day > 29))
			{
				strReturnValue = strDisplayName + MSG_DATE_LEAP_YEAR;
				return strReturnValue;
			}
		}
		
	}
	return strReturnValue;
}

