package src.application.client.scenes;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class SceneManager {
	
	//path to common stylesheets
	public static final String STYLE = "/src/resource/stylesheets/application.css";
	
	// Singleton instance for the scene manager
	private static SceneManager INSTANCE;
	private static final StackPane DEFAULT_SCENE = new StackPane();
	
	private Stage m_stage; // the main application window.
	
	/**
	 * Constructs a scene manager and fills in the layout for the default
	 * scene displayed if an error occurs.
	 */
	private SceneManager() {
		Text error = new Text("SCENE PLACEHOLDER");
		error.setStyle("-fx-text-fill: red");
		DEFAULT_SCENE.getChildren().add(error);
	}	
	
	/**
	 * Loads the specified fxml file to create a populated scene node ready to style
	 * and display.
	 * 
	 * @param scene
	 * 	- the path and name of the fxml file specifying the scene to load.
	 * @param width
	 *  - the width of the screen to draw, in pixels
	 * @param height
	 *  - the height of the screen to draw, in pixels
	 * @return
	 *  The scene node for the requested scne to load.
	 *  
	 * @throws IOException - an exception may be thrown if the specified file 
	 * cannot be loaded properly.
	 */
	private Scene loadSceneFromFile(String scene, int width, int height) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource(scene));
		} catch (Exception e) {
			System.out.println("Unable to load \"" + scene + "\"");
			e.printStackTrace();
			root = DEFAULT_SCENE;
		}
		return new Scene(root, width, height);
	}
	
	/**
	 * Initializes the scene manager by creating a singleton instance and
	 * setting the main application stage.
	 * 
	 * @param application_stage - the main stage for the application.
	 */
	public static void initialize(Stage application_stage) {
		if (getSingleton().m_stage == null)
			getSingleton().m_stage = application_stage;
	}
	
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
	
	/**
	 * Loads the specified fxml scene layout and css style page from disk
	 * and returns the scene with stylization applied.
	 * 
	 * @param scenePath - the path to the scene fxml file.
	 * @param style - the path to the css file.
	 * @param width - the width of the new scene in pixels.
	 * @param height - the height of the new scene in pixels.
	 * 
	 * @return
	 * Returns a new scene object with the given stlye applied.
	 */
	private Scene getStylizedScene(String scenePath, String style, int width, int height) {
		String css = getClass().getResource(style).toExternalForm();
		Scene scene = loadSceneFromFile(scenePath, width, height);
		scene.getStylesheets().add(css);
		return scene;
	}
	
	/**
	 * Loads the specified fxml and css files and paints the application window
	 * with the resulting scene.
	 * 
	 * @param scenePath - the path to the scene's fxml layout file
	 * @param css - the path the the css stylesheet for the scene
	 * @param width - the width of the resulting scene in pixels.
	 * @param height - the height of the resulting scene in pixels.
	 */
	public void loadScene(String scenePath, String style, int width, int height) {
		Scene scene = getStylizedScene(scenePath, style, width, height);
		this.m_stage.setScene(scene);
		this.m_stage.show();
	}
}
