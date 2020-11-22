package bcit.infosys.student.timesheet;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bcit.infosys.student.employee.EditableEmployee;
import bcit.infosys.student.timesheetrow.EditableTimesheetRow;
import bcit.infosys.student.timesheetrow.RowListBean;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;

/**
 * Bean that allows the user to create a timesheet.
 * @author Ivan (John) Vaganov and Scott Reid
 *
 */
@Named("createTimesheetBean")
@SessionScoped
public class CreateTimesheetBean extends EditableTimesheet
    implements Serializable {

	@Inject private RowListBean rowListBean;
	@Inject private TimesheetCollectionBean timesheetCollectionBean;
	@Inject private TimeSheetBean timesheetBean;
	
	/**
	 * The new timesheet being added to the system.
	 */
	private EditableTimesheet newTs;
    
    /**
     * Zero param constructor.
     */
    public CreateTimesheetBean() {}
    
    /**
     * Getter for the new timesheet.
     * @return the new timesheet being added.
     */
    public Timesheet getNewTs() {
		return newTs;
	}

	/**
	 * Setter for the new timesheet being added to the system.
	 * @param newTs is the new timesheet being added to the system.
	 */
	public void setNewTs(EditableTimesheet newTs) {
		this.newTs = newTs;
	}

	/**
	 * Method that adds a new timesheet to the system, adds 5 empty rows to
	 * start.
	 * @param e is the employee (current user) to associate with this
	 * timesheet.
	 * @return a string to reload the page.
	 */
	public String createNewTimesheet(final EditableEmployee e) {
		LocalDate date = LocalDate.now();
		date = date.with(TemporalAdjusters
		        .nextOrSame(DayOfWeek.FRIDAY));
		boolean alreadyTimesheetForWeek = isAlreadySheetForWeek(date, e);
		if (alreadyTimesheetForWeek) {
			return "invalidSheetCreation";
		}
		
		newTs = new EditableTimesheet();
		newTs.setEmployee(e);
    	newTs.setEndWeek(date);
    	timesheetCollectionBean.addTimesheet(newTs);
    	
    	EditableTimesheet newSheet = timesheetCollectionBean
    	        .findTimesheet(e, LocalDate.now().with(TemporalAdjusters
		        .nextOrSame(DayOfWeek.FRIDAY)));
    	newTs = newSheet;
    	rowListBean.setCurrentSheet(newSheet);
    	for (int i = 0; i < 5; i++) {
    		EditableTimesheetRow newTsr = new EditableTimesheetRow();
        	newTsr.setOwnerSheetId(newSheet.getTimesheetId());
        	rowListBean.addTimesheetRow(newTsr);
    	}
    	timesheetBean.setTimesheet(newTs);
    	timesheetCollectionBean.refresh();
    	return "viewTimesheet?faces-redirect=true";
    }
	
	/**
	 * Checks if there is already a timesheet for a given week for an
	 * employee.
	 * @param date the date of the given week.
	 * @param e the employee to check.
	 * @return boolean representing the existence of a timesheet for
	 * a given week.
	 */
	public boolean isAlreadySheetForWeek(LocalDate date,
	        EditableEmployee e) {
		List<EditableTimesheet> sheetList = timesheetCollectionBean
		        .getSheetsByEmployee(e);
		DateTimeFormatter formatter = DateTimeFormatter
		        .ofPattern("yyyy-MM-dd");
		for (EditableTimesheet ts : sheetList) {
			if ((LocalDate.parse(ts.getWeekEnding(), formatter))
			        .compareTo(date) == 0) {
				return true;
			}
		}
		return false;
	}
    
    /**
     * Method that adds a new row to the timesheet being added to the system.
     * @return a string to reload the page.
     */
    public String addRowFromBean() {
    	EditableTimesheetRow newTsr = new EditableTimesheetRow();
    	newTsr.setOwnerSheetId(newTs.getTimesheetId());
    	rowListBean.addTimesheetRow(newTsr);
    	return "";
    }
    
    /**
     * Saves the timesheet.
     * @return the string for viewTimesheet page.
     */
    public String save() {
    	rowListBean.setCurrentSheet(newTs);
    	rowListBean.refresh();
    	return "viewTimesheet?faces-redirect=true";
    }
    
    /**
     * Navigates back to the page that displays the timesheets.
     * @return the string of the page to go back to.
     */
    public String back() {
    	timesheetCollectionBean.refresh();
    	return "timesheetList";
    }
}
