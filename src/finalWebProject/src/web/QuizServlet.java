package finalWebProject.src.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QuizServlet
 */
public class QuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public QuizServlet() {
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
		Quiz quiz = (Quiz)request.getAttribute("quiz");
		try {
			ArrayList<String> question_ids;
			if(quiz.getRandomFlag()){
				question_ids = quiz.generateRandomQSet(quiz.numberQuestions());
			} else {
				question_ids = quiz.getQuestion_ids();
			}
			quiz.incrementTimesTaken();
			if(quiz.getMultiPageFlag()){
				multiPageSetup(question_ids, request, response);
			}else {
				singlePageSetup(question_ids, request, response);
			} 
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * This method sets up the necessary information to forward to the single page jsp display page
	 */
	private void singlePageSetup(ArrayList<String> question_ids, HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		int numQuestions = question_ids.size();
		ArrayList<Question> questions = new ArrayList<Question>(); 
		for(int i = 0; i < numQuestions; i++){
			String id = question_ids.get(i);
			Question question = new Question(id);
			questions.add(question);
		}
		request.setAttribute("questions", questions);
		RequestDispatcher dispatch = request.getRequestDispatcher("singlePageQuiz.jsp");
		dispatch.forward(request, response);
	}
	
	/*
	 * 
	 */
	private void multiPageSetup(ArrayList<String> question_ids, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("question_ids", question_ids);
		ArrayList<String> answers = new ArrayList<String>();
		request.setAttribute("answers", answers);
		request.setAttribute("question_number", 1);
		request.setAttribute("gradeNote", "");
		request.setAttribute("submitAction", "Submit");
		RequestDispatcher dispatch = request.getRequestDispatcher("multiPageQuiz.jsp");
		request.setAttribute("startTime", System.currentTimeMillis());
		dispatch.forward(request, response);
	}
}
