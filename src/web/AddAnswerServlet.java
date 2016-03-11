package web;

import java.io.IOException;
import web.UserDataManager.Achievements;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddAnswerServlet
 */
@WebServlet("/AddAnswerServlet")
public class AddAnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddAnswerServlet() {
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
		
		int quizNumber = (Integer) session.getAttribute("quizNumber");
		int currentQuestion = (Integer) session.getAttribute("currentQuestion");
		int numQuestions = (Integer) session.getAttribute("numQuestion");
		
		String questionType = request.getParameter("quesType");
		int questionNumber = Integer.parseInt(request.getParameter("questionNumber"));
		int numChoices = Integer.parseInt(request.getParameter("numChoices"));
		
		if (questionType.equals("multiple-choice") || questionType.equals("multiple-choice-multiple-answer")) {
			String[] ansArray = new String[numChoices];
			boolean[] correctArray = new boolean[numChoices];
			String[] options = request.getParameterValues("correctOptions");
			if (options != null) {
				for (String s : options) {
					int choice = Integer.parseInt(s);
					correctArray[choice-1] = true;
				}
			}
			for (int i = 1; i <= numChoices; i++) {
				String ans = request.getParameter("" + i);
				ansArray[i-1] = ans;
			}
			QzManager.addMultiChoiceAnswer(questionNumber, ansArray, correctArray);
		} else if (questionType.equals("matching")) {
			String[] choiceArray = new String[numChoices];
			String[] answerArray = new String[numChoices];
			for (int i = 1; i <= numChoices; i++) {
				String choiceText = request.getParameter("choice" + i);
				String answerText = request.getParameter("answer" + i);
				choiceArray[i-1] = choiceText;
				answerArray[i-1] = answerText;
			}
			QzManager.addMatchingAnswer(questionNumber, choiceArray, answerArray);
		} else {
			String[] ansArray = new String[numChoices];
			for (int i = 1; i <= numChoices; i++) {
				String ans = request.getParameter("" + i);
				ansArray[i-1] = ans;
			}
			QzManager.addAnswer(questionNumber, ansArray);
		}

		if (currentQuestion < numQuestions) {
			session.setAttribute("currentQuestion", currentQuestion+1);
			RequestDispatcher dispatcher = request.getRequestDispatcher("AddQuestion.jsp");
			dispatcher.forward(request, response);
		} else if (currentQuestion == numQuestions) {
			QzManager.updateQuizCreation(quizNumber);
			userDataManager.updateUserAchievements(Achievements.CREATE_QUIZ, username, quizNumber, 0);
			RequestDispatcher dispatcher = request.getRequestDispatcher("QuizCreated.jsp");
			dispatcher.forward(request, response);
		}
	}

}
