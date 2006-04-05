
// -----------------------------------------------------------------------------------------------------------------
// Documentation of showHide.js
// -----------------------------------------------------------------------------------------------------------------

/*
This javascript is used to show or hide substeps of the Canoo Webtest 'group' steptype in a HTML Webtest Resultfile.

The script provides functions to show or hide substeps for different levels in the hierarchically structured html tree:

 1)   For a single group step: 	showSubstepsOfGroup / hideSubstepsOfGroup / changeLevelOfDetailForGroup
 2)   For a testspec: 		showAllSubstepsOfTestspec / hideAllSubstepsOfTestspec
 3)   For the entire document: 	hideAllSubstepsOnPage
	
The function's algorithm is as follows:

 1)   Functions for a single group step:
 
      The functions for a single group step get as argument the node which represents 
      the image that has been clicked in the browser to show or hide the substeps of a group step.
      These functions navigate in the DOM tree to the node which is the parent of all substeps of the group.
      For each substep the attribute 'style.display' is assigned the appropriate value to show or hide the substep.
 
 2/3) Functions for a testspec or the entire document:
 
      These functions get as argument the node which represents the image that has been clicked 
      in the browser to show or hide the substeps of the group steps within a tespec or within the entire
      document. They determine each image in the scope of the testpec respectively in the document
      that is used to show or hide the substeps of a single group step and use each of these images 
      to call the according function for a single group step to show or hide the substeps for each 
      of the group steps.

The functions are based on the following structure of the HTML document:

 1)   showSubstepsOfGroup/hideSubstepsOfGroup:  

		<tr> <!-- tablerow for one step in a testspec -->
			<td> 	<!-- (( 2 )) SKRIPT: td node = imageForGroup.parentNode.parentNode.parentNode.parentNode.parentNode -->
				<!-- position of step in testspec -->
				<table>
					<tr><td>1</td></tr>
					<tr><td>
						<img onclick="changeLevelOfDetailForGroup(this)" groupStep="true" src="./images/expandall.png"> 
						<!-- (( 1 )) SKRIPT: img node = imageForGroup -->
					</td></tr>
				</table>
			</td>
			<td>	<!-- result of step execution (failed, successfull, not executed) -->
				<img src="./images/ok.gif">
			</td>
			<td>	<!-- stepType and stepID -->
				<table>
					<tr><td>group</td></tr>
					<tr><td>stepID here ... </td></tr>
				</table>
			</td>
			<td> <!-- (( 3 )) SKRIPT: td node = tdContainingImage.nextSibling.nextSibling.nextSibling -->
				<table></table>
				<table>
					<tr><th>No</th><th>Result</th><th>Name</th><th>Parameter</th><th>Duration</th></tr>
					<tr> ... substeps of group step ... </tr>


 2)   showAllSubstepsOfTestspec/hideAllSubstepsOfTestspec:

		<tr>	<!-- (( 2 )) SKRIPT: tr node: tablerowContainingImage = imageForTestSpec.parentNode.parentNode
			<td>	
				<img alt="show all substeps of 'group' steps in testspec" src="./images/expandall.png" onclick="showAllSubstepsOfTestspec(this)">
				<img alt="hide all substeps of 'group' steps in testspec" src="./images/collapseall.png" onclick="hideAllSubstepsOfTestspec(this)">
				<!-- (( 1 )) SKRIPT: img node = imageForTestSpec -->
			</td>
		</tr>
		<tr> 	<!-- (( 3 )) SKRIPT: tr node =  tablerowContainingImage.nextSibling -->
			<td>
				<table>
					<tr><th>No</th><th>Result</th><th>Name</th><th>Parameter</th><th>Duration</th></tr>
					<tr> ... steps of testspec ... <tr>



      
*/		 

var imgPath;	
var expandSource;
var collapseSource;

function hideAllSubstepsOnPage(imgPathVar, expandSourceVar, collapseSourceVar)
{
    imgPath = imgPathVar;
    expandSource = expandSourceVar;
    collapseSource = collapseSourceVar;
    var images = document.getElementsByTagName("img");
    for (i=0; i<images.length; i++) 
    	{
    	var groupStepAttributeOfImage = images[i].getAttribute("groupStep");
    	if (groupStepAttributeOfImage != null)
    		{hideSubstepsOfGroup(images[i]);}
    	}
}


function hideAllSubstepsOfTestspec(imageForTestSpec)
{
	var tablerowContainingImage = imageForTestSpec.parentNode.parentNode;
	var nextFollowingSiblingOfTablerowContainingImage = tablerowContainingImage.nextSibling;
	// while loop is used to handle 'text' nodes between 'TR' nodes in Mozilla and Netscape.
	while (nextFollowingSiblingOfTablerowContainingImage.nodeName.indexOf('TR') == -1)
		{
		nextFollowingSiblingOfTablerowContainingImage = nextFollowingSiblingOfTablerowContainingImage.nextSibling;
		}
	var tablerowContainingStepsOfTestspec = nextFollowingSiblingOfTablerowContainingImage;
	var images = tablerowContainingStepsOfTestspec.getElementsByTagName("img");
	for (i=0; i<images.length; i++) 
		{
		var groupStepAttributeOfImage = images[i].getAttribute("groupStep");
		if (groupStepAttributeOfImage != null)
			{
			hideSubstepsOfGroup(images[i]);
			}
		}
}


function showAllSubstepsOfTestspec(imageForTestSpec)
{
	var tablerowContainingImage = imageForTestSpec.parentNode.parentNode;
	var nextFollowingSiblingOfTablerowContainingImage = tablerowContainingImage.nextSibling;
	// while loop is used to handle 'text' nodes between 'TR' nodes in Mozilla and Netscape.
	while (nextFollowingSiblingOfTablerowContainingImage.nodeName.indexOf('TR') == -1)
		{
		nextFollowingSiblingOfTablerowContainingImage = nextFollowingSiblingOfTablerowContainingImage.nextSibling;
		}
	var tablerowContainingStepsOfTestspec = nextFollowingSiblingOfTablerowContainingImage;
	var images = tablerowContainingStepsOfTestspec.getElementsByTagName("img");
	for (i=0; i<images.length; i++) 
		{
		var groupStepAttributeOfImage = images[i].getAttribute("groupStep");
		if (groupStepAttributeOfImage != null)
			{
			showSubstepsOfGroup(images[i]);
			}
		}
}


function changeLevelOfDetailForGroup(imageForGroup)
{
	if (imageForGroup.src.indexOf(collapseSource) > -1) 
		{hideSubstepsOfGroup(imageForGroup);}		
	else if (imageForGroup.src.indexOf(expandSource) > -1) 
		{showSubstepsOfGroup(imageForGroup);}		
}


function hideSubstepsOfGroup(imageForGroup)
{
	var tdContainingImage = imageForGroup.parentNode.parentNode.parentNode.parentNode.parentNode;	// Note: XML DOM creates a TableBody node
	var tdContainingSubsteps = tdContainingImage.nextSibling.nextSibling.nextSibling;
	var substepTableRows = tdContainingSubsteps.getElementsByTagName("TR");
	for (var j = 1; j < substepTableRows.length; j++) 
		{
		 substepTableRows[j].style.display = "none";
		}
	imageForGroup.src = imgPath + expandSource;      		
}


function showSubstepsOfGroup(imageForGroup)
{
	var tdContainingImage = imageForGroup.parentNode.parentNode.parentNode.parentNode.parentNode;	// Note: XML DOM creates a TableBody node
	var tdContainingSubsteps = tdContainingImage.nextSibling.nextSibling.nextSibling;
	var substepTableRows = tdContainingSubsteps.getElementsByTagName("TR");
	for (var j = 1; j < substepTableRows.length; j++) 
		{
		 substepTableRows[j].style.display = "inline";
		}
	imageForGroup.src = imgPath + collapseSource;      		
}





