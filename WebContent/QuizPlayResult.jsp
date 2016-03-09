<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result Page</title>
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
<%
	Quiz currentQuiz = (Quiz) request.getSession().getAttribute("currentQuiz");
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	String username = (String) request.getSession().getAttribute("username");
	int quizId = (Integer) request.getSession().getAttribute("quizId");
%>
<h1>Quiz Result: </h1>
<h3>Your score: <%= (Integer) request.getAttribute("Score") %> out of <%= currentQuiz.getTotalMaxScore() %></h3>
<h3>Time taken to complete Quiz: <%= request.getAttribute("elapsedTime") %> sec</h3>
<h3><u>Quiz Score Comparison:</u></h3>
<%	
	Map<String, String> allUserQuizRecord = QzManager.getAllUserQuizRecords(quizId);
	String userBestScore = QzManager.getUserQuizRecord(quizId, username);
	String quizBestScore = QzManager.getQuizTopScore(quizId);
	String userBest = "No Past Score Available.";
	String quizBest = "No Past Score Available.";
	String current = request.getAttribute("Score") + ", " + request.getAttribute("elapsedTime") + "sec";
	if (!userBestScore.equals("")) {
		String[] parts = userBestScore.split(",");
		userBest = parts[0] + ", " + parts[1] + "sec";
	}
	if (!quizBestScore.equals("")) {
		String[] parts = quizBestScore.split(",");
		quizBest = "<a href=\"userpage.jsp?username=" + parts[0] + "\">" + parts[0] + "</a>, " + parts[1] + ", " + parts[2] + "sec";
	}
%>
<table> 
<thead> 
<tr> 
    <th align="center">Your Best Score till now</th> 
    <th align="center">Your Current Score</th>
    <th align="center">Top Score of this Quiz till now</th>
</tr> 
</thead> 
<tbody> 
<%
			
%>
<tr>
    <td align="center"><%= userBest %></td> 
    <td align="center"><%= current %></td> 
    <td align="center"><%= quizBest %></td> 
</tr> 
</tbody> 
</table>

<p>Go to <a href="reviewquiz.jsp">Review Quiz</a></p>
<p>Go to <a href="ratequiz.jsp">Rate Quiz</a></p>
<p>Go to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>