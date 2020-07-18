import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * This class gets number from a client and sends back true if the number is
 * prime, and false otherwise.
 *
 */
public class WordFrequencyAppServer extends Application {

	/**
	 * Main method to launch the Application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		TextArea ta = new TextArea();

		Scene scene = new Scene(new ScrollPane(ta));
		
		
		stage.setScene(scene);

		stage.setTitle("Server");
		stage.show();

		// start the server thread.
		new Thread(() -> {
			try {
				// Create server socket
				ServerSocket serverSocket = new ServerSocket(7000);
				Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));
				// Listen for client's connection request
				Socket socket = serverSocket.accept();

				// Create input reader and output writer
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				// Read the file name
				String fileName = br.readLine();
				Platform.runLater(() -> ta.appendText("File name received from client: " + fileName + '\n'));

				// Process words from file
				WordFrequencyProcessor wordFreqProcessor = new WordFrequencyProcessor();
				try {
					wordFreqProcessor.readFile(fileName);
					Platform.runLater(() -> ta.appendText("File processing completed.\n"));
				} catch (IOException e) {
					e.printStackTrace();
					Platform.runLater(() -> ta.appendText("Error reading from the file and updating the database.\n"));
					return;
				}

				// Get all the words and their count and send them to the client.
				List<WordFrequency> list = wordFreqProcessor.getFrequency();
				int size = list.size();
				for (int i = 0; i < size; i++) {
					WordFrequency wordFreq = (WordFrequency) list.get(i);

					String word = wordFreq.getWord();
					int count = wordFreq.getCount();
					// Write "word length"
					String wordCount = word + " " + count;
					System.out.println("Word Freq: " + wordCount);
					bw.write(wordCount);
					//write new line char
					bw.write('\n');
					bw.flush();
					Platform.runLater(() -> ta.appendText("Word Frequency sent: " + wordCount + "\n"));
				} // for

				// Write "END OF DATA" to denote the end of data
				String s = "END OF DATA";
				System.out.println(s);
				bw.write(s);
				//write new line char
				bw.write('\n');
				bw.flush();

				Platform.runLater(() -> ta.appendText("\nSent: " + s + "\n"));
			} catch (Exception ex) {
				ex.printStackTrace();
				Platform.runLater(() -> ta.appendText("Error starting server.\n"));
			}
		}).start();
	}

}
