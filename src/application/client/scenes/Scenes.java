package src.application.client.scenes;

import src.application.client.scenes.controllers.*;
import src.application.client.scenes.dialogs.*;

public class Scenes {
	public static final String DEFAULT_STYLESHEET = "/src/resource/stylesheets/application.css";
	
	public static final String LOGIN_SCREEN = "/src/resource/stylesheets/LoginScreen.fxml";
	public static final String MAIN_SCREEN = "/src/resource/stylesheets/MainScreenWIP.fxml";
	public static final String HOME_PANE = "/src/resource/stylesheets/Home.fxml";
	public static final String BOOKS_PANE = "/src/resource/stylesheets/Books.fxml";
	public static final String LOANS_PANE = "/src/resource/stylesheets/Loans.fxml";
	public static final String BORROWERS_PANE = "/src/resource/stylesheets/Borrowers.fxml";
	public static final String FINES_PANE = "/src/resource/stylesheets/Fines.fxml";
	public static final String CALENDAR_DIALOG = "/src/resource/stylesheets/Calendar.fxml";
	public static final String CHECKOUT_DIALOG = "/src/resource/stylesheets/Checkout.fxml";
	
	/**
	 * Returns the appropriate controller for the given scene layout.
	 * 
	 * @param scene - the scene layout to obtain a controller for.
	 * 
	 * @return
	 *  Returns an AbstractController to handle GUI events for the
	 *  requested layout.
	 *  
	 * @throws Exception 
	 *  Throws an exception if the provided scene is not found.
	 */
	public static IController getController(String scene)
		throws Exception 
	{
		switch (scene) {
			case(LOGIN_SCREEN): 
				return new LoginScreen();
			case(MAIN_SCREEN): 
				return new MainScreenWIP();
			case(HOME_PANE):
				return new HomePane();
			case(BOOKS_PANE):
				return new BookSearchPane();
			case(LOANS_PANE):
				return new LoanSearchPane();
			case(BORROWERS_PANE):
				return new BorrowerManagementPane();
			case(FINES_PANE):
				return new FineSearchPane();
			case(CALENDAR_DIALOG):
				return new CalendarDialog();
			case(CHECKOUT_DIALOG):
				return new CheckoutDialog();
			default: 
				throw new Exception("Scene: " + "\'" + scene + "\'" +  " not found.");
		}
	}
}
