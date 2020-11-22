package bcit.infosys.student.credential;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;


/**
 * A bean for editing the credentials (password) of an employee. To be used
 * by an admin.
 * @author Scott Reid, John Vaganov
 *
 */
@Named("editCredentialBean")
@RequestScoped
public class EditCredentialBean extends Credentials implements Serializable {

	/**
	 * Method that instructs the credentials list to persist an updated
	 * set of credentials.
	 * @return a string which will send the user back to the admin welcome
	 * screen.
	 */
	public String updateCreds() {
		return "adminWelcome";
	}
}
