import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This program reads an input file, parses it into word tokens, count the
 * number of occurrence of each word and print them in descending order.
 */
public class WordFrequencyAppClient extends Application {

	/**
	 * Input file name
	 */
	private static final String FILE_NAME = "poem.txt";

	/**
	 * The TableView to display the words and their count.
	 */
	private TableView<WordFrequency> freqTable = null;

	/**
	 * The ScrollPane to provide scrolling functionality to the table.
	 */
	private ScrollPane tableScrollPane;

	/**
	 * Input reader.
	 */
	BufferedReader br = null;

	/**
	 * Output writer.
	 */
	BufferedWriter bw = null;

	/**
	 * Main method to launch the WordFrequencyApp.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		// Create Table
		createTable();
		// Add Table to scroll pane
		tableScrollPane = new ScrollPane();
		tableScrollPane.setContent(freqTable);

		// Creating a Grid Pane
		GridPane gridPane = new GridPane();

		// Setting size for the pane
		gridPane.setMinSize(500, 500);

		// Setting the padding
		gridPane.setPadding(new Insets(10, 10, 10, 10));

		// Setting the vertical and horizontal gaps between the columns
		gridPane.setVgap(5);
		gridPane.setHgap(5);

		// Setting the Grid alignment
		gridPane.setAlignment(Pos.CENTER);

		Button processB = new Button("Get Words Count");
		processB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showWordFrequency();
			}
		});

		// Arranging all the nodes in the grid
		gridPane.add(processB, 0, 0);
		gridPane.add(tableScrollPane, 0, 1);

		// Setting the back ground color
		gridPane.setStyle("-fx-background-color: PURPLE, BLUE;");

		// Create Scene
		Scene scene = new Scene(gridPane);

		// Adding scene to the stage
		stage.setScene(scene);
		// Set title for stage
		stage.setTitle("Word Frequency Client");

		// Displaying the contents of the stage
		stage.show();

		try {
			// client socket to connect to the server
			Socket socket = new Socket("localhost", 7000);

			// create input reader
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// create input writer
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (IOException ex) {
			showErrorAlert("Error", "Read write Error", "Error occurred while communicating with server");
			System.exit(0);
		}
	}

	/**
	 * Creates and returns the TableView.
	 */
	private void createTable() {
		freqTable = new TableView<>();

		TableColumn<WordFrequency, Integer> column1 = new TableColumn<WordFrequency, Integer>("No.");
		column1.setCellValueFactory(new PropertyValueFactory<WordFrequency, Integer>("serialNumber"));

		TableColumn<WordFrequency, String> column2 = new TableColumn<WordFrequency, String>("Word");
		column2.setCellValueFactory(new PropertyValueFactory<WordFrequency, String>("word"));

		TableColumn<WordFrequency, Integer> column3 = new TableColumn<WordFrequency, Integer>("Count");
		column3.setCellValueFactory(new PropertyValueFactory<WordFrequency, Integer>("count"));

		List<TableColumn<WordFrequency, ?>> list = new ArrayList<TableColumn<WordFrequency, ?>>();
		list.add(column1);
		list.add(column2);
		list.add(column3);

		freqTable.getColumns().addAll(list);
	}

	/**
	 * Displays the words and their frequency in the TableView.
	 */
	private void showWordFrequency() {
		// connect to server
		// Send number to the server
		try {
			// Write the file name
			bw.write(FILE_NAME);
			//write new line char
			bw.write('\n');
			bw.flush();

			// Get data from the server
			int num = 1;
			while (true) {
				// Read word count
				String wordAndCount = br.readLine();
				if ((wordAndCount == null) || wordAndCount.startsWith("END OF DATA")) {
					// Reached End Of Data
					break;
				}
				int spacePos = wordAndCount.indexOf(' ');
				if (spacePos == -1) {
					spacePos = wordAndCount.length();
				}
				String word = wordAndCount.substring(0, spacePos);
				System.out.println("word: " + word);
				
				int count = Integer.parseInt(wordAndCount.substring(spacePos + 1));

				// Create and add WordFrequency to the list.
				WordFrequency wordFreq = new WordFrequency(num, word, count);
				// Add data to the table
				freqTable.getItems().add(wordFreq);
				num++;
			}
		} catch (IOException ex) {
			showErrorAlert("Error", "Read write Error", "Error occurred while communicating with server");
			System.exit(0);
		}
	}

	/**
	 * Displays error message as an alert dialog.
	 * 
	 * @param title
	 * @param headerText
	 * @param contentText
	 */
	public static void showErrorAlert(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
}
