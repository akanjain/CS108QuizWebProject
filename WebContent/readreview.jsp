<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Read Reviews</title>
</head>
<body>
<h1>Player Reviews:</h1>
<%
	String idName = request.getParameter("id");
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	ResultSet rs = QzManager.getQuizRatingReview(Integer.parseInt(idName));
	boolean flag = false;
	while (rs.next()) {
		flag = true;
		String rate = rs.getString("rating");
		int currentRating = Integer.parseInt(rate);
		double calculated = (((double) currentRating)/5)*100;
		String currentReview = rs.getString("review");
		String currentUser = rs.getString("username");
%>		
	<span style="display: block; width: 65px; height: 13px; background: url('http://3.bp.blogspot.com/-20YHLJH00QE/UCI80sALTCI/AAAAAAAABaU/qY-6r-Rz5hw/s600/star-rating-sprite.png') 0 0;">
    <span style="display: block; width: <%= calculated %>%; height: 13px; background: url('http://3.bp.blogspot.com/-20YHLJH00QE/UCI80sALTCI/AAAAAAAABaU/qY-6r-Rz5hw/s600/star-rating-sprite.png') 0 -13px;"></span>
	</span>	
	<b>by <%= currentUser %></b>
	<p><%= currentReview %></p>
<%
	}
	if (!flag) {
%>
		<h3>No Ratings and Reviews.</h3>
<%
	}
%>
<p>Go back to <a href="QuizPage.jsp?id=<%= idName %>">Quiz Page</a></p>
<%
	if (((String) request.getSession().getAttribute("username")).equals("guest")) {
		out.println("<p>Go back to <a href=\"guestHomepage.jsp\">Homepage</a></p>");
	} else {
		out.println("<p>Go back to <a href=\"homepage.jsp\">Homepage</a></p>");
	}
%>
</body>
</html>