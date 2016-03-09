<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Score Feedback</title>
</head>
<body>
<%
	Quiz currentQuiz = (Quiz) request.getSession().getAttribute("currentQuiz");
	int currentQuizQuestion = (Integer) request.getSession().getAttribute("currentQuizQuestion");
	int numQuestion = (Integer) request.getSession().getAttribute("currentQuizTotalQuestions");
	int questionScore = (Integer) request.getAttribute("QuestionScore");
	int maxScore = (Integer) request.getAttribute("MaxScore");
	String isPracticeMode = (String) request.getSession().getAttribute("isPracticeMode");
	int i = currentQuiz.getCurrentQuestionNumber();
%>
<h1>Score of Question <%= currentQuizQuestion %> of <%= numQuestion %>:</h1>
<h3><%= questionScore %> correct answer out of total <%= maxScore %> correct answers.</h3>
<form action="MultiplePageServlet" method="post">
<%
	if (currentQuizQuestion < numQuestion) {
%>
		Press <b>"Next Question"</b> button to go to next question.<br><br>
		<input type="submit" name ="token" value="Next Question" />
<%
		if (isPracticeMode.equals("true")) {
%>
			<input type="submit" name ="token" value="End Practice" />
<%
			if (questionScore == maxScore) {
				if (!(((PracticeQuiz) currentQuiz).increaseCount(i))) {
%>
					<input type="submit" name ="token" value="Remove Practice Question" />		
<%	
				}
			} else {
%>
				<input type="submit" name ="token" value="Remove Practice Question" />			
<%			
			}
		}
	}
	if (currentQuizQuestion == numQuestion) {
		if (isPracticeMode.equals("false")) {
%>
		Press <b>"Score Summary"</b> to view total Quiz Score.<br><br>
		<input type="hidden" name="Score" value=<%= (Integer) request.getAttribute("Score") %>>
		<input type="hidden" name="elapsedTime" value=<%= (String) request.getAttribute("elapsedTime") %>>
		<input type="submit" name ="token" value="Score Summary" />
<%
		} else if (isPracticeMode.equals("true")) {
%>
		Press <b>"Next Question"</b> button to go to next question.<br><br>
		<input type="submit" name ="token" value="Next Question" />
		<input type="submit" name ="token" value="End Practice" />
<%
			if (questionScore == maxScore) {
				if (!(((PracticeQuiz) currentQuiz).increaseCount(i))) {
%>
					<input type="submit" name ="token" value="Remove Practice Question" />
<%		
				}
			} else {
%>
				<input type="submit" name ="token" value="Remove Practice Question" />
<%
			}
		}
	}
%>
</form>
</body>
</html>