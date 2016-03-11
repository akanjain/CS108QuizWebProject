<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.sql.ResultSet, java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="web.QuizManager" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Quiz Question</title>
</head>
<body>
<h2>Add Question <%= request.getSession().getAttribute("currentQuestion") %> of <%= request.getSession().getAttribute("numQuestion") %> </h2>
<p>Select Question type to be added from below:</p>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="question-response"/>
<input type="submit" value="question-response"></p>
</form>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="fill-in-blank"/>
<input type="submit" value="fill-in-blank"></p>
</form>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="multiple-choice"/>
<input type="submit" value="multiple-choice"></p>
</form>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="picture-response"/>
<input type="submit" value="picture-response"></p>
</form>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="multiple-answer-ordered"/>
<input type="submit" value="multiple-answer-ordered"></p>
</form>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="multiple-answer-unordered"/>
<input type="submit" value="multiple-answer-unordered"></p>
</form>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="multiple-choice-multiple-answer"/>
<input type="submit" value="multiple-choice-multiple-answer"></p>
</form>
<form action="QuestionServlet" method="post">
<p><input name="questionType" type="hidden" value="matching"/>
<input type="submit" value="matching"></p>
</form>
</body>
</html>