package src.application.client.scenes;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

public abstract class AbstractAppDialog<T> extends Dialog<T> {

	protected T m_dialogRetVal;
	
	/**
	 * Loads the dialog box layout from an fxml file and binds the
	 * neccessary result handler to retrieve the selected input from
	 * the user and sets the default selection.
	 */
	public AbstractAppDialog(String fxml, T defaultVal) {
		super();
		
		// get the fxml file path and set the controller
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		loader.setController(this);
		this.m_dialogRetVal = defaultVal;
		
		// attempt to load the scene layout for the dialog box
		try {
			this.setDialogPane(loader.load());
			
			// bind the result converter to the ok button to retrieve values upon
			// user commit
			this.setResultConverter(select());
			
		} catch (IOException e) {
			System.out.println("Unable to load dialog popup");
			e.printStackTrace();
		}
	}
	

	/**
	 * Sets the button callback to retrieve the user's selected date
	 * from the calendar popup.
	 * 
	 * @return
	 *  Returns a callback to the input selection button.
	 */
	private Callback<ButtonType, T> select() {
		return new Callback<ButtonType, T>() {
			
			@Override
			public T call(ButtonType button) {
				if (button == ButtonType.OK) {
					OnMakeSelection();
					return m_dialogRetVal;
				} else
					return null;
			}
		};
	}
	
	/**
	 * Retrieves the user selection from the appropriate
	 * field on the dialog screen and sets the dialog return value.
	 */
	protected abstract void OnMakeSelection();
}
