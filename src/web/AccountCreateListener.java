package web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class AccountCreateListener
 *
 */
@WebListener
public class AccountCreateListener implements ServletContextListener {
	
	DBConnection con;

    /**
     * Default constructor. 
     */
    public AccountCreateListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	con.closeDB();
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	con = new DBConnection();
    	con.openDB();
    	AccountManager accountManager = new AccountManager(con.getStatement());
    	arg0.getServletContext().setAttribute("Account Manager", accountManager);
    	arg0.getServletContext().setAttribute("User Data Manager", new UserDataManager(con.getStatement(), accountManager));
    	arg0.getServletContext().setAttribute("Quiz Manager", new QuizManager(con.getStatement()));
 
         // TODO Auto-generated method stub
    }
	
}
