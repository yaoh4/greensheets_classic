<!-- Start of Preferences Code -->
<!-- End of Preferences -->


<table cellpadding="3" bgcolor="#CCCCCC">
	<tr>
		<td>
			<P align="right">
				<STRONG>Grants From:</STRONG>
			</P>
		</td>
		<td>
			<SELECT name="selGrantsFrom">
				<OPTION value="myportfolio">
					My Porfolio
				</option>
				<OPTION value="mycanceractivity">
					My Cancer Activity
				</option>
				<OPTION value="allncigrants">
					All NCI Grants
				</option>
			</SELECT>
		</td>
		<td>
		</td>
		<td>
			<P align="right">
				<STRONG>Grant Type:</STRONG>
			</P>
		</td>
		<td>
			<SELECT name="selGrantType">
				<OPTION value="competinggrants">
					Competing Grants
				</option>
				<OPTION value="noncompetinggrants">
					Non-Competing Grants
				</option>
				<OPTION value="both">
					Both
				</option>
			</SELECT>
	</tr>
	<tr>
	</tr>
	<tr>
		<td>
			<P align="right">
				<STRONG>Mechanism:</STRONG>
			</P>
		</td>
		<td>
			<INPUT type="text" name="selMechanism" />
			Example: R01
		</td>
		<td>
			<P align="left">
			</P>
		</td>
		<td>
			<P align="right">
				<STRONG>Only Grants within Payline:</STRONG>
			</P>
		</td>
		<td>
			<label>
				<input type="radio" name="selWithinPayline" value="yes" />
				Yes
			</label>
			<label>
				<input type="radio" name="selWithinPayline" value="no" />
				No
			</label>
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td>
			<P align="left">
				Leaving Mechanism blank
				<BR>
				will return all mechanisms
			</P>
		</td>
		<td>
		</td>
		<td>
		</td>
		<td>
		</td>
	</tr>
	<tr>
		<td>
			<P align="right">
				<STRONG>Grant Number:</STRONG>
			</P>
		</td>
		<td>
			<INPUT type="text" name="selGrantNumber" />
		</td>
		<td>
			<P align="center">
				-or-
			</P>
		</td>
		<td>
			<P align="right">
				<STRONG>PI Name:</STRONG>
			</P>
		</td>
		<td>
			<INPUT type="text" name="selPiName" />
		</td>
	<tr>
	<tr>
		<td colspan="5" valign="bottom">
			<div align="right">
				<%@include file="/jsp/common/PreferencesNav.jsp"%>
			</div>
		</td>
	</tr>
</table>



