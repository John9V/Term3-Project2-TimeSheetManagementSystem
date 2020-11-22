package bcit.infosys.student.employee;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import bcit.infosys.student.credential.CredentialManagerBean;
import ca.bcit.infosys.employee.Credentials;

/**
 * This bean allows the system to create a new employee.
 * @author Scott Reid, John Vaganov
 *
 */
@Named("createEmployeeBean")
@RequestScoped
public class CreateEmployeeBean extends EditableEmployee implements Serializable {
    @Inject EmployeeManagerBean employeeManager;
    @Inject CredentialManagerBean credentialManager;
    @Inject EmployeeListBean employeeList;
	/**
	 * The password for the new employee.
	 */
	private String password;
	/**
	 * Adds an employee (and associated credential) using the information
	 * entered by the user on the adEmployee page. Only Admins can add an
	 * Employee.
	 * @return a string that returns the user to the viewUsers page.
	 */
	public String addEmployee() {
		employeeManager.addEmployee(this);
		addCredential();
		employeeList.refresh();
		return "viewUsers";
	}
	
	/**
	 * Helper method that adds a credential to the list of credentials.
	 */
	public void addCredential() {
		Credentials cred = new Credentials();
		cred.setUserName(this.getUserName());
		cred.setPassword(password);
		credentialManager.addCredentials(cred);
	}

	/**
	 * Getter for the password.
	 * @return the password entered by the user on the addEmployee page.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for the password.
	 * @param password is the password entered by the user on the
	 * addEmployee page.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
