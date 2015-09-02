<!DOCTYPE html>
<meta charset="UTF-8">
<html>
<head>
<%@page 
import="com.mikealbert.service.OraSessionService,java.util.ResourceBundle, org.springframework.context.*,
org.springframework.web.context.support.*"
contentType="text/html;" %>

<%
ApplicationContext beanFactory =
    WebApplicationContextUtils
        .getRequiredWebApplicationContext(getServletContext());
        
OraSessionService oraSessionService =
     (OraSessionService)beanFactory
          .getBean("oraSessionService", OraSessionService.class);
          
ResourceBundle resource = ResourceBundle.getBundle("buildInfo");

String applicationName = resource.getString("build.appName");
String version =  resource.getString("build.version");        
String databaseName = oraSessionService.getDatabaseNameForDevQATrain();
String databaseRefreshDate = oraSessionService.getDBRefreshdate();
%>

<title>System Info</title>
</head>

<body>
	<p>
		Application Name: <%=applicationName%><br>
		Version: <%=version%><br>
		Database Name: <%=databaseName%><br>
		Last Refreshed: <%=databaseRefreshDate%><br>
	</p>
</body>

</html>