package src.application.client.scenes;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.application.client.scenes.controllers.*;
import src.application.client.scenes.dialogs.AbstractDialogController;

public class SceneManager {
	
	// Singleton instance for the scene manager
	private static SceneManager INSTANCE;
	private static final StackPane DEFAULT_SCENE = new StackPane();
	
	private Stage m_stage; // the main application window.
	
	/**
	 * Constructs a scene manager and fills in the layout for the default
	 * scene displayed if an error occurs.
	 */
	private SceneManager() {
		Text placeholder_text = new Text("SCENE PLACEHOLDER");
		placeholder_text.setStyle("-fx-text-fill: red");
		DEFAULT_SCENE.getChildren().add(placeholder_text);
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
	 * Loads the specified fxml and css files and paints the application window
	 * with the resulting scene.
	 * 
	 * @param fxml - the path to the scene's fxml layout file
	 * @param css - the path the the css stylesheet for the scene
	 * @param width - the width of the resulting scene in pixels.
	 * @param height - the height of the resulting scene in pixels.
	 */
	public void setMainStage(String fxml, String style, int width, int height) {
		Scene scene = getStylizedScene(fxml, style, width, height);
		this.m_stage.setScene(scene);
		this.m_stage.show();
	}
	
	/**
	 * @param <T> - the type of the value returned by the dialog when closed.
	 * 
	 * @param fxml - the path to the dialog's fxml layout file
	 * 
	 * @return
	 *  Returns a dialog controller for the specified layout.
	 *  
	 * @throws Exception
	 *  Throws an exception if the controller class cannot be found or the
	 *  fxml file cannot be loaded.
	 */
	@SuppressWarnings("unchecked")
	public <T> Dialog<T> openDialog(String fxml) throws Exception {
		IController dialog = Scenes.getController(fxml);
		DialogPane pane = (DialogPane) loadScene(fxml, dialog);
		
		if (dialog instanceof AbstractDialogController<?>) {
			((AbstractDialogController<T>) dialog).setDialogPane(pane);
			((AbstractDialogController<T>) dialog).postInitialize();
			return (Dialog<T>) dialog;
		} else throw new RuntimeException("Failed");
		//return new Dialog<T>();
	}
	
	/**
	 * Loads an AbstractPane from the specified fxml file.
	 * 
	 * @param fxml - the path to the pane layout to be loaded.
	 * 
	 * @return
	 *  Returns an AbstractPane object with the given layout.
	 *  
	 * @throws Exception
	 *  Throws an exception if the fxml controller cannot be found for
	 *  the given file or the file cannot be read.
	 */
	public AbstractPane loadPane(String fxml) throws Exception {
		IController controller = Scenes.getController(fxml);
		loadScene(fxml, controller);
		if (controller instanceof AbstractPane) 
			return (AbstractPane) controller;
		else throw new Exception("Controller is not an AbstractPane type");
	}
	
	/**
	 * Obtains an instance of the screen controller then loads the scene
	 * layout and binds it to the controller and returns a root node for
	 * the scene.
	 * 
	 * @param fxml - the path to the scene layout to be loaded.
	 * @param controller - the controller to handle UI events in the scene.
	 * 
	 * @throws IOException
	 *  Throws an IPException if the scene layout cannot be found or there
	 *  is an error reading it from disk.
	 */
	private Pane loadScene(String fxml, IController controller) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		loader.setController(controller);
		return loader.load();
	}
	
	/**
	 * Loads the specified fxml scene layout and css style page from disk
	 * and returns the scene with stylization applied.
	 * 
	 * @param fxml - the path to the scene fxml file.
	 * @param style - the path to the css file.
	 * @param width - the width of the new scene in pixels.
	 * @param height - the height of the new scene in pixels.
	 * 
	 * @return
	 * Returns a new scene object with the given stlye applied.
	 */
	private Scene getStylizedScene(String fxml, String style, int width, int height) {
		String css = getClass().getResource(style).toExternalForm();
		Scene scene = loadFXMLFromFile(fxml, width, height);
		scene.getStylesheets().add(css);
		return scene;
	}
	
	
	/**
	 * Loads the specified fxml file to create a populated scene node ready to style
	 * and display.
	 * 
	 * @param fxml
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
	private Scene loadFXMLFromFile(String fxml, int width, int height) {
		Parent root = DEFAULT_SCENE;
		try {
			IController controller = Scenes.getController(fxml);
			root = loadScene(fxml, controller);
		} catch (Exception e) {
			System.out.println("Unable to load \"" + fxml + "\"");
			e.printStackTrace();
		}
		return new Scene(root, width, height);
	}
}
