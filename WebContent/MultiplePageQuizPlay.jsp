<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Playing Quiz</title>
</head>
<body>
<%
	Quiz currentQuiz = (Quiz) request.getSession().getAttribute("currentQuiz");
	int currentQuizQuestion = (Integer) request.getSession().getAttribute("currentQuizQuestion");
	int numQuestion = (Integer) request.getSession().getAttribute("currentQuizTotalQuestions");
	String isPracticeMode = (String) request.getSession().getAttribute("isPracticeMode");
%>
<h1>Enter answers in the form below.</h1>
<%
	if (currentQuizQuestion < numQuestion) {
%>
		<h3>Press "Submit" button to submit answer and go to next question.</h3>
<%
	}
	if (currentQuizQuestion == numQuestion) {
		if (isPracticeMode.equals("false")) {
%>
		<h3>Press "Submit Quiz" to submit quiz and view total Quiz Score.</h3>
<%
		} else if (isPracticeMode.equals("true")) {
%>
		<h3>Press "Submit" button to submit answer and go to next question.</h3>
<%		
		}
	}
%>
<form action="MultiplePageServlet" method="post">
<%
		Question qt = currentQuiz.getCurrentQuestion();
		if (qt instanceof PictureResponseQuestion) {
%>
			<p><b>Question <%= currentQuizQuestion %> of <%= numQuestion %>: </b></p>
			<img src=<%= qt.getQuestion() %> alt="Image not displayed."><br><br>
<%
		} else if (qt instanceof FillInBlankQuestion) {
			String[] parts = qt.getQuestion().split("_____");
%>		
			<p><b>Question <%= currentQuizQuestion %> of <%= numQuestion %>: </b><%= parts[0] %>
			<input type="text" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" size="50" value="">
			<%= parts[1] %></p>
<%
		} else {
%>		
			<p><b>Question <%= currentQuizQuestion %> of <%= numQuestion %>: </b><%= qt.getQuestion() %></p>
<%
		}
		String selected = currentQuiz.getAnswerAtQuestion(currentQuiz.getCurrentQuestionNumber());
		if (qt instanceof MultipleChoiceQuestion) {
			for (String questext : qt.getQuestionOptions()) {
%>
				<input type="radio" name="answer-<%= currentQuiz.getCurrentQuestionNumber() %>" value="<%= questext %>"><%= questext %><br>
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
			List<String> allAnswerOption = new ArrayList<String>();
			for (String a : qt.getAnswerOptions()) {
				allAnswerOption.add(a);
			}
			Collections.shuffle(allAnswerOption);
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
		out.println("<br>");
%>
<%
	if (currentQuizQuestion < numQuestion) {
%>
<input type="submit" name ="token" value="Submit" />
<%
	}
	if (currentQuizQuestion == numQuestion) {
		if (isPracticeMode.equals("false")) {
%>
<input type="submit" name ="token" value="Submit Quiz" />
<%
		} else if (isPracticeMode.equals("true")) {
%>
<input type="submit" name ="token" value="Submit" />
<%		
		}
	}
	if (isPracticeMode.equals("true")) {
%>
<input type="submit" name ="token" value="End Practice" />
<%
	}
%>
</form>
</body>
</html>