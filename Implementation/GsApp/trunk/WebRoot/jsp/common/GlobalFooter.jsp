<!-- start default footer include -->
<br />
<br />
<script language="javascript">
function showAppInfo(act,event)
{
	if(act=='open'){
		m = window.open('<%=request.getContextPath()%>/jsp/common/AppInfo.jsp','appinfo','width=300,height=200,toolbar=no,titlebar=no', 'screenX='+event.clientX, 'screenY='+event.clientY);
	}
	else{
 		m.close();
	}
}
</script>

<table width="100%" cellspacing="0" class="footer">
	<tr>
		<td class="align1">
			<img src="./images/LogoNCI.gif" alt="National Cancer Institute logo" 
			onmouseover="javascript:showAppInfo('open',event);" onmouseout="javascript:showAppInfo('close',event);"/>
		</td>
	</tr>
</table>
<!-- end default footer include -->