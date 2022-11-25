package src.application.client.scenes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import src.application.server.database.query.BorrowerInsertHandler;
import src.application.server.database.query.BorrowerLookupHandler;

public class BorrowerManagementPane extends AbstractPane {

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
	
	@FXML
	private Text ssn_error;
	@FXML
	private Text fname_error;
	@FXML
	private Text lname_error;
	@FXML
	private Text address_error;
	@FXML
	private Text phone_error;
	
	@FXML
	private Label success_text;
	
	/**
	 * Clears all text in the error fields
	 * on initilization.
	 */
	@Override
	public void initialize() {
		super.initialize();
		clearErrors();
	}
	
	/**
	 * Tries to parse the given string and disables the action
	 * button if parsing fails.
	 * 
	 * @param value - the value to parse.
	 * @param length - the required number of digits.
	 * 
	 * @returns
	 *  Returns true if the value parsed was a valid number,
	 *  false otherwise.
	 */
	private boolean parseNumericField(String value, int length) {
		if (value.length() == length) {
			for(int i = 0; i < length; i++) {
				if (!Character.isDigit(value.charAt(i)))
					return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Creates a new user in the library database by gathering
	 * information entered by the user and passing to the appropriate
	 * query handler.
	 */
	@Override
	protected void onPerformAction() {
		this.m_parent.setActionError("");
		clearErrors();
		
		String ssn = this.user_ssn_field.getText();
		String fname = this.user_fname_field.getText();
		String lname = this.user_lname_field.getText();
		String address = this.user_address_field.getText();
		String phone = this.user_phone_field.getText();
		
		createUser(ssn, fname, lname, address, phone);
	}

	/**
	 * Clears the label next to each text field for new user
	 * information.
	 */
	private void clearErrors() {
		this.ssn_error.setText("");
		this.fname_error.setText("");
		this.lname_error.setText("");
		this.address_error.setText("");
		this.phone_error.setText("");
		this.success_text.setText("");
	}
	
	/**
	 * Checks that all information required to create a new user
	 * is present and attempts to create a new entry in the database.
	 * Missing fields will be indicated to the user.
	 * 
	 * @param ssn - the ssn entered for the new user.
	 * @param fname - the first name entered for the new user.
	 * @param lname - the last name entered for the new user.
	 * @param address - the address entered for the new user.
	 * @param phone - the phone number entered for he new user.
	 */
	private void createUser(
		String ssn, String fname, String lname,
		String address, String phone
	) {
		boolean isSsnValid = validateSsn(ssn);
		boolean isfNameValid = checkRequired(fname, this.fname_error);
		boolean islNameValid = checkRequired(lname, this.lname_error);
		boolean isAddrValid = checkRequired(address, this.address_error);
		boolean isPhoneValid = validatePhoneNum(phone);
		
		if (isSsnValid && isfNameValid && 
			islNameValid && isAddrValid && isPhoneValid) 
		{
			String fullName = fname + " " + lname;
			onNewBorrowerEntry(ssn, fullName, address, phone);
		}
	}

	/**
	 * Returns whether the specified string is a valid ssn.
	 * 
	 * @param ssn - the ssn to validate.
	 * 
	 * @return
	 *  Returns true if the specified string is exactly 9 numerical
	 *  digits.
	 */
	private boolean validateSsn(String ssn) {
		if (!checkRequired(ssn, this.ssn_error)) 
			return false;
		if (!parseNumericField(ssn, 9)) {
			this.ssn_error.setText("*Must be 9-digit number");
			return false;
		}
		return true;
	}
	
	/**
	 * Returns whether the specified string is a valid phone number.
	 * 
	 * @param phone - the phone number to validate.
	 * 
	 * @return
	 *  Returns true if the specified string is exactly 10 numerical
	 *  digits.
	 */
	private boolean validatePhoneNum(String phone) {
		if (phone.isEmpty() || phone.isBlank())
			return true; // phone number is not a required field.
		if (!parseNumericField(phone, 10)) {
			this.phone_error.setText("*Must be 10-digit number");
			return false;
		}
		return true;
	}

	/**
	 * Creates a new borrower entry in the database.
	 * 
	 * @param ssn - the ssn of the new user.
	 * @param fullName - the name of the new user.
	 * @param address - the addresas of the new user.
	 * @param phone - the phone number of the new user.
	 */
	private void onNewBorrowerEntry(
		String ssn, String fullName, String address, String phone
	) {
		try {
			String formattedSsn = new BorrowerInsertHandler().createUser(
				ssn, fullName, address, phone);
			int borrowerID = new BorrowerLookupHandler().lookup(formattedSsn);
			clearValues();
			this.success_text.setText("New Borrower's ID: " + borrowerID);
		} catch (Exception e) {
			displayError(e);
		}
	}

	/**
	 * Clears all text fields in the management screen.
	 */
	private void clearValues() {
		this.user_ssn_field.clear();
		this.user_fname_field.clear();
		this.user_lname_field.clear();
		this.user_address_field.clear();
		this.user_phone_field.clear();
	}

	/**
	 * Checks that the provided text is not empty or only
	 * whitespace and displays error text to the user if so.
	 * 
	 * @param text - the text to check for contents.
	 * @param error_msg - the text label to display the required
	 *  field error.
	 *  
	 * @returns
	 *  Returns true if the given text has valid character input,
	 *  false if it is empty or blank (only whitespace) 
	 */
	private boolean checkRequired(String text, Text error_msg) {
		if (text.isEmpty() || text.isBlank()) {
			error_msg.setText("*Required Field");
			return false;
		}
		return true;
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
