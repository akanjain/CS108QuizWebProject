<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.Statement, java.sql.ResultSet, java.util.*, java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Quiz</title>
</head>
<body>
<h1>New Quiz Creator</h1>
<p>Please fill in below detail: </p>
<form action="QuizCreatorServlet" method="post">
<p>Quiz title:</p>
<p><input type="text" name="quizTitle" size="50" value=""></p>
<p>Quiz description:</p>
<p><input type="text" name="quizDescription" size="50" value=""></p>
<p>Quiz category(Enter "Other" for no category):</p>
<p><input type="text" name="quizCategory" size="50" value=""></p>
<input name="quizCreator" type="hidden" value="<%= request.getSession().getAttribute("username") %>"/>
<input name="quizDate" type="hidden" value="<%= new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) %>"/>
<p><input type="checkbox" name="preferences" value="random"> Random Order</p>
<p><input type="checkbox" name="preferences" value="multiple"> Multiple Page Quiz</p>
<p><input type="checkbox" name="preferences" value="immediate"> Immediate Correction(Valid Only For Multiple Page Quiz)</p>
<p><input type="checkbox" name="preferences" value="practice"> Practice Mode available</p>
<p>Number of Questions in Quiz:</p>
<p><input type="text" name="numQuestion" size="50" value=""></p>
<p><input type="submit" value = "Start Creating Quiz"></p>
</form>
</body>
</html>