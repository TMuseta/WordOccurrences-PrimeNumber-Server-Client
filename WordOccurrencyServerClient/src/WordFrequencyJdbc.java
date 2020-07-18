import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;



/**
 * This class helps to store and retrieve word and its count in the database.
 *
 */
public class WordFrequencyJdbc {

	/**
	 * The JDBC connection.
	 */
	private Connection connection;

	/**
	 * Constructor
	 */
	public WordFrequencyJdbc() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost:3306/wordOccurrences?serverTimezone=UTC";
			String user = "root";
			String password = "minana06";

			connection = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error connecting to database.");
			System.exit(0);
		}
	}

	/**
	 * Inserts the given word into the database.
	 * 
	 * @param word
	 *            the word to be inserted.
	 * @param count
	 *            the word count.
	 * @return
	 */
	public boolean insertWord(String word, int count) {
		try {
			Statement statement = connection.createStatement();
			String sql = "insert into wordOccurrences.word (word, word_count) values ('" + word + "', " + count + ")";
			int updatedCount = statement.executeUpdate(sql);
			if (updatedCount > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the list of words from the database.
	 * 
	 * @return
	 */
	public ArrayList<String> selectWords() {
		ArrayList<String> wordsList = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			String sql = "select * from wordOccurrences.word";
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String word = result.getString("word");
				wordsList.add(word);
			}
			return wordsList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the word count for the given word.
	 * 
	 * @param word
	 *            the word to be searched.
	 * @return
	 */
	public int getWordCount(String word) {
		try {
			Statement statement = connection.createStatement();
			String sql = "select word_count from wordOccurrences.word where word = '" + word + "'";
			ResultSet result = statement.executeQuery(sql);
			int count = 0;
			if (result.next()) {
				count = result.getInt("word_count");
			}
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Update the word and its count in the database.
	 * 
	 * @param word
	 *            the word to be inserted.
	 * @param count
	 *            the word count.
	 * @return
	 * @return
	 */
	public boolean updateWordCount(String word, int count) {
		try {
			Statement statement = connection.createStatement();
			String sql = "update wordOccurrences.word set word_count  = " + count + " where word = '" + word + "'";
			int updatedCount = statement.executeUpdate(sql);
			if (updatedCount > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the list of WordFrequency.
	 * 
	 * @return
	 */
	public List<WordFrequency> getWordsFrequency() {

		List<WordFrequency> list = new ArrayList<WordFrequency>();
		try {
			Statement statement = connection.createStatement();
			// Get the words ordered by count descending
			String sql = "select * from wordOccurrences.word order by word_count desc";
			ResultSet result = statement.executeQuery(sql);

			int num = 1;
			while (result.next()) {
				String word = result.getString("word");
				int count = result.getInt("word_count");
				WordFrequency wordFrequency = new WordFrequency(num, word, count);
				list.add(wordFrequency);
				num++;
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Deletes all the previous records from the word table.
	 * 
	 * @return
	 */
	public boolean clearWordTable() {
		try {
			Statement statement = connection.createStatement();
			String sql = "delete from wordOccurrences.word";
			statement.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}