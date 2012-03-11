package finalWebProject.src.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;


public class Quiz {
	
	private String creator_id;  
	private final String quiz_id;
	private String quiz_name;
	private String description;
	private ArrayList<String> question_ids;
	private ArrayList<String> genre_ids;
	private String date_created;
	private int timesTaken;
	private boolean rand_flag;
	private boolean multipage_flag;
	private boolean practice_flag;
	private boolean grade_now_flag;
	private QuizDBConnection db;
	private static final String TABLE_NAME = "quizzes";
	private static final String QUIZ_ID = "quiz_id";
	private static final String QUIZ_NAME = "quiz_name";
	private static final String CREATOR_ID = "creator_id";
	private static final String DESCRIPTION = "description";
	private static final String DATE_CREATED = "date_created";
	private static final String QUESTIONS = "question_ids";
	private static final String GENRES = "genre_ids";
	private static final String TIMES_TAKEN = "num_times_taken";
	private static final String RANDOM = "rand_flag";
	private static final String PAGES = "multipage_flag";
	private static final String PRACTICE = "practice_flag";
	private static final String GRADE = "grade_now_flag";
	private static final String ID_DELIMS = ", \t\n\r\f";
	
	
	/**
	 * Full constructor, taking in values for all ivars. 
	 * If quiz_id is null, will assume that new quiz is being created.
	 * If new quiz, pass in 0 as argument to timesTaken
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param description
	 * @param question_ids
	 * @param genre_ids
	 * @param timesTaken
	 * @param rand_flag
	 * @param multipage_flag
	 * @param practice_flag
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, String description,
			ArrayList<String> question_ids, ArrayList<String> genre_ids, int timesTaken, 
			boolean rand_flag, boolean multipage_flag, boolean practice_flag, boolean grade_now_flag) throws SQLException {
		super();
		db = new QuizDBConnection();
		this.creator_id = creator_id;
		this.quiz_name = quiz_name;
		this.description = description;
		this.question_ids = question_ids;
		this.genre_ids = genre_ids;
		this.timesTaken = timesTaken;
		this.rand_flag = rand_flag;
		this.multipage_flag = multipage_flag;
		this.practice_flag = practice_flag;
		this.grade_now_flag = grade_now_flag;
		if(quiz_id == null) {
			this.quiz_id = addToDatabase();
			setDate();
		} else {
			setDate();
			this.quiz_id = quiz_id;
		}
	}
	
	/*
	 * Retrieves the date that the quiz was added to the database.
	 * Note: must be called after adding a quiz to the database
	 */
	private void setDate() throws SQLException{
		this.date_created = this.db.getQuizAttribute(TABLE_NAME, this.quiz_id, "date_created");
	}
	
	
	private void setFlags(ResultSet set) throws SQLException{
		this.rand_flag = set.getBoolean(RANDOM);
		this.multipage_flag = set.getBoolean(PAGES);
		this.practice_flag = set.getBoolean(PRACTICE);
		this.grade_now_flag = set.getBoolean(GRADE);
	}

	//note that if only putting in question_ids, put description before ArrayList in 
	//arguments
	/**
	 * Constructor handling if genre_ids not given
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param description
	 * @param question_ids
	 * @param timesTaken
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, String description,
			ArrayList<String> question_ids, int timesTaken) throws SQLException {
		this(quiz_id, creator_id, quiz_name, description, question_ids, null, timesTaken, false, false, false, false);
		if(quiz_id != null){
			ResultSet set = db.getQuizRowInfo(TABLE_NAME, this.quiz_id);
			set.next();
			genre_ids = toArrayList(set.getString(GENRES));
			setFlags(set);
		}
	}
	
	//note that if only passing in genre_ids, put description after
	/**
	 * constructor handling if question_ids omitted. Be careful in use,
	 * as, in order to distinguish from "genre_id" lacking constructor, 
	 * "description" and "genre_ids" order switched.
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param genre_ids
	 * @param description
	 * @param timesTaken
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name,
			ArrayList<String> genre_ids, String description, int timesTaken) throws SQLException {
		this(quiz_id, creator_id, quiz_name, description, null, genre_ids, timesTaken, false, false, false, false);
		if(quiz_id != null){
			ResultSet set = db.getQuizRowInfo(TABLE_NAME, this.quiz_id);
			set.next();
			question_ids = toArrayList(set.getString(QUESTIONS));
			setFlags(set);
		}
	}
	
	/**
	 * Case where both lists of genres and questions are omitted.
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param description
	 * @param timesTaken
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, boolean isNew, String description, int timesTaken) throws SQLException {
		this(quiz_id, creator_id, quiz_name, description, null, null, timesTaken, false, false, false, false);
		if(quiz_id != null){
			ResultSet set = db.getQuizRowInfo(TABLE_NAME, this.quiz_id);
			set.next();
			question_ids = toArrayList(set.getString(QUESTIONS));
			genre_ids = toArrayList(set.getString(GENRES));
			setFlags(set);
		}
	}
	
	/**
	 * Case where only the quiz_id, creator_id, quiz_name, and timesTaken are given
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param timesTaken
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, int timesTaken) throws SQLException {
		this(quiz_id, creator_id, quiz_name, "", null, null, timesTaken, false, false, false, false);
		if(quiz_id != null){
			ResultSet set = db.getQuizRowInfo(TABLE_NAME, this.quiz_id);
			set.next();
			question_ids = toArrayList(set.getString(QUESTIONS));
			genre_ids = toArrayList(set.getString(GENRES));
			description = set.getString(DESCRIPTION);
			setFlags(set);
		}
	}
	/**
	 * This constructor can only be called using a quiz_id that has already been
	 * created
	 * @param quiz_id
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id) throws SQLException{
		this(quiz_id, "", "", "", null, null, 0, false, false, false, false);
		ResultSet set = db.getQuizRowInfo(TABLE_NAME, this.quiz_id);
		set.next();
		question_ids = toArrayList(set.getString(QUESTIONS));
		genre_ids = toArrayList(set.getString(GENRES));
		description = set.getString(DESCRIPTION);
		quiz_name = set.getString(QUIZ_NAME);
		creator_id = set.getString(CREATOR_ID);
		timesTaken = set.getInt(TIMES_TAKEN);
		setFlags(set);
	}
	
	/*
	 * Takes a formatted string, and tokenizes the elements into an ArrayList
	 */
	 private static ArrayList<String> toArrayList(String str) {
		StringTokenizer tokenizer = new StringTokenizer(str, Quiz.ID_DELIMS);
		ArrayList<String> list = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()){
			list.add(tokenizer.nextToken());
		}
		return list;
	}
	
	/*
	 * returns a properly formatted String to be entered in the database -
	 * with the elements delineated by commas, bracketed by quotation marks
	 */
	private static String arrayToString(ArrayList<String> list) {
		if(list == null) return "";
		String str = list.toString();
		str = str.substring(1, str.length() - 1);
		return "\"" + str + "\"";
	}
	
	/*
	 * Extracts an attribute in String form from the table
	 */
	private static String parseCategory(ResultSet rowEntry, String colName) throws SQLException {
		rowEntry.next();
		return rowEntry.getString(colName);
	}
	

	/**
	 * returns the quiz_name
	 * @return
	 */
	public String getQuiz_name() {
		return quiz_name;
	}

	/**
	 * sets the quiz_name, updates database appropriately
	 * @param quiz_name
	 * @throws SQLException 
	 */
	public void setQuiz_name(String quiz_name) throws SQLException {
		this.quiz_name = quiz_name;
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUIZ_NAME, "\"" + this.quiz_name + "\"");
	}

	/**
	 * returns description
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * sets description, updates database
	 * @param description
	 * @throws SQLException 
	 */
	public void setDescription(String description) throws SQLException {
		this.description = description;
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, DESCRIPTION, "\"" + this.description + "\"");
	}

	/**
	 * returns ArrayList of question ids
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
	 * sets the question_ids to be the arg, updates the database with a
	 * String version
	 * @param question_ids
	 * @throws SQLException 
	 */
	public void setQuestion_ids(ArrayList<String> question_ids) throws SQLException {
		this.question_ids = question_ids;
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	/**
	 * returns an ArrayList of the genres
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
	 * sets genre_ids to the arg, then updates the database with a String
	 * version
	 * @param genre_ids
	 * @throws SQLException 
	 */
	public void setGenre_ids(ArrayList<String> genre_ids) throws SQLException {
		this.genre_ids = genre_ids;
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * returns the creator_id
	 * @return
	 */
	public String getCreator_id() {
		return creator_id;
	}
	
	/**
	 * returns the quiz_id in String form
	 * @return
	 */
	public String getQuiz_id() {
		return quiz_id;
	}
	
	/**
	 * returns the date created in String form
	 * @return
	 */
	public String getDate_created() {
		return date_created;
	}
	
	/**
	 * Adds a question at the specified index, if the index is not greater than the size
	 * of the ArrayList. Updates the database
	 * @param question_id
	 * @param question_index
	 * @throws SQLException 
	 */
	public void addQuestion(String question_id, int question_index) throws SQLException {
		if(question_index > question_ids.size()){
			question_ids.add(question_id);
		}
		question_ids.add(question_index, question_id);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * Adds a question to the end of the question list
	 * @param question_id
	 * @throws SQLException 
	 */
	public void addQuestion(String question_id) throws SQLException{
		question_ids.add(question_id);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * Adds a set of questions to the Quiz
	 * @param questions
	 * @throws SQLException 
	 */
	public void addQuestions(ArrayList<String> questions) throws SQLException{
		question_ids.addAll(questions);
		String questionStr = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questionStr);
	}
	
	/**
	 * Adds a set of questions to the Quiz, beginning at the specified index
	 * @param questions
	 * @param index
	 * @throws SQLException 
	 */
	public void addQuestions(ArrayList<String> questions, int index) throws SQLException{
		if(index > question_ids.size()){
			question_ids.addAll(questions);
		}
		question_ids.addAll(index, questions);
		String questionStr = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questionStr);
	} 
	
	/**
	 * Removes a question from the quiz, based on the question id
	 * @param question_id
	 * @throws SQLException 
	 */
	public void removeQuestion(String question_id) throws SQLException{
		question_ids.remove(question_id);
		String questionStr = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questionStr);
	}
	
	/**
	 * Removes a question from the quiz, based on the index of the question in
	 * the question list
	 * @param index
	 * @throws SQLException 
	 */
	public void removeQuestion(int index) throws SQLException{
		question_ids.remove(index);
		String questionStr = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questionStr);
	}
	
	/**
	 * Removes a set of questions from the question list
	 * @param toRemove
	 * @throws SQLException 
	 */
	public void removeQuestions(ArrayList<String> toRemove) throws SQLException{
		question_ids.removeAll(toRemove);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * Removes all questions from the question list
	 * @throws SQLException 
	 */
	public void clearQuestions() throws SQLException {
		question_ids.clear();
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, "\"\"");
	}
	
	/**
	 * Adds a genre association to the quiz
	 * @param genre_id
	 * @throws SQLException 
	 */
	public void addGenre(String genre_id) throws SQLException{
		genre_ids.add(genre_id);
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * Adds a set of genre associations to the Quiz
	 * @param genres
	 * @throws SQLException 
	 */
	public void addGenres(ArrayList<String> genres) throws SQLException{
		genre_ids.addAll(genres);
		String genreStr = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genreStr);
	}
	
	/**
	 * Removes a genre association from the quiz
	 * @param question_id
	 * @throws SQLException 
	 */
	public void removeGenre(String genre_id) throws SQLException{
		genre_ids.remove(genre_id);
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * Removes a set of genre associations from the quiz
	 * @param toRemove
	 * @throws SQLException 
	 */
	public void removeGenres(ArrayList<String> toRemove) throws SQLException{
		genre_ids.removeAll(toRemove);
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * Clears the list of genre associations from the quiz
	 * @throws SQLException 
	 */
	public void clearGenres() throws SQLException {
		genre_ids.clear();
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, "\"\"");
	}
	
	/**
	 * returns the number of questions in the quiz
	 * @return
	 */
	public int numberQuestions(){
		return question_ids.size();
	}
	
	/**
	 * Returns an ArrayList (sized to numberQs or the number of total quiz questions, whichever is smaller)
	 * of randomly ordered questions from the quiz.
	 * @param numberQs
	 * @return
	 */
	public ArrayList<String> generateRandomQSet(int numberQs){
		ArrayList<String> sublist = new ArrayList<String>();
		Random random = new Random();
		int size = question_ids.size();
		ArrayList<String> question_temp = new ArrayList<String>(question_ids);
		if(numberQs > size) numberQs = size;
		for(int i = 0; i < numberQs; i++){
			int rand = random.nextInt(size);
			sublist.add(question_temp.get(rand));
			question_temp.remove(rand);
			size--;
		}
		return sublist;
	}
	
	/**
	 * Sets the number of timesTaken, updates database
	 * @param newAmount
	 * @throws SQLException 
	 */
	public void setTimesTaken(int newAmount) throws SQLException {
		this.timesTaken = newAmount;
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, "num_times_taken", Integer.toString(this.timesTaken));
	}
	
	/**
	 * returns timesTaken
	 * @return
	 */
	public int getTimesTaken() {
		return this.timesTaken;
	}
	
	/**
	 * To be called, essentially, after a quiz is taken to increment the number of times
	 * it was taken
	 * @return
	 * @throws SQLException 
	 */
	public void incrementTimesTaken() throws SQLException {
		setTimesTaken(this.timesTaken + 1);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getRandomFlag(){
		return this.rand_flag;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getMultiPageFlag() {
		return this.multipage_flag;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getPracticeFlag() {
		return this.practice_flag;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getGradeNowFlag() {
		return this.grade_now_flag;
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void setRandomFlag() throws SQLException {
		if(this.rand_flag) db.setQuizAttribute(TABLE_NAME, this.quiz_id, "rand_flag", "TRUE");
		else db.setQuizAttribute(TABLE_NAME, this.quiz_id, "rand_flag", "FALSE");
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void setMultiPageFlag() throws SQLException{
		if(this.multipage_flag) db.setQuizAttribute(TABLE_NAME, this.quiz_id, "multipage_flag", "TRUE");
		else db.setQuizAttribute(TABLE_NAME, this.quiz_id, "multipage_flag", "FALSE");
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void setPracticeFlag() throws SQLException{
		if(this.practice_flag) db.setQuizAttribute(TABLE_NAME, this.quiz_id, "practice_flag", "TRUE");
		else db.setQuizAttribute(TABLE_NAME, this.quiz_id, "practice_flag", "FALSE");
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void setGradeNowFlag() throws SQLException {
		if(this.grade_now_flag) db.setQuizAttribute(TABLE_NAME, this.quiz_id, "grade_now_flag", "TRUE");
		else db.setQuizAttribute(TABLE_NAME, this.quiz_id, "grade_now_flag", "FALSE");
	}
	
	/**
	 * Do not call further methods on quiz object after calling this method.
	 * Behavior is undefined.
	 * @throws SQLException 
	 */
	public void removeQuiz() throws SQLException {
		db.removeItem(TABLE_NAME, this.quiz_id);
		db.close();
	}
	
	/*
	 * adds the quiz to the database by formatting a string that delineates all of its attributes
	 */
	private String addToDatabase() throws SQLException {
		String attributes = "( \"" + this.quiz_name + "\", \"" + this.creator_id + "\", \"" + this.description + "\", \"" + 
				arrayToString(this.question_ids) + "\", \"" + arrayToString(this.genre_ids) + "\", " + this.timesTaken + ", CURDATE(), " + getFlagString() + ")";
		return db.addQuizEntry(TABLE_NAME, attributes);
	}
	
	/*
	 * sets up the flag section of the attribute string in the addToDatabase method
	 */
	private String getFlagString() {
		String str = "";
		if(this.rand_flag) str += "TRUE, ";
		else str += "FALSE, ";
		if(this.multipage_flag) str += "TRUE, ";
		else str += "FALSE, ";
		if(this.practice_flag) str += "TRUE, ";
		else str += "FALSE, ";
		if(this.grade_now_flag) str += "TRUE";
		else str += "FALSE";
		return str;
	}

}

