package bcit.infosys.student.timesheet;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bcit.infosys.student.employee.EditableEmployee;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;

/**
 * Implementation of the timesheet collection that persists a list
 * of timesheets in the system.
 * @author Scott Reid and John Vaganov
 *
 */
@Named("timesheetCollectionBean")
@ConversationScoped
public class TimesheetCollectionBean implements EditableTimesheetCollection,
    Serializable {
    
	@Inject private TimesheetManagerBean sheetManager;
	@Inject Conversation conversation;
    /**
     * Persistent list of timesheets in this system.
     */
    List<EditableTimesheet> sheets;
    /**
     * Employee that this sheet belongs to.
     */
    EditableEmployee e;
    
    /**
     * get all timesheets for an employee.
     * @param emp the employee whose timesheets are returned.
     * @return all of the timesheets for an employee.
     */
    @Override
    public List<EditableTimesheet> getEditableTimesheets(EditableEmployee emp) {
    	e = emp;
		if (!conversation.isTransient()) {
			conversation.end();
		}
		conversation.begin();
        if (sheets == null) {
        	sheets = new ArrayList<EditableTimesheet>();
    		refresh();
        }
		return sheets;
    }
    
    /**
     * Gets all of the timesheets for an employee.
     * @param e the employee whose timesheets you're looking for.
     * @return the list of timesheets associated with the passed in employee.
     */
    public List<EditableTimesheet> getSheetsByEmployee(EditableEmployee e) {
    	return sheetManager.getTimesheets(e);
    }
    
    /**
     * Resets the list of employees to display on the front end.
     * @return the list of editable timesheets given the associated employee
     * instance variable.
     */
    public List<EditableTimesheet> refresh() {
		List<EditableTimesheet> sheetList = sheetManager.getTimesheets(e);
		sheets = new ArrayList<EditableTimesheet>();
		for (int i = 0; i < sheetList.size(); i++) {
			sheets.add(sheetList.get(i));
		}
		return sheetList;
	}

    /**
     * Creates a Timesheet object and adds it to the collection.
     *
     * @param sheet the timesheet to be added.
     * @return a String representing navigation to the newTimesheet page.
     */
    @Override
    public String addTimesheet(EditableTimesheet sheet) {
        sheetManager.addTimesheet(sheet);
        return null;
    }
    
    /**
     * Finds a timesheet based on its employee and date.
     * @param e the employee whose sheet we're looking for.
     * @param d the date of the timesheet we'ree looking for.
     * @return the timesheet found.
     */
    public EditableTimesheet findTimesheet(EditableEmployee e, LocalDate d) {
    	EditableTimesheet t = sheetManager.findTimesheet(e, d);
    	return t;
    }
	
	/**
	 * Updates the passed in timesheet.
	 * @param t the timesheet you want to update.
	 */
	@Override
	public void updateTimesheet(EditableTimesheet t) {
		sheetManager.updateTimesheet(t);
	}
	
    /**
     * unused
     * @param e the employee whose current timesheet is returned
     * @return the current timesheet for an employee.
     */
    @Override
    public Timesheet getCurrentTimesheet(Employee e) {
        return null;
    }
	
	/**
	 * unused
	 */
	@Override
	public String addTimesheet() {
		return null;
	}
	
	/**
	 * unused
	 */
	@Override
	public List<EditableTimesheet> getEditableTimesheets() {
		return null;
	}
	
	/**
	 * unused
	 */
	@Override
	public List<Timesheet> getTimesheets(Employee arg0) {
		return null;
	}
	
	/**
     * unused
     * @return all of the timesheets.
     */
    @Override
    public List<Timesheet> getTimesheets() {
    	ArrayList<Timesheet> sheets = new ArrayList<Timesheet>();
        return sheets;
    }

}
