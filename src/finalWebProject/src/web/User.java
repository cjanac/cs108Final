package finalWebProject.src.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
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


	public User(String userId) throws SQLException{
		UserDBConnection db = new UserDBConnection();
		
		this.name = db.getUserAttribute("user",userId,"name");
		this.uid = db.getUserAttribute("user",userId,"uid");
		this.date_joined =  db.getUserAttribute("user",userId,"date_joined");
		this.last_login =  db.getUserAttribute("user",userId,"last_login");
		this.is_admin = (Integer) Integer.parseInt(db.getUserAttribute("user",userId,"is_admin"));
		this.quiz_friends =  db.getUserAttribute("user",userId,"friends").split(",");
		this.achievements = db.getUserAttribute("user",userId,"achievements").split(",");	

		
		
	}
}
