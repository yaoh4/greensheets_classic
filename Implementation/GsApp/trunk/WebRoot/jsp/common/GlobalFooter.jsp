<!-- start default footer include -->
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.application.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%@ page import="java.util.*" %>

<%
	String urlWorkbench1 = application.getInitParameter("urlWorkbench");
	String urlImpac21 = application.getInitParameter("urlImpac2");
%>

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

<table width="100%" class="line">
	<tr>
		<td></td>
	</tr>
</table>

<table width="100%" cellspacing="0">
	<tr>
		<td>
			<table cellspacing="0" >
				<tbody>
					<tr>
						<td>
							<table cellspacing="0">
								<tbody>
									<tr>
										<td>
											<span class="otherappsmenu">
												<a class="oambutton" href="<%=urlWorkbench1%>">Workbench</a>
											</span>
										</td>
										<td>
											<span class="otherappsmenu">
												<a class="oambutton" href="<%=urlImpac21%>">IMPAC II Applications</a>
											</span>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</td>
		<td class="align1">
			<img src="<%=request.getContextPath()%>/images/LogoNCI.gif" alt="National Cancer Institute" title="National Cancer Institute">
		</td>
	</tr>
</table>
<!-- end default footer include --> 

