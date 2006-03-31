<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
	<head>
		<title>Greensheets</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/GreensheetsStyleSheet.css" type="text/css" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/otherappsmenu.css" type="text/css" />
	</head>
	<body>

		<!-- start header -->
		<img src="<%=request.getContextPath()%>/images/Logo_Greensheets.gif" alt="Greensheets" border="0" />
		<!-- end header -->

		<!--  horiz. line -->
		<table width="100%" class="line">
			<tr>
				<td></td>
			</tr>
		</table>

		<h1>
			Select options and save to set your startup preferences
		</h1>

		<!--Expanded Criteria -->
		<table cellpadding="3">
			<!--  Grants From Section -->
			<tr>
				<td valign="top">
					<strong>Grants From</strong>
				</td>
				<form name="form1" method="post" action="">
				<td valign="top">
					<label>
						<input type="radio" name="My group" value="radio">
						My Portfolio
					</label>
				</td>
				<td valign="top">
					<label>
						<input type="radio" name="My group" value="radio">
						My Cancer Activites
					</label>
				</td>
				<td valign="top">
					<label>
						<input type="radio" name="My group" value="radio">
						All NCI Grants
					</label>
				</td>
				</form>
			</tr>
			<!--  Grants Type Section -->
			<tr>
				<td>
					<strong>Grant Type</strong>
				</td>
				<form name="form2" method="post" action="">
				<td valign="top">
					<label>
						<input type="radio" name="grant types" value="radio">
						Competing Grants
					</label>
				</td>
				<td valign="top">
					<label>
						<input type="radio" name="grant types" value="radio">
						Non-Competing Grants
					</label>
				</td>
				<td valign="top">
					<label>
						<input type="radio" name="grant types" value="radio">
						Both Competing and Non-Competing Grants
					</label>
					</form>
			</tr>

			<!--  Budget Mechanism Section -->
			<tr>
				<td valign="top">
					<strong>Budget Mechanism</strong>
				</td>
				<td valign="top">
					<input name="" type="text">
				</td>
				<td valign="top">
					<strong>Only Grants within the Payline</strong>
				</td>
				<td valign="top">
					<label>
						<input type="radio" name="grant types" value="radio">
						Yes
					</label>
					<label>
						<input type="radio" name="grant types" value="radio">
						No
					</label>
				</td>
			</tr>
		</table>
		<p></p>

		<!-- Save prefs section -->
		<table>
			<tr>
				<td>
					<a href="Confirm.html"><img src="<%=request.getContextPath()%>/images/SavePreferences.gif" border="0"></a>
				</td>
				<td>
					<a href="home.html"><img src="<%=request.getContextPath()%>/images/Cancel.gif" border="0" alt="Close"> </a>
				</td>
			</tr>
		</table>

		<!--  horiz. line -->
		<table width="100%" class="line">
			<tr>
				<td></td>
			</tr>
		</table>

		<!--  logo section -->
		<table width="100%" cellspacing="0">
			<tr>
				<td align="right">
					<img src="<%=request.getContextPath()%>/images/LogoNCI.gif" alt="National Cancer Institute logo" />
				</td>
			</tr>
		</table>

	</body>
</html>
