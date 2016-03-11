<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
table, th, td {
    border: 1px solid black;
}

tr:nth-child(even) {background-color: #f2f2f2}

th {
    background-color: #E0FFFF;
    color: black;
}

table {
    width: 50%;
}

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
	UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
	ResultSet rs = QzManager.getQuiz(Integer.parseInt(idName));
	String creatorName = null;
	String isPractice = "false";
	if (rs.next()) {
		creatorName = rs.getString("creatorUsername");
		isPractice = rs.getString("isPracticeMode");
	}
	String username = (String) request.getSession().getAttribute("username");
	int timeFrame = 6*24*60;
%>
	<h1>Welcome to Quiz Page.</h1>
	<h3><u>Quiz ID:</u> <%= idName %></h3>
	<h3><u>Quiz Title:</u> <%= rs.getString("title") %></h3>
	<h3><u>Quiz Description:</u> <%= rs.getString("description") %></h3>
	<h3><u>Quiz Max Score:</u> <%= rs.getString("maxScore") %></h3>
	<h3><u>Quiz Category:</u> <a href="quizbycategorypage.jsp?id=<%= rs.getString("category") %>"><%= rs.getString("category") %></a></h3>
<%
	rs = QzManager.getQuizTags(Integer.parseInt(idName));
	Set<String> allTags = new TreeSet<String>();
	while (rs.next()) {
		allTags.add(rs.getString("tagName").toLowerCase());
	}
	if (allTags.size() == 0) {
		out.println("<h3><u>Quiz Tags:</u> No Tags Assigned.");
	} else {
%>
		<h3><u>Quiz Tags:</u> [
<%		
		Iterator<String> iter = allTags.iterator();
		if (iter.hasNext()) {
			String s = iter.next();
%>
			<a href="quizbytagpage.jsp?id=<%= s %>"><%= s %></a>
<%			
		}
		while (iter.hasNext()) {
			String s = iter.next();
%>
			, <a href="quizbytagpage.jsp?id=<%= s %>"><%= s %></a>
<%
		}
%>
		]</h3>
<%
	}
	if (userDataManager.isUserAccountValid(creatorName)) {
%>
	<h3><b>Quiz Created by</b> <a href="userpage.jsp?username=<%= creatorName %>"><%= creatorName %></a></h3>
<%
	} else {
%>
	<h3><b>Quiz Created by</b> <%= creatorName %></h3>		
<%		
	}
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
	<td align="center"><a href="userpage.jsp?username=<%= highestperfrs.getString("username") %>"><%= highestperfrs.getString("username") %></a></td> 
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
	ResultSet currenthighestperfrs = QzManager.getHighestPerformers(Integer.parseInt(idName), timeFrame, 5);
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
	<td align="center"><a href="userpage.jsp?username=<%= currenthighestperfrs.getString("username") %>"><%= currenthighestperfrs.getString("username") %></a></td> 
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
	ResultSet currentperfrs = QzManager.getPerformers(Integer.parseInt(idName), timeFrame, 20);
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
	<td align="center"><a href="userpage.jsp?username=<%= currentperfrs.getString("username") %>"><%= currentperfrs.getString("username") %></a></td> 
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
<h3><u>Summary Statistics of Users taken this quiz:</u></h3>
<%	
	//ResultSet currentperfrs = QzManager.getPerformers(Integer.parseInt(idName), timeFrame, 20);
	Map<String, String> userQuizRecord = QzManager.getAllUserQuizRecords(Integer.parseInt(idName));
	if (userQuizRecord.size() == 0) {
		out.println("<p>No user has played this quiz in past.</p>");
	} else {
%>
<table> 
<thead> 
<tr> 
    <th align="center">User</th> 
    <th align="center">No. of Attempts</th>
    <th align="center">Best Score</th> 
    <th align="center">Worst Score</th> 
</tr> 
</thead> 
<tbody> 
<%
		for (String key: userQuizRecord.keySet()) {
			String[] firstpart = userQuizRecord.get(key).split("_");
			String[] bestScore = firstpart[1].split(",");
			String[] worstScore = firstpart[2].split(",");
%>
<tr>
	<td align="center"><a href="userpage.jsp?username=<%= key %>"><%= key %></a></td> 
    <td align="center"><%= firstpart[0] %></td> 
    <td align="center"><%= bestScore[0] + ", " + bestScore[1] + "sec" %></td> 
    <td align="center"><%= worstScore[0] + ", " + worstScore[1] + "sec" %></td> 
</tr> 
<%
		}
%>
</tbody> 
</table>
<%
	}
%> 
<form action="QuizPlayServlet" method="post">
<p>
<input name="quizId" type="hidden" value="<%= idName %>"/>
<input name="quizMode" type="hidden" value="realtest"/>
<%
	if (username.equals("guest")) {
		out.println("<input type=\"submit\" value=\"Start Taking Quiz\" disabled/></p>");
	} else {
		out.println("<input type=\"submit\" value=\"Start Taking Quiz\" /></p>");
	}
%>
</form>
<%
	if (isPractice.equals("true")) {
%>
<form action="QuizPlayServlet" method="post">
<p>
<input name="quizId" type="hidden" value="<%= idName %>"/>
<input name="quizMode" type="hidden" value="practice"/>
<%
	if (username.equals("guest")) {
		out.println("<input type=\"submit\" value=\"Start Taking Quiz in Practice Mode\" disabled/> You must login to take quiz.</p>");
	} else {
		out.println("<input type=\"submit\" value=\"Start Taking Quiz in Practice Mode\" /></p>");
	}
%>
</form>
<%
	}
%>
<form action="AddTagServlet" method="post">
<input name="quizId" type="hidden" value="<%= idName %>"/>
<h3><u>Enter Tag Name in the text box below and click "Add Tag" to assign tag to quiz.</u></h3>
<p><input type="text" name="tagname" />
<%
	if (username.equals("guest")) {
%>
<input type="submit" value="Add Tag" disabled/>You must login to add tag.</p>
<%
	} else {
%>
<input type="submit" value="Add Tag"/></p>
<%
	}
%>
</form>
<%
	if (username.equals("guest")) {
		out.println("<p>Go back to <a href=\"guestHomepage.jsp\">Homepage</a></p>");
	} else {
		out.println("<p>Go back to <a href=\"homepage.jsp\">Homepage</a></p>");
	}
%>

</body>
</html>