package finalWebProject.src.web;


import java.sql.*;



public class QuestionDBConnection {
	static String account = "quizzesdb";
	static String password = "IlPY!py4p!";
	static String server = "quizzesdb.db.8260487.hostedresource.com";
	static String database = "quizzesdb"; 
	private Connection con;
	private Statement stmt;
	private static QuestionDBConnection thisconnection;
	private static final String TABLENAME = "questions";
	
	private static final String QUESTION_COLS = "(quesetion_id, quiz_id, question_text, type, picture_url, genre_ids)";
	
	
	public QuestionDBConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
			thisconnection = this;
		}catch (SQLException e){
			e.printStackTrace();
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/* Given parameter of the question_id returns the result set containing
	 *  that question's associated quiz_id, text, type, optional pictures,
	 *  and genres. 
	 */
	
	public ResultSet getQuestionInfo(String question_id) throws SQLException{
		return stmt.executeQuery("SELECT * FROM "+TABLENAME+" WHERE question_id = "+question_id+";");

	}
	
	public String addQuestionEntry(String questioninfo) throws SQLException{
		stmt.executeUpdate("INSERT INTO " + TABLENAME + " " + QUESTION_COLS + " VALUES " + questioninfo + ";");
		return Long.toString(stmt.executeQuery("SELECT LAST_INSERT_ID();").getLong("question_id"));
	}
	
	public static final QuestionDBConnection getConnection(){
		return thisconnection;
	}
	
	
	
}
