package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
 * Servlet implementation class MultiplePageServlet
 */
@WebServlet("/MultiplePageServlet")
public class MultiplePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MultiplePageServlet() {
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
		
		Quiz currentQuiz = (Quiz) request.getSession().getAttribute("currentQuiz");
		int currentQuizQuestion = (Integer) request.getSession().getAttribute("currentQuizQuestion");
		int numQuestion = (Integer) request.getSession().getAttribute("currentQuizTotalQuestions");
		String isImm = (String) request.getSession().getAttribute("isImmediate");
		String isPracticeMode = (String) request.getSession().getAttribute("isPracticeMode");
		UserDataManager userDataManager = (UserDataManager) sc.getAttribute("User Data Manager");
		String username = (String) request.getSession().getAttribute("username");
		int quizId = (Integer) request.getSession().getAttribute("quizId");
		QuizManager quizManager = (QuizManager) request.getServletContext().getAttribute("Quiz Manager");
		
		String token = request.getParameter("token");
		
		if (token.equals("Submit") || token.equals("Submit Quiz")) {
			int i = currentQuiz.getCurrentQuestionNumber();
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
		
		if (token.equals("End Practice")) {
			/* Check practice mode achievement. */
			userDataManager.updateUserAchievements(UserDataManager.Achievements.PRACTICE_MODE, username, quizId, 0);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("PracticeEnd.jsp");
			dispatcher.forward(request, response);
		}
		
		if (token.equals("Remove Practice Question")) {
			((PracticeQuiz) currentQuiz).removePracticeQuestion(currentQuiz.getCurrentQuestionNumber());
			RequestDispatcher dispatcher = request.getRequestDispatcher("RemovePracticeQuestion.jsp");
			dispatcher.forward(request, response);
		}
		
		if (token.equals("Next Question")) {
			if (currentQuizQuestion == numQuestion && isPracticeMode.equals("true")) {
				currentQuiz.resetNextQuestionNumber();
				session.setAttribute("currentQuizQuestion", 1);
				session.setAttribute("currentQuizTotalQuestions", currentQuiz.getNumQuestion());
				currentQuiz.setQuestionIndexes();
				if (currentQuiz.getNumQuestion() == 0) {
					userDataManager.updateUserAchievements(UserDataManager.Achievements.PRACTICE_MODE, username, quizId, 0);
					RequestDispatcher dispatcher = request.getRequestDispatcher("PracticeEnd.jsp");
					dispatcher.forward(request, response);
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("MultiplePageQuizPlay.jsp");
					dispatcher.forward(request, response);
				}
			} else {
				currentQuiz.setNextQuestionNumber();
				session.setAttribute("currentQuizQuestion", currentQuizQuestion+1);
				RequestDispatcher dispatcher = request.getRequestDispatcher("MultiplePageQuizPlay.jsp");
				dispatcher.forward(request, response);
			}
		}
		
		if (token.equals("Submit")) {
			if (isImm.equals("true") || isPracticeMode.equals("true")) {
				int i = currentQuiz.getCurrentQuestionNumber();
				request.setAttribute("QuestionScore", currentQuiz.getScore(i));
				request.setAttribute("MaxScore", currentQuiz.getMaxScore(i));
				//if (isPracticeMode.equals("true") && (currentQuiz.getScore(i) == currentQuiz.getMaxScore(i))) {
				//	((PracticeQuiz) currentQuiz).increaseCount(i);
				//}
				RequestDispatcher dispatcher = request.getRequestDispatcher("ImmediateScorePage.jsp");
				dispatcher.forward(request, response);
			} else {
				currentQuiz.setNextQuestionNumber();
				session.setAttribute("currentQuizQuestion", currentQuizQuestion+1);
				RequestDispatcher dispatcher = request.getRequestDispatcher("MultiplePageQuizPlay.jsp");
				dispatcher.forward(request, response);
			}
		} else if (token.equals("Score Summary")) {
			int score = Integer.parseInt(request.getParameter("Score"));
			String timeTaken = request.getParameter("elapsedTime");
			request.setAttribute("Score", score);
			request.setAttribute("elapsedTime", timeTaken);
			RequestDispatcher dispatcher = request.getRequestDispatcher("QuizPlayResult.jsp");
			dispatcher.forward(request, response);
		} else if (token.equals("Submit Quiz")) {
			long end = System.currentTimeMillis();
			long start = (Long) request.getSession().getAttribute("startTime");
			int score = currentQuiz.calculateScore();
			request.setAttribute("Score", score);
			long totalTime = TimeUnit.MILLISECONDS.toSeconds(end-start);
			String timeTaken = totalTime + "";
			request.setAttribute("elapsedTime", timeTaken);
			
			/* Update quiz records and achievements. */
			/* Insert to quiz records. */
			quizManager.addUserQuizRecord(username, quizId, (int) totalTime , score);
			/* Update achievements. */
			userDataManager.updateUserAchievements(Achievements.TAKE_QUIZ, username, quizId, score);

			if (isImm.equals("true")) {
				int i = currentQuiz.getCurrentQuestionNumber();
				request.setAttribute("QuestionScore", currentQuiz.getScore(i));
				request.setAttribute("MaxScore", currentQuiz.getMaxScore(i));
				RequestDispatcher dispatcher = request.getRequestDispatcher("ImmediateScorePage.jsp");
				dispatcher.forward(request, response);
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("QuizPlayResult.jsp");
				dispatcher.forward(request, response);
			}
		}
	}

}
