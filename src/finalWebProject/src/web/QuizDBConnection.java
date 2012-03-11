package finalWebProject.src.web;

import java.sql.*;

public class QuizDBConnection extends DBConnection {
	private static final String TABLE_NAME = "quizzes";
	private static final String ID_COL = "quiz_id";
	private static final String INPUT_COLS = "(quiz_name, creator_id, description, question_ids, genre_ids, num_times_taken, date_created, rand_flag, multipage_flag, practice_flag)";
	private static final String DEFAULT_ORDER_COL = "date_created";
	private static final String DEFAULT_ORDER_DIR = "DESC";
	
	/**
	 * Creates a new QuizDBConnection instance
	 */
	public QuizDBConnection() {
		super(TABLE_NAME, ID_COL, INPUT_COLS);
	}
	
	
	/**
	 * Get all quizzes ordered by date_taken DESC
	 * 
	 * @return a ResultSet
	 * @throws SQLException
	 */
	public ResultSet getQuizzes() throws SQLException {
		return getQuizzes(DEFAULT_ORDER_COL, DEFAULT_ORDER_DIR);
	}
	
	
	/**
	 * Get all quizzes ordered by order_col in the direction of order_dir
	 * 
	 * @param order_col
	 * @param order_dir
	 * @return a ResultSet
	 * @throws SQLException
	 */
	public ResultSet getQuizzes(String order_col, String order_dir) throws SQLException {
		return stmt.executeQuery("SELECT * FROM " + table_name + " ORDER BY " + order_col + " " + order_dir);
	}
	
	/**
	 * Get all quizzes created by a particular user ordered by date_taken DESC
	 * 
	 * @param uid
	 * @return a ResultSet
	 * @throws SQLException
	 */
	public ResultSet getQuizzesCreatedByUser(String uid) throws SQLException {
		return getQuizzesCreatedByUser(uid, DEFAULT_ORDER_COL, DEFAULT_ORDER_DIR);
	}
	
	/**
	 * Get all quizzes created by a particular user ordered by order_col
	 * in the direction of order_dir
	 * 
	 * @param uid
	 * @param order_col
	 * @param order_dir
	 * @return a ResultSet
	 * @throws SQLException
	 */
	public ResultSet getQuizzesCreatedByUser(String uid, String order_col, String order_dir) throws SQLException {
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE creator_id = " + uid + " ORDER BY " + order_col + " " + order_dir);
	}
	
	
	public ResultSet getPopularQuizzes() throws SQLException {
		return getQuizzes("num_times_taken", "DESC");
	}
	
	
	public ResultSet getRecentlyCreatedQuizzes() throws SQLException {
		return getQuizzes();
	}
	
	
	public ResultSet getQuizzesInGenre(String genre_id) throws SQLException {
		return getQuizzesInGenre(genre_id, DEFAULT_ORDER_COL, DEFAULT_ORDER_DIR);
	}
	
	
	public ResultSet getQuizzesInGenre(String genre_id, String order_col, String order_dir) throws SQLException {
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE " + genre_id + " IN (genre_ids) ORDER BY " + order_col + " " + order_dir);
	}
	
	
	public String addQuiz(String quiz_name, String creator_id, String description, String question_ids, String genre_ids, String rand_flag, String multipage_flag, String practice_flag, String grade_now_flag) {
		String attributes = "(" + quiz_name + "," + creator_id + "," + description + "," + question_ids + "," + genre_ids + ",0,SYSDATE()" + rand_flag + "," + multipage_flag + "," + practice_flag + "," + grade_now_flag + ")";
		return addEntry(attributes);
	}
}
