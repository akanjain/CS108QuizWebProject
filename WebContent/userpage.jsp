<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String username = request.getParameter("username");
	ResultSet rs = null;
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
	
%>
<title><%= username %>'s Homepage</title>
<style type="text/css">

#cssTable
{
    text-align:center; 
    vertical-align:middle;
}
table, th, td {
    border: 1px solid black;
}

tr:nth-child(even) {background-color: #f2f2f2}

th {
    background-color: #E0FFFF;
    color: black;
}

table {
    width: 50%;
}
</style>
</head>
<body>

<h3><%= username %>'s Recent Activities</h3>

<%
	rs = QzManager.getUserRecentActivities(username, 5);
	
	if (!rs.isBeforeFirst()) {
		out.println("<p>" + username + " has no recent activity.</p>");
	} else {
		out.println("<table id=\"cssTable\" align style=\"border:20px\">");
		out.println("<tr><th>Time</th><th>ID</th><th>Title</th><th>Score</th><th>Duration</th>");
		while(rs.next()) {
			out.println("<tr><td>" + rs.getString("time") +  "</td><td>" + rs.getString("quizId") + "</td><td><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td><td>" + rs.getString("score") + "</td><td>" + rs.getString("duration") + "</td>");
		}
		out.println("</table>");
	}	

%>

<h3>List of quizzes created by <%= username %>:</h3>
<ul>
<%
	ResultSet quizUserRs = QzManager.getQuizListbyUser(username);
	if (quizUserRs.next()) {
%>
<li><a href="QuizPage.jsp?id=<%= quizUserRs.getString("quizId") %>"><%= quizUserRs.getString("title") %></a></li>
<%		
	} else {
%>
<li><p>No quizzes created by <%= username %></p></li>
<%	
	}
	while (quizUserRs.next()) {
%>
<li><a href="QuizPage.jsp?id=<%= quizUserRs.getString("quizId") %>"><%= quizUserRs.getString("title") %></a></li>
<%
	}
%>
</ul>

<h3><%= username %>'s Achievements</h3>
<%
	rs = userDataManager.getUserAchievements(username);

	if (!rs.isBeforeFirst()) {
		out.println("<p>" + username + "has no achievement </p>");
	} else {
		while (rs.next()) {
		String achievement = rs.getString("achievement");
		
		out.println ("<p>" + achievement + "</p>" );
		}
	}
%>

<h3><%= username %>'s friends</h3>
<%
	rs = userDataManager.getUserFriends(username);
	
	if (!rs.isBeforeFirst()) {
		out.println("<p>" + username + "has no friend </p>");
	} else {
		while (rs.next()) {
		String friend = rs.getString("toUser");	
		out.println ("<p><a href=\"userpage.jsp?username=" + friend + "\">" + friend +  "</a></p>" );
	}
}
%>

<h3>To add this user as your friend, please enter your message and click "Add To Friends".</h3>
<form action="UserpageServlet" method="post">
<input type="text" name="message">
<%
	if (((String) request.getSession().getAttribute("username")).equals("guest")) {
		out.println("<input type=\"submit\" value=\"Add To Friends\" disabled> You must login to add friends.");
	} else {
		out.println("<input type=\"submit\" value=\"Add To Friends\">");
	}
%>

<input type="hidden" name="toUser" value="<%= username %>">
</form>
<%
		String returnStatus = (String) request.getAttribute("Return Status");
		if ( returnStatus != null) {
			out.println(returnStatus);
		}
%>

<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>