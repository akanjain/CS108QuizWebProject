<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% String username = (String) request.getSession().getAttribute("username"); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= username %>'s History Page</title>
</head>
<body>
<%
	UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
	ResultSet rs = userDataManager.getUserHistory(username);
	
	if (!rs.isBeforeFirst()) {
		out.println("<h1>You have no quiz taking history. Try to take soms quizzes!</h1>");
	} else {
		while (rs.next()) {
			String subject = rs.getString("title");
			String time = rs.getString("time");
			String score = rs.getString("score");
			String duration = rs.getString("duration");
			out.println("<p>quiz name: " + subject + " , time: " + time + " , score: " + score + " , time used: " + duration + "</p>");
		}
	}
%>

<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>