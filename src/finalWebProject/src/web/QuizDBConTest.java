package finalWebProject.src.web;

import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

public class QuizDBConTest {

	private QuizDBConnection db;
	private static final String TABLE_NAME = "quizzes";
	private static final String ATTRIBUTES = "(\"name\", \"creator\", \"description\"," +
			"\"question list\", \"genre list\", 10, CURDATE())";
	private static final String COL_NAMES = "(quiz_name, creator_id, description, question_ids, genre_ids, num_times_taken, date_created)";
	private String quiz_id;
	
	@Before
	public void SetUp() throws SQLException {
		db = new QuizDBConnection();
		quiz_id = db.addQuizEntry(TABLE_NAME, ATTRIBUTES);
	}
	
	@Test
	public void RowInfoTest() throws SQLException {
		ResultSet set = db.getQuizRowInfo(TABLE_NAME, quiz_id);
		set.next();
		assertEquals("name", set.getString("quiz_name"));
		assertEquals("creator", set.getString("creator_id"));
		assertEquals("10", set.getString("num_times_taken"));
		System.out.println(set.getString("date_created"));
	}
	
	@Test
	public void getAttributeTest() throws SQLException {
		assertEquals("name", db.getQuizAttribute(TABLE_NAME, quiz_id, "quiz_name"));
		assertEquals("10", db.getQuizAttribute(TABLE_NAME, quiz_id, "num_times_taken"));
		assertEquals("question list", db.getQuizAttribute(TABLE_NAME, quiz_id, "questions_ids"));
	}
	
	@Test
	public void setGetAttributeTest() throws SQLException {
		db.setQuizAttribute(TABLE_NAME, quiz_id, "quiz_name", "\"poppycock\"");
		db.setQuizAttribute(TABLE_NAME, quiz_id, "num_times_taken", "30");
		assertEquals("poppycock", db.getQuizAttribute(TABLE_NAME, quiz_id, "quiz_name"));
		assertEquals("30", db.getQuizAttribute(TABLE_NAME, quiz_id, "num_times_taken"));
	}
	
	
}
