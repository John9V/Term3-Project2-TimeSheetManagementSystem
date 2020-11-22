package bcit.infosys.student.timesheet;

import java.io.Serializable;

import ca.bcit.infosys.timesheet.Timesheet;

public class EditableTimesheet extends Timesheet implements Serializable {
    
    /**
     * The ID of the timesheet.
     */
	private int timesheetId;

	/**
	 * Returns the ID of the timesheet.
	 * @return the ID.
	 */
	public int getTimesheetId() {
		return timesheetId;
	}

	/**
	 * Sets the ID for the timesheet.
	 * @param timesheetId the ID to set.
	 */
	public void setTimesheetId(int timesheetId) {
		this.timesheetId = timesheetId;
	}
}
