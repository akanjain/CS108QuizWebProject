<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style>

#cssTable
{
    text-align:center; 
    vertical-align:middle;
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
		out.println("<tr><td align=\"center\">Rank</td><td align=\"center\">Title</td><td align=\"right\">Plays</td></tr>");
		
		int rank = 1;
		while (rs.next()) {
			out.println("<tr><td align=\"center\">" + rank + "</td><td><a href=\"QuizPage.jsp?id=" + rs.getString("quizId") +  "\">" + rs.getString("title") + "</td><td align=\"right\">" + rs.getString("COUNT(quizId)") + "</td></tr>");
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
		out.println("<tr><td align=\"center\">Created Time</td><td align=\"center\">ID</td><td>Title</td>");
		while (rs.next()) {
			out.println("<tr><td align=\"center\"> " + rs.getString("dateCreated") +  "</td><td align=\"left\">" + rs.getString("quizId") + "</td><td = align=\"center\">" + rs.getString("title") + "</td>");
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
		out.println("<tr><td>Time</td><td>ID</td><td>Title</td><td>Score</td><td>Duration</td>");
		while(rs.next()) {
			out.println("<tr><td>" + rs.getString("time") +  "</td><td>" + rs.getString("quizId") + "</td><td>" + rs.getString("title") + "</td><td>" + rs.getString("score") + "</td><td>" + rs.getString("duration") + "</td><td>");
		}
		out.println("</table>");
	}
	

%>
<p><a href="message.jsp">Messages</a>   You have <%= userDataManager.getUserNumNewMessages(username) %> new message(s).</p>
<p><a href="friendRequest.jsp">Friend Requests</a>   You have <%= userDataManager.getUserNumNewFriendRequests(username) %> new friend request(s).</p>
<p><a href="challenge.jsp">Challenges</a>   You have <%= userDataManager.getUserNumNewChallenges(username) %> new challenge(s).</p>
<p><a href="CreateQuiz.jsp">Create New Quiz</a></p>
<p><u>List of all quizzes:</u></p>
<ul>
<%
	
	ResultSet quizrs = QzManager.getQuizList();
	if (quizrs.next()) {
%>
<li><a href="QuizPage.jsp?id=<%= quizrs.getString("quizId") %>"><%= quizrs.getString("title") %></a></li>
<%		
	} else {
%>
<li><p>No quizzes</p></li>
<%	
	}
	while (quizrs.next()) {
%>
<li><a href="QuizPage.jsp?id=<%= quizrs.getString("quizId") %>"><%= quizrs.getString("title") %></a></li>
<%
	}
%>
</ul>
<p><u>List of quizzes created by you:</u></p>
<ul>
<%
	ResultSet quizUserRs = QzManager.getQuizListbyUser(username);
	if (quizUserRs.next()) {
%>
<li><a href="QuizPage.jsp?id=<%= quizUserRs.getString("quizId") %>"><%= quizUserRs.getString("title") %></a></li>
<%		
	} else {
%>
<li><p>No quizzes created by you</p></li>
<%	
	}
	while (quizUserRs.next()) {
%>
<li><a href="QuizPage.jsp?id=<%= quizUserRs.getString("quizId") %>"><%= quizUserRs.getString("title") %></a></li>
<%
	}
%>
</ul>
<p><a href="searchUsers.jsp">Find Players</a></p>
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
</body>
</html>
