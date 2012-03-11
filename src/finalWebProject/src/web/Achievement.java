package finalWebProject.src.web;

import java.sql.ResultSet;

public class Achievement {
	private String achievement_id;
	private String achievement_name;
	private String description;
	private String picture_url;
	private AchievementDBConnection db;
	
	/**
	 * This constructor is meant for the creation of totally new instances of an Achievement -
	 * i.e. one that needs to be added to the database
	 * @param achievement_name
	 * @param description
	 * @param picture_url
	 */
	public Achievement(String achievement_name, String description, String picture_url){
		db = new AchievementDBConnection();
		this.achievement_name = achievement_name;
		this.description = description;
		this.picture_url = picture_url;
		this.achievement_id = db.addAchievement(achievement_name, description, picture_url);
	}
	
	public Achievement(String achievement_id){
		db = new AchievementDBConnection();
		this.achievement_id = achievement_id;
		ResultSet set = db.getAchievementInfo(achievement_id); 
		//need database functionality for returning ResultSet that contains row info for Achievement base on id
		this.achievement_name = set.getString(arg0)
	}
}
