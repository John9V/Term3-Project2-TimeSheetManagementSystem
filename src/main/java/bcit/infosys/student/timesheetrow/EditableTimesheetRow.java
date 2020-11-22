package bcit.infosys.student.timesheetrow;

import java.math.BigDecimal;

import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Class for a timesheet row, includes flags for deletion and
 * editability.
 * @author Scott Reid, Ivan Vaganov
 *
 */
public class EditableTimesheetRow extends TimesheetRow {
	/**
	 * Boolean that represents whether or not the row is editable.
	 */
	private boolean editable;
    /**
     * Boolean that represents whether or not the row is flagged for deletion.
     */
    private boolean deletable;
    /**
     * Timesheet that owns this row.
     */
    private int ownerSheetId;
    /**
     * Primary key of this row in the database.
     */
    private int rowId;
    
    /**
     * Constructor for an editable timesheet row.
     * @param id the project id of the timesheet row.
     * @param wp the work package for this timesheet row.
     * @param hours the number of hours worked (array of bigdecimal,
     * one index for each day of the week starting on Saturday).
     * @param comments the comments of this timesheet row.
     * @param ownerSheetId the primary key of this row in the database.
     */
    public EditableTimesheetRow(final int id, final String wp,
            final BigDecimal[] hours, final String comments,
            final int ownerSheetId) {
        setProjectID(id);
        setWorkPackage(wp);
        setHoursForWeek(hours);
        setNotes(comments);
        setOwnerTimesheet(ownerSheetId);
        editable = false;
        deletable = false;
    }
    
	/**
	 * Default constructor for EditableTimesheetRow
	 */
	public EditableTimesheetRow() { }

    /**
     * Getter for row id (primary key of this row in the database).
     * @return row id (int).
     */
    public int getRowId() {
		return rowId;
	}

	/**
	 * Used to remember the primary key of this row from the database.
	 * @param rowId pk for this row.
	 */
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

    /**
     * Getter for the primary key of sheet which owns this row.
     * @return the primary key of the owner sheet.
     */
    public int getOwnerSheetId() {
		return ownerSheetId;
	}

	/**
	 * Setter for the owner sheet id.
	 * @param ownerSheetId integer representing the owner sheet's
	 * primary key.
	 */
	public void setOwnerSheetId(int ownerSheetId) {
		this.ownerSheetId = ownerSheetId;
	}

	/**
     * Getter for the editable property of this row.
     * @return a boolean representing whether or not this row can be edited.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Setter for the editable property.
     * @param editable represents whether or not this row can be edited.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Getter for whether or not a row is flagged for deletion.
     * @return whether or not this row can be edited.
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * Setter for whether or not the row can be deleted.
     * @param deletable represents whether or not the row is flagged
     * to be deleted.
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
    
    /**
     * unused
     * @param ownerSheetId2
     */
    private void setOwnerTimesheet(int ownerSheetId2) {}
}
