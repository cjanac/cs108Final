package finalWebProject.src.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MultipageQuizServlet
 */
public class MultipageQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MultipageQuizServlet() {
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
		String submitAction = (String)request.getAttribute("submitAction");
		ArrayList<String> question_ids = (ArrayList<String>)request.getAttribute("question_ids");
		int qnum = (Integer)request.getAttribute("question_number");
		if(submitAction.equals("Submit")){ //if the user was submitting intial answer for grading
			ArrayList<String> answers = (ArrayList<String>)request.getAttribute("answers");
			String answer = request.getParameter("answer");
			answers.add(answer);
			request.setAttribute("answers", answers);
			//Assuming that the parameter name for a multipage quiz for the answer is simply "answer"
			//Will need to clarify with Elliott on this with how this should be approached
			if(qnum == question_ids.size()) request.setAttribute("submitAction", "Finish Quiz");
			else request.setAttribute("submitAction", "Next Question");
			//setting "submitAction" attribute determines both jsp display and servlet action
			Question question;
			try {
				question = new Question(question_ids.get(qnum));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DisplayGraded(question, answer, request, response);
		} else if(submitAction.equals("Next Question")) { //if user was on graded question display page
			request.setAttribute("submitAction", "Submit");
			DisplayNext(qnum, request, response);
		} else if(submitAction.equals("Finish Quiz")){ //if user was on graded question display page for final quiz question
			RequestDispatcher dispatch = request.getRequestDispatcher("QuizResultsServlet");
			request.setAttribute("finishTime", System.currentTimeMillis());
			dispatch.forward(request, response);
		} else {
			System.out.println("Illegal submitAction: " + submitAction);
		}
	}
	
	/*
	 * Main purpose of private method is to construct the String for the "gradeNote" attribute,
	 * which is the HTML for the desired display for the grading information.
	 */
	private void DisplayGraded(Question question, String answer, HttpServletRequest request, HttpServletResponse response){
		String gradeNote = "<b>Grade: </ b><br />";
		if(question.isCorrect(answer)){ //assuming an "isCorrect" method that takes in the answer in String form
			gradeNote += "Correct! <br />";
			gradeNote += "Your answer of \"" + answer + "\" is correct. <br />";
			gradeNote += "Your score: " + question.getPossibleScore() + "/" + question.getPossibleScore() + "<br />";
			//need to get at the question's total possible score here
		}else{
			gradeNote += "Your answer: " + answer + " <br />";
			gradeNote += "Correct answer(s): " + question.getAnswers() + "<br />";
			//above needs some way to appropriately display the answer set associated with a specific question
			gradeNote += "Your score: " + question.getScore(answer) + "/" + question.getPossibleScore() + "<br />";
			//need both ways to get possible score and to get the assigned score for a given answer
			//I would imagine partial scores would only apply for multi-answer questions...
		}
		request.setAttribute("gradeNote", gradeNote);
		RequestDispatcher dispatch = request.getRequestDispatcher("multiPageQuiz.jsp");
		dispatch.forward(request, response);
	}
	
	/*
	 * Main purpose is to reset the gradeNote attribute to the empty string and to increment the question number
	 * so that the multiPageQuiz jsp will have the correct question to display.
	 */
	private void DisplayNext(int lastQNum, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("gradeNote", "");
		request.setAttribute("question_number", lastQNum + 1);
		RequestDispatcher dispatch = request.getRequestDispatcher("multiPageQuiz.jsp");
		dispatch.forward(request, response);
	}

}
