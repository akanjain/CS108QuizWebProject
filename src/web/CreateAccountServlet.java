package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateAccountServlet
 */
@WebServlet("/CreateAccountServlet")
public class CreateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAccountServlet() {
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
		AccountManager manager = (AccountManager) request.getServletContext().getAttribute("Account Manager");
		System.out.println("manager" + manager);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		/* If account asked to create already exist, go to "NameInUse" page, 
		 * Otherwise go to welcome page. */
		if (manager.accountExist(username) || username.equals("guest") || manager.deletedAccountExist(username)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("nameInUse.jsp");
			dispatcher.forward(request, response);
		} else {
			manager.createAccount(username, password);
			RequestDispatcher dispatcher = request.getRequestDispatcher("homepage.jsp");
			dispatcher.forward(request, response);
			
		}
	}

}
