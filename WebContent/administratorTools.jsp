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
		QuizManager quizManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
		if (!userDataManager.isAdministrator(username)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("permissionDenied.jsp");
			dispatcher.forward(request, response);
		}
	%>
	<h1>Site Statistics</h1>
	<p> Total Users:  <%= userDataManager.getNumUsers() %> </p>
	<p> Total Quizzes:  <%= quizManager.getNumQuizzes() %> </p>
	<p> Total Play Count:  <%= quizManager.getNumQuizRecords() %> </p>
	
	<h3>Creates Announcement</h3>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Create Announcement">
		<input type="text" name="announcement">  (Enter Announcement)
	</p>
	<input type="submit" value="Create Announcement">
	</form>
	
	<h3>Give User Administrator Role</h3>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Promote User">
		<input type="text" name="username">  (Enter User Name Here)
	</p>
	<input type="submit" value="Promote User">
	</form>
	<h3>Remove User Account</h3>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Remove User">
		<input type="text" name="username">  (Enter User Name Here)
	</p>
	<input type="submit" value="Remove User">
	</form>
	
	<h3>Remove Quiz</h3>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Remove Quiz">
		<input type="text" name="quizNumber">  (Enter Quiz ID Here)
	</p>
	<input type="submit" value="Remove Quiz">
	</form>
	
	<h3>Clear Quiz History</h3>
	<form action="AdministratorToolsServlet" method="post">
	<p>
		<input type="hidden" name="option" value="Clear Quiz History">
		<input type="text" name="quizNumber">  (Enter Quiz ID Here) 
	</p>
	<input type="submit" value="Clear Quiz History">
	</form>
	
	<h3>To Load XML Quiz, Click <a href="LoadXMLQuiz.jsp">Here</a></h3>
	
	<%
		String returnStatus = (String) request.getAttribute("Return Status");
		if ( returnStatus != null) {
			out.println(returnStatus);
		}
	%>
	
<br>
<br>
<p>Go back to <a href="homepage.jsp">Homepage</a></p>

</body>
</html>