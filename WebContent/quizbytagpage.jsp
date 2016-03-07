<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz by Tag</title>
</head>
<body>
<h1>Quiz Listing by Tag:</h1>
<%
	String tagName = request.getParameter("id");
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	Map<String, Set<String>> allTagQuizMp;
	if (tagName == null) {
		allTagQuizMp = QzManager.getAllQuizTags();
	} else {
		allTagQuizMp = QzManager.getAllQuizMatchingTags(tagName);
	}
%>

<%
	if (allTagQuizMp.size() == 0) {
		out.println("No Quiz in this tag listing.");
	} else {
		for (String key : allTagQuizMp.keySet()) {
%>
<h3>List of Quizzes with tag "<%= key.toUpperCase() %>":</h3>
<%
	Set<String> allQuizSet = allTagQuizMp.get(key);
	out.println("<table style=\"border:20px\">");
	out.println("<tr><td align=\"center\">Created Time</td><td align=\"center\">ID</td><td align=\"center\">Title</td>");
	for (String s : allQuizSet) {
		String[] parts = s.split(",");
%>
		<tr>
			<td align="center"><%= parts[2] %></td> 
    		<td align="center"><%= parts[0] %></td> 
    		<td align="center"><a href="QuizPage.jsp?id=<%= parts[0] %>"><%= parts[1] %></a></td> 
		</tr> 
<%	
	}
	out.println("</table>");
%>
<%
		}
	}
%>
</body>
</html>