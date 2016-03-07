<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Load XML Quiz</title>
</head>
<body>
<h1>New XML Quiz Creator</h1>
<p>Enter the XML file name below (i.e. buddy.xml): </p>
<form action="XMLQuizServlet" method="post">
<p><input type="text" name="fileName" size="50" value=""></p>
<p><input type="submit" value = "Load File"></p>
</form>
</body>
</html>