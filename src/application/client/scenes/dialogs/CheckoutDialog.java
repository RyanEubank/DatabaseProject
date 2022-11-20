package src.application.client.scenes.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CheckoutDialog extends AbstractDialogController<Integer>{
	
	@FXML
	private TextField borrower_id_field;
	
	/**
	 * Constructs a new CheckoutDialog with the default borrower ID set
	 * to -1, an invalid ID.
	 */
	public CheckoutDialog() {
		super(-1);
	}
	
	/**
	 * Sets the returned id in the dialog box from the text field.
	 */
	public void onMakeSelection() {
		parseID(this.borrower_id_field.getText());
	}
	
	/**
	 * Checks the validity of the borrower id entered in the text box
	 * and enables/disables the OK button based on parsing success.
	 * 
	 * @param id - the string to parse as the selected borrower card number.
	 */
	public void parseID(String id) {
		try {
			int value = Integer.parseInt(id);
			if (value < 0)
				throw new NumberFormatException("Negative ID value.");
			this.m_dialogRetVal = value;
			this.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
		} catch (NumberFormatException e) {;
			this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		}
	}

	/**
	 * Sets up a listener to parse the borrower ID entered by the user.
	 */
	@Override
	public void initialize() {
		this.borrower_id_field.textProperty().addListener(
			(obs, oldID, newID) -> parseID(newID));
	}

	/**
	 * Disables the OK button on dialog open to prevent accidental
	 * submission of a null ID until the user has actually entered one.
	 */
	@Override
	public void postInitialize() {
		this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
	}
}
