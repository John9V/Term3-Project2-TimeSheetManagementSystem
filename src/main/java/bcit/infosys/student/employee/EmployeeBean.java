package bcit.infosys.student.employee;

import java.io.Serializable;
import java.util.Random;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import bcit.infosys.student.credential.CredentialManagerBean;
import ca.bcit.infosys.employee.Credentials;

/**
 * A bean representing the currently logged in user.
 * @author Scott Reid, John Vaganov
 *
 */
@Named("employeeBean")
@SessionScoped
@Path("/login")
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
	 * authentication identifier.
	 */
	String saltString;
	
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
	 * Getter for the salt string.
	 * @return the salt string
	 */
	public String getSaltString() {
		return saltString;
	}

	/**
	 * Setter for the salt string.
	 * @param saltString
	 */
	public void setSaltString(String saltString) {
		this.saltString = saltString;
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
	 * Generates and returns a randomly generated salt string for
	 * authentication.
	 * @return a new salt string
	 */
	protected String generateSaltString() {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
	
	/**
	 * REST endpoint for logging in.
	 * @param userName the path parameter for the user's user name to
	 * log in with
	 * @param password the path parameter for the user's password to
	 * log in with
	 * @return the generated authentication salt string
	 */
	@Path("/{userName}/{password}")
    @GET
    @Produces("application/json")
	public String loginREST(@PathParam("userName") String userName,
	        @PathParam("password") String password) {
		System.out.println("running" + userName + password);
		Credentials c = new Credentials();
		c.setUserName(userName);
		c.setPassword(password);
		credentialManager.validCredentials(c);
		if (credentialManager.validCredentials(c)) {
			System.out.println("valid");
			saltString = generateSaltString();
			EditableEmployee loggedInEmployee = employeeManager
			        .getEmployeeByUserName(userName);
			this.setName(loggedInEmployee.getName());
			this.setEmpNumber(loggedInEmployee.getEmpNumber());
			this.setUserName(loggedInEmployee.getUserName());
			isAdmin = employeeManager
			        .isAdmin(loggedInEmployee);
			if (isAdmin) {
				System.out.println("saltstring: " + saltString);
				return saltString;
			} else {
				return saltString;
			}
		} else {
			return saltString;
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
