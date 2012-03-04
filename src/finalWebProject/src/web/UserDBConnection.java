package finalWebProject.src.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDBConnection extends DBConnection {
	//private static final String USER_ID_COL = "user_id";
	//private static final String INPUT_COLS = "(name,user_id,date_joined,last_login,is_admin,quiz_friends,achievements)";
	
	private static final String TABLE_NAME = "users";
	private static final String ID_COL = "uid";
	private static final String INPUT_COLS = "(is_admin,name,fb_friends,quiz_friends,date_joined,last_login,password,achievements)";
	private static final String DEFAULT_ORDER_COL = "uid";
	private static final String DEFAULT_ORDER_DIR = "ASC";
	
	
	public UserDBConnection() {
		super(TABLE_NAME, ID_COL, INPUT_COLS);
	}
	
	
	public ResultSet getUser(String uid) throws SQLException {
		return getRowInfo(uid);
	}
	
	
	public ResultSet getAdmins() throws SQLException {
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE is_admin = 1");
	}
	
	
	public ResultSet getFriendsOfUser(String uid) throws SQLException {
		String fb_friends = getAttribute(uid,"fb_friends");
		String quiz_friends = getAttribute(uid,"quiz_friends");
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE uid IN (" + fb_friends + "," + quiz_friends + ") ORDER BY " + DEFAULT_ORDER_COL + " " + DEFAULT_ORDER_DIR);
	}
	
	
	public ResultSet getUsersWithAchievement(String achievement_id) throws SQLException {
		return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE " + achievement_id + " IN (achievements) ORDER BY " + DEFAULT_ORDER_COL + " " + DEFAULT_ORDER_DIR);
	}
	
	
	public String addUser(String name, String password) {
		String attributes = "(0," + name + ",\"\",\"\",SYSDATE(),SYSDATE()," + password + ",\"\"";
		return addEntry(attributes);
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
