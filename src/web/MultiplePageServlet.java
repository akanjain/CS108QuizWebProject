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
		
		String token = request.getParameter("token");
		
		String userAnswer = request.getParameter("answer-" + currentQuiz.getCurrentQuestionNumber());
		List<String> anslist = new ArrayList<String>();
		anslist.add(userAnswer);
		currentQuiz.setAnswers(currentQuiz.getCurrentQuestionNumber(), anslist);
		
		if (token.equals("Previous")) {
			currentQuiz.setPreviousQuestionNumber();
			session.setAttribute("currentQuizQuestion", currentQuizQuestion-1);
			RequestDispatcher dispatcher = request.getRequestDispatcher("MultiplePageQuizPlay.jsp");
			dispatcher.forward(request, response);
		} else if (token.equals("Next")) {
			currentQuiz.setNextQuestionNumber();
			session.setAttribute("currentQuizQuestion", currentQuizQuestion+1);
			RequestDispatcher dispatcher = request.getRequestDispatcher("MultiplePageQuizPlay.jsp");
			dispatcher.forward(request, response);
		} else if (token.equals("Submit Quiz")) {
			long end = System.currentTimeMillis();
			long start = (Long) request.getSession().getAttribute("startTime");
			int score = currentQuiz.calculateScore();
			request.setAttribute("Score", score);
			long totalTime = TimeUnit.MILLISECONDS.toSeconds(end-start);
			String timeTaken = totalTime + " seconds";
			request.setAttribute("elapsedTime", timeTaken);
			RequestDispatcher dispatcher = request.getRequestDispatcher("QuizPlayResult.jsp");
			dispatcher.forward(request, response);
		}
	}

}
