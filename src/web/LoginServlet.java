package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		AccountManager manager = (AccountManager) request.getServletContext().getAttribute("Account Manager");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		RequestDispatcher dispatcher;
		
		if (username.equals("guest")) {
			dispatcher = request.getRequestDispatcher("guestHomepage.jsp");
		} else if (!manager.accountExist(username) || !manager.checkCredential(username, password)) {
			dispatcher = request.getRequestDispatcher("loginIncorrect.html");
		} else {
			dispatcher = request.getRequestDispatcher("homepage.jsp");		
		}
		dispatcher.forward(request, response);
		
	}

}

