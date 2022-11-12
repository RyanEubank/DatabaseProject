package src.application.client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CheckoutDialog extends AbstractAppDialog<Integer>{
	
	@FXML
	private TextField borrower_id_box;
	
	/**
	 * Loads the checkout dialog box layout from an fxml file and binds the
	 * neccessary result handler to retrieve the selected borrower id from user
	 * input.
	 * 
	 * @parm fxml - the path to the fxml file storing the dialog layout.
	 */
	public CheckoutDialog(String fxml) {
		super(fxml, -1);
		this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		this.borrower_id_box.textProperty().addListener(
			(obs, oldID, newID) -> parseID(newID));
	}
	
	/**
	 * Sets the returned id in the dialog box from the text field.
	 */
	public void OnMakeSelection() {
		parseID(this.borrower_id_box.getText());
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
}
