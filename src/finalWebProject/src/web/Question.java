package finalWebProject.src.web;

import java.util.*;
import java.sql.*;

public class Question {
	public static final String MC = "MC";
	public static final String RESPONSE = "RESPONSE";
	public static final String FILLBLANK = "FILLBLANK";
	public static final String PICTURE = "PICTURE";
	private QuestionDBConnection dbconnect;
	
	private String questionid;
	private String quizid;
	private String text;
	private String type;
	private String picurl;
	private String genre;
//	private Answer answer;
	
	
	/* 
	 * Constructor to get question instance when given a known question_id.
	 */
	public Question(String id) throws SQLException{
		dbconnect = QuestionDBConnection.getConnection();
		ResultSet rs = dbconnect.getQuestionInfo(id);
		quizid = id;
		text = rs.getString("question_text");
		type = rs.getString("type");
		picurl = rs.getString("picture_url");
		genre = rs.getString("genre_ids");
		
	}
	
	/*
	 * Constructor to create a new question when given all parameters including id, 
	 * quiz_id, text, type, picture, and genre. Adds this question information to 
	 * the database table. 
	 */
	public Question(String question_id, String quiz_id, String question_text, String question_type, String pic_url, String genre_id) throws SQLException{
		dbconnect = QuestionDBConnection.getConnection();
		questionid = question_id;
		quizid = quiz_id;
		text = question_text;
		type = question_type;
		picurl = pic_url;
		genre = genre_id;
		String questioninfo = "( \"" + quizid + "\", \"" + text + "\", \"" + type + "\", \"" + 
				picurl + "\", \"" +genre+")";
		dbconnect.addQuestionEntry(questioninfo);
		
		
	}
	
	/* 
	 * Get the text of the question.
	 */
		
	public String getText(){
		return text;
	}
	
	/* Get ID of question */
	
	public String question_id(){
		return questionid;
	}
	
	
}
