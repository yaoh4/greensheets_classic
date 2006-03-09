<html>
<body>
<title>
Greensheets Test Stuff Launch Page
</title>

<form name="linktest">
<table>
<tr><td>
<a href="javascript:greensheetList();">link test</a>
</td></tr>
</table>
</form>

<form name="testfrm" action="/greensheets/retrievegreensheet.do" METHOD="Post">
	ORACLE_ID
    <INPUT type="text" name="ORACLE_ID" value="gs_pd"></br>
    GS_GRANT_ID
    <INPUT type="text" name="GRANT_ID" value="1R01AG020686-01"></br>
    GS_GROUP
    <INPUT type="text" name="GS_GROUP_TYPE" value="PGM"></br>
    CLIENT
    <INPUT type="text" name="CLIENT" value=""></br>
    <input type="submit" name="submit">

</form>

</br> launch the question attachements screen </br>
<form name="attachTest" action="/greensheets/questionattachments.do" method="post">
    <input type="hidden" name="method" value="findAttachments">
    <input type="submit" name="attach" value="Go To Attach">
    <input type="hidden" name="RESP_DEF_ID" value="PCQ_1_RD_FL_1">
</form>

</br> Change lock Test </br>
<form name="changeLockTest" action="/greensheets/changeGreensheetLock" method="post">
    <input type="submit" name="attach" value="Change Lock">
    <input type="text" name="APPL_ID" value="6794947">
    <input type="text" name="GS_GROUP_TYPE" value="SPEC">
    <input type="text" name="GRANT_ID" value="5P01CA075138-07">
</form>

</body>
</html>
<script>
  function greensheetList(){

    var urlString = "http://localhost:8101/greensheets/retrievegreensheet.do?GS_GROUP_TYPE=PGM&APPL_ID=6685933&USER_ID=gs_noid"

	var toolbarDisplay = "toolbar=yes";
	var statusbarDisplay = "status=yes";
	var scrollbarDisplay = "scrollbars=yes";
	var locationbarDisplay = "location=yes";
	var menubarDisplay = "menubar=yes";
	var directoryDisplay = "directories=no";
	var showMaximizeButton = "resizable=yes";
	var windowName = "_blank";  // display a new unnamed window every time.


	var displayString = toolbarDisplay + "," + showMaximizeButton + "," + statusbarDisplay + "," + scrollbarDisplay + "," +                           locationbarDisplay + "," + menubarDisplay + "," + directoryDisplay;

	window.open(urlString, windowName , displayString);
  }
  </script>


