import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;


public class Quiz {
	
	private final String creator_id;
	private final String quiz_id;
	private String quiz_name;
	private String description;
	private ArrayList<String> question_ids;
	private ArrayList<String> genre_ids;
	private final String date_created;
	private DatabaseConnection db;
	private static final String TABLE_NAME = "quizzes";
	private static final String[] COL_NAMES = {"quiz_id", "quiz_name", 
		"creator_id", "description", "date_created", "question_ids", "genre_ids", "date_created", "quiz_id"};
	private static final String ID_DELIMS = ", \t\n\r\f";
	
	
	/**
	 * if quiz_id is null, will assume that new quiz is being created
	 * @param creator_id
	 * @param quiz_name
	 * @param isNew
	 * @param description
	 * @param question_ids
	 * @param genre_ids
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, String description,
			ArrayList<String> question_ids, ArrayList<String> genre_ids) throws SQLException {
		super();
		db = new DatabaseConnection();
		this.creator_id = creator_id;
		this.quiz_name = quiz_name;
		this.description = description;
		this.question_ids = question_ids;
		this.genre_ids = genre_ids;
		if(quiz_id == null) {
			this.date_created = new Date().toString();
			this.quiz_id = generateId();
			addToDatabase();
		} else {
			this.date_created = parseCategory(db.getRowEntry(TABLE_NAME, quiz_id), COL_NAMES[7]);
			this.quiz_id = quiz_id;
		}
	}

	//note that if only putting in question_ids, put description before ArrayList in 
	//arguments
	/**
	 * 
	 * @param creator_id
	 * @param quiz_name
	 * @param isNew
	 * @param description
	 * @param question_ids
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, String description,
			ArrayList<String> question_ids) throws SQLException {
		this(quiz_id, creator_id, quiz_name, description, question_ids, null);
		if(quiz_id != null){
			genre_ids = toArrayList(parseCategory(db.getRowEntry(TABLE_NAME, this.quiz_id), COL_NAMES[6]));
		}
	}
	
	//note that if only passing in genre_ids, put description after
	/**
	 * 
	 * @param creator_id
	 * @param quiz_name
	 * @param isNew
	 * @param genre_ids
	 * @param description
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name,
			ArrayList<String> genre_ids, String description) throws SQLException {
		this(quiz_id, creator_id, quiz_name, description, null, genre_ids);
		if(quiz_id != null){
			question_ids = toArrayList(parseCategory(db.getRowEntry(TABLE_NAME, this.quiz_id), COL_NAMES[5]));
		}
	}
	
	/**
	 * 
	 * @param creator_id
	 * @param quiz_name
	 * @param isNew
	 * @param description
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, boolean isNew, String description) throws SQLException {
		this(quiz_id, creator_id, quiz_name, description, null, null);
		if(quiz_id != null){
			question_ids = toArrayList(parseCategory(db.getRowEntry(TABLE_NAME, this.quiz_id), COL_NAMES[5]));
			genre_ids = toArrayList(parseCategory(db.getRowEntry(TABLE_NAME, this.quiz_id), COL_NAMES[6]));
		}
	}
	
	/**
	 * 
	 * @param creator_id
	 * @param quiz_name
	 * @param isNew
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name) throws SQLException {
		this(quiz_id, creator_id, quiz_name, "", null, null);
		if(quiz_id != null){
			question_ids = toArrayList(parseCategory(db.getRowEntry(TABLE_NAME, this.quiz_id), COL_NAMES[5]));
			genre_ids = toArrayList(parseCategory(db.getRowEntry(TABLE_NAME, this.quiz_id), COL_NAMES[6]));
			description = parseCategory(db.getRowEntry(TABLE_NAME, this.quiz_id), COL_NAMES[3]);
		}
	}
	
	/*
	 * 
	 */
	private ArrayList<String> toArrayList(String str) {
		StringTokenizer tokenizer = new StringTokenizer(str, Quiz.ID_DELIMS);
		ArrayList<String> list = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()){
			list.add(tokenizer.nextToken());
		}
		return list;
	}

	/*
	 * 
	 */
	private String parseCategory(ResultSet rowEntry, String colName) throws SQLException {
		rowEntry.next();
		return rowEntry.getString(colName);
	}
	

	/**
	 * 
	 * @return
	 */
	public String getQuiz_name() {
		return quiz_name;
	}

	/**
	 * 
	 * @param quiz_name
	 */
	public void setQuiz_name(String quiz_name) {
		this.quiz_name = quiz_name;
		//TODO: set in database ala:
		// attributeMap.put(COL_NAMES[1], quiz_name);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
		//
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
		//TODO: set in database ala:
		// attributeMap.put(COL_NAMES[3], description);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
		//
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getQuestion_ids() {
		return question_ids;
		/*
		 * Alternatively: with String return
		 * String questions = "";
		 * for(int i = 0; i < question_ids.size(); i++){
		 * 	questions += question_ids.get(i) + ", ";
		 * }
		 * return questions;
		 */
	}

	/**
	 * 
	 * @param question_ids
	 */
	public void setQuestion_ids(ArrayList<String> question_ids) {
		this.question_ids = question_ids;
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
		//
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getGenre_ids() {
		return genre_ids;
		/*
		 * Alternatively: with String return
		 * String genres = "";
		 * for(int i = 0; i < genre_ids.size(); i++){
		 * 	genres += genre_ids.get(i) + ", ";
		 * }
		 * return genres;
		 */
	}
	
	/**
	 * 
	 * @param genre_ids
	 */
	public void setGenre_ids(ArrayList<String> genre_ids) {
		this.genre_ids = genre_ids;
		//TODO: set in database ala:
		// String genres = toString(genre_ids); - make sure that this will give database friendly format
		// attributeMap.put(COL_NAMES[6], genres);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
		//
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCreator_id() {
		return creator_id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQuiz_id() {
		return quiz_id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDate_created() {
		return date_created;
		//what format do we want to work with here?
		//allow clients of class to have flexibility
		//of directly manipulating Date, or ease of
		//processing with formatted form?
	}
	
	/**
	 * 
	 * @param question_id
	 * @param question_index
	 */
	public void addQuestion(String question_id, int question_index) {
		if(question_index > question_ids.size()){
			question_ids.add(question_id);
		}
		question_ids.add(question_index, question_id);
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param question_id
	 */
	public void addQuestion(String question_id){
		question_ids.add(question_id);
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param questions
	 */
	public void addQuestions(ArrayList<String> questions){
		question_ids.addAll(questions);
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param questions
	 * @param index
	 */
	public void addQuestions(ArrayList<String> questions, int index){
		if(index > question_ids.size()){
			question_ids.addAll(questions);
		}
		question_ids.addAll(index, questions);
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	} 
	
	/**
	 * 
	 * @param question_id
	 */
	public void removeQuestion(String question_id){
		question_ids.remove(question_id);
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param index
	 */
	public void removeQuestion(int index){
		question_ids.remove(index);
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param toRemove
	 */
	public void removeQuestions(ArrayList<String> toRemove){
		question_ids.removeAll(toRemove);
		//TODO: set in database ala:
		// String questions = toString(question_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 */
	public void clearQuestions() {
		question_ids.clear();
		//TODO: set in database ala:
		// String questions = toString(question_ids); - this would result in either "" or null, depending on desired sentinel
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param genre_id
	 */
	public void addGenre(String genre_id){
		genre_ids.add(genre_id);
		//TODO: set in database ala:
		// String genres = toString(genre_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param genres
	 */
	public void addGenres(ArrayList<String> genres){
		genre_ids.addAll(genres);
		//TODO: set in database ala:
		// String genres = toString(genre_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param question_id
	 */
	public void removeGenre(String genre_id){
		genre_ids.remove(genre_id);
		//TODO: set in database ala:
		// String genres = toString(genre_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @param toRemove
	 */
	public void removeGenres(ArrayList<String> toRemove){
		genre_ids.removeAll(toRemove);
		//TODO: set in database ala:
		// String genres = toString(genre_ids);
		// attributeMap.put(COL_NAMES[5], questions);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 */
	public void clearGenres() {
		genre_ids.clear();
		//TODO: set in database ala:
		// String genres = toString(genre_ids); - this would result in either "" or null, depending on desired sentinel
		// attributeMap.put(COL_NAMES[5], genres);
		// db.setAttributes(TABLE_NAME, quizID, attributeMap);
	}
	
	/**
	 * 
	 * @return
	 */
	public int numberQuestions(){
		return question_ids.size();
	}
	
	/**
	 * 
	 * @param numberQs
	 * @return
	 */
	public ArrayList<String> generateRandomQSet(int numberQs){
		ArrayList<String> sublist = new ArrayList<String>();
		Random random = new Random();
		int size = question_ids.size();
		for(int i = 0; i < numberQs; i++){
			sublist.add(question_ids.get(random.nextInt(size)));
		}
		return sublist;
	}
	
	/*
	 * 
	 */
	private String generateId() {
		//TODO: fill in this code here
		//need to decide how ID is going to be generated - 
		//is this something that we pawn off on the connection class?
		//what is method behind ID generation?
		
		return null;
	}
	
	/*
	 * 
	 */
	private void addToDatabase() {
		//TODO: construct so all pertinent information is passed on to the dbConnection for processing
		//set in database a la:
		//add each ivar/identifier pair to hashMap
		//db.addEntry(TABLE_NAME, hashMap);
	}

}

