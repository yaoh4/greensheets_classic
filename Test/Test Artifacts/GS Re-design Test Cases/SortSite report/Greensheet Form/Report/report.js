/*
	Developed by:
		PowerMapper Software Limited
		Quartermile Two
		2 Lister Square
		Edinburgh EH3 9GL
		Scotland

	Copyright PowerMapper Software Limited 2007-2013
	
	NOTE: this file isn't used directly: the minified version of this file
	is referenced instead. If you add/remove any functions or change function
	interfaces you must create a new minified version based on current date.
*/

var categoryArraySize = 9;
var categories = new Array(categoryArraySize) 
categories[0]="overallQuality"; 
categories[1]="errors"; 
categories[2]="compliance"; 
categories[3]="standards"; 
categories[4]="accessibility"; 
categories[5]="usability"; 
categories[6]="seo"; 
categories[7]="security"; 
categories[8]="compatibility"; 

function pageLoad()
{
	// Maintain the rules that the user had expanded
	expandRules();

	enableRules();
	enableSpellings();

	fixupOptions();
}

function pageLoadDashboard()
{
	pageLoad();
	showAllBenchmark();
}


function fixupOptions()
{
	for ( var i = 0 ; i < document.links.length ; ++i )
	{
		var oLink = document.links[i];

		if ( oLink.onclick && oLink.onclick.toString().indexOf( "Options(" ) > 0 )
		{
			oLink.oncontextmenu = function() { this.click(); return false; }
		}
	}
}

function expandRules()
{



	var expandedRuleArray = document.getElementById("expandedRuleList").value.split('#');

	if (expandedRuleArray[0] != "")
	{
		// Check if the expanded rule is already in the list
		for (var i=0; i<expandedRuleArray.length; i++)
		{

			toggleChevron(expandedRuleArray[i]);
		}
	}
}

function removeExpandedRule(id)
{
	var expandedRuleArray = document.getElementById("expandedRuleList").value.split('#');

	// Clear the list of expanded rules
	document.getElementById("expandedRuleList").value = "";

	if (expandedRuleArray[0] != "")
	{
		for (var i=0; i<expandedRuleArray.length; i++)
		{
			if (expandedRuleArray[i] != id)
			{
				addExpandedRule(expandedRuleArray[i]);
			}
		}
	}
}

function expandAll()
{
	var bShow = $( '#chevExpandAll' ).attr( 'src' ).indexOf( "chevron-down.png" ) != -1;

	if ( bShow )
		$( 'tbody.expando' ).show();
	else
		$( 'tbody.expando' ).hide();
	
	
	var images = document.getElementsByTagName("img");

	for ( var i = 0 ; i < images.length ; ++i )
	{
		var imgId = images[i].id;

		if ( imgId.indexOf( "chev" ) == 0 )
		{
			if ( bShow )
				images[i].src = "Report/chevron-up.png";
			else
				images[i].src = "Report/chevron-down.png";
		}
	}


	return false;
}

function addExpandedRule(id)
{
	var expandedRuleArray = document.getElementById("expandedRuleList").value.split('#');

	if (expandedRuleArray[0] == "")
	{
		document.getElementById("expandedRuleList").value = id;
	}
	else
	{
		var blnExistsInList = false;


		// Check if the expanded rule is already in the list
		for (var i=0; i<expandedRuleArray.length; i++)
		{
			if (expandedRuleArray[i] == id)
			{
				blnExistsInList = true;
				break;
			}
		}


		if (!blnExistsInList)
		{
			document.getElementById("expandedRuleList").value = document.getElementById("expandedRuleList").value + "#" + id;
		}
	}
}

function showTab( navName, subnavName )
{
	document.getElementById( "menu-summary" ).className = '';
	document.getElementById( "menu-issues" ).className = '';
	document.getElementById( "menu-all" ).className = '';
	document.getElementById( "menu-" + navName ).className = 'selected';

	document.getElementById( "tab-summary" ).style.display = 'none';
	document.getElementById( "tab-all" ).style.display = 'none';
	document.getElementById( "tab-issues" ).style.display = 'none';

	document.getElementById( "tab-" + navName ).style.display = 'inline';

	document.getElementById( "tab-errors" ).style.display = 'none';
	document.getElementById( "tab-compliance" ).style.display = 'none';
	document.getElementById( "tab-standards" ).style.display = 'none';
	document.getElementById( "tab-accessibility" ).style.display = 'none';
	document.getElementById( "tab-usability" ).style.display = 'none';
	document.getElementById( "tab-seo" ).style.display = 'none';
	document.getElementById( "tab-compatibility" ).style.display = 'none';
	document.getElementById( "tab-security" ).style.display = 'none';

	if ( subnavName )
	{
		document.getElementById( "menu-errors" ).className = '';
		document.getElementById( "menu-compliance" ).className = '';
		document.getElementById( "menu-standards" ).className = '';
		document.getElementById( "menu-accessibility" ).className = '';
		document.getElementById( "menu-usability" ).className = '';
		document.getElementById( "menu-seo" ).className = '';
		document.getElementById( "menu-compatibility" ).className = '';
		document.getElementById( "menu-security" ).className = '';

		document.getElementById( "tab-" + subnavName ).style.display = 'inline';
		document.getElementById( "menu-" + subnavName  ).className = 'selected';
	}
}


function toggleChevron( rowid )
{
	var oElement = document.getElementById( "chev" + rowid );

	if ( oElement.src.indexOf( "chevron-up.png" ) != -1 )
	{
		oElement.src = "Report\\chevron-down.png";
		removeExpandedRule(rowid);
	}
	else
	{
		oElement.src = "Report\\chevron-up.png";
		addExpandedRule(rowid);
	}

	$( '#expand-' + rowid ).toggle();
	
	return false;
}

function toggleVisibility( id )
{					
	var pos = 0;					

	var oElement = document.getElementById( "row-" + id + "-" + pos );					

	var strVisible = 'table-row';

	if ( navigator.userAgent.indexOf( "compatible; MSIE" ) > 0 )
		strVisible = 'inline';

	do
	{
		if ( oElement == null )
			; /* may be null for pos = 0 (used for too many issues message) */
		else if ( oElement.style.display == 'none' )
			oElement.style.display = strVisible;
		else
			oElement.style.display = 'none';

		++pos;
		oElement = document.getElementById( "row-" + id + "-" + pos );
	}
	while ( oElement != null );
}

function enableRules()
{
	var oRows = document.getElementsByTagName('tr');

	//alert( "enableRules: rows=" + oRows.length );

	for ( i = 0 ; i < oRows.length ; ++i )
	{
		var	oElement = oRows[i];

		if ( oElement.id && oElement.id.indexOf( "rule-" ) == 0  )
		{

			try
			{
				var ruleId = oElement.id.substr( 5 );
				var bEnabled = window.external.GetRuleEnabled( ruleId );

				if ( bEnabled )
					oElement.className = '';
				else
					oElement.className = 'disabled';
			}
			catch(err)
			{
				//Do nothing
			}
		}
	}
}

function enableSpellings()
{
	var oRows = document.getElementsByTagName('a');

	for ( i = 0 ; i < oRows.length ; ++i )
	{
		var	oElement = oRows[i];

		if ( oElement.className == "spell-bad"  )
		{

			try
			{
				if ( oElement.innerText )
				{
					var strWord = oElement.innerText;

					var bCorrect = window.external.GetSpellingCorrect( strWord );

					if ( bCorrect )
						oElement.className = 'spell-good';
				}
			}
			catch(err)
			{
				//Do nothing
			}
		}
	}
}

function spellOptions( oLink, strWord, strAppEdition )
{
	SpellAction = {
		spellOther : 0,
		spellAdded : 1,
		spellRemoved : 2
	};
	
	try
	{
		var action = SpellAction.spellOther;
		
		
		if ( strAppEdition.indexOf( "web" ) != -1 )
		{
			// add to custom dictionary
			var urlAdd = '/Scans/AddCustomSpelling?word=' + strWord;
			var urlRemove = '/Scans/RemoveCustomSpelling?word=' + strWord;
			
			if ( $( '#iframeSpelling' ).attr('src') == urlAdd )
			{
				// clicking word again removes it from dictionary
				action = SpellAction.spellRemoved;	
				$( '#iframeSpelling' ).attr('src', urlRemove );
			}
			else
			{
				action = SpellAction.spellAdded;
				$( '#iframeSpelling' ).attr('src', urlAdd );
			}
		}
		else
		{
			// popup menu
			action = window.external.SpellOptions( strWord );
		}

		if ( action == SpellAction.spellAdded )
		{
			// mark this one as good
			oLink.className = 'spell-good';

			// mark all instances of this word as good
			for (var i = 0; i < document.links.length; i++)
			{
				var	oAnchor = document.links[i];

				if ( oAnchor.className == 'spell-bad' && oAnchor.innerHTML == strWord )
				{
					oAnchor.className = 'spell-good';
				}
			}
		}
		else if ( action == SpellAction.spellRemoved )
		{
			// mark this one as bad again
			oLink.className = 'spell-bad';

			// mark all instances of this word as good
			for (var i = 0; i < document.links.length; i++)
			{
				var	oAnchor = document.links[i];

				if ( oAnchor.className == 'spell-good' && oAnchor.innerHTML == strWord )
				{
					oAnchor.className = 'spell-bad';
				}
			}
		}
	}
	catch( err )
	{
		// don't warn about this in exported reports
		//alert( "Spelling options are only available when the report is viewed in SortSite." );
	}

	// don't jump around or scroll after showing options menu
	return false;
}

function ruleOptions( element, ruleId )
{
	try
	{
		var bEnabled = window.external.RuleOptions( element, ruleId );
		var oElement = document.getElementById( "rule-" + ruleId );

		if ( bEnabled )
			oElement.className = '';
		else
			oElement.className = 'disabled';
			
		$( "#rule-" + ruleId + "-disabled" ).toggle();
	}
	catch( err )
	{
		alert( "Options are only available when the report is viewed in SortSite." );
	}

	// don't jump around or scroll after showing options menu
	return false;
}

function pageOptions( element, url )
{
	try
	{
		bEnabled = window.external.PageOptions( element, url );
	}
	catch( err )
	{
		alert( "Options are only available when the report is viewed in SortSite." );
	}

	// don't jump around or scroll after showing options menu
	return false;
}

function viewSource( pageid, line, col )
{	
	try
	{
		if ( g_viewSourceDir != '' )
		{
			window.location.href = g_viewSourceDir + pageid + ".view.htm#htm-l" + line;
		}
		else
		{
			window.external.ViewSource( pageid, line, col );
		}
	}
	catch( err )
	{
		if ( g_errViewSource )
			alert( g_errViewSource );
	}
}

function hideRuleHelp()
{
	var oHelpPane = document.getElementById( "HelpPane" );
	oHelpPane.style.display = "none";
}

function applyFilter( object )
{
	//alert( "applyFilter: in" );
	//alert( object.selectedIndex );
	//alert( object.options[object.selectedIndex].value );
	eval( object.options[object.selectedIndex].value );
}

var	g_strRefNameFilter;
var	g_priorityFilter;
var	g_oRow;

function filterGuidelines( strRefName, priority )
{
	//alert( "filterGuidelines: in " + strRefName + " " + priority );
	var	rows = document.getElementsByTagName("tr"); 

	g_strRefNameFilter = strRefName;
	g_priorityFilter = priority;

	//alert( rows.length );

	for( var i = 0 ; i < rows.length ; ++i )
	{
		var	funcFilterMatch = rows[i].getAttribute("onkeypress");

		if ( funcFilterMatch )
		{
			g_oRow = rows[i];
			funcFilterMatch.call();
			g_oRow = null;
		}
	}
}

function filterMatch( strRefNames, priority )
{
	//alert( "filterMatch: in " + strRefNames + " " + priority );
	//alert( "filterMatch: filter " + g_strRefNameFilter + " " + priority );

	if ( g_oRow )
	{
		var strVisible = 'table-row';
		var aCategories = strRefNames.split( ',' );

		if ( navigator.userAgent.indexOf( "compatible; MSIE" ) > 0 )
			strVisible = 'inline';

		if ( g_strRefNameFilter == 'All' )
		{
			g_oRow.style.display = strVisible;
		}
		else if ( strRefNames.indexOf( g_strRefNameFilter ) != -1 )
		{

			g_oRow.style.display = 'none'; 

			var i;

			for ( i = 0 ; i < aCategories.length ; ++i )
			{
				if ( aCategories[i].indexOf( g_strRefNameFilter ) != -1 )
				{
					var categoryPriority = 6;

					if ( aCategories[i].indexOf( "=1" ) != -1 )
						categoryPriority = 1;
					else if ( aCategories[i].indexOf( "=2" ) != -1 )
						categoryPriority = 2;
					else if ( aCategories[i].indexOf( "=3" ) != -1 )
						categoryPriority = 3;

					if ( categoryPriority <= g_priorityFilter )
						g_oRow.style.display = strVisible;
				}
			}
		}
		else
		{
			g_oRow.style.display = 'none'; 
		}
	}
}

