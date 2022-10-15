package src.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import src.application.client.SceneManager;
import javafx.scene.*;
import javafx.scene.text.Font;

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
	public void start(Stage stage) {
		try {
			Scene scene = SceneManager.getSingleton().loadScene(SceneManager.LOGIN_SCREEN, 1200, 800);
			String style = getClass().getResource("/src/resource/stylesheets/application.css").toExternalForm();
			scene.getStylesheets().add(style);
			stage.setTitle("LibraryManager");
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
