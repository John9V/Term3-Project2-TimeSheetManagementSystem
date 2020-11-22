package bcit.infosys.student.credential;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;
/**
 * This bean allows a user to edit their own credentials (password).
 * @author Scott Reid, John Vaganov
 *
 */
@Named("editOwnCredentialsBean")
@RequestScoped
public class EditOwnCredentialsBean extends Credentials implements Serializable {
	@Inject CredentialManagerBean credentialManager;
	/**
	 * @param userName is the userName of the user editing their own
	 * credentials (password)
	 * @return a string which will reload the current page.
	 */
	public String updateOwnCredentials(String userName) {
		Credentials cred = credentialManager.findCredentials(userName);
		cred.setPassword(this.getPassword());
		credentialManager.updateCredentials(cred);
		return "";
	}
}
