
import java.sql.*;

public class DBConnection {
	protected static String account = "quizzesdb";
	protected static String password = "IlPY!py4p!";
	protected static String server = "quizzesdb.db.8260487.hostedresource.com";
	protected static String database = "quizzesdb"; 
	protected Connection con;
	protected Statement stmt;
	protected String id_col_name;
	protected String input_col_names;
	//protected static final String QUIZ_ID_COL = "quiz_id";
	//protected static final String INPUT_COLS = "(quiz_name, creator_id, description, question_ids, genre_ids, num_times_taken, date_created)";
	
	public DBConnection() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(server, account, password);
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
	public void setAttribute(String table_name, String id, String col_name, String value) {
		stmt.executeUpdate("UPDATE " + table_name + " SET " + col_name + " = " + value 
				+ " WHERE " + id_col_name + " = " + id);
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
	public ResultSet getRowInfo(String table_name, String id){
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ id_col_name + " = " + id);
	}
	
	/**
	 * return quiz_id just added
	 * @param table_name
	 */
	public String addEntry(String table_name, String attributes) {
		int insert_id = stmt.executeUpdate("INSERT INTO " + table_name + " " + input_col_names + " VALUES " + attributes,Statement.RETURN_GENERATED_KEYS);
		return Integer.toString(insert_id);
	}
	
	/**
	 * 
	 * @param table_name
	 * @param quiz_id
	 * @param col_name
	 * @return
	 */
	public String getAttribute(String table_name, String id, String col_name){
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ id_col_name + " = " + id).getString(col_name);
	}
	
	/**
	 * 
	 * @param table_name
	 * @param quiz_id
	 */
	public void removeItem(String table_name, String id){
		stmt.executeUpdate("DELETE FROM " + table_name + " WHERE " + id_col_name + " = "
				+ id);
	}
	
}
