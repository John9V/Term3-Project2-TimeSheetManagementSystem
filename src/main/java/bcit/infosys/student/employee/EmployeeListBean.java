package bcit.infosys.student.employee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bcit.infosys.student.credential.CredentialManagerBean;

/**
 * Implementation of the employee list interface.
 * @author Scott Reid, John Vaganov
 *
 */
@Named("employeeListBean")
@ConversationScoped
public class EmployeeListBean implements Serializable {
	
	/**
	 * DAO for employees.
	 */
	@Inject private EmployeeManagerBean employeeManager;
	/**
	 * DAO for credential manager.
	 */
	@Inject private CredentialManagerBean credentialManager;
	/**
	 * Conversation for scope management.
	 */
	@Inject Conversation conversation;
	
	/**
	 * Persistent list of employees with access to the system.
	 */
	private static ArrayList<EditableEmployee> editableEmployees;
	
	/**
	 * Getter for the employees in the database, manages conversation scope.
	 * @return the current list of employees.
	 */
	public List<EditableEmployee> getEmployees() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		conversation.begin();
        if (editableEmployees == null) {
        	editableEmployees = new ArrayList<EditableEmployee>();
    		refresh();
        }
		return editableEmployees;
	}
	
	/**
	 * Resets the list of employees in the database to reflect the current
	 * state of the database.
	 * @return the current list of employees.
	 */
	public List<EditableEmployee> refresh() {
		List<EditableEmployee> employees = employeeManager
		        .getEditableEmployees();
		editableEmployees = new ArrayList<EditableEmployee>();
		for (int i = 0; i < employees.size(); i++) {
			editableEmployees.add(employees.get(i));
		}
		return editableEmployees;
	}

	/**
	 * Updates and deletes any employees flagged as such, and refreshes the
	 * list.
	 * @return a string to reload the page.
	 */
	public String save() {
		for (int i = 0; i < editableEmployees.size(); i++) {
			if (editableEmployees.get(i).isDeletable()) {
				employeeManager.deleteEmployee(editableEmployees.get(i));
				credentialManager.deleteCredentials(editableEmployees.get(i)
				        .getUserName());
			} else if (editableEmployees.get(i).isEditable()) {
				employeeManager.updateEmployee(editableEmployees.get(i));
				editableEmployees.get(i).setEditable(false);
			}
		}
		refresh();
		return "";
	}

}
