package bcit.infosys.student.employee;
/**
 * DAO for employee objects.
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import bcit.infosys.student.credential.CredentialManagerBean;
import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

@Named("employeeManagerBean")
@ConversationScoped
@Path("/employees")
public class EmployeeManagerBean implements EditableEmployeeList, Serializable {
    
    /**
     * Connector to the database.
     */
    @Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;
    /**
     * Credential manager, necessary to delete and add credentials when
     * employees are added.
     */
    @Inject CredentialManagerBean credentialManager;
    @Inject EmployeeBean employeeBean;
    
    /**
     * Adds an employee to the database.
     */
    @Override
    public void addEmployee(EditableEmployee e) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement("INSERT INTO Employees "
                            + "(Name, Username, isAdmin) "
                            + "VALUES (?, ?, ?)");
                    stmt.setString(1, e.getName());
                    stmt.setString(2, e.getUserName());
                    stmt.setBoolean(3, e.isAdmin());
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
            System.out.println("Error in addEmployee for employee number "
                    + e.getEmpNumber());
            ex.printStackTrace();
        }
    }
    
    @POST
    public void addEmployeeREST(@QueryParam("number") Integer number,
            @QueryParam("name") String name,
            @QueryParam("userName") String userName,
            @QueryParam("isAdmin") boolean isAdmin,
            @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
	        EditableEmployee newEmp = new EditableEmployee();
	        newEmp.setEmpNumber(number);
	        newEmp.setName(name);
	        newEmp.setUserName(userName);
	        newEmp.setAdmin(isAdmin);
	        addEmployee(newEmp);
		}
    }
    
    /**
     * Updates an employee in the database.
     */
    public void updateEmployee(EditableEmployee e) {
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                	EditableEmployee oldEmployee = getEmployeeByNumber(e
                	        .getEmpNumber());
                	credentialManager.updateCredentialUsername(oldEmployee
                	        .getUserName(), e.getUserName());
                    stmt = connection.prepareStatement("UPDATE Employees "
                            + "SET EmployeeNumber = ?, Name = ?, "
                            + "Username = ?, isAdmin = ? "
                            + "WHERE EmployeeNumber = ?;");
                    stmt.setInt(1, e.getEmpNumber());
                    stmt.setString(2, e.getName());
                    stmt.setString(3, e.getUserName());
                    stmt.setBoolean(4, e.isAdmin());
                    stmt.setInt(5, e.getEmpNumber());
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
            System.out.println("Error in merge " + e);
            ex.printStackTrace();
        }
	}
    
    @PUT
    public void updateEmployeeREST(@QueryParam("number") Integer number,
            @QueryParam("name") String name,
            @QueryParam("userName") String userName,
            @QueryParam("isAdmin") boolean isAdmin,
            @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
	        EditableEmployee newEmp = new EditableEmployee();
	        newEmp.setEmpNumber(number);
	        newEmp.setName(name);
	        newEmp.setUserName(userName);
	        newEmp.setAdmin(isAdmin);
	        updateEmployee(newEmp);
		}
    }

    /**
     * Deletes an employee from the database.
     */
    @Override
    public void deleteEmployee(Employee e) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.prepareStatement(
                            "DELETE FROM Employees WHERE EmployeeNumber LIKE '"
                                    + e.getEmpNumber() + "'");
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
            System.out.println("Error in deleteEmployee for employee number "
                    + e.getEmpNumber());
            ex.printStackTrace();
        }
    }
    
    @Path("/{number}")
    @DELETE
    public void deleteEmployeeREST(@PathParam("number") Integer number, @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		}
    	EditableEmployee newEmp = new EditableEmployee();
        newEmp.setEmpNumber(number);
        deleteEmployee(newEmp);
    }

    /**
     * Method to find an employee by their unique number.
     * @param empNum the number of the employee to update.
     * @return the found employee object.
     */
    public EditableEmployee getEmployeeByNumber(int empNum) {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees WHERE EmployeeNumber "
                            + "LIKE '" + empNum + "'");
                    if (result.next()) {
                        return new EditableEmployee(
                                result.getString("Name"),
                                result.getInt("EmployeeNumber"),
                                result.getString("UserName"),
                                result.getBoolean("isAdmin"));
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
            System.out.println("Error in getEmployee by number");
            ex.printStackTrace();
            return null;
        }
        return null;
    }
    
    @Path("/number/{number}")
    @GET
    @Produces("application/json")
    public Employee getEmployeeByNumberREST(@PathParam("number") Integer number, @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
			return null;
		}
    	return getEmployeeByNumber(number);
    }

    /**
     * Gets an employee object from the database by name
     * @param name string representing the name of the employee to find.
     * @return the employee found.
     */
    @Override
    public Employee getEmployee(String name) {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees WHERE Name LIKE '"
                                    + name + "'");
                    if (result.next()) {
                        return new Employee(
                                result.getString("Name"),
                                result.getInt("EmployeeNumber"),
                                result.getString("UserName"));
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
            System.out.println("Error in getEmployee by name");
            ex.printStackTrace();
            return null;
        }
        return null;
    }
    
    @Path("/name/{name}")
    @GET
    @Produces("application/json")
    public Employee getEmployeeByNameREST(@PathParam("name") String name, @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
			return null;
		}
        return getEmployee(name);
    }

    /**
     * Gets all employees from the database.
     * @return a list of all employees in the database.
     */
    @Override
    public List<Employee> getEmployees() {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees ORDER BY EmployeeNumber");
                    while (result.next()) {
                        employees.add(new Employee(
                                result.getString("Name"),
                                result.getInt("EmployeeNumber"),
                                result.getString("UserName")));
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
            System.out.println("Error in getEmployees");
            ex.printStackTrace();
            return null;
        }

        return employees;
    }
    
    @GET
    @Produces("application/json")
    public List<Employee> getEmployeesREST(@QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
			return null;
		}
        return getEmployees();
    }

    /**
     * Gets credentials from the database.
     * @return a map of credentials from the database.
     */
    @Override
    @Path("/credentials")
    @GET
    @Produces("application/json")
    public Map<String, String> getLoginCombos() {
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
            System.out.println("Error in getLoginCombos");
            ex.printStackTrace();
            return null;
        }
        return credentialsMap;
    }

    /**
     * Determines if the entered credentials match credentials in the database,
     * granting access to the system.
     * @param credentials user entered credentials.
     * @return boolean for whether the user is granted access to the system.
     */
    @Override
    public boolean verifyUser(Credentials credentials) {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
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
            System.out.println("Error in verifyUser");
            ex.printStackTrace();
            return false;
        }
        return false;
    }
    
    @Path("/verify/{userName}/{password}")
    @GET
    @Produces("application/json")
    public boolean verifyUserREST(@PathParam("userName") String userName,
            @PathParam("password") String password) {
        Credentials incomingCreds = new Credentials();
        incomingCreds.setUserName(userName);
        incomingCreds.setPassword(password);
        return verifyUser(incomingCreds);
    }

    /**
     * This method finds an employee by username.
     * @param userName the username of the employee to find.
     * @return the found employee.
     */
    @Override
    public EditableEmployee getEmployeeByUserName(String userName) {
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees WHERE Username LIKE '%"
                                    + userName + "%';");
                    if (result.next()) {
                        return new EditableEmployee(
                                result.getString("Name"),
                                result.getInt("EmployeeNumber"),
                                result.getString("UserName"),
                                result.getBoolean("isAdmin"));
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
            System.out.println("Error in getEmployee by Username");
            ex.printStackTrace();
            return null;
        }
        return null;
    }
    
    @Path("/userName/{userName}")
    @GET
    @Produces("application/json")
    public Employee getEmployeeByUserNameREST(@PathParam("userName") String userName) {
        return getEmployeeByUserName(userName);
    }

    /**
     * Getter for editable employees.
     * @return the found list of employees from the database.
     */
    @Override
    public List<EditableEmployee> getEditableEmployees() {
        ArrayList<EditableEmployee> employees = new
                ArrayList<EditableEmployee>();
        Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees ORDER BY EmployeeNumber");
                    while (result.next()) {
                        employees.add(new EditableEmployee(
                                result.getString("Name"),
                                result.getInt("EmployeeNumber"),
                                result.getString("UserName"),
                                result.getBoolean("isAdmin")));
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
            System.out.println("Error in getEditableEmployees");
            ex.printStackTrace();
            return null;
        }

        return employees;
    }
    
    @Path("/editable")
    @GET
    @Produces("application/json")
    public List<EditableEmployee> getEditableEmployeesREST() {
        return getEditableEmployees();
    }

    /**
     * Method to determine if the employee in the database is an administrator.
     * @param employee
     * @return a boolean determining if the employee is or is not an
     * administrator.
     */
    @Override
    public boolean isAdmin(Employee employee) {
    	Connection connection = null;
        Statement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                	String userName = employee.getUserName();
                    stmt = connection.createStatement();
                    ResultSet result = stmt.executeQuery(
                            "SELECT * FROM Employees WHERE Username LIKE '"
                                    + userName + "'");
                    if (result.next()) {
                        boolean isAdmin = result.getBoolean("isAdmin");
                        return isAdmin;
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
            System.out.println("Error in getEmployee by Username");
            ex.printStackTrace();
        }
        return false;
    }
    
    @Path("/isAdmin/{userName}")
    @GET
    @Produces("application/json")
    public boolean isAdminREST(@PathParam("userName") String userName, @QueryParam("saltString") String saltString) {
    	if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		}
    	return isAdmin(getEmployeeByUserName(userName));
    }

	/**
	 * unused
	 */
	@Override
	public void addEmployee(Employee e) {
		
	}
	
	/**
	 * unused
	 */
	@Override
    public Employee getAdministrator() {
        return null;
    }

    /**
     * unused
     */
    @Override
    public Employee getCurrentEmployee() {
        return null;
    }

    /**
     * unused
     */
    @Override
    public void save() {
    }
    
    /**
     * unused
     */
    @Override
    public String logout(Employee e) {
        return null;
    }
}
