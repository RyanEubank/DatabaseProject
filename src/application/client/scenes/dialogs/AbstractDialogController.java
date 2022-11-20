package src.application.client.scenes.dialogs;

import javafx.scene.control.*;
import javafx.util.Callback;
import src.application.client.scenes.controllers.IController;

public abstract class AbstractDialogController<T> extends Dialog<T> implements IController {

	protected T m_dialogRetVal;
	
	/**
	 * Binds the result handler to retrieve the selected input from
	 * the user and sets the default selection.
	 * 
	 * @param defaultVal - the default selection for the dialog.
	 */
	public AbstractDialogController(T defaultVal) {
		super();
		this.m_dialogRetVal = defaultVal;

		// bind the result converter to the ok button to retrieve values upon
		// user commit
		setResultConverter(select());
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
					onMakeSelection();
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
	protected abstract void onMakeSelection();

	/**
	 * Sets up any additional UI elements after the layout is loaded.
	 */
	public abstract void postInitialize();
}
