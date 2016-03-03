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

/**
 * Servlet implementation class QuizRatingServlet
 */
@WebServlet("/QuizRatingServlet")
public class QuizRatingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizRatingServlet() {
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
		String username = (String) request.getSession().getAttribute("username");
		
		int userRating = Integer.parseInt(request.getParameter("rating"));
		String reviewText = request.getParameter("comment");
		String quizId = (String) request.getSession().getAttribute("QuizId");
		int quizNumber = Integer.parseInt(quizId);
		
		QzManager.addRatingReview(quizNumber, username, userRating, reviewText);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("RatingAdded.jsp");
		dispatcher.forward(request, response);
	}

}
