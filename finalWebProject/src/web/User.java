package web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class User {

	private String name;
	private String uid;
	private String date_joined;
	private String last_login;
	private int is_admin;
	private String[] quiz_friends;
	private String[] achievements;
	private ArrayList<Quiz> popular_quizzes;
	private ArrayList<Quiz> recent_quizzes;


	public User(String userName, String password) {
		ResultSet userRs = stmt.executeQuery("SELECT * FROM useuserRs WHERE uid = \""+ userName +"\" AND password  = \""+ password +"\"");
	
		this.name = (String) userRs.getObject("name");
		this.uid = (String) userRs.getObject("uid");
		this.date_joined = (String) userRs.getObject("date_joined");
		this.last_login = (String) userRs.getObject("last_login");
		this.is_admin = (Integer) userRs.getObject("is_admin");
		this.quiz_friends = ((String) userRs.getObject("friends")).split(",");
		this.achievements = ((String) userRs.getObject("achievements")).split(",");
		
			//changed it

	}

}
