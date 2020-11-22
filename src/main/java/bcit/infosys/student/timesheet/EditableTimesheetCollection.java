package bcit.infosys.student.timesheet;

import java.util.List;

import bcit.infosys.student.employee.EditableEmployee;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.TimesheetCollection;

public interface EditableTimesheetCollection extends TimesheetCollection {
	public String addTimesheet(EditableTimesheet sheet);
	public List<EditableTimesheet> getEditableTimesheets();
	public List<EditableTimesheet> getEditableTimesheets(EditableEmployee e);
	public void updateTimesheet(EditableTimesheet t);
}
