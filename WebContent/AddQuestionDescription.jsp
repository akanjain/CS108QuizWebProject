<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Question Description</title>
</head>
<body>
<h1>Question Description</h1>
<form action="AddQuestionServlet" method="post">
<%
	if (((String)request.getAttribute("questionType")).equals("fill-in-blank")) {
%>
<p>Enter text of question before blank:</p>
<p><textarea name="textBefore" rows="5" cols="100"></textarea></p>
<p>Enter text of question after blank:</p>
<p><textarea name="textAfter" rows="5" cols="100"></textarea></p>
<%
	} else if (((String)request.getAttribute("questionType")).equals("picture-response")) {
%>
<p>Enter url of image corresponding to question below:</p>
<p><textarea name="textAll" rows="5" cols="100"></textarea></p>
<%
	} else {
%>
<p>Enter question below:</p>
<p><textarea name="textAll" rows="5" cols="100"></textarea></p>
<%
	}
	if (((String)request.getAttribute("questionType")).equals("multiple-choice") || ((String)request.getAttribute("questionType")).equals("multiple-choice-multiple-answer")) {
%>
<p>Number of options: <input type="text" name="numAnswers" /></p>
<%
	} else if (((String)request.getAttribute("questionType")).equals("matching")) {
%>
<p>Number of options to be matched: <input type="text" name="numAnswers" /></p>
<%
	} else if (((String)request.getAttribute("questionType")).equals("multiple-answer-unordered")) {
%>
<p>Number of acceptable answers: <input type="text" name="numAnswers" /></p>
<p>Number of answer slots: <input type="text" name="numSlots" /></p>
<%
	} else {
%>
<p>Number of answers: <input type="text" name="numAnswers" /></p>
<%
	}
%>
<input name="quesType" type="hidden" value="<%= (String) request.getAttribute("questionType") %>"/>
<p><input type="submit" value="Add Question"></p>
</form>
</body>
</html>