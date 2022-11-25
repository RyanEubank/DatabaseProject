package src.application.client.scenes.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import src.application.client.scenes.Scenes;
import src.application.server.database.exceptions.LibraryRuleException;

public abstract class AbstractPane implements IController {
	
	protected static final String UNKNOWN_ERROR = "An unknown error has occured.";
	
	@FXML
	private Pane primary_node;
	
	protected MainScreen m_parent;
	
	/**
	 * Binds the visibilty of the pane in the parent screen
	 * to its managaed property. This causes it to automatically resize 
	 * to fit its parent when visible.
	 */
	@Override
	public void initialize() {
		this.primary_node.managedProperty().bind(
			this.primary_node.visibleProperty());
	}
	
	/**
	 * The secondary initialization tasks called after the parent controller
	 * is linked to the pane.
	 */
	protected void postInitialize() {
		addVisibilityListener();
	}
	
	/**
	 * Adds listeners to change various UI elements attachted to the
	 * pane's parent screen when the pane is visible.
	 */
	protected void addVisibilityListener() {
		this.getPane().visibleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) 
				this.m_parent.setActionName(getActionName());
		});
	}
	
	/**
	 * Displays an error relevant for the caught exception. Library
	 * defined exceptions have specific error details while general
	 * exceptions will log the stack trace and display a generic error.
	 * 
	 * @param e - the exception caught to display an error for.
	 */
	protected void displayError(Exception e) {
		if (e instanceof LibraryRuleException)
			this.m_parent.setActionError(e.getMessage());
		else {
			this.m_parent.setActionError(UNKNOWN_ERROR);
		}
	}
	
	/**
	 * Sets a reference to the pane's parent controller.
	 * 
	 * @param parent - the parent controller managing
	 *  the pane.
	 */
	public void setParent(MainScreen parent) {
		this.m_parent = parent;
		postInitialize();
	}
	
	/**
	 * Returns the pane being managed by this controller.
	 * 
	 * @return
	 *  Returns a pane object containing the UI elements this
	 *  controller is bound to.
	 */
	public Pane getPane() {
		return this.primary_node;
	}
	
	/**
	 * Performs the main action of the pane, such as checking out books
	 * or paying fines.
	 */
	protected abstract void onPerformAction();
	
	
	/**
	 * Returns the name of the action the pane will invoke when
	 * the action button is clicked in its parent screen.
	 * 
	 * @return
	 *  Returns a string that can be used to provide context for
	 *  the action button in the parent screen.
	 */
	protected abstract String getActionName();
}
