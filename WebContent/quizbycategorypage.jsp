<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz by Category</title>
<style type="text/css">
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
<h1>Quiz Listing by Category:</h1>
<%
	String categoryName = request.getParameter("id");
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	Map<String, List<String>> allCategoryQuizMp;
	if (categoryName == null) {
		allCategoryQuizMp = QzManager.getAllQuizByCategory();
	} else {
		allCategoryQuizMp = QzManager.getQuizByCategory(categoryName);
	}
%>

<%
	if (allCategoryQuizMp.size() == 0) {
		out.println("No Quiz in this category listing.");
	} else {
		for (String key : allCategoryQuizMp.keySet()) {
%>
<h3>List of Quizzes with category "<%= key.toUpperCase() %>":</h3>
<%
	List<String> allQuizSet = allCategoryQuizMp.get(key);
	out.println("<table style=\"border:20px\">");
	out.println("<tr><th align=\"center\">Created Time</th><th align=\"center\">ID</th><th align=\"center\">Title</th>");
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
<%
	if (((String) request.getSession().getAttribute("username")).equals("guest")) {
		out.println("<p>Go back to <a href=\"guestHomepage.jsp\">Homepage</a></p>");
	} else {
		out.println("<p>Go back to <a href=\"homepage.jsp\">Homepage</a></p>");
	}
%>
</body>
</html>