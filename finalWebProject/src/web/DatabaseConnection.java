import java.sql.ResultSet;
import java.util.HashMap;


public class DatabaseConnection {
	//wanted methods:
	
	public void setAttributes(String table_name, String itemID, HashMap<String, String> attributes){
		//would need some way to associate itemID with specific col in given table:
		// -static final map connecting the table_names with primary ids (i.e. quiz_id for quizzes)
		//attributes would map colnames to values wanted to be set for given column
		//could use mapping to null as indicator to clear field
		//if key doesn't exist, then don't use to specify in query
	}

	public ResultSet getRowEntry(String tableName, String itemID) {
		// TODO would get appropriate col in table for itemID
		//construct SQL query based on that, return rowEntry as ResultSet
		return null;
	}
	
	public void addEntry(String table_name, HashMap<String, String> attributes){
		//will basically just need to parse attributes into proper form,
		//construct the SQL statement and feed it in
		//may need to be careful about how attributes is formatted, so SQL methods
		//parse into non-String cols in a consistent manner
	}
}

