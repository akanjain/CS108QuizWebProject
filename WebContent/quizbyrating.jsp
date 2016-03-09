<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz by Rating</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://tablesorter.com/__jquery.tablesorter.min.js"></script>
<script>
$(document).ready(function(){
	{ 
        $("#myTable").tablesorter( {sortList: [[3,1]]} ); 
    } 
});
</script>
<style type="text/css">
table.tablesorter {
	font-family:arial;
	background-color: #CDCDCD;
	margin:10px 0pt 15px;
	font-size: 8pt;
	width: 100%;
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
<h1>Quiz Listing by Rating:</h1>
<%
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	Map<String, String> allRatedQuiz = QzManager.getRatedQuizList();
%>
<%	
	if (allRatedQuiz.size() == 0) {
		out.println("<p>There are no rated quizzes avaiable in the system.</p>");
	} else {
%>
<table id="myTable" class="tablesorter"> 
<thead> 
<tr> 
    <th align="center">Created Time</th> 
    <th align="center">ID</th> 
    <th align="center">Title</th>
    <th align="center">No. of Ratings</th>
    <th align="center">Average Rating</th>
</tr> 
</thead> 
<tbody> 
<%
		for (String key : allRatedQuiz.keySet()) {
			String[] parts = allRatedQuiz.get(key).split(",");
%>
<tr> 
    <td align="center"><%= parts[0] %></td> 
    <td align="center"><%= key %></td> 
    <td align="center"><a href="QuizPage.jsp?id=<%= key %>"><%= parts[1] %></a></td> 
    <td align="center"><%= parts[2] %></td>
    <td align="center"><%= parts[3] %></td>
</tr> 
<%
		}
%>
</tbody> 
</table>
<%
	}
%>
<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>