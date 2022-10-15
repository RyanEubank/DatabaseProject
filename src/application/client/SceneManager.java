package src.application.client;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;

public class SceneManager {
	
	public static final String LOGIN_SCREEN = "src/resource/stylesheets/LoginScreen.fxml";
	
	// Singleton instance for the scene manager
	private static SceneManager INSTANCE;
	
	/**
	 * Returns an instance of the scene manager, or creates a new
	 * instance if it does not yet exist: i.e. the first call to this
	 * method.
	 * 
	 * @return
	 * 	a static instance of the scene manager for the library client GUI.
	 */
	public static SceneManager getSingleton() {
		if (INSTANCE == null)
			INSTANCE = new SceneManager();
		return INSTANCE;
	}
	
	public Scene loadScene(String name, int width, int height) throws IOException {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(name));
		return new Scene(root, width, height);
		
	}
}
