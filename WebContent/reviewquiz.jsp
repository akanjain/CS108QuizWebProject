<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quiz Review</title>
</head>
<body>
<%
	Quiz currentQuiz = (Quiz) request.getSession().getAttribute("currentQuiz");
	int numQuestion = currentQuiz.getNumQuestion();
	currentQuiz.resetNextQuestionNumber();
%>
<h1><b><u>Quiz Review:</u></b></h1>
<%
	for (int i = 1; i <= numQuestion; i++) {
		Question q = currentQuiz.getCurrentQuestion();
		int qNum = currentQuiz.getCurrentQuestionNumber();
		if (q instanceof PictureResponseQuestion) {
%>
			<p><b>Question <%= i %> of <%= numQuestion %>: </b></p>
			<img src=<%= q.getQuestion() %> alt="Image not displayed."><br><br>
			Your Answer: <b><%= currentQuiz.getAnswerAtQuestion(qNum) %></b><br>
<%
		} else if (q instanceof FillInBlankQuestion) {
			String[] parts = q.getQuestion().split("_____");
%>		
			<p><b>Question <%= i %> of <%= numQuestion %>: </b><%= parts[0] %>
			<input type="text" size="10" value=""><%= parts[1] %></p>
			Your Answer: <b><%= currentQuiz.getAnswerAtQuestion(qNum) %></b><br>
<%
		} else if (q instanceof MultipleChoiceQuestion) {
%>
			<p><b>Question <%= i %> of <%= numQuestion %>: </b><%= q.getQuestion() %></p>
<%
			for (String questext : q.getQuestionOptions()) {
%>
				<input type="radio"><%= questext %><br>
<%
			}
%>
			Your Answer: <b><%= currentQuiz.getAnswerAtQuestion(qNum) %></b><br>
<%		
		} else if (q instanceof MultipleChoiceAnswerQuestion) {
%>
			<p><b>Question <%= i %> of <%= numQuestion %>: </b><%= q.getQuestion() %></p>
<%
			for (String questext : q.getQuestionOptions()) {
%>
				<input type="checkbox">	<%= questext %><br>
<%
			}
%>
			Your Answers:
			<ul>
<%
			for (String s : currentQuiz.getAnswerListAtQuestion(qNum)) {
				out.println("<li>" + s + "</li>");
			}
%>
			</ul>
<%
		} else if (q instanceof MatchingQuestion) {
%>
			<p><b>Question <%= i %> of <%= numQuestion %>: </b><%= q.getQuestion() %></p>
<%
			List<String> allQuestionOption = q.getQuestionOptions();
			List<String> allAnswerOption = new ArrayList<String>();
			for (String a : q.getAnswerOptions()) {
				allAnswerOption.add(a);
			}
			for (int j = 0; j < allQuestionOption.size(); j++) {
%>
				<input type="text" value="<%= allQuestionOption.get(j) %>">
				<select>
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
%>
			Your Answers:
			<ul>
<%
			List<String> matchAnswers = currentQuiz.getAnswerListAtQuestion(qNum);
			for (int j = 0; j < allQuestionOption.size(); j++) {
%>
				<li><%= allQuestionOption.get(j) %> ----> <%= matchAnswers.get(j) %></li>

<%				
			}
%>
			</ul>		
<%
		} else if (q instanceof MultipleAnswerOrderedQuestion || q instanceof MultipleAnswerUnOrderedQuestion) {
%>
			<p><b>Question <%= i %> of <%= numQuestion %>: </b><%= q.getQuestion() %></p>
<%
			int slots = q.getNumSlot();
			for (int ct = 0; ct < slots; ct++) {
%>
				<%= ct+1 %>: <input type="text" size="100" value=""><br>
<%				
			}
%>
			Your Answers:
			<ul>
<%
			for (String s : currentQuiz.getAnswerListAtQuestion(qNum)) {
				out.println("<li>" + s + "</li>");
			}
%>
			</ul>
<%
		} else {
%>
			<p><b>Question <%= i %> of <%= numQuestion %>: </b><%= q.getQuestion() %></p>
			Your Answer: <b><%= currentQuiz.getAnswerAtQuestion(qNum) %></b><br>
<%
		}
		
		if (q instanceof MatchingQuestion) {
			List<String> allQuestionOption = q.getQuestionOptions();
			List<String> allAnswerOption = q.getAnswerOptions();
%>
			Correct Answers:
			<ul>
<%
			for (int k = 0; k < allQuestionOption.size(); k++) {
%>
				<li><%= allQuestionOption.get(k) %> ----> <%= allAnswerOption.get(k) %></li>
<%				
			}
%>
			</ul>	
			Score = <b><%= currentQuiz.getScore(qNum) %> out of <%= currentQuiz.getMaxScore(qNum) %></b><br>
<%
		} else {
%>
			Correct Answers:
			<ul>
<%
			for (String s : q.getAnswers()) {
				out.println("<li>" + s + "</li>");
			}
%>
			</ul>
			Score = <b><%= currentQuiz.getScore(qNum) %> out of <%= currentQuiz.getMaxScore(qNum) %></b><br>		
<%			
		}
		currentQuiz.setNextQuestionNumber();
	}
%>
<p>Go to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>