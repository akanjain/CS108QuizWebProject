<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Playing Quiz</title>
</head>
<body>
<h1>Enter answers in the form below and press submit button when done.</h1>
<form action="QuizEvaluateServlet" method="post">
<%
	Quiz currentQuiz = (Quiz) request.getSession().getAttribute("currentQuiz");
	int numQuestion = (Integer) request.getSession().getAttribute("currentQuizTotalQuestions");
	int count = 1;
	for (int i = 0; i < numQuestion; i++) {
		currentQuiz.setNextQuestionNumber();
		Question qt = currentQuiz.getCurrentQuestion();
		if (qt instanceof PictureResponseQuestion) {
%>
			<p><b>Question <%= count %> of <%= numQuestion %>: </b></p>
			<img src=<%= qt.getQuestion() %> alt="Image not displayed."><br><br>
<%
		} else {
%>		
			<p><b>Question <%= count %> of <%= numQuestion %>: </b><%= qt.getQuestion() %></p>
<%
		}
		if (qt instanceof MultipleChoiceQuestion) {
			for (String questext : qt.getQuestionOptions()) {
%>
				<input type="radio" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" value="<%= questext %>">	<%= questext %><br>
<%
			}
		} else {
%>
			Enter Answer: <input type="text" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" size="100" value="">	
<%
		}
		out.println("<br><br>");
		count++;
	}
%>
<input type="submit" value="Submit Quiz" />
</form>
</body>
</html>