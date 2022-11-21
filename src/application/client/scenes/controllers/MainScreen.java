package src.application.client.scenes.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.beans.Observable;
import javafx.beans.binding.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import src.application.client.scenes.*;
import src.application.server.database.ConnectionManager;
import src.application.server.database.query.DateUpdater;

public class MainScreen extends AbstractWindow {

	@FXML 
	private StackPane child_container;	
	@FXML
	private Button home_button;
	@FXML
	private HBox search_bar_container;
	@FXML
	private HBox action_bar_container;
	@FXML
	private TextField search_bar;
	@FXML
	private ChoiceBox<String> search_type;
	@FXML
	private MenuItem calendar_dropdown;
	@FXML
	private MenuItem logout_dropdown;
	@FXML
	private Text date;
	@FXML
	private Button action_button;
	@FXML
	private Label error_label;
	
	private final AbstractPane m_homePane;
	private final AbstractPane m_borrowersPane;
	private final AbstractPane m_booksPane;
	private final AbstractPane m_loansPane;
	private final AbstractPane m_finesPane;
	
	private BooleanBinding searchBarVisibility;
	private BooleanBinding actionBarVisibility;
	
	/**
	 * Constructs a new main screen controller and
	 * initializes its children pane controllers.
	 * 
	 * @throws Exception 
	 *  Throws an exception if there is an error loading
	 *  the various panes in the main screen.
	 */
	public MainScreen() throws Exception {
		this.m_homePane = SceneManager.getSingleton().loadPane(Scenes.HOME_PANE);
		this.m_booksPane = SceneManager.getSingleton().loadPane(Scenes.BOOKS_PANE);
		this.m_loansPane = SceneManager.getSingleton().loadPane(Scenes.LOANS_PANE);
		this.m_borrowersPane = SceneManager.getSingleton().loadPane(Scenes.BORROWERS_PANE);
		this.m_finesPane = SceneManager.getSingleton().loadPane(Scenes.FINES_PANE);
	}
	
	/**
	 * Initializes the various panes displayed by the main screen
	 * and sets the default active pane.
	 */
	@Override
	public void initialize() {
		super.initialize();
		initChildren();
		addChildrenToScene();
		bindSearchBarVisibilty();
		bindActionBarVisibility();
		registerEvenHandlers();
		this.date.setText(LocalDate.now().toString());
		setActivePane(this.m_homePane);
	}

	/**
	 * Initializes all ofthe children panes in the main screen.
	 */
	private void initChildren() {
		this.m_homePane.setParent(this);
		this.m_booksPane.setParent(this);
		this.m_loansPane.setParent(this);
		this.m_borrowersPane.setParent(this);
		this.m_finesPane.setParent(this);
	}

	/**
	 * Adds all children panes to the container displaying the main screen's 
	 * content.
	 */
	private void addChildrenToScene() {
		this.child_container.getChildren().addAll(
			this.m_homePane.getPane(),
			this.m_booksPane.getPane(),
			this.m_loansPane.getPane(),
			this.m_borrowersPane.getPane(),
			this.m_finesPane.getPane()
		);
	}
	
	/**
	 * Binds the visibility of the search bar to a boolean
	 * property listening for both the home and borrower screens being hidden
	 * (and therefore the active screen must be a search pane)
	 */
	private void bindSearchBarVisibilty() {
		List<AbstractPane> nonSearchPanes = List.of(this.m_homePane, this.m_borrowersPane);
		searchBarVisibility = Bindings.createBooleanBinding(
			() -> nonSearchPanes.stream().allMatch(e -> !e.getPane().isVisible()),
			nonSearchPanes.stream().map(pane -> pane.getPane().visibleProperty())
				.toArray(Observable[]::new)
		);
		this.search_bar_container.visibleProperty().bind(searchBarVisibility);
	}

	/**
	 * Binds the action bar visibility to the opposite of the home pane's
	 * visibility, i.e. it is visible whenever the home pane is hidden.
	 */
	private void bindActionBarVisibility() {
		this.actionBarVisibility = Bindings.createBooleanBinding(
			() -> !this.m_homePane.getPane().isVisible(), 
			List.of(this.m_homePane.getPane().visibleProperty()).toArray(Observable[]::new)
		);
		this.action_bar_container.visibleProperty().bind(actionBarVisibility);
	}

	/**
	 * Registers event handlers for the various buttons and controls on the
	 * main screen.
	 */
	private void registerEvenHandlers() {
		this.home_button.setOnAction(e -> setActivePane(this.m_homePane));
		this.search_bar.setOnAction(e -> onSearch());
		this.action_button.setOnAction(e -> onActionClicked());
		this.calendar_dropdown.setOnAction(e -> onOpenCalendar());
		this.logout_dropdown.setOnAction(e -> onLogout());
	}
	
	/**
	 * Sets the active child pane in the main screen window.
	 * 
	 * @param pane - the child pane to be set as the active visible child.
	 */
	public void setActivePane(AbstractPane pane) {
		if (pane != this.m_homePane) 
			this.m_homePane.getPane().setVisible(false);
		if (pane != this.m_booksPane)
			this.m_booksPane.getPane().setVisible(false);
		if (pane != this.m_loansPane)
			this.m_loansPane.getPane().setVisible(false);
		if (pane != this.m_borrowersPane)
			this.m_borrowersPane.getPane().setVisible(false);
		if (pane != this.m_finesPane)
			this.m_finesPane.getPane().setVisible(false);

		pane.getPane().setVisible(true);
	}
	
	/**
	 * Return the currently active pane.
	 * 
	 * @return
	 *  Returns the child pane that is currently visible in the main screen.
	 */
	public AbstractPane getActivePane() {
		if (this.m_homePane.getPane().isVisible())
			return this.m_homePane;
		else if (this.m_booksPane.getPane().isVisible())
			return this.m_booksPane;
		else if (this.m_loansPane.getPane().isVisible())
			return this.m_loansPane;
		else if (this.m_borrowersPane.getPane().isVisible())
			return this.m_borrowersPane;
		else if (this.m_finesPane.getPane().isVisible())
			return this.m_finesPane;
		else
			throw new RuntimeException(
				"No search pane is currently active: Search field null.");
	}
	
	/**
	 * Sets the error text displayed next to the main screen's action button.
	 * 
	 * @param error - the error to be displayed to the user.
	 */
	public void setActionError(String error) {
		this.error_label.setText(error);
	}
	
	/**
	 * Sets the name displayed on the main screen's action button.
	 * 
	 * @param name - the new name of the button.
	 */
	public void setActionName(String name) {
		this.action_button.setText(name);
	}
	
	/**
	 * Sets the etxt for the search filter options in the search bar.
	 * 
	 * @param options - the list of selectable search options.
	 */
	public void setFilterOptions(String[] options) {
		this.search_type.getItems().clear();
		this.search_type.getItems().addAll(options);
		this.search_type.setValue(options[0]);
	}
	
	/**
	 * Returns the books pane managed by the main screen.
	 * 
	 * @return
	 *  Returns the AbstractPane object for the book search pane.
	 */
	public AbstractPane getBooksPane() {
		return this.m_booksPane;
	}
	
	/**
	 * Returns the loans pane managed by the main screen.
	 * 
	 * @return
	 *  Returns the AbstractPane object for the loan search pane.
	 */
	public AbstractPane getLoansPane() {
		return this.m_loansPane;
	}
	
	/**
	 * Returns the borrowers pane managed by the main screen.
	 * 
	 * @return
	 *  Returns the AbstractPane object for the borrower managament pane.
	 */
	public AbstractPane getBorrowersPane() {
		return this.m_borrowersPane;
	}
	
	/**
	 * Returns the fines pane managed by the main screen.
	 * 
	 * @return
	 *  Returns the AbstractPane object for the fine search pane.
	 */
	public AbstractPane getFinesPane() {
		return this.m_finesPane;
	}
	
	/**
	 * Returns the contents of the search bar at the top of the main screen.
	 * 
	 * @return
	 *  Returns the text entered by the user into the search bar.
	 */
	public String getSearchText() {
		return this.search_bar.getText();
	}
	
	/**
	 * Returns the filter selected for database searches to filter based
	 * on attribute.
	 * 
	 * @return
	 *  Returns the attribute name to filter the search on.
	 */
	public String getSearchFilter() {
		return this.search_type.getValue();
	}
	
	/**
	 * Called when the main screen action button is clicked. This
	 * performs some database operation defined by the active child
	 * pane.
	 */
	public void onActionClicked() {
		getActivePane().onPerformAction();
	}
	
	/**
	 * Called when the user hits the enter key when in the search bar
	 * to query the database for information specific to the active pane.
	 */
	public void onSearch() {
		AbstractPane pane = getActivePane();
		if (pane instanceof AbstractSearchPane<?>)
			((AbstractSearchPane<?>) pane).onSearch();
		else
			throw new RuntimeException("Active child is not a search pane.");
	}
	
	/**
	 * Opens the calendar dialog box to send updates to the database to simulate
	 * the date changing to track various information such as book due dates
	 * and overdue fees.
	 */
	public void onOpenCalendar() {
		try {
			Dialog<LocalDate> dialog = SceneManager.getSingleton()
				.openDialog(Scenes.CALENDAR_DIALOG);
			Optional<LocalDate> result = dialog.showAndWait();
			if (result.isPresent()) {
				DateUpdater.setDate(result.get());
				this.date.setText(result.get().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clears the connection properties and returns to the application
	 * login screen.
	 */
	public void onLogout() {
		ConnectionManager.getSingleton().clearProperties();
		SceneManager.getSingleton().setMainStage(
			Scenes.LOGIN_SCREEN, Scenes.DEFAULT_STYLESHEET, 1200, 800);
	}
}
