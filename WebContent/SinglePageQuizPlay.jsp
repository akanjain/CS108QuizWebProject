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
		} else if (qt instanceof FillInBlankQuestion) {
			String[] parts = qt.getQuestion().split("_____");
%>		
			<p><b>Question <%= count %> of <%= numQuestion %>: </b><%= parts[0] %>
			<input type="text" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" size="50" value="">
			<%= parts[1] %></p>
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
		} else if (qt instanceof MultipleChoiceAnswerQuestion) {
			for (String questext : qt.getQuestionOptions()) {
%>
				<input type="checkbox" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" value="<%= questext %>">	<%= questext %><br>
<%
			}
		} else if (qt instanceof MatchingQuestion) {
			List<String> allQuestionOption = qt.getQuestionOptions();
			List<String> allAnswerOption = qt.getAnswerOptions();
			for (int j = 0; j < allQuestionOption.size(); j++) {
%>
				<input type="text" value="<%= allQuestionOption.get(j) %>">
				<select name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>">
				<option value="None selected">Please select below</option>
<%
				for (String ans : allAnswerOption) {
%>
				<option value="<%= ans %>"><%= ans %></option>
<%
				}
%>
				</select><br>
<%
			}
		} else if (qt instanceof MultipleAnswerOrderedQuestion || qt instanceof MultipleAnswerUnOrderedQuestion) {
			int slots = qt.getNumSlot();
			for (int ct = 0; ct < slots; ct++) {
%>
				Enter Answer <%= ct+1 %>: <input type="text" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" size="100" value=""><br>
<%
				
			}
		} else if (qt instanceof FillInBlankQuestion) {
%>
			Enter Fill In Blank Answer in the text box above.<br>	
<%
		} else {
%>
			Enter Answer: <input type="text" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" size="100" value=""><br>	
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