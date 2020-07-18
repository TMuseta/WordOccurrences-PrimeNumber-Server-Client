import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * This program reads an input file, parses it into word tokens, count the
 * number of occurrence of each word and print them in descending order.
 */
public class WordFrequencyProcessor {

	/**
	 * The WordFrequencyJdbc object.
	 */
	private WordFrequencyJdbc jdbc = null;

	/**
	 * Default constructor
	 */
	public WordFrequencyProcessor() {
		jdbc = new WordFrequencyJdbc();
		boolean done = jdbc.clearWordTable();
		if (!done) {
			System.out.println("Could not clear table.");
			System.exit(0);
		}
	}

	/**
	 * This function reads file line by line, parse each line to get word tokens and
	 * put token into map.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void readFile(String fileName) throws IOException {
		try {

			// Using buffered reader to read the file.
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			String line;

			// Looping through line by line till file has a line in it.
			while ((line = reader.readLine()) != null) {
				// Converting each line into lower case letters
				line = line.toLowerCase();
				// Removing all characters other than space, a-z or A-Z
				line = line.replaceAll("[^\\sa-zA-Z]", "");
				// Replacing all whitespaces occurring more than once into single space,
				// then trimming whitespaces from left, right side of the line
				line = line.replaceAll("\\s+", " ").trim();

				// If line is not empty
				if (!line.isEmpty()) {
					// splitting line into words considering space as the delimiter
					String[] s = line.split(" ");
					// For each word in array s, putting it into map
					for (String token : s) {
						// If word is already present, incrementing its count
						int dbCount = jdbc.getWordCount(token);
						if (dbCount == 0) {
							// Add word for the first time.
							jdbc.insertWord(token, 1);
						} else {
							// Update the word count
							jdbc.updateWordCount(token, dbCount + 1);
						}
					} // for
				} // if
			} // while

			reader.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			System.out.println("File not found: " + ex.getMessage());
			throw ex;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error occurred: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Returns the list of WordFrequency.
	 * 
	 * @return
	 */
	public List<WordFrequency> getFrequency() {
		return jdbc.getWordsFrequency();
	}
}
