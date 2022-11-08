package src.application.client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * AbstractScreen defines an fxml loaded scene with a node
 * ID'd as "exit_button" that exits the program when clicked.
 * @author Ryan
 *
 */
public class AbstractScreen {

	@FXML
	private Button exit_button;
	
	/**
	 * Exits the program when the exit button is clicked.
	 */
	public void OnExit() {
		System.exit(0);
	}

	@FXML
	public void initialize() {
		this.exit_button.setOnAction((event)-> OnExit());
	}
}
