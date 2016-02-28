package web;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
 * Servlet implementation class QuizEvaluateServlet
 */
@WebServlet("/QuizEvaluateServlet")
public class QuizEvaluateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizEvaluateServlet() {
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
		long end = System.currentTimeMillis();
		long start = (Long) request.getSession().getAttribute("startTime");
		HttpSession session = request.getSession();
		ServletContext sc = request.getServletContext();
		
		Quiz currentQuiz = (Quiz) request.getSession().getAttribute("currentQuiz");
		int numQuestion = (Integer) request.getSession().getAttribute("currentQuizTotalQuestions");
		
		for (int i = 0; i < numQuestion; i++) {
			Question qt = currentQuiz.getCurrentQuestion(i);
			List<String> anslist = new ArrayList<String>();
			String[] options = request.getParameterValues("answer-" + i);
			if (options != null) {
				for (String s : options) {
					anslist.add(s);
				}
			}
			currentQuiz.setAnswers(i, anslist);
		}
		
		int score = currentQuiz.calculateScore();
		request.setAttribute("Score", score);
		long totalTime = TimeUnit.MILLISECONDS.toSeconds(end-start);
		String timeTaken = totalTime + " seconds";
		request.setAttribute("elapsedTime", timeTaken);
		
		/* Update quiz records and achievements. */
		String username = (String) request.getSession().getAttribute("username");
		int quizId = (Integer) request.getSession().getAttribute("quizId");
		QuizManager quizManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
		UserDataManager userDataManager = (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		
		/* Insert to quiz records. */
		quizManager.addUserQuizRecord(username, quizId, (int) totalTime , score);
				
		/* Update achievements. */
		userDataManager.updateUserAchievements(Achievements.TAKE_QUIZ, username, quizId, score);

		RequestDispatcher dispatcher = request.getRequestDispatcher("QuizPlayResult.jsp");
		dispatcher.forward(request, response);
	}

}
