<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Administrator Tools</title>
</head>
<body>
	<%
		String username = (String) request.getSession().getAttribute("username");	
		UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		if (!userDataManager.isAdministrator(username)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("permissionDenied.jsp");
			dispatcher.forward(request, response);
		}
	%>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Create Announcement">
		<input type="text" name="announcement"><input type="submit" value="Create Announcement">
	</p>
	</form>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Promote User">
		<input type="text" name="username"><input type="submit" value="Promote User">
	</p>
	</form>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Remove User">
		<input type="text" name="username"><input type="submit" value="Remove User">
	</p>
	</form>
	
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Remove Quiz">
		<input type="text" name="quizNumber"><input type="submit" value="Remove Quiz">
	</p>
	</form>
	
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Clear Quiz History">
		<input type="text" name="quizNumber"><input type="submit" value="Clear Quiz History">
	</p>
	</form>
	
	<%
		String returnStatus = (String) request.getAttribute("Return Status");
		if ( returnStatus != null) {
			out.println(returnStatus);
		}
	%>

</body>
</html>