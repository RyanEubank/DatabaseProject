package src.application.client.scenes;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.util.Callback;

public class CalendarDialog extends Dialog<LocalDate> {

	private static final String SCREEN_LAYOUT = "/src/resource/stylesheets/Calendar.fxml";
	
	private LocalDate m_date;
	
	@FXML
	private DatePicker calendar;
	
	/**
	 * Loads the calendar dialog box layout from an fxml file and binds the
	 * neccessary result handler to retrieve the selected date from user
	 * selection.
	 */
	public CalendarDialog() {
		super();
		
		// get the fxml file path and set the controller
		FXMLLoader fxml = new FXMLLoader(getClass().getResource(SCREEN_LAYOUT));
		fxml.setController(this);
		
		// attempt to load the scene layout for the dialog box
		try {
			this.setDialogPane(fxml.load());
			
			// bind the result converter to the ok button to retrieve values upon
			// user commit
			this.setResultConverter(selectDate());
			
		} catch (IOException e) {
			System.out.println("Unable to load calendar dialog");
			e.printStackTrace();
		}
	}

	/**
	 * Sets the button callback to retrieve the user's selected date
	 * from the calendar popup.
	 * 
	 * @return
	 *  Returns a callback to the date selection button.
	 */
	private Callback<ButtonType, LocalDate> selectDate() {
		return new Callback<ButtonType, LocalDate>() {
			
			// set the callback to return the selected date when the "OK"
			// button is clicked
			@Override
			public LocalDate call(ButtonType button) {
				if (button == ButtonType.OK)
					return m_date;
				else
					return null;
			}
		};
	}
	
	/**
	 * Sets the current date in the dialog box from the calendar selection.
	 */
	public void OnSelectDate() {
		this.m_date = this.calendar.getValue();
	}
}
