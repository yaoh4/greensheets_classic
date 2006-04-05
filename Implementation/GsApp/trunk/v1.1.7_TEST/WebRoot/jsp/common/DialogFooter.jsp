<!-- start default footer include -->
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>

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



<table width="100%" cellspacing="0"  class="header">
	<tr class="bottomRow">
		<td class="globalNav">&nbsp;</td>
		<td class="align1">&nbsp;</td>
	</tr>

	<tr>
		<td>
			&nbsp;
		</td>
		<td class="align1">
			<img src="./images/LogoNCI.gif" alt="National Cancer Institute logo" 
			onmouseover="javascript:showAppInfo('open',event);" onmouseout="javascript:showAppInfo('close',event);"/>
		</td>
	</tr>
</table>
<!-- end default footer include --> 

