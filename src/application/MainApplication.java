package src.application;

import javafx.application.Application;
import javafx.stage.*;
import src.application.client.scenes.*;

/**
 * The main entry point for the library application. Includes a
 * main method that calls launch for javaFX applications.
 */
public class MainApplication extends Application {
	
	public static void main(String[] args) {
		launch(args); 
	}
	
	/**
	 * Sets window properties and starts the main window for the application.
	 * 
	 * @param primaryStage
	 * 	- the initial window stage setup by javaFX.
	 */
	@Override
	public void start(Stage stage) {
		stage.setTitle("LibraryManager");
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		SceneManager.initialize(stage);
		SceneManager.getSingleton().setMainStage(
			Scenes.LOGIN_SCREEN, Scenes.DEFAULT_STYLESHEET, 1200, 800);
	}
}
