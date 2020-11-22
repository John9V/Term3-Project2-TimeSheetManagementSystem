package bcit.infosys.student.employee;

import java.io.Serializable;

import ca.bcit.infosys.employee.Employee;

/**
 * Editable Employee class which holds additional data compared to the
 * base class.
 * @author Scott Reid, Ivan Vaganov
 *
 */
public class EditableEmployee extends Employee implements Serializable {
	/**
	 * Boolean representing whether or not this employee is flagged for
	 * editing.
	 */
	private boolean editable = false;
    /**
     * Boolean representing whether or not this employee is flagged for
     * deletion.
     */
    private boolean deletable = false;
    /**
     * Boolean representing whether or not this employee is an admin.
     */
    private boolean admin = false;
    
    /**
     * Constructor for an editable employee.
     * @param empName the employee's name
     * @param number the unique employee number.
     * @param id the employee id.
     * @param isAdmin whether or not the employee is an administrator.
     */
    public EditableEmployee(final String empName, final int number,
            final String id, boolean isAdmin) {
        super(empName, number, id);
    }
    
    /**
     * Default constructor.
     */
    public EditableEmployee() { }
    
    /**
     * Setter for whether or not the employee is an administrator.
     * @return whether or not the employee is an admin.
     */
    public boolean isAdmin() {
		return admin;
	}

	/**
	 * Sets whether or not the employee is an admin.
	 * @param admin whether or not the employee is an admin.
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
     * Getter for whether an employee is deletable.
     * @return whether or not the employee is deletable.
     */
    public boolean isDeletable() {
		return deletable;
	}

	/**
	 * Setter for whether an employee can be deleted.
	 * @param deletable is a boolean representing whether a current employee
	 * is deletable.
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	/**
	 * Getter for whether or not an employee is editable.
	 * @return a boolean representing whether an employee can be edited.
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Setter for whether or not an employuee can be edited.
	 * @param editable is whether or not an employee can be edited.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
