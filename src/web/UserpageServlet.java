package web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserpageServlet
 */
@WebServlet("/UserpageServlet")
public class UserpageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserpageServlet() {
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
		String message = (String) request.getParameter("message");
		
		/* Send friend request via User Data Manager. */
		UserDataManager userDataManager =   (UserDataManager) request.getServletContext().getAttribute("User Data Manager");
		
		String returnStatus = userDataManager.sendFriendRequest(fromUser, toUser, message);
		request.setAttribute("Return Status", returnStatus);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("userpage.jsp?username=" + toUser);
		dispatcher.forward(request, response);
	}

}
