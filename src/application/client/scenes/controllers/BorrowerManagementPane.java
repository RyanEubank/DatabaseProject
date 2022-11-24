package src.application.client.scenes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
	
	@Override
	public void postInitialize() {
		super.postInitialize();
	}

	@Override
	protected void onPerformAction() {
		this.m_parent.setActionError("");
		String ssn = this.user_ssn_field.getText();
		String fname = this.user_fname_field.getText();
		String lName = this.user_lname_field.getText();
		String address = this.user_address_field.getText();
		String phone = this.user_phone_field.getText();
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
