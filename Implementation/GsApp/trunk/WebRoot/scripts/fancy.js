///////// JS theme file for FlatCalendarXP 7.0 /////////
// This file is totally configurable. You may remove all the comments in this file to shrink the download size.
// Since the plugins are loaded after theme config, sometimes we would redefine(override) some theme options there for convenience.
////////////////////////////////////////////////////////

// ---- PopCalendar Specific Options ----
var gsSplit="/";	// separator of date string, AT LEAST one char.
var giDatePos=1;	// date format sequence  0: D-M-Y ; 1: M-D-Y; 2: Y-M-D
var gbPadZero=true;	// whether to pad the digits with 0 in the left when less than 10.
var giMonthMode=0;	// month format 0: digits ; 1: full name from gMonths; >2: abbreviated month name in specified length.
var gbShortYear=false;   // year format   true: 2-digits; false: 4-digits
var gbAutoPos=true;	// enable auto-adpative positioning or not
var gbPopDown=true;	// true: pop the calendar below the dateCtrl; false: pop above if gbAutoPos is false.
var gbAutoClose=false;	// whether to close the calendar after selecting a date.
var gPosOffset=[0,0];	// Offsets used to adjust the pop-up postion, [leftOffset, topOffset].
var gbFixedPos=false;	// true: pop the calendar absolutely at gPosOffset; false: pop it relatively.

// ---- Common Options ----
var gMonths=["January","February","March","April","May","June","July","August","September","October","November","December"];
var gWeekDay=["Su","Mo","Tu","We","Th","Fr","Sa"];	// weekday caption from Sunday to Saturday

var gBegin=[1980,1,1];	// calendar date range begin from [Year,Month,Date]. Using gToday here will make it start from today.
var gEnd=[2030,12,31];	// calendar date range end at [Year,Month,Date]
var gsOutOfRange="Sorry, you may not go beyond the designated range!";	// out-of-date-range error message. If set to "", no alerts will popup on such error.
var guOutOfRange=null;	// the background image url for the out-range dates. e.g. "outrange.gif"

var gbEuroCal=true;	// true: ISO-8601 calendar layout - Monday is the 1st day of week; false: US layout - Sunday is the 1st day of week.

var gcCalBG="white";	// the background color of the outer calendar panel.
var guCalBG=null;	//  the background image url for the inner table.
var gcCalFrame="white";	// the background color of the inner table, showing as a frame.
var gsInnerTable="border=0 cellpadding=1 cellspacing=0";	// HTML tag properties of the inner <table> tag, which holds all the calendar cells.
var gsOuterTable=NN4?"border=1 cellpadding=3 cellspacing=0":"border=0 cellpadding=0 cellspacing=2";	// HTML tag properties of the outmost container <table> tag, which holds the top, middle and bottom sections.

var gbHideTop=false;	// true: hide the top section; false: show it according to the following settings
var giDCStyle=1;	// the style of month-controls in top section.	0: use predefined html dropdowns & gsNavPrev/Next; 1: use gsCalTitle & gsNavPrev/Next; 2: use only gsCalTitle;
var gsCalTitle="\"<a class='PopAnchor' href='javascript:void(0);' onclick='if(this.blur)this.blur();fPopMenu(this,2,event);return false;' HIDEFOCUS>�\"+gMonths[gCurMonth[1]-1]+\"<IMG  SRC='../images/down.gif' width=12 height=10 border=0></a> <a class='PopAnchor' href='javascript:void(0);' onclick='if(this.blur)this.blur();fPopMenu(this,1,event);return false;' HIDEFOCUS>�\"+gCurMonth[0]+\"<IMG SRC='../images/down.gif' width=12 height=10 border=0></a>\"";	// dynamic statement to be eval()'ed as the title when giDCStyle>0.
var gbDCSeq=true;	// (effective only when giDCStyle is 0) true: show month box before year box; false: vice-versa;
var gsYearInBox="i";	// dynamic statement to be eval()'ed as the text shown in the year box. e.g. "'A.D.'+i" will show "A.D.2001"
var gsNavPrev="<A class='PopAnchor' href='javascript:void(0)' onclick='if(this.blur)this.blur();fPrevMonth();return false;' onmouseover='return true;' HIDEFOCUS>�<IMG src='../images/left.gif' width=10 height=11 border=0>�</A> <A class='PopAnchor' href='javascript:void(0)' onclick='if(this.blur)this.blur();fNextMonth();return false;' onmouseover='return true;' HIDEFOCUS>�<IMG src='../images/right.gif' width=10 height=11 border=0>�</A>";	// the content of the left month navigator
var gsNavNext="<A class='PopAnchor' href='javascript:void(0)' onclick='fHideCal();return false;' onmouseover='return true;' HIDEFOCUS>�<IMG src='../images/close.gif' width=10 height=11 border=0>�</A>";	// the content of the right month navigator

var gbHideBottom=false;	// true: hide the bottom section; false: show it with gsBottom.
var gsBottom="<A class='Today' href='javascript:void(0)' onclick='if(this.blur)this.blur();if(!fSetDate(gToday[0],gToday[1],gToday[2]))alert(\"You may not pick this day!\");return false;' onmouseover='return true;' title='Go to today!'>Today is "+gd.toString().substring(0,3)+", "+gToday[2]+" "+gMonths[gToday[1]-1].substr(0,3)+" "+gToday[0]+"</A>";	// the content of the bottom section.

var giCellWidth=22;	// calendar cell width;
var giCellHeight=14;	// calendar cell height;
var giHeadHeight=14;	// calendar head row height;
var giWeekWidth=22;	// calendar week-number-column width;
var giHeadTop=0;	// calendar head row top offset;
var giWeekTop=0;	// calendar week-number-column top offset;

var gcCellBG="white";	// default background color of the cells. Use "" for transparent!!!
var gsCellHTML="";	// default HTML contents for days without any agenda, usually an image tag.
var guCellBGImg="";	// url of default background image for each calendar cell.
var gsAction=" ";	// default action to be eval()'ed on everyday except the days with agendas, which have their own actions defined in agendas.
var gsDays="dayNo";	// the dynamic statement to be eval()'ed into each day cell.

var giWeekCol=0;	// -1: disable week-number-column;  0~7: show week numbers at the designated column.
var gsWeekHead="#";	// the text shown in the table head of week-number-column.
var gsWeeks="weekNo";	// the dynamic statement to be eval()'ed into the week-number-column. e.g. "'week '+weekNo" will show "week 1", "week 2" ...

var gcWorkday="black";	// Workday font color
var gcSat="black";	// Saturday font color
var gcSatBG=null;	// Saturday background color
var gcSun="black";	// Sunday font color
var gcSunBG=null;	// Sunday background color

var gcOtherDay="gainsboro";	// the font color of days in other months; It's of no use when giShowOther is set to hide.
var gcOtherDayBG=gcCellBG;	// the background color of days in other months. when giShowOther set to hiding, it'll substitute the gcOtherDay.
var giShowOther=2;	// control the look of days in OTHER months. 1: show date & agendas effects; 2: show selected & today effects; 4: hide days in previous month; 8: hide days in next month; 16: when set with 4 and/or 8, the days will be visible but not selectable.  NOTE: values can be added up to create mixed effects.

var gbFocus=false;	// whether to enable the gcToggle highlight whenever mouse pointer focuses over a calendar cell.
var gcToggle="#D4D0C8";	// the highlight color for the focused cell

var gcFGToday="#239950";	// the font color for today 
var gcBGToday="#239950";	// the background (border) color for today 
var guTodayBGImg="";	// url of image as today's background
var giMarkToday=2; // Effects for today - 0: nothing; 1: set background color with gcBGToday; 2: draw a box with gcBGToday; 4: bold the font; 8: set font color with gcFGToday; 16: set background image with guTodayBGImg; - they can be added up to create mixed effects.

var gcFGSelected="white";	// the font color for the selected date
var gcBGSelected="#a0a0a0";	// the background color for the selected date
var guSelectedBGImg="../images/selected.gif";	// url of image as background of the selected date
var giMarkSelected=4+8+16;	// Effects for selected date - 0: nothing; 1: set background color with gcBGSelected; 2: draw a box with gcBGSelected; 4: bold the font; 8: set font color with gcFGSelected; 16: set background image with guSelectedBGImg; - they can be added up to create mixed effects.

var gbBoldAgenda=true;	// whether to boldface the dates with agendas.
var gbInvertBold=false;	// true: invert the boldface effect set by gbBoldAgenda; false: no inverts.
var gbShrink2fit=false;	// whether to hide the week line if none of its day belongs to the current month.
var gdSelect=[0,0,0];	// default selected date in format of [year, month, day]; [0,0,0] means no default date selected.
var giFreeDiv=1;	// The number of absolutely positioned layers you want to customize, they will be named as "freeDiv0", "freeDiv1"...
var gAgendaMask=[-1,-1,gcCellBG,-1,null,false,null];	// Set the relevant bit to -1 to keep the original agenda info of that bit unchanged, otherwise the new value will substitute the one defined in agenda.js.

var giResizeDelay=KO3?150:50;	// delay in milliseconds before resizing the calendar panel. Calendar may have incorrect initial size if this value is too small.
var gbFlatBorder=false;	// flat the .CalCell css border of any agenda date by setting it to solid style. NOTE: it should always be set to false if .CalCell has no explicit border size.
var gbInvertBorder=false;	// true: invert the effect caused by gbFlatBorder; false: no change.
var gbShareAgenda=false;	// if set to true, a global agenda store will be created and used to share across calendars. Check tutorials for details.

