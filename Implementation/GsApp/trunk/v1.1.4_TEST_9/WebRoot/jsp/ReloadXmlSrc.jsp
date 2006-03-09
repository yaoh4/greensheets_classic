
<%@ page language="java"%>
<%@ page import="java.lang.*,java.util.*" %>
<%@ page import="gov.nih.nci.iscs.numsix.greensheets.utils.*" %>
<%
String root = System.getProperty("conf.dir");

String configPath = root + "/" + application.getInitParameter("CONFIG_PATH") + "/";
AppConfigLoader.loadQuestionsXmlSrc(configPath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>Reload Xml Src</title>

  </head>
  <body>
    Reloading Questions XML Src Documents. <br>
  </body>
</html>
