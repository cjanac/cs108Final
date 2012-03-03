package finalWebProject.src.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDBConnection {
	static String account = "quizzesdb";
	static String password = "IlPY!py4p!";
	static String server = "quizzesdb.db.8260487.hostedresource.com";
	static String database = "quizzesdb"; 
	private Connection con;
	private Statement stmt;
	private static final String USER_ID_COL = "user_id";
	private static final String INPUT_COLS = "(name,user_id,date_joined,last_login,is_admin,quiz_friends,achievements)";
	
	
	
	public UserDBConnection() {
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
	 * Adjusts the given cell in table_name (user_id, col_name) to be value
	 * @param table_name
	 * @param user_id
	 * @param col_name
	 * @param value
	 * @throws SQLException 
	 */
	public void setUserAttribute(String table_name, String user_id, String col_name, String value) throws SQLException {
		stmt.executeUpdate("UPDATE " + table_name + " SET " + col_name + " = " + value 
				+ " WHERE " + USER_ID_COL + " = " + user_id + ";");
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
	 * Returns a ResultSet for the given user_id
	 * @param table_name
	 * @param user_id
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet getUserRowInfo(String table_name, String user_id) throws SQLException{
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ USER_ID_COL + " = " + user_id + ";");
	}
	
	
	
	
	/**
	 * Takes in a string, attributes, that is formatted to list the values to be inserted into the 
	 * columns indicated by INPUT_COLS. The method returns the id of the newly inserted quiz.
	 * @param table_name
	 * @return quiz id
	 * @throws SQLException 
	 */
	public String addUserEntry(String table_name, String attributes) throws SQLException {
		stmt.executeUpdate("INSERT INTO " + table_name + " " + INPUT_COLS + " VALUES " + attributes + ";");
		return Long.toString(stmt.executeQuery("SELECT LAST_INSERT_ID();").getLong(USER_ID_COL));
	}
	
	
	/**
	 * checks whether the resultSet for the user_id query is empty.
	 *  * @param table_name
	 * @param user_id
	 *  * @return
	 * @throws SQLException 
	 */
	public boolean userExists(String table_name,String user_id) throws SQLException {
		return getUserRowInfo(table_name,user_id).next();
	}
	
	
	
	/**
	 *Checks whether the
	 * (user_id, col_name).
	 * @param table_name
	 * @param user_id
	 * @param password
	 * @return
	 * @throws SQLException 
	 */
	public boolean nameMatchesPassword(String table_name,String user_id,String password) throws SQLException {
		ResultSet rs = getUserRowInfo(table_name,user_id);
		String hashedPassword = generate(password);
		return (hashedPassword.equals(rs.getObject("password")));
	}

	
	
	
	/**
	 * This method returns, in String form, the given entry in table_name at cell
	 * (user_id, col_name).
	 * @param table_name
	 * @param user_id
	 * @param col_name
	 * @return
	 * @throws SQLException 
	 */
	public String getUserAttribute(String table_name, String user_id, String col_name) throws SQLException{
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE "
				+ USER_ID_COL + " = " + user_id + ";").getString(col_name);
	}

	
	
	/**
	 * This method removes a quiz object from the database, designated by user_id
	 * @param table_name
	 * @param user_id
	 * @throws SQLException 
	 */
	public void removeItem(String table_name, String user_id) throws SQLException{
		stmt.executeUpdate("DELETE FROM " + table_name + " WHERE " + USER_ID_COL + " = "
				+ user_id + ";");
	}
	
	
	private static String generate(String input) {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] msg = input.getBytes();

			// Update the message digest with some more bytes
			// This can be performed multiple times before creating the hash
			md.update(msg);

			// Create the digest from the message
			byte[] aMessageDigest = md.digest();
			return hexToString(aMessageDigest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       

		return null;

	}



	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	
	
	
	
	
	
	
		
}
