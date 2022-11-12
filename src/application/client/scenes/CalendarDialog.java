package src.application.client.scenes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.fxml.*;
import javafx.scene.control.*;

public class CalendarDialog extends AbstractAppDialog<LocalDate> {
	
	@FXML
	private DatePicker calendar;
	
	/**
	 * Loads the calendar dialog box layout from an fxml file and binds the
	 * neccessary result handler to retrieve the selected date from user
	 * selection.
	 */
	public CalendarDialog(String fxml) {
		super(fxml, LocalDate.now());
		this.calendar.setValue(m_dialogRetVal);
		this.calendar.getEditor().textProperty()
			.addListener((obs, oldDate, newDate) -> parseDate(newDate));
	}

	/**
	 * Sets the current date in the dialog box from the calendar selection.
	 */
	@Override
	public void OnMakeSelection() {
		this.m_dialogRetVal = this.calendar.getValue();
	}
	
	/**
	 * Checks the validity of the date entered in the calendar text box
	 * and enables/disables the OK button based on parsing success.
	 * 
	 * @param date - the string to parse as the selected date.
	 */
	public void parseDate(String date) {
		try {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate d = LocalDate.parse(date, format);
			this.calendar.setValue(d);
			this.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
		} catch (DateTimeParseException e) {
			this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		}
	}
}
