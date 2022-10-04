package app;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import app.server.connection.ConnectionManager;

/**
 * The main entry point for the library application. Includes a
 * main method that calls launch for javaFX applications.
 */
public class MainApplication extends Application {
	
	public static void main(String[] args) {
		ConnectionManager.getSingleton().getConnection();
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
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 400, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
