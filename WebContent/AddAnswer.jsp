<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Answer</title>
</head>
<body>
<h1>Add Answer to Question below:</h1>
<p>Question: <%= (String) request.getAttribute("questionText") %></p>
<p></p>
<form action="AddAnswerServlet" method="post">
<%
	if (((String) request.getAttribute("questionType")).equals("multiple-choice")) {
%>
<ul>
<%
		for (int i = 1; i <= ((Integer) request.getAttribute("numChoices")); i++) {
%>
<li><p><%= "Enter option " + i + ": " %><textarea name="<%= i %>" rows="1" cols="100"></textarea>
<input type="checkbox" name="correctOptions" value="<%= i %>">isCorrectOption</p></li>
<%
		}
%>
</ul>
<%
	} else {
%>
<ul>
<%
		for (int i = 1; i <= ((Integer) request.getAttribute("numChoices")); i++) {
%>
<li><p><%= "Enter answer " + i + ": " %><textarea name="<%= i %>" rows="1" cols="100"></textarea></p></li>
<%
		}
%>
</ul>
<%
	}
%>
<input name="questionNumber" type="hidden" value="<%= (Integer) request.getAttribute("questionNumber") %>"/>
<input name="quesType" type="hidden" value="<%= (String) request.getAttribute("questionType") %>"/>
<input name="numChoices" type="hidden" value="<%= (Integer) request.getAttribute("numChoices") %>"/>
<p><input type="submit" value="Add Answers to Question"></p>
</form>
</body>
</html>