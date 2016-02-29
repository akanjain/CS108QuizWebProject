package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChallengeServlet
 */
@WebServlet("/ChallengeServlet")
public class ChallengeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChallengeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fromUser = (String) request.getSession().getAttribute("username");
		String toUser = (String) request.getParameter("toUser");
		int quizId = Integer.parseInt(request.getParameter("quizId"));
		
		UserDataManager userDataManager =  (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		QuizManager quizManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
		String option = (String) request.getParameter("option");
		String returnStatus;
		
		RequestDispatcher dispatcher;
		
		switch(option) {
			case "send":
				System.out.println("send");
				
				if (!quizManager.quizExist(quizId)) {
					returnStatus = "The quiz id you entered does not exist!";
				} else {
					returnStatus = userDataManager.sendChallenge(fromUser, toUser, quizId);
				}
				
				request.setAttribute("Return Status", returnStatus);	
				dispatcher = request.getRequestDispatcher("challenge.jsp");
				dispatcher.forward(request, response);
				break;
			case "take":
				System.out.println("take");
				userDataManager.removeChallenge(request.getParameter("fromUser"), request.getParameter("toUser"), Integer.parseInt(request.getParameter("quizId")));
				dispatcher = request.getRequestDispatcher("QuizPage.jsp?id=" + request.getParameter("quizId"));
				dispatcher.forward(request, response);
				break;
		}
		
		
		
		
	}

}
