package bcit.infosys.student.timesheetrow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bcit.infosys.student.timesheet.EditableTimesheet;
import ca.bcit.infosys.timesheet.Timesheet;

/**
 * A bean that stores persistent information about the rows in timesheets.
 * 
 * @author Ivan (John) Vaganov and Scott Reid
 *
 */
@Named("rowListBean")
@ConversationScoped
public class RowListBean implements RowList, Serializable {

	/**
	 * Injected DAO for the rows.
	 */
	@Inject private RowManagerBean rowManager;
	
	/**
	 * List of editable timesheet row.
	 */
	ArrayList<EditableTimesheetRow> list;
	
	/**
	 * Conversation to manage scope.
	 */
	@Inject Conversation conversation;
	
	/**
	 * Current timesheet being edited by the user.
	 */
	EditableTimesheet currentSheet;

	/**
	 * Gets the rows for a timesheet. Called through the timesheet bean,
	 * loads a list of rows to be edited.
	 * @param sheet is the timesheet to get the rows for.
	 * @return a list of editable timesheet rows.
	 */
	@Override
	public List<EditableTimesheetRow> getRows(EditableTimesheet sheet) {
		currentSheet = sheet;
		if (!conversation.isTransient()) {
			conversation.end();
		}
		conversation.begin();
        if (list == null) {
    		list = new ArrayList<EditableTimesheetRow>();
    		refresh();
        }
		return list;
	}
	
	/**
	 * Refreshes the timesheet row list for the current timesheet through
	 * the DAO.
	 * @return the current list of rows for the timesheet being currently
	 * edited.
	 */
	public List<EditableTimesheetRow> refresh() {
		List<EditableTimesheetRow> sheets = rowManager
		        .getRows(currentSheet);
		list = new ArrayList<EditableTimesheetRow>();
		for (int i = 0; i < sheets.size(); i++) {
			list.add(sheets.get(i));
		}
		return list;
	}

	/**
	 * Getter for the current timesheet.
	 * @return the current timesheet being edited by the user.
	 */
	public EditableTimesheet getCurrentSheet() {
		return currentSheet;
	}

	/**
	 * Setter for the timesheet being edited by the user.
	 * @param currentSheet the timesheet being set by the user.
	 */
	public void setCurrentSheet(EditableTimesheet currentSheet) {
		this.currentSheet = currentSheet;
	}

	/**
	 * Adds a row to the timesheet.
	 * @param tsr the row to be added.
	 * @return a string to reload the page.
	 */
	@Override
	public String addTimesheetRow(EditableTimesheetRow tsr) {
		rowManager.addTimesheetRow(tsr);
		refresh();
		return "";
	}

	/**
	 * Deletes a row from the timesheet.
	 * @param tsr the row to be deleted.
	 */
	@Override
	public void deleteRow(EditableTimesheetRow tsr) {
		rowManager.deleteRow(tsr);
	}


	/**
	 * Updates a given timesheet row.
	 * @param tsr the row to be updated.
	 */
	@Override
	public void updateRow(EditableTimesheetRow tsr) {
		rowManager.updateRow(tsr);
	}


	/**
	 * Saves the current edits made by the user and deletes any
	 * rows marked for deletion.
	 * @param sheet the sheet to be saved.
	 * @return a string to reload the page.
	 */
	@Override
	public String save(EditableTimesheet sheet) {
		List<EditableTimesheetRow> rowsToSave = list;
        for (int i = 0; i < rowsToSave.size(); i++) {
            if (rowsToSave.get(i).isDeletable()) {
                this.deleteRow(rowsToSave.get(i));
            } else if (rowsToSave.get(i).isEditable()) {
            	this.updateRow(rowsToSave.get(i));
                rowsToSave.get(i).setEditable(false);
            }
        }
        refresh();
        return "";
	}
	
	/**
	 * unused
	 */
	@Override
	public EditableTimesheetRow findRow(int projectID, String pack) {
		return null;
	}
	
	/**
	 * unused
	 */
	@Override
	public Timesheet getCurrentTimeSheet(EditableTimesheetRow r) {
		return null;
	}

}
