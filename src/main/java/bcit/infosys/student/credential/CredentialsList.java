package bcit.infosys.student.credential;

import java.io.Serializable;
import java.util.Map;

import ca.bcit.infosys.employee.Credentials;

/**
 * This is an interface which defines CRUD methods for the persistent
 * list of credentials.
 * @author Scott Reid, John Vaganov
 *
 */
public interface CredentialsList extends Serializable {

	/**
	 * Getter for the persistent list of credentials.
	 * 
	 * @return a map of strings that represents valid credentials for
	 * accessing the system.
	 */
	Map<String, String> getCredentials();
	
	/**
	 * Method to validate that the passed in credentials match a member
	 * of the credentials list.
	 * 
	 * @param credentials to validate, entered into the front end.
	 * @return a boolean representing whether the credentials are valid.
	 */
	boolean validCredentials(Credentials credentials);
	
	/**
	 * Method to add credentials to the list.
	 * @param credentials to add to the list of credentials.
	 */
	void addCredentials(Credentials credentials);
	
	/**
	 * Method to update the credentials (password) of a set of existing
	 * credentials.
	 * 
	 * @param credentials is a set of credentials with an existing username
	 * and a new password.
	 */
	void updateCredentials(Credentials credentials);
	
	/**
	 * Method to remove some credentials from a list of credentials.
	 * @param userName the username for which credentials to delete
	 * list of credentials.
	 */
	void deleteCredentials(String userName);
	
	/**
	 * Method to find credentials given a username.
	 * 
	 * @param userName the username of an employee to validate or update the
	 * credentials for.
	 * @return the Ceredentials of the passed in employee.
	 */
	public Credentials findCredentials(String userName);
	
}
