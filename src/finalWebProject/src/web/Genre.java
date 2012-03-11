package finalWebProject.src.web;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

public class Genre {
	private String genre_id;
	private String genre_name;
	private ArrayList<String> related_genres; //will contain ids of other genres
	//NOTE: add a related_genres field to database. Also, as of now, associations are directional
	private GenreDBConnection db;
	private static final String GENRE_NAME = "genre_name";
	private static final String RELATED = "related_genres";
	private static final String ID_DELIMS = ", \t\n\r\f";
	
	/**
	 * This constructor is intended to create a new genre instance
	 * @param genre_name
	 * @param related_genres
	 */
	public Genre(String genre_name, ArrayList<String> related_genres){
		this.genre_name = genre_name;
		this.related_genres = related_genres;
		db = new GenreDBConnection();
		this.genre_id = db.addGenre(genre_name, arrayToString(related_genres)); //need this method to return the genre_id
	}
	
	/**
	 * This constructor is intended to fetch genre data from the database based on the id
	 * @param genre_id
	 */
	public Genre(String genre_id){
		ResultSet set = db.getGenreInfo(genre_id); //Assuming method that will return ResultSet with just row info
		//for the genre designated by the genre_id
		this.genre_id = genre_id;
		this.genre_name = set.getString(GENRE_NAME);
		this.related_genres = toArrayList(set.getString(RELATED));
	}

	/*
	 * 
	 */
	private ArrayList<String> toArrayList(String str) {
		StringTokenizer tokenizer = new StringTokenizer(str, ID_DELIMS);
		ArrayList<String> list = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()){
			list.add(tokenizer.nextToken());
		}
		return list;
	}

	/*
	 * Puts the contents of a String ArrayList into a comma delineate String
	 * format
	 */
	private static String arrayToString(ArrayList<String> list) {
		if(list == null) return "";
		String str = list.toString();
		str = str.substring(1, str.length() - 1);
		return "\"" + str + "\"";
	}
	
	/**
	 * This method sets the name of a genre, and alters the database accordingly
	 * @param name
	 */
	public void setGenreName(String name){
		this.genre_name = name;
		db.setGenreAttribute(genre_id, GENRE_NAME, name); //need method that can affect individual cells of a given row
	}
	
	/**
	 * returns the name of the genre
	 * @return
	 */
	public String getGenreName(){
		return this.genre_name;
	}
	
	/**
	 * returns the id of the genre
	 * @return
	 */
	public String getGenreID() {
		return this.genre_id;
	}
	
	/**
	 * Adds a genre associations to the genre, updates database accordingly
	 * @param genre_id
	 */
	public void addRelatedGenre(String genre_id){
		this.related_genres.add(genre_id);
		String genres = arrayToString(this.related_genres);
		db.setGenreAttribute(this.genre_id, RELATED, genres); //uses same basic method as in setGenreName
	}
	
	/**
	 * Adds a collection of genre associations to the genre, updates database accordingly 
	 * @param genre_ids
	 */
	public void addRelatedGenres(Collection<String> genre_ids) {
		this.related_genres.addAll(genre_ids);
		String genres = arrayToString(this.related_genres);
		db.setGenreAttribute(this.genre_id, RELATED, genres); //uses same basic method as in setGenreName
	}
	
	/**
	 * Removes a genre association based on the genre_id, if an association existed
	 * @param genre_id
	 */
	public void removeRelatedGenre(String genre_id){
		if(this.related_genres.remove(genre_id)) {
			String genres = arrayToString(this.related_genres);
			db.setGenreAttribute(this.genre_id, RELATED, genres); //uses same basic method as in setGenreName
		}
	}
	
	/**
	 * Removes a collection of genre associations, if associations exist
	 * @param genre_ids
	 */
	public void removeRelatedGenres(Collection<String> genre_ids){
		if(this.related_genres.removeAll(genre_ids)){
			String genres = arrayToString(this.related_genres);
			db.setGenreAttribute(this.genre_id, RELATED, genres); //uses same basic method as in setGenreName
		}
	}
	
	/**
	 * Clears all genre associations
	 */
	public void clearRelatedGenres(){
		this.related_genres.clear();
		db.setGenreAttribute(this.genre_id, RELATED, ""); //uses same basic method as in setGenreName
	}
	
	/**
	 * Returns whether an association exists between the genre in the argument and the given genre
	 * @param genre_id
	 * @return 
	 */
	public boolean containsAssociation(String genre_id){
		return this.related_genres.contains(genre_id);
	}
	
	/**
	 * Returns whether a set of associations exist between the genre in the argument and the given genre
	 * @param genre_ids
	 * @return
	 */
	public boolean containsAssoiciations(Collection<String> genre_ids){
		return this.related_genres.containsAll(genre_ids);
	}
}
