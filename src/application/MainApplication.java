package src.application;

import javafx.application.Application;
import javafx.stage.Stage;
import src.application.client.SceneManager;

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
		stage.setTitle("LibraryManager");
		stage.setResizable(false);
		SceneManager.initialize(stage);
		SceneManager.getSingleton().loadScene(SceneManager.LOGIN_SCREEN, SceneManager.STYLE, 1200, 800);
	}
}
