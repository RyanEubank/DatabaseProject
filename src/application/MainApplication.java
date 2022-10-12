package src.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;

/**
 * The main entry point for the library application. Includes a
 * main method that calls launch for javaFX applications.
 */
public class MainApplication extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Starts the main window for the application.
	 * 
	 * @param primaryStage
	 * 	- the initial window stage setup by javaFX.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/resource/LoginScreen.fxml"));
			primaryStage.setTitle("LibraryManager");
			primaryStage.setScene(new Scene(root, 1200, 800));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
