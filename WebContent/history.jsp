<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="http://tablesorter.com/__jquery.tablesorter.min.js"></script>
<script>
$(document).ready(function(){
	{ 
        $("#myTable").tablesorter( {sortList: [[1,1]]} ); 
    } 
});
</script>
<style type="text/css">
table.tablesorter {
	font-family:arial;
	background-color: #CDCDCD;
	margin:10px 0pt 15px;
	font-size: 8pt;
	width: 75%;
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
<head>
<% String username = (String) request.getSession().getAttribute("username"); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= username %>'s History Page</title>
</head>
<h1>Your Quiz History:</h1>
<body>
<%
	UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
	ResultSet rs = userDataManager.getUserHistory(username);
	
	if (!rs.isBeforeFirst()) {
		out.println("<h1>You have no quiz taking history. Try to take some quizzes!</h1>");
	} else {
%>
<table id="myTable" class="tablesorter"> 
<thead> 
<tr> 
    <th align="center">Quiz Title</th> 
    <th align="center">Date Quiz Taken</th>
    <th align="center">Score</th> 
    <th align="center">Time Taken</th> 
</tr> 
</thead> 	
<tbody> 	
<%
		while (rs.next()) {
			String subject = rs.getString("title");
			String time = rs.getString("time");
			String score = rs.getString("score");
			String maxScore = rs.getString("maxScore");
			String duration = rs.getString("duration");
%>
<tr>
	<td align="center"><%= subject %></td> 
    <td align="center"><%= time %></td> 
    <td align="center"><%= score + "/" + maxScore %></td> 
    <td align="center"><%= duration + " sec" %></td> 
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