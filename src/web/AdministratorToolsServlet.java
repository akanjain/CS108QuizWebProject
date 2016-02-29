package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AdministratorToolsServelet
 */
@WebServlet("/AdministratorToolsServlet")
public class AdministratorToolsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdministratorToolsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String currentUser = (String) request.getSession().getAttribute("username");	
		UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		QuizManager quizManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
		String option = (String) request.getParameter("option");
		String returnStatus = null;
		String username;
		
		 switch (option) {
		 	case "Create Announcement":
		 		String announcement = (String) request.getParameter("announcement");
		 		returnStatus = userDataManager.createAnnouncement(currentUser, announcement);
		 		break;
		 	case "Promote User":
		 		username = (String) request.getParameter("username");
		 		returnStatus = userDataManager.promoteUser(username);
		 		break;	 		
		 	case "Clear Quiz History": // TODO: Not tested yet
		 		int quizNumber = Integer.parseInt(request.getParameter("quizNumber"));
		 		returnStatus = quizManager.clearQuizHistory(quizNumber);
		 		break;
		 	case "Remove Quiz":
		 		quizNumber = Integer.parseInt(request.getParameter("quizNumber"));
		 		returnStatus = quizManager.removeQuiz(quizNumber);
		 		break;
		 	case "Remove User":
		 		username = (String) request.getParameter("username");
		 		if (username.equals(currentUser)) {
		 			returnStatus = "You cannot remove yourself!!!";
		 		} else {
		 			returnStatus = userDataManager.removeUser(username);	 	
		 		}		
		 }	 
		 
		 request.setAttribute("Return Status", returnStatus);
		 RequestDispatcher dispatcher = request.getRequestDispatcher("administratorTools.jsp");
		 dispatcher.forward(request, response);
	}

}
