<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tag Added</title>
</head>
<body>
<h1>Tag Added to Quiz.</h1>
<p>Go back to <a href="QuizPage.jsp?id=<%= request.getParameter("id") %>">Quiz Page</a></p>
<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>