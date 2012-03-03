package finalWebProject.src.web;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
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
		 ServletContext context = getServletContext();
		 UserDBConnection userDB = context.getAttribute("userDB");

		 String user_id=  request.getParameter("user_id");
		 
		String password= request.getParameter("password");

		if(!userDB.userExists("user",user_id) || !userDB.nameMatchesPassword("user",user_id,password)) {

			RequestDispatcher dispatch = request.getRequestDispatcher("badPassword.jsp");

			dispatch.forward(request, response);
			
			
		} else {
		
		 
        // accountManager account = (accountManager) context.getAttribute("account");
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
