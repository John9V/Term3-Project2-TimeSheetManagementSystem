package bcit.infosys.student.credential;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import bcit.infosys.student.employee.EmployeeBean;
import ca.bcit.infosys.employee.Credentials;

/**
 * A credential DAO for interacting with a mysql database.
 * @author Ivan Vaganov
 * @author Scott Reid
 * Nov 27 2020 version 2
 */
@Named("credentialManagerBean")
@ConversationScoped
@Path("/credentials")
public class CredentialManagerBean implements CredentialsList, Serializable {
	
    /**
     * Used for authentication with a salt string.
     */
	@Inject EmployeeBean employeeBean;
    /**
     * Default constructor.
     */
    public CredentialManagerBean() { }
    
    /**
     * Connector to the mysql database on wildfly.
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;

    /**
     * Getter for a pair of credentials.
     * @Return a hashmap of strings, one for the username, and one for
     * the password.
     */
    @Override
    public Map<String, String> getCredentials() {
        Map<String, String> credentialsMap = new HashMap<String, String>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM CredentialsT ORDER BY Username");
                    while (result.next()) {
                        credentialsMap.put(result.getString("Username"),
                                result.getString("Password"));
                        System.out.println("Getting credentials: "
                                + result.getString("Username") + " "
                                + result.getString("Password"));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in getCredentials");
            ex.printStackTrace();
            return null;
        }
        return credentialsMap;
    }

    /**
     * Validates credentials by ensuring that the password of the credentials
     * passed in matches the password
     * of the credential looked up by username.
     * @param credentials credentials entered by the user.
     * @return boolean for whether the credentials are valid.
     */
    @Override
    public boolean validCredentials(Credentials credentials) {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                System.out.println("running in valid credentials");
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM CredentialsT WHERE Username LIKE '"
                                    + credentials.getUserName() + "';");
                    if (result.next()) {
                    	if (result.getString("Password")
                    	        .contentEquals(credentials.getPassword())) {
                            return true;
                        }
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in validCredentials");
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Method which adds credentials to the database, usually during user
     * creation.
     * @param credentials represents the credentials to be added to the
     * database.
     */
    @Override
    public void addCredentials(Credentials credentials) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection
                            .prepareStatement("INSERT INTO CredentialsT "
                            + "VALUES(?, ?)");
                    stmt.setString(1, credentials.getUserName());
                    stmt.setNString(2, credentials.getPassword());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in addCredentials for "
                    + credentials.getUserName());
            ex.printStackTrace();
        }
    }
    
    /**
     * REST endpoint which adds credentials to the database.
     * @param userName the path parameter for the username for which
     * to add the credentials
     * @param password the path parameter for the password
     * @param saltString the query parameter authentication string
     */
    @Path("/{userName}/{password}")
    @POST
    public void addCredentialsREST(@PathParam("userName") String userName,
            @PathParam("password") String password,
            @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
	        Credentials incomingCreds = new Credentials();
	        incomingCreds.setUserName(userName);
	        incomingCreds.setPassword(password);
	        addCredentials(incomingCreds);
		}
    }
    
    /**
     * Updates the username of the credentials.
     * @param oldUserName is the previous username, used to look up the right
     * credentials.
     * @param newUserName is the username to update the credentials to remember.
     */
    public void updateCredentialUsername(String oldUserName,
            String newUserName) {
    	Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE CredentialsT "
                            + "SET Username = ?"
                            + " WHERE Username LIKE '" + oldUserName + "'");
                    stmt.setString(1, newUserName);
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in updateCredentials for " + newUserName);
            ex.printStackTrace();
        }
    }
    
    /**
     * REST endpoint which updates the username.
     * @param oldUserName the username to replace
     * @param newUserName the new username to use
     * @param saltString the authentication salt string
     */
    @Path("/userName/{oldUserName}/{newUserName}")
    @PUT
    public void updateCredentialUsernameREST(
            @PathParam("oldUserName") String oldUserName,
            @PathParam("newUserName") String newUserName,
            @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
			updateCredentialUsername(oldUserName, newUserName);
		}
    }

    /**
     * Updates the credential password given a username.
     * @param credentials contains the information to find the credentials to
     * alter (a username).
     */
    @Override
    public void updateCredentials(Credentials credentials) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("UPDATE CredentialsT "
                            + "SET Password = ?"
                            + " WHERE Username LIKE '"
                            + credentials.getUserName() + "'");
                    stmt.setString(1, credentials.getPassword());
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in updateCredentials for "
        + credentials.getUserName());
            ex.printStackTrace();
        }
    }
    
    /**
     * REST endpoint that updates the credential password given a username.
     * @param userName the username path parameter for which to update
     * the password
     * @param newPassword the path parameter representing the new password
     * to set
     * @param saltString the authentication salt string query parameter
     */
    @Path("/{userName}/{password}")
    @PUT
    public void updateCredentialsREST(@PathParam("userName") String userName,
            @PathParam("password") String newPassword,
            @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		}
    	Credentials incomingCreds = new Credentials();
        incomingCreds.setUserName(userName);
        incomingCreds.setPassword(newPassword);
        updateCredentials(incomingCreds);
    }

    /**
     * Deletes credentials, usually called when an employee is deleted
     * rather than explicitly by the administrator.
     * @param userName is the username of the credentials to be deleted.
     */
    @Override
    public void deleteCredentials(String userName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM CredentialsT WHERE Username LIKE '"
                    + userName + "'");
                    stmt.executeUpdate();
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in deleteCredentials for " + userName);
            ex.printStackTrace();
        }
    }
    
    /**
     * REST endpoint that deletes credentials based on the username and
     * salt string.
     * @param userName the username for which to delete credentials
     * @param saltString the authentication salt string query parameter
     */
    @Path("/{userName}")
    @DELETE
    public void deleteCredentialsREST(@PathParam("userName") String userName,
            @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
			deleteCredentials(userName);
		}
    }

    /**
     * Search function that finds a pair of credentials by username.
     * @param userName the username of the credentials to look up.
     * @return the credentials found in the database.
     */
    @Override
    public Credentials findCredentials(String userName) {
        Map<String, String> credentialsMap = new HashMap<String, String>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM CredentialsT WHERE Username LIKE '"
                    + userName + "'");
                    while (result.next()) {
                        credentialsMap.put(result.getString("Username"),
                                result.getString("Password"));
                        System.out.println("findCredentials credentials: "
                                + result.getString("Username") + " "
                                + result.getString("Password"));
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in findCredentials");
            ex.printStackTrace();
            return null;
        }
        Credentials c = new Credentials();
        c.setUserName(userName);
        c.setPassword(credentialsMap.get(userName));
        if (credentialsMap.get(userName) == null) {
            System.out.println("null password for " + userName);
        }
        return c.getPassword() != null ? c : null;
    }
    
    /**
     * REST endpoint that finds credentials by username.
     * @param userName the username path parameter
     * @param saltString the authentication salt string query parameter
     * @return credentials that are found
     */
    @Path("/{userName}")
    @GET
    @Produces("application/json")
    public Credentials findCredentialsREST(
            @PathParam("userName") String userName,
            @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
			return null;
		}
    	return findCredentials(userName);
    }

}
