package web;

import java.util.ArrayList;

public class User {
	
	String name;
	ArrayList<String> friends;
	String dateJoined;
	String dateLastLogin;
	
	public User(String userId, String password) {
		ResultSet rs = stmt.executeQuery("SELECT  * FROM Users WHERE userID = \""+userId+"\"")
		
				
				
	}

	

}
