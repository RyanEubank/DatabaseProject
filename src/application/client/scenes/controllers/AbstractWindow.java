package src.application.client.scenes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * AbstractWindow defines an fxml scene controller that has a node
 * ID'd as "exit_button" to exits the program when clicked.
 */
public abstract class AbstractWindow implements IController {

	@FXML
	private Button exit_button;

	/**
	 * Registers the event handler for the screen's exit button.
	 */
	@Override
	public void initialize() {
		this.exit_button.setOnAction((event)-> OnExit());
		this.exit_button.setFocusTraversable(false);
	}
	
	/**
	 * Called when the exit button is clicked to exit the program.
	 */
	public void OnExit() {
		System.exit(0);
	}
}
