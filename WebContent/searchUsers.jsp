<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="web.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Find Players</title>
</head>
<body>
<h3>Enter keyword to search for users.</h3>
<p>(Leave the field blank to list all users)</p>
<form action="SearchUsersServlet" method="post">
<input type="text" name="keyword">
<input type="submit" name="search">
<input type="hidden" name="action" value="search">

<h3>Search Results</h3>
</form>


<%

	List<String> returnStatusList = (List<String>) request.getAttribute("Return Status List");
	if (returnStatusList != null) {
		for (String returnStatus : returnStatusList) {
			out.println("<p>" + returnStatus + "</p>");
		}
	}

	
	ResultSet rs = (ResultSet) request.getAttribute("Search Result");
	if (rs != null) {
		if (!rs.isBeforeFirst()) {
			out.println("<p>There is no match for your search</p>.");
		} else {
			out.println("<p>To add user as friend, select user and click \"add to friends\"</p>");
			out.println("<form action=\"SearchUsersServlet\" method=\"post\">");
			
			UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
			String currUserName = (String) request.getSession().getAttribute("username");
			List<String> userList = new LinkedList<String>();
			
			
			// TODO: Interleave problem.
			while (rs.next()) {
				userList.add(rs.getString("username"));
			}
			
			out.println("<table>");
			for (String username : userList) {
				
				if (username.equals(currUserName)) {
					out.println("<tr><td><a href=\"userpage.jsp?username=" + username + "\">" + username + "</a></td><td> This is you.</td></tr>");
				} else if (userDataManager.areFriends(currUserName, username)) {
					out.println("<tr><td><a href=\"userpage.jsp?username=" + username + "\">" + username + "</a></td><td>  Already your friend.</td></tr>");
				} else {
					out.println("<tr><td><a href=\"userpage.jsp?username=" + username + "\">" + username + "</a></td><td>  <input type=\"checkbox\" name=\"" + username + "\"></td></tr>");
				}
				
			}
			out.println("</table>");
			out.println("<p><input type=\"submit\" value=\"add to friends\">");
			out.println("<input type=\"hidden\" name=\"action\" value=\"add\">");
			out.println("</form>");
		}
	}
	
%>

<p>Go back to <a href="homepage.jsp">Homepage</a></p>	
</body>
</html>