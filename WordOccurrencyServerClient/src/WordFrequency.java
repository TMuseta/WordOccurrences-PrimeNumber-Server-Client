/**
 * Class containing word and it's frequency
 *
 */
public class WordFrequency {

	/**
	 * The serial number.
	 */
	private int serialNumber;
	/**
	 * The word.
	 */
	private String word;
	/**
	 * The count.
	 */
	private int count;

	/**
	 * Constructor that initializes fields.
	 * 
	 * @param serialNumber
	 * @param word
	 * @param count
	 */
	public WordFrequency(int serialNumber, String word, int count) {
		super();
		this.serialNumber = serialNumber;
		this.word = word;
		this.count = count;
	}

	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
