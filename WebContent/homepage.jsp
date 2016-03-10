<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style type="text/css">

#cssTable
{
    text-align:center; 
    vertical-align:middle;
}
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
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
	/* Store username to session. */
	if (request.getParameter("username") != null) {
		request.getSession().setAttribute("username", request.getParameter("username"));
	}

	String username = (String) request.getSession().getAttribute("username");
	UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	ResultSet rs = null;
	
%>
<title>Welcome <%= username %></title>
</head>
<body>
<h1>Welcome <%= username %></h1>

<h3>Announcements</h3>
<%
	rs = userDataManager.getAnnouncements();
	
	if (!rs.isBeforeFirst()) {
		out.println("<p> There is no announcement </p>");
	} else {
		while (rs.next()) {
		String timeStamp = rs.getString("time");
		String author = rs.getString("username");
		String announcement = rs.getString("announcement");
		
		out.println ("<p>" + timeStamp + " by " + author + ":" + announcement);
		}
	}
%>

<h3>Popular Quizzes</h3>
<%
	rs = QzManager.getPopularQuizzes(5);

	if (!rs.isBeforeFirst()) {
		out.println("<p>There is no quiz in the system.</p>");
	} else {
		out.println("<table style=\"border:20px\">");
		out.println("<tr><th align=\"center\">Rank</th><th align=\"center\">Title</th><th align=\"center\">Plays</th></tr>");
		
		int rank = 1;
		while (rs.next()) {
			out.println("<tr><td align=\"center\">" + rank + "</td><td align=\"center\"><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td><td align=\"center\">" + rs.getString("COUNT(quizId)") + "</td></tr>");
			rank++;
		}
		out.println("</table>");
	}
	
%>

<h3>New Quizzes</h3>
<%
	rs = QzManager.getRecentlyCreatedQuizzes(5);

	if (!rs.isBeforeFirst()) {
		out.println("<p>There is no new quiz in the system.</p>");
	} else {
		out.println("<table id=\"cssTable\" style=\"border:20px\">");
		out.println("<tr><th align=\"center\">Created Time</th><th align=\"center\">ID</th><th>Title</th>");
		while (rs.next()) {
			out.println("<tr><td align=\"center\"> " + rs.getString("dateCreated") +  "</td><td align=\"center\">" + rs.getString("quizId") + "</td><td = align=\"center\"><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td>");
		}
		out.println("</table>");
	}
	

	
%>

<h3>Recent Activities</h3>

<%
	rs = QzManager.getUserRecentActivities(username, 5);
	
	
	if (!rs.isBeforeFirst()) {
		out.println("<p>You have no recent activity.</p>");
	} else {
		out.println("<table id=\"cssTable\" align style=\"border:20px\">");
		out.println("<tr><th>Time</th><th>ID</th><th>Title</th><th>Score</th><th>Duration</th>");
		while(rs.next()) {
			out.println("<tr><td>" + rs.getString("time") +  "</td><td>" + rs.getString("quizId") + "</td><td><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td><td>" + rs.getString("score") + "</td><td>" + rs.getString("duration") + "</td>");
		}
		out.println("</table>");
	}
	

%>
<h3>List of Quizzes created recently by you:</h3>
<%
	rs = QzManager.getRecentlyCreatedQuizzes(username, 5);

	if (!rs.isBeforeFirst()) {
		out.println("<p>There is no new quiz in the system created by you.</p>");
	} else {
		out.println("<table style=\"border:20px\">");
		out.println("<tr><th align=\"center\">Created Time</th><th align=\"center\">ID</th><th align=\"center\">Title</th>");
		while (rs.next()) {
			out.println("<tr><td align=\"center\"> " + rs.getString("dateCreated") +  "</td><td align=\"center\">" + rs.getString("quizId") + "</td><td = align=\"center\"><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td>");
		}
		out.println("</table>");
	}
	

	
%>
<p><a href="message.jsp">Messages</a>   You have <%= userDataManager.getUserNumNewMessages(username) %> new message(s).</p>
<p><a href="friendRequest.jsp">Friend Requests</a>   You have <%= userDataManager.getUserNumNewFriendRequests(username) %> new friend request(s).</p>
<p><a href="challenge.jsp">Challenges</a>   You have <%= userDataManager.getUserNumNewChallenges(username) %> new challenge(s).</p>
<p><a href="searchUsers.jsp">Find Players</a></p>
<p><a href="CreateQuiz.jsp">Create New Quiz</a></p>
<p><a href="quizbycategorypage.jsp">Find Quiz by Categories</a></p>
<p><a href="quizbytagpage.jsp">Find Quiz by Tags</a></p>
<p><a href="quizbyrating.jsp">Find Quiz by Ratings</a></p>
<p><a href="history.jsp">History</a></p>
<p><a href="administratorTools.jsp"> Administrator Tools</a></p>

<%
	
%>



<h3>Achievements</h3>
<%
	
	rs = userDataManager.getUserAchievements(username);

	if (!rs.isBeforeFirst()) {
		out.println("<p> You have no achievement </p>");
	} else {
		while (rs.next()) {
		String achievement = rs.getString("achievement");
		
		out.println ("<p>" + achievement + "</p>" );
		}
	}
%>

<h3>Your Friends' Recent Activities </h3>
<%
	List<String> friendActivities = userDataManager.getUserFriendsRecentActivities(username, 10);
	out.println("<table style=\"border:20px\">");
	for (String activity : friendActivities) {
		out.println("<tr><td>" + activity + "</td><tr>");
	}
	out.println("</table>");
%>
	
	
	
</body>
</html>
