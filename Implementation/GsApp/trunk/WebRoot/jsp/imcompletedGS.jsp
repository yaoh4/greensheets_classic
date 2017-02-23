<html>
    <head>
        <title>Test</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <script language="javascript" src="./scripts/ClientSideMethods.js"></script>
        
        <script type="text/javascript">
        function displayMessage() {
        	
        	var strSpecMessage = "Due to a technical error, the Greensheet checklist document is not available for this grant award segment. \n";
        	strSpecMessage += "The technical issue was identified and corrected; ";
        	strSpecMessage += "but the data that was completed previously by the Grants Management Specialist documenting their review could not be restored to the file.";
			
			var strPGMMessage = "Due to a technical error, the Greensheet checklist document is not available for this grant award segment. \n";
			strPGMMessage += "The technical issue was identified and corrected;";
			strPGMMessage += "but the data that was completed previously by the Program Director documenting their review could not be restored to the file.";
			
			var message = "";
			var urlString = window.location.href;
			if(urlString.indexOf("GS_GROUP_TYPE=SPEC") > -1){
				message = strSpecMessage;
			}
			else {
				message = strPGMMessage;
			}
			 alert(message);
			 window.close();
          //  urlString = urlString +"&MA=true";
           // alert(window.opener.location);
          //  window.opener.location.reload();
            /* var toolbarDisplay = "toolbar=no";
            var statusbarDisplay = "status=yes";
            var scrollbarDisplay = "scrollbars=yes";
            var locationbarDisplay = "location=no";
            var menubarDisplay = "menubar=no";
            var directoryDisplay = "directories=no";
            var showMaximizeButton = "resizable=yes";
            var windowName = "_self";  // display a new unnamed window every time.


            var displayString = toolbarDisplay + "," + showMaximizeButton + "," + statusbarDisplay + "," + scrollbarDisplay + "," + locationbarDisplay + "," + menubarDisplay + "," + directoryDisplay;
            window.open(urlString, windowName , displayString); */
        }
        </script>
    </head>
    <body>
    <body onload="displayMessage();">
    </body>
</html>