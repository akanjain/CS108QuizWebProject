<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
	/* Store username to session. */
	if (request.getParameter("username") != null) {
		request.getSession().setAttribute("username", request.getParameter("username"));
	}

	String username = (String) request.getSession().getAttribute("username");
	UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	ResultSet rs = null;
	
%>
<title>Welcome Guest!</title>
<body>
<h1>Welcome Guest!</h1>
<h3>Announcements:</h3>
<%
	rs = userDataManager.getAnnouncements();
	
	if (!rs.isBeforeFirst()) {
		out.println("<p> There is no announcement </p>");
	} else {
		out.println("<table style=\"border:20px\">");
		out.println("<tr><th align=\"center\">Announcement Time</th><th align=\"center\">Admin</th><th align=\"center\">Announcement</th>");
		while (rs.next()) {
		String timeStamp = rs.getString("time");
		String author = rs.getString("username");
		String announcement = rs.getString("announcement");
		
		out.println("<tr><td align=\"center\">" + timeStamp + "</td><td align=\"center\">"+ author + "</td><td align=\"left\">"+ announcement + "</td><tr>");
		}
		out.println("</table>");
	}
%>

<h3>Popular Quizzes:</h3>
<%
	rs = QzManager.getPopularQuizzes(5);

	if (!rs.isBeforeFirst()) {
		out.println("<p>There is no quiz in the system.</p>");
	} else {
		out.println("<table style=\"border:20px\">");
		out.println("<tr><th align=\"center\">Rank</th><th align=\"center\">Title</th><th align=\"center\">Plays</th></tr>");
		
		int rank = 1;
		while (rs.next()) {
			out.println("<tr><td align=\"center\">" + rank + "</td><td align=\"center\"><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td><td align=\"center\">" + rs.getString("COUNT(quizId)") + "</td></tr>");
			rank++;
		}
		out.println("</table>");
	}
	
%>

<h3>New Quizzes:</h3>
<%
	rs = QzManager.getRecentlyCreatedQuizzes(5);

	if (!rs.isBeforeFirst()) {
		out.println("<p>There is no new quiz in the system.</p>");
	} else {
		out.println("<table id=\"cssTable\" style=\"border:20px\">");
		out.println("<tr><th align=\"center\">Created Time</th><th align=\"center\">ID</th><th>Title</th>");
		while (rs.next()) {
			out.println("<tr><td align=\"center\"> " + rs.getString("dateCreated") +  "</td><td align=\"center\">" + rs.getString("quizId") + "</td><td = align=\"center\"><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td>");
		}
		out.println("</table>");
	}
	

	
%>
</body>
</html>