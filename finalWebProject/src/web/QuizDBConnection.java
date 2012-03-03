
import java.sql.*;

public class QuizDBConnection {
	static String account = "quizzesdb";
	static String password = "IlPY!py4p!";
	static String server = "quizzesdb.db.8260487.hostedresource.com";
	static String database = "quizzesdb"; 
	private Connection con;
	private Statement stmt;
	private static final String QUIZ_ID_COL = "quiz_id";
	private static final String INPUT_COLS = "(quiz_name, creator_id, description, question_ids, genre_ids, num_times_taken, date_created)";
	
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
	 * 
	 * @param table_name
	 * @param quiz_id
	 * @param col_name
	 * @param value
	 */
	public void setQuizAttribute(String table_name, String quiz_id, String col_name, String value) {
		stmt.executeUpdate("UPDATE " + table_name + " SET " + col_name + " = " + value 
				+ " WHERE " + QUIZ_ID_COL + " = " + quiz_id + ";");
	}
	
	/**
	 * 
	 */
	public void close() {
		stmt.close();
		con.close();
	}
	
	/**
	 * 
	 * @param table_name
	 * @param quiz_id
	 * @return
	 */
	public ResultSet getQuizRowInfo(String table_name, String quiz_id){
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ QUIZ_ID_COL + " = " + quiz_id + ";");
	}
	
	/**
	 * return quiz_id just added
	 * @param table_name
	 */
	public String addQuizEntry(String table_name, String attributes) {
		stmt.executeUpdate("INSERT INTO " + table_name + " " + INPUT_COLS + " VALUES " + attributes + ";");
		return Long.toString(stmt.executeQuery("SELECT LAST_INSERT_ID();").getLong());
	}
	
	/**
	 * 
	 * @param table_name
	 * @param quiz_id
	 * @param col_name
	 * @return
	 */
	public String getQuizAttribute(String table_name, String quiz_id, String col_name){
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ QUIZ_ID_COL + " = " + quiz_id + ";").getString(col_name);
	}
	
	/**
	 * 
	 * @param table_name
	 * @param quiz_id
	 */
	public void removeItem(String table_name, String quiz_id){
		stmt.executeUpdate("DELETE FROM " + table_name + " WHERE " + QUIZ_ID_COL + " = "
				+ quiz_id + ";");
	}
	
}
