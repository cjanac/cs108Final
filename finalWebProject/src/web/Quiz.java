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
	private int timesTaken;
	private QuizDBConnection db;
	private static final String TABLE_NAME = "quizzes";
	private static final String[] COL_NAMES = {"quiz_id", "quiz_name", 
		"creator_id", "description", "date_created", "question_ids", "genre_ids", "quiz_id", "num_times_taken"};
	private static final String QUIZ_ID = "quiz_id";
	private static final String QUIZ_NAME = "quiz_name";
	private static final String CREATOR_ID = "creator_id";
	private static final String DESCRIPTION = "description";
	private static final String DATE_CREATED = "date_created";
	private static final String QUESTIONS = "question_ids";
	private static final String GENRES = "genre_ids";
	private static final String TIMES_TAKEN = "num_times_taken";
	private static final String ID_DELIMS = ", \t\n\r\f";
	
	
	/**
	 * if quiz_id is null, will assume that new quiz is being created.
	 * If new quiz, pass in 0 as argument to timesTaken
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param description
	 * @param question_ids
	 * @param genre_ids
	 * @param timesTaken
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, String description,
			ArrayList<String> question_ids, ArrayList<String> genre_ids, int timesTaken) throws SQLException {
		super();
		db = new DatabaseConnection();
		this.creator_id = creator_id;
		this.quiz_name = quiz_name;
		this.description = description;
		this.question_ids = question_ids;
		this.genre_ids = genre_ids;
		this.timesTaken = timesTaken;
		if(quiz_id == null) {
			this.quiz_id = addToDatabase();
			setDate();
		} else {
			this.date_created = parseCategory(db.getQuizRowInfo(TABLE_NAME, quiz_id), DATE_CREATED);
			this.quiz_id = quiz_id;
		}
	}
	
	/*
	 * 
	 */
	private void setDate(){
		this.date_created = this.db.getQuizAttribute(TABLE_NAME, this.quiz_id, "date_created");
	}
	

	//note that if only putting in question_ids, put description before ArrayList in 
	//arguments
	/**
	 * 
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
		this(quiz_id, creator_id, quiz_name, description, question_ids, null, timesTaken);
		if(quiz_id != null){
			genre_ids = toArrayList(parseCategory(db.getQuizRowInfo(TABLE_NAME, this.quiz_id), COL_NAMES[6]));
		}
	}
	
	//note that if only passing in genre_ids, put description after
	/**
	 * 
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
		this(quiz_id, creator_id, quiz_name, description, null, genre_ids, int timesTaken);
		if(quiz_id != null){
			question_ids = toArrayList(parseCategory(db.getQuizRowInfo(TABLE_NAME, this.quiz_id), COL_NAMES[5]));
		}
	}
	
	/**
	 * 
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param description
	 * @param timesTaken
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, boolean isNew, String description, int timesTaken) throws SQLException {
		this(quiz_id, creator_id, quiz_name, description, null, null, timesTaken);
		if(quiz_id != null){
			question_ids = toArrayList(parseCategory(db.getQuizRowInfo(TABLE_NAME, this.quiz_id), COL_NAMES[5]));
			genre_ids = toArrayList(parseCategory(db.getQuizRowInfo(TABLE_NAME, this.quiz_id), COL_NAMES[6]));
		}
	}
	
	/**
	 * 
	 * @param quiz_id
	 * @param creator_id
	 * @param quiz_name
	 * @param timesTaken
	 * @throws SQLException 
	 */
	public Quiz(String quiz_id, String creator_id, String quiz_name, int timesTaken) throws SQLException {
		this(quiz_id, creator_id, quiz_name, "", null, null, timesTaken);
		if(quiz_id != null){
			question_ids = toArrayList(parseCategory(db.getQuizRowInfo(TABLE_NAME, this.quiz_id), COL_NAMES[5]));
			genre_ids = toArrayList(parseCategory(db.getQuizRowInfo(TABLE_NAME, this.quiz_id), COL_NAMES[6]));
			description = parseCategory(db.getQuizRowInfo(TABLE_NAME, this.quiz_id), COL_NAMES[3]);
		}
	}
	
	/*
	 * 
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
	 * 
	 */
	private static String arrayToString(ArrayList<String> list) {
		if(list = null) return "";
		String str = list.toString();
		str = str.substring(1, str.length() - 1);
		return str;
	}
	
	/*
	 * 
	 */
	private static String parseCategory(ResultSet rowEntry, String colName) throws SQLException {
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
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUIZ_NAME, this.quiz_name)
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
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, DESCRIPTION, this.description);
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
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
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
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
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
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * 
	 * @param question_id
	 */
	public void addQuestion(String question_id){
		question_ids.add(question_id);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * 
	 * @param questions
	 */
	public void addQuestions(ArrayList<String> questions){
		question_ids.addAll(questions);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
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
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	} 
	
	/**
	 * 
	 * @param question_id
	 */
	public void removeQuestion(String question_id){
		question_ids.remove(question_id);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * 
	 * @param index
	 */
	public void removeQuestion(int index){
		question_ids.remove(index);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * 
	 * @param toRemove
	 */
	public void removeQuestions(ArrayList<String> toRemove){
		question_ids.removeAll(toRemove);
		String questions = arrayToString(this.question_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, questions);
	}
	
	/**
	 * 
	 */
	public void clearQuestions() {
		question_ids.clear();
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, QUESTIONS, "");
	}
	
	/**
	 * 
	 * @param genre_id
	 */
	public void addGenre(String genre_id){
		genre_ids.add(genre_id);
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * 
	 * @param genres
	 */
	public void addGenres(ArrayList<String> genres){
		genre_ids.addAll(genres);
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * 
	 * @param question_id
	 */
	public void removeGenre(String genre_id){
		genre_ids.remove(genre_id);
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * 
	 * @param toRemove
	 */
	public void removeGenres(ArrayList<String> toRemove){
		genre_ids.removeAll(toRemove);
		String genres = arrayToString(this.genre_ids);
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, genres);
	}
	
	/**
	 * 
	 */
	public void clearGenres() {
		genre_ids.clear();
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, GENRES, "\"\"");
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
	
	/**
	 * 
	 * @param newAmount
	 */
	public void setTimesTaken(int newAmount) {
		this.timesTaken = newAmount;
		db.setQuizAttribute(TABLE_NAME, this.quiz_id, "num_times_taken", this.timesTaken);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTimesTaken() {
		return this.timesTaken;
	}
	
	/**
	 * 
	 * @return
	 */
	public int incrementTimesTaken() {
		setTimesTaken(this.timesTaken + 1);
	}
	
	/**
	 * Do not call further methods on quiz object after calling this method.
	 * Behavior is undefined.
	 */
	public void removeQuiz() {
		db.removeQuiz(TABLE_NAME, this.quiz_id);
		db.close();
	}
	
	/*
	 * 
	 */
	private void addToDatabase() {
		String attributes = "( \"" + this.quiz_name + "\", \"" + this.creator_id + "\", \"" + this.description + "\", \"" + 
				arrayToString(this.question_ids) + "\", \"" + arrayToString(this.genre_ids) + "\", " + this.num_times_taken + ", CURDATE())";
		db.addQuizEntry(TABLE_NAME, attributes);
	}

}

