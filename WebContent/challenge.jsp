<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% String username = (String) request.getSession().getAttribute("username"); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= username %>'s Challenges</title>
</head>
<body>

<h1>Send a Challenge</h1>
<p> Note: to send a challenge, select a friend and enter a quiz id, then press "Send a Challenge" </p>

	<%
	
		UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");	
		userDataManager.markUserAllChallengesViewed(username);
		
		ResultSet rs = userDataManager.getUserFriends(username);
		
		if (!rs.isBeforeFirst()) {
			out.println("<p><b>You don't have any friend to challenge! Add some friends and challenge them!</b></p>");
		} else {
			out.println("<form action=\"ChallengeServlet\" method=\"post\">");
			boolean firstUserChecked = false;
			while (rs.next()) {
				if (!firstUserChecked) {
					out.println("<p><input type=\"radio\" name=\"toUser\" value=\"" + rs.getString("toUser") + "\" checked>" + rs.getString("toUser") + "</p>");
					firstUserChecked = true;
				} else {
					out.println("<p><input type=\"radio\" name=\"toUser\" value=\"" + rs.getString("toUser") + "\">" + rs.getString("toUser") + "</p>");
				}		
			}
			out.println("<p><input type=\"text\" name=\"quizId\"> (Enter quiz ID here to send challenge)</p>");
			out.println("<p><input type=\"submit\" value=\"Send a Challenge!\"></p>");
			out.println("<input type=\"hidden\" name=\"option\" value=\"send\">");
			out.println("</form>");		
		}
		
		
	%>

	<%
		String returnStatus = (String) request.getAttribute("Return Status");
		if ( returnStatus != null) {
			out.println(returnStatus);
		}
	%>
<h1> Challenges received: </h1>
	<%
		List<UserDataManager.Challenge> challenges = (List<UserDataManager.Challenge>) userDataManager.getUserChallengeStrings(username);
		
		if (challenges.isEmpty()) {
			out.println("You currently have no challenge.");
		} else {
			for (UserDataManager.Challenge challenge : challenges) {
				out.println("<form action=\"ChallengeServlet\" method=\"post\">");
				out.println("From: " + challenge.getFromUser() + ", Quiz ID: " + challenge.getQuizId() + ", Best Score: " + challenge.getBestScore());			
				out.println("<input type=\"hidden\" name=\"fromUser\" value=\"" + challenge.getFromUser() + "\">");
				out.println("<input type=\"hidden\" name=\"toUser\" value=\"" + challenge.getToUser() + "\">");
				out.println("<input type=\"hidden\" name=\"quizId\" value=\"" + challenge.getQuizId() + "\">");	
				out.println("<input type=\"hidden\" name=\"option\" value=\"take\">");
				out.println("<input type=\"submit\" value=\"Take This Challenge\">");		
				out.println("</form>");
			}
		}
		
	%>

<br>
<br>
<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>