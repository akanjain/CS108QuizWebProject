package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import web.UserDataManager.Achievements;

/**
 * Servlet implementation class QuizCreator
 */
@WebServlet("/QuizCreatorServlet")
public class QuizCreatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizCreatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ServletContext sc = request.getServletContext();
		QuizManager QzManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
		UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		String username = (String) request.getSession().getAttribute("username");
		
		String quizTitle = request.getParameter("quizTitle");
		String quizDescription = request.getParameter("quizDescription");
		String quizCategory = request.getParameter("quizCategory");
		String creatorName = request.getParameter("quizCreator");
		String dateCreated = request.getParameter("quizDate");
		String[] options = request.getParameterValues("preferences");
		boolean isRandom = false;
		boolean isOnePage = true;
		boolean isImmediate = false;
		boolean isPracticeMode = false;
		if (options != null) {
			for (String s : options) {
				if (s.equals("random")) {
					isRandom = true;
				} else if (s.equals("multiple")) {
					isOnePage = false;
				} else if (s.equals("immediate")) {
					isImmediate = true;
				} else if (s.equals("practice")) {
					isPracticeMode = true;
				}
			}
		}
		if (quizCategory.equals("")) {
			quizCategory = "Other";
		}
		int numQuestions = Integer.parseInt(request.getParameter("numQuestion"));
		int quizNum = QzManager.createQuiz(quizTitle, quizDescription, quizCategory, creatorName, dateCreated, isRandom, isOnePage, isImmediate, isPracticeMode, numQuestions);
		userDataManager.updateUserAchievements(Achievements.CREATE_QUIZ, username, quizNum, 0);
		session.setAttribute("quizNumber", quizNum);
		session.setAttribute("currentQuestion", 1);
		session.setAttribute("numQuestion", numQuestions);
		RequestDispatcher dispatcher = request.getRequestDispatcher("AddQuestion.jsp");
		dispatcher.forward(request, response);
	}

}
