<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% String username = (String) request.getSession().getAttribute("username"); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= username %>'s friend request</title>
</head>
<body>
	<%
		
		UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		userDataManager.markUserAllFriendRequestsViewed(username);	
		
		ResultSet rs = userDataManager.getUserFriendRequests(username);
	
		out.println("<h2>Your Friend Request</h2>");
		/* Check if there is any message. */
		if (!rs.isBeforeFirst()) { /* Have no friend requests. */  
			out.println("You have no new friend request.");
		} else {
			while (rs.next()) {
				/* Request from current user, still pending for receiver's approval. */
				if (rs.getString("fromUser").equals(username)) {
					out.println("<p>From: " + rs.getString("fromUser") + ", To: "+ rs.getString("toUser") + ", Pending for reicever's approval.</p>"); 
				/* Request sent to current user. */
				} else {
					assert rs.getString("toUser").equals(username);
					out.println("<p>");
					out.println("From: " + rs.getString("fromUser") + ", To: " + rs.getString("toUser") + ", Message: " + rs.getString("Message"));
					
					/* A button for accepting the request. */
					out.println("<form action=\"AcceptFriendRequestServlet\" method=\"post\">");
					out.println("<input type=\"submit\" value=\"accept\">");
					out.println("<input type=\"hidden\" name=\"fromUser\" value=\"" + rs.getString("fromUser") + "\">");
					
					out.println("</form>");
					out.println("</p>");
				}
			}		
		}
		
	%>
	
	
<form action="FriendRequestServlet" method="post">
<h2>Send a Friend Request</h2>
<p>Receiver<input type="text" name="toUser"></p>
<p>Message<input type="text" name="message"></p>
<input type="submit" value="send friend request">
</form>
	<%
		String returnStatus = (String) request.getAttribute("Return Status");
		if ( returnStatus != null) {
			out.println(returnStatus);
		}
	%>

<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>