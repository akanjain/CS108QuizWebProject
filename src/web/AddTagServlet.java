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
 * Servlet implementation class AddTagServlet
 */
@WebServlet("/AddTagServlet")
public class AddTagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTagServlet() {
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
		
		int quizId = Integer.parseInt(request.getParameter("quizId"));
		String tagname = request.getParameter("tagname");
		if (QzManager.getQuizExistbyTag(tagname, quizId)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("TagRejected.jsp?id=" + request.getParameter("quizId"));
			dispatcher.forward(request, response);
		} else {
			QzManager.addQuizTag(quizId, tagname);
			RequestDispatcher dispatcher = request.getRequestDispatcher("TagAdded.jsp?id=" + request.getParameter("quizId"));
			dispatcher.forward(request, response);
		}
	}

}
