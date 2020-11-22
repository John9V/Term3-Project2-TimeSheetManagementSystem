package bcit.infosys.student.timesheetrow;

import java.util.List;

import bcit.infosys.student.timesheet.EditableTimesheet;
import ca.bcit.infosys.timesheet.Timesheet;

/**
 * An interface for a list of rows.
 *
 * @author Ivan (John) Vaganov and Scott Reid
 *
 */
public interface RowList {
    
    /**
     * Getter for the rows of the timesheet.
     * @param sheet
     * @return the list that stores the rows
     */
    List<EditableTimesheetRow> getRows(EditableTimesheet sheet);
    
    /**
     * Getter for the timesheet.
     * @param r
     * @return the current timesheet
     */
    Timesheet getCurrentTimeSheet(EditableTimesheetRow r);
    
    /**
     * Adds another row.
     * @return a string that reloads the page.
     * @param tsr the row to add
     */
    String addTimesheetRow(EditableTimesheetRow tsr);
    
    /**
     * Finds a row based on the project id and package.
     * @param projectID
     * @param pack
     * @return the found row
     */
    EditableTimesheetRow findRow(int projectID, String pack);
    
    /**
     * deletes a row from the list
     * @param tsr the row to delete
     */
    void deleteRow(EditableTimesheetRow tsr);
    
    /**
     * updates a row from the list
     * @param tsr the row to update
     */
    void updateRow(EditableTimesheetRow tsr);
    
    /**
     * Save method to delete rows marked for deletion and to save new
     * information in the current timesheet.
     * @param sheet is the sheet currently being saved.
     * @return a string to reload the page.
     */
    public String save(EditableTimesheet sheet);

}
