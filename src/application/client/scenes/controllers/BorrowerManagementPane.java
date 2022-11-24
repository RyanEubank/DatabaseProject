package src.application.client.scenes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import src.application.server.database.query.LibraryCardManager;

public class BorrowerManagementPane extends AbstractPane {

	@FXML
	private AnchorPane borrower_pane;
	@FXML
	private TextField user_ssn_field;
	@FXML
	private TextField user_fname_field;
	@FXML
	private TextField user_lname_field;
	@FXML
	private TextField user_address_field;
	@FXML
	private TextField user_phone_field;
	
	
	@Override
	public void postInitialize() {
		super.postInitialize();
	}

	/**
	 * Creates a new user in the library database.
	 */
	@Override
	protected void onPerformAction() {
		this.m_parent.setActionError("");
		
		String ssn = this.user_ssn_field.getText();
		String fname = this.user_fname_field.getText();
		String lname = this.user_lname_field.getText();
		String address = this.user_address_field.getText();
		String phone = this.user_phone_field.getText();
		
		createUser(ssn, fname, lname, address, phone);
	}
	
	/**
	 * Gathers the information entered by the user and 
	 * displays an error if there is missing information.
	 * If all information is provided the method attempts
	 * to create a new user in the library database.
	 */
	private void createUser(
		String ssn,
		String fname,
		String lname,
		String address,
		String phone
	) throws Exception {
		if (isRequiredFieldMissing(ssn, fname, lname, address))
			setErrorLabels();
		else {
			clearErrorLabels();
			new LibraryCardManager().createUser(ssn, fname, lname, address, phone);
		}
	}

	/**
	 * Sets the error text if there are missing fields
	 * of the required information for creating a new user.
	 */
	private void setErrorLabels() {
		this.borrower_pane.getChildren().stream()
			.filter((c) -> c instanceof Text)
			.forEach((label) -> ((Text)label).setText("*Required Field"));
	}

	/**
	 * Clears the text from the error labels next to the 
	 * required fields.
	 */
	private void clearErrorLabels() {
		this.borrower_pane.getChildren().stream()
			.filter((c) -> c instanceof Text)
			.forEach((label) -> ((Text)label).setText(""));
	}
	
	/**
	 * Returns whether any of the provided fields are
	 * empty or blank.
	 * 
	 * @param fields - the list of required fields.
	 * 
	 * @return
	 *  Returns true if all of te required fields are present.
	 */
	private boolean isRequiredFieldMissing(String... fields) {
		for (String field : fields) {
			if (field.isEmpty() || field.isBlank())
				return true;
		}
		return false;
	}

	/**
	 * Returns the name of the action performed by the borrower
	 * management pane: 'Create' for adding a new borrower and
	 * issuing a library card.
	 */
	@Override
	protected String getActionName() {
		return "Create";
	}
}
