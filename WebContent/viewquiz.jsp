<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="web.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Quiz</title>
</head>
<body>
<h1>Below is the created quiz:</h1>
<ul>
<%
	QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
	String idName = request.getParameter("id");
	int quizNumber = Integer.parseInt(idName);
	ResultSet rs = QzManager.getQuiz(quizNumber);
	if (rs.next()) {
%>
<li><p><u>Quiz Title:</u> <%= rs.getString("title") %></p></li>
<li><p><u>Quiz Description:</u> <%= rs.getString("description") %></p></li>
<li><p><u>Quiz Category:</u> <%= rs.getString("category") %></p></li>
<li><p><u>Quiz to be presented random order:</u> <%= rs.getString("isRandom") %></p></li>
<li><p><u>Quiz to be presented in one page:</u> <%= rs.getString("isOnePage") %></p></li>
<%
		if (rs.getString("isOnePage").equals("false")) {
%>
<li><p><u>Quiz to be presented in immediate correction mode:</u> <%= rs.getString("isImmediate") %></p></li>
<%
		}
%>
<li><p><u>Quiz available in practice mode:</u> <%= rs.getString("isPracticeMode") %></p></li>
<%
	}
%>
</ul>
<%
	ResultSet quesrs = QzManager.getQuestion(quizNumber);
	List<String> allQuesText = new ArrayList<String>();
	List<String> allQuesType = new ArrayList<String>();
	List<Integer> allQuesId = new ArrayList<Integer>();
	while (quesrs.next()) {
		allQuesText.add(quesrs.getString("questionText"));
		allQuesType.add(quesrs.getString("questionType"));
		allQuesId.add(Integer.parseInt(quesrs.getString("questionId")));
	}
%>
<%
int count = 1;
for (int i = 0; i < allQuesId.size(); i++) {
	if (allQuesType.get(i).equals("picture-response")) {
		out.println("<p><b>Question " + count + ": </b></p>");
		out.println("<img src=" + allQuesText.get(i) + " alt=\"Image not displayed.\">");
	} else {
		out.println("<p><b>Question " + count + ": </b>" + allQuesText.get(i) + "</p>");
	}
	if (allQuesType.get(i).equals("multiple-choice") || allQuesType.get(i).equals("multiple-choice-multiple-answer")) {
		ResultSet answerOptRs = QzManager.getAnswerOption(allQuesId.get(i));
		out.println("<p><b>Options:</b></p>");
		out.println("<ol>");
		while (answerOptRs.next()) {
			out.print("<li><p>");
			if (answerOptRs.getString("isCorrect").equals("true")) {
				//out.println("<li><p><b>Correct --> </b>" + answerOptRs.getString("optionText") + "</p></li>");
				out.print("<b>Correct --> </b>" + answerOptRs.getString("optionText"));
			} else {
				//out.println("<li><p>" + answerOptRs.getString("optionText") + "</p><li>");
				out.print(answerOptRs.getString("optionText"));
			}
			out.println("</li><p>");
		}
		out.println("</ol>");
	} else if (allQuesType.get(i).equals("matching")) {
		ResultSet matchOptRs = QzManager.getMatchingOption(allQuesId.get(i));
		out.println("<p><b>Matching Answers:</b></p>");
		out.println("<ol>");
		while (matchOptRs.next()) {
			out.print("<li><p>");
			out.print(matchOptRs.getString("optionText") + "<b> ----> " + matchOptRs.getString("matchingText") + "</b>");
			out.println("</li><p>");
		}
		out.println("</ol>");
	} else {
		if (allQuesType.get(i).equals("multiple-answer-unordered")) {
			ResultSet slotRs = QzManager.getAnswerSlot(allQuesId.get(i));
			slotRs.next();
			out.println("<p><b>Number of Answer Slots = " + slotRs.getString("numSlot") + "</b></p>");
		}
		ResultSet answerRs = QzManager.getAnswer(allQuesId.get(i));
		out.println("<p><b>Acceptable Answers:</b></p>");
		out.println("<ol>");
		while (answerRs.next()) {
				out.println("<li><p>" + answerRs.getString("answer") + "</p></li>");
		}
		out.println("</ol>");
	}
	count++;
	out.println("<br>");
}
%>
<p>Go back to <a href="homepage.jsp">Homepage</a></p>
</body>
</html>
