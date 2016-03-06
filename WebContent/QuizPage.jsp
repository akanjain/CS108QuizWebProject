<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Summary Page</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://tablesorter.com/__jquery.tablesorter.min.js"></script>
<script>
$(document).ready(function(){
	{ 
        $("#myTable").tablesorter( {sortList: [[0,1]]} ); 
    } 
});
</script>
<style type="text/css">
table.tablesorter {
	font-family:arial;
	background-color: #CDCDCD;
	margin:10px 0pt 15px;
	font-size: 8pt;
	width: 50%;
	text-align: left;
}
table.tablesorter thead tr th, table.tablesorter tfoot tr th {
	background-color: #e6EEEE;
	border: 1px solid #FFF;
	font-size: 8pt;
	padding: 4px;
}
table.tablesorter thead tr .header {
	background-image: url(img/bg.gif);
	background-repeat: no-repeat;
	background-position: center right;
	cursor: pointer;
}
table.tablesorter tbody td {
	color: #3D3D3D;
	padding: 4px;
	background-color: #FFF;
	vertical-align: top;
}
table.tablesorter tbody tr.odd td {
	background-color:#F0F0F6;
}
table.tablesorter thead tr .headerSortUp {
	background-image: url(img/asc.gif);
}
table.tablesorter thead tr .headerSortDown {
	background-image: url(img/desc.gif);
}
table.tablesorter thead tr .headerSortDown, table.tablesorter thead tr .headerSortUp {
background-color: #8dbdd8;
}  
</style>
</head>
<body>
<%
	String idName = request.getParameter("id");
	request.getSession().setAttribute("QuizId", idName);
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	ResultSet rs = QzManager.getQuiz(Integer.parseInt(idName));
	String creatorName = null;
	if (rs.next()) {
		creatorName = rs.getString("creatorUsername");
	}
	String username = (String) request.getSession().getAttribute("username");
%>
	<h1>Welcome to Quiz Page.</h1>
	<h3><u>Quiz Title:</u> <%= rs.getString("title") %></h3>
	<h3><u>Quiz Description:</u> <%= rs.getString("description") %></h3>
	<h3><u>Quiz Category:</u> <%= rs.getString("category") %></h3>
	<h3><b>Quiz Created by</b> <a href="userpage.jsp?username=<%= creatorName %>"><%= creatorName %></a></h3>
<%
	rs = QzManager.getQuizRatingReview(Integer.parseInt(idName));
	int totalRating = 0;
	int totalReviews = 0;
	while (rs.next()) {
		totalReviews++;
		String rate = rs.getString("rating");
		totalRating += Integer.parseInt(rate);
	}
	double calculated = 0.0;
	if (totalReviews != 0) {
		calculated = (((double) totalRating)/totalReviews)/5*100;
	}
%>
<h3><u>Quiz Rating:</u></h3>
<%
	if (totalReviews == 0) {
%>
		No Rating and Reviews.
<%
	} else {
%>
<span style="display: block; width: 65px; height: 13px; background: url('http://3.bp.blogspot.com/-20YHLJH00QE/UCI80sALTCI/AAAAAAAABaU/qY-6r-Rz5hw/s600/star-rating-sprite.png') 0 0;">
    <span style="display: block; width: <%= calculated %>%; height: 13px; background: url('http://3.bp.blogspot.com/-20YHLJH00QE/UCI80sALTCI/AAAAAAAABaU/qY-6r-Rz5hw/s600/star-rating-sprite.png') 0 -13px;"></span>
</span>
<%
	}
%>
<p><b>Read</b> <a href="readreview.jsp?id=<%= idName %>"><b>Reviews</b></a></p>
<%
	String currentUser = (String) request.getSession().getAttribute("username");
	if (currentUser.equals(creatorName)) {
%>
<p><a href="viewquiz.jsp?id=<%= idName %>" ><b>View Created Quiz</b></a></p>
<%
	}
%>
<h3><u><%= username %>'s past performance (Click on table column names to sort by that column):</u></h3>
<%	
	ResultSet perfrs = QzManager.getUserQuizPerformance(username, Integer.parseInt(idName), 5);
	if (!perfrs.isBeforeFirst()) {
		out.println("<p>There is no record of quiz taken by you in the system.</p>");
	} else {
%>
<table id="myTable" class="tablesorter"> 
<thead> 
<tr> 
    <th align="center">Quiz Taken Date</th> 
    <th align="center">Score</th> 
    <th align="center">Time Taken</th> 
</tr> 
</thead> 
<tbody> 
<%
		while (perfrs.next()) {
%>
<tr> 
    <td align="center"><%= perfrs.getString("time") %></td> 
    <td align="center"><%= perfrs.getString("score") %></td> 
    <td align="center"><%= perfrs.getString("duration") %></td> 
</tr> 
<%
		}
%>
</tbody> 
</table>
<%
	}
%> 
<h3><u>List of highest performers:</u></h3>
<%	
	ResultSet highestperfrs = QzManager.getHighestPerformers(Integer.parseInt(idName), 20);
	if (!highestperfrs.isBeforeFirst()) {
		out.println("<p>No user has played this quiz before.</p>");
	} else {
%>
<table id="highestPerfTable"> 
<thead> 
<tr> 
    <th align="center">User</th> 
    <th align="center">Date Quiz Taken</th>
    <th align="center">Score</th> 
    <th align="center">Time Taken</th> 
</tr> 
</thead> 
<tbody> 
<%
		while (highestperfrs.next()) {
%>
<tr>
	<td align="center"><%= highestperfrs.getString("username") %></td> 
    <td align="center"><%= highestperfrs.getString("time") %></td> 
    <td align="center"><%= highestperfrs.getString("score") %></td> 
    <td align="center"><%= highestperfrs.getString("duration") %></td> 
</tr> 
<%
		}
%>
</tbody> 
</table>
<%
	}
%> 
<h3><u>List of recent top performers:</u></h3>
<%	
	ResultSet currenthighestperfrs = QzManager.getHighestPerformers(Integer.parseInt(idName), 5*24*60, 5);
	if (!currenthighestperfrs.isBeforeFirst()) {
		out.println("<p>No user has played this quiz recently.</p>");
	} else {
%>
<table id="currentHighestPerfTable"> 
<thead> 
<tr> 
    <th align="center">User</th> 
    <th align="center">Date Quiz Taken</th>
    <th align="center">Score</th> 
    <th align="center">Time Taken</th> 
</tr> 
</thead> 
<tbody> 
<%
		while (currenthighestperfrs.next()) {
%>
<tr>
	<td align="center"><%= currenthighestperfrs.getString("username") %></td> 
    <td align="center"><%= currenthighestperfrs.getString("time") %></td> 
    <td align="center"><%= currenthighestperfrs.getString("score") %></td> 
    <td align="center"><%= currenthighestperfrs.getString("duration") %></td> 
</tr> 
<%
		}
%>
</tbody> 
</table>
<%
	}
%> 
<h3><u>List of recent quiz takers and their score:</u></h3>
<%	
	ResultSet currentperfrs = QzManager.getPerformers(Integer.parseInt(idName), 5*24*60, 20);
	if (!currentperfrs.isBeforeFirst()) {
		out.println("<p>No user has played this quiz recently.</p>");
	} else {
%>
<table id="currentPerfTable"> 
<thead> 
<tr> 
    <th align="center">User</th> 
    <th align="center">Date Quiz Taken</th>
    <th align="center">Score</th> 
    <th align="center">Time Taken</th> 
</tr> 
</thead> 
<tbody> 
<%
		while (currentperfrs.next()) {
%>
<tr>
	<td align="center"><%= currentperfrs.getString("username") %></td> 
    <td align="center"><%= currentperfrs.getString("time") %></td> 
    <td align="center"><%= currentperfrs.getString("score") %></td> 
    <td align="center"><%= currentperfrs.getString("duration") %></td> 
</tr> 
<%
		}
%>
</tbody> 
</table>
<%
	}
%> 
<p>print summary statistics</p>
<form action="QuizPlayServlet" method="post">
<p>
<input name="quizId" type="hidden" value="<%= idName %>"/>
<input name="quizMode" type="hidden" value="realtest"/>
<input type="submit" value="Start Taking Quiz" /></p>
</form>
<form action="QuizPlayServlet" method="post">
<p>
<input name="quizId" type="hidden" value="<%= idName %>"/>
<input name="quizMode" type="hidden" value="practice"/>
<input type="submit" value="Start Taking Quiz in Practice Mode" /></p>
</form>
<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>