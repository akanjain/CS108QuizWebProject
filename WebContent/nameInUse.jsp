<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Account</title>
</head>
<body>
<h1>The Name <%= request.getParameter("username") %> is Already In Use</h1>
<p>Please enter another name and password. </p>
<form action="CreateAccountServlet" method="post">
<table>
	<tr><td>User Name</td><td><input type="text" name="username"><td></tr>
	<tr><td>Password</td><td><input type="text" name="password"></td>
</table>
<input type="submit">
</form>
</body>
</html>