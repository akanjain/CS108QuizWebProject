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
<title><%= username %>'s message</title>
</head>
<body>
	<%
		
		UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		userDataManager.markUserAllMessagesViewed(username);
		
		ResultSet rs = userDataManager.getUserMessages(username);
		
		out.println("<h2>Your Messages:</h2>");
		/* Check if there is any message. */
		if (!rs.isBeforeFirst()) { /* Have no message. */  
			out.println("You have no message.");
		} else {
			while (rs.next()) { 
				if (rs.getString("fromUser").equals(username)) {
					out.println("<b>Message Sent to:</b> "+ rs.getString("toUser") + " at " + rs.getString("time") + "</br>"); 
					out.println("Message: " + rs.getString("Message") + "</br></br>");
				} else if (rs.getString("toUser").equals(username)) {
					out.println("<b>Message Received from:</b> "+ rs.getString("fromUser") + " at " + rs.getString("time") + "</br>"); 
					out.println("Message: " + rs.getString("Message") + "</br></br>");
				}
			}
		}
		
	%>
	
	
<form action="MessageServlet" method="post">
<h2>Send a Message</h2>
<p>Receiver<input type="text" name="toUser"></p>
<p>Message<input type="text" name="message"></p>
<input type="submit" value="send message">
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