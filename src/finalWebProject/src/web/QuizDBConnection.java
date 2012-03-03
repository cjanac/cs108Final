package finalWebProject.src.web;

import java.sql.*;

public class QuizDBConnection {
	static String account = "quizzesdb";
	static String password = "IlPY!py4p!";
	static String server = "quizzesdb.db.8260487.hostedresource.com";
	static String database = "quizzesdb"; 
	private Connection con;
	private Statement stmt;
	private static final String QUIZ_ID_COL = "quiz_id";
	private static final String INPUT_COLS = "(quiz_name, creator_id, description, question_ids, genre_ids, num_times_taken, date_created, rand_flag, " +
			"multipage_flag, practice_flag)";
	
	public QuizDBConnection() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
		}catch (SQLException e){
			e.printStackTrace();
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Adjusts the given cell in table_name (quiz_id, col_name) to be value
	 * @param table_name
	 * @param quiz_id
	 * @param col_name
	 * @param value
	 * @throws SQLException 
	 */
	public void setQuizAttribute(String table_name, String quiz_id, String col_name, String value) throws SQLException {
		stmt.executeUpdate("UPDATE " + table_name + " SET " + col_name + " = " + value 
				+ " WHERE " + QUIZ_ID_COL + " = " + quiz_id + ";");
	}
	
	/**
	 * Simply shuts off the connection. This is used specifically if someone is removing the quiz object.
	 * @throws SQLException 
	 */
	public void close() throws SQLException {
		stmt.close();
		con.close();
	}
	
	/**
	 * Returns a ResultSet for the given quiz_id
	 * @param table_name
	 * @param quiz_id
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet getQuizRowInfo(String table_name, String quiz_id) throws SQLException{
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ QUIZ_ID_COL + " = " + quiz_id + ";");
	}
	
	/**
	 * Takes in a string, attributes, that is formatted to list the values to be inserted into the 
	 * columns indicated by INPUT_COLS. The method returns the id of the newly inserted quiz.
	 * @param table_name
	 * @return quiz id
	 * @throws SQLException 
	 */
	public String addQuizEntry(String table_name, String attributes) throws SQLException {
		stmt.executeUpdate("INSERT INTO " + table_name + " " + INPUT_COLS + " VALUES " + attributes + ";");
		return Long.toString(stmt.executeQuery("SELECT LAST_INSERT_ID();").getLong(QUIZ_ID_COL));
	}
	
	/**
	 * This method returns, in String form, the given entry in table_name at cell
	 * (quiz_id, col_name).
	 * @param table_name
	 * @param quiz_id
	 * @param col_name
	 * @return
	 * @throws SQLException 
	 */
	public String getQuizAttribute(String table_name, String quiz_id, String col_name) throws SQLException{
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ QUIZ_ID_COL + " = " + quiz_id + ";").getString(col_name);
	}
	
	/**
	 * This method removes a quiz object from the database, designated by quiz_id
	 * @param table_name
	 * @param quiz_id
	 * @throws SQLException 
	 */
	public void removeItem(String table_name, String quiz_id) throws SQLException{
		stmt.executeUpdate("DELETE FROM " + table_name + " WHERE " + QUIZ_ID_COL + " = "
				+ quiz_id + ";");
	}
	
}
