package finalWebProject.src.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: accountCreationServlet
 *
 */
public class accountCreationServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public accountCreationServlet() {
		super();
	}   	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}  	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ServletContext context = getServletContext();
		UserDBConnection userDB = context.getAttribute("userDB");

		String user_id=  request.getParameter("user_id");

		String password= request.getParameter("password");
		String name= request.getParameter("name");

		if(userDB.userExists("users",user_id)) {
			RequestDispatcher dispatch = request.getRequestDispatcher("userIdTakenjsp");

			dispatch.forward(request, response);	

		} else {


			QuizDBConnection quizDB = context.getAttribute("quizDB");


			User user = new User(user_id);

			ArrayList<String> popularQuizzes = quizDB.listPopular();
			ArrayList<String> recentQuizzes = quizDB.listRecent();
			ArrayList<String> recentActivities = userDB.listRecentQuizzes(user_id);
			ArrayList<String> friendActivities = userDB.listRecentQuizzes(user_id);

			ArrayList<String> achievements = userDB.listAchievements(user_id);
			//  ArrayList<Messages> messages = userDB.listMessages(user_id);

			String user_id = request.getParameter("user_id");

			request.setAttribute(user,"user");
			request.setAttribute(popularQuizzes,"popularQuizzes");
			request.setAttribute(recentQuizzes,"recentQuizzes");
			request.setAttribute(recentActivities,"recentActivities");
			request.setAttribute(achievements,"achievements");
			request.setAttribute(friendActivities,"friendActivities");

			// request.setAttribute(messages,"messages");
			RequestDispatcher dispatch = request.getRequestDispatcher("userHomePage.jsp");

			dispatch.forward(request, response);	
		}
	}   	  	    
}