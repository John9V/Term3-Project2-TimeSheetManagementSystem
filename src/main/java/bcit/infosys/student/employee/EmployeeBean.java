package bcit.infosys.student.employee;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import bcit.infosys.student.credential.CredentialManagerBean;
import ca.bcit.infosys.employee.Credentials;

/**
 * A bean representing the currently logged in user.
 * @author Scott Reid, John Vaganov
 *
 */
@Named("employeeBean")
@SessionScoped
public class EmployeeBean extends EditableEmployee implements Serializable {

	/**
	 * DAO object for employees.
	 */
	@Inject EmployeeManagerBean employeeManager;
	/**
	 * DAO object for credentials.
	 */
	@Inject CredentialManagerBean credentialManager;
	
	/**
	 * Constructor for the employee bean.
	 * @param empName the name of the employee to be created.
	 * @param number the number of the employee to be created.
	 * @param id the id of the employee to be created.
	 * @param isAdmin whether or not the employee is an administrator.
	 */
	public EmployeeBean(String empName, int number, String id,
	        boolean isAdmin) {
		super(empName, number, id, isAdmin);
	}
	
	/**
	 * Default constructor.
	 */
	public EmployeeBean() { }

	/**
	 * The username of the currently logged in user.
	 */
	private String userName;
	
	/**
	 * The password of the currently logged in user.
	 */
	private String password;
	
	/**
	 * Whether or not the currently logged in user is an admin.
	 */
	private boolean isAdmin;
	
	/**
	 * Method used to get the correct template based on the user,
	 * whether they are an admin or user.
	 * @return a string path to the template file.
	 */
	public String template() {
	    String path = "/templates";
	    return isAdmin ? path + "/adminLayout.xhtml" : path
	            + "/userLayout.xhtml";
	}
	
	/**
	 * Getter for the user's username, to be used on the login screen.
	 * @return string representing the user's username.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Setter to set the username based on input from the login screen.
	 * @param userName user's username.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Getter for the typed in password on the login screen.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for the password typed in on the login screen.
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Login method. Validates that credentials are correct, and, if so,
	 * loads that user's data from the employee list into this bean.
	 * @return a string directing the user to the right page based on the
	 * type of user that just logged in.
	 */
	public String login() {
		Credentials c = new Credentials();
		c.setUserName(userName);
		c.setPassword(password);
		credentialManager.validCredentials(c);
		if (credentialManager.validCredentials(c)) {
			EditableEmployee loggedInEmployee = employeeManager
			        .getEmployeeByUserName(userName);
			this.setName(loggedInEmployee.getName());
			this.setEmpNumber(loggedInEmployee.getEmpNumber());
			this.setUserName(loggedInEmployee.getUserName());
			isAdmin = employeeManager
			        .isAdmin(loggedInEmployee);
			if (isAdmin) {
				return "adminWelcome";
			} else {
				return "userWelcome";
			}
		} else {
			return "invalidCredentials";
		}
	}
	
	/**
	 * Logs out the user.
	 * @return a string directing the user to the index page.
	 */
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext()
		    .invalidateSession();
		return "index?faces-redirect=true";
	}
}
