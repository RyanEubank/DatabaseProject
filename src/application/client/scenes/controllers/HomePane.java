package src.application.client.scenes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomePane extends AbstractPane {

	@FXML
	private Button books_button;
	@FXML
	private Button loans_button;
	@FXML
	private Button borrowers_button;
	@FXML 
	private Button fines_button;
	
	@Override
	public void postInitialize() {
		this.books_button.setOnAction(e -> onBookClicked());
		this.loans_button.setOnAction(e -> onLoanClicked());
		this.borrowers_button.setOnAction(e -> onBorrowerClicked());
		this.fines_button.setOnAction(e -> onFineClicked());
	}
	
	private void onBookClicked() {
		AbstractPane bookPane = this.m_parent.getBooksPane();
		this.m_parent.setActivePane(bookPane);
	}
	
	private void onLoanClicked() {
		AbstractPane loanPane = this.m_parent.getLoansPane();
		this.m_parent.setActivePane(loanPane);
	}
	
	private void onBorrowerClicked() {
		AbstractPane borrowerPane = this.m_parent.getBorrowersPane();
		this.m_parent.setActivePane(borrowerPane);
	}
	
	private void onFineClicked() {
		AbstractPane finePane = this.m_parent.getFinesPane();
		this.m_parent.setActivePane(finePane);
	}

	@Override
	protected void onPerformAction() {
		throw new RuntimeException("Operation not suppoerted for HomePane.");
	}

	@Override
	protected String getActionName() {
		return null; // action button is invisible on home screen
	}
}
