package bcit.infosys.student.timesheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import bcit.infosys.student.employee.EditableEmployee;
import bcit.infosys.student.employee.EmployeeBean;
import bcit.infosys.student.employee.EmployeeManagerBean;

/**
 * DAO for timesheets.
 * @author Scott Reid, Ivan Vaganov
 *
 */
@Named("timesheetManager")
@ConversationScoped
@Path("/timesheets")
public class TimesheetManagerBean implements Serializable {

	/**
	 * Connector to the database.
	 */
	@Resource(mappedName = "java:jboss/datasources/MySQLDS")
	private DataSource dataSource;
	
	/**
	 * Used for getting employees by number.
	 */
	@Inject EmployeeManagerBean employeeManager;
	
	/**
	 * Used for authentication with a salt string.
	 */
	@Inject EmployeeBean employeeBean;
	
	/**
	 * Gets a list of timesheets for a given employee as a restful service.
	 * @param saltString the authentication salt string path parameter
	 * @param employeeNumber the employee number to get timesheets for.
	 * @return a list of timesheets associated with the passed in employee.
	 */
	@Path("/number/{saltString}/{employeeNumber}")
    @GET
    @Produces("application/json")
	public List<EditableTimesheet> getTimesheetsREST(
	        @PathParam("saltString") String saltString,
	        @PathParam("employeeNumber") Integer employeeNumber) {
		if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
			return null;
		}
		ArrayList<EditableTimesheet> sheetList = new
		        ArrayList<EditableTimesheet>();
		System.out.println("Injected saltString: "
		        + employeeBean.getSaltString());
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			try {
				connection = dataSource.getConnection();
				try {
					int empId = employeeNumber;
					
					stmt = connection.prepareStatement("SELECT * FROM "
					        + "TimesheetsT WHERE "
							+ "EmployeeNumber = ?;");
					stmt.setInt(1, empId);
					ResultSet result = stmt.executeQuery();
					while (result.next()) {
						Date endWeek = result.getDate("EndWeek");
						BigDecimal ot = result.getBigDecimal("Overtime");
						BigDecimal ft = result.getBigDecimal("Flextime");
						int sheetId = result.getInt("sheet_id");
						EditableTimesheet t = new EditableTimesheet();
						EditableEmployee e = employeeManager
						        .getEmployeeByNumber(employeeNumber);
						t.setEmployee(e);
						t.setEndWeek(endWeek.toLocalDate());
						t.setTimesheetId(sheetId);
						sheetList.add(t);
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
		System.out.println("Error in get rows by timesheet");
		ex.printStackTrace();
		return null;
	}
		return sheetList;
	}
	
	/**
	 * Gets a list of timesheets for a given employee.
	 * @param e the employee to get timesheets for.
	 * @return a list of timesheets associated with the passed in employee.
	 */
	public List<EditableTimesheet> getTimesheets(EditableEmployee e) {
		ArrayList<EditableTimesheet> sheetList = new
		        ArrayList<EditableTimesheet>();
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			try {
				connection = dataSource.getConnection();
				try {
					int empId = e.getEmpNumber();
					
					stmt = connection.prepareStatement("SELECT * FROM "
					        + "TimesheetsT WHERE "
							+ "EmployeeNumber = ?;");
					stmt.setInt(1, empId);
					ResultSet result = stmt.executeQuery();
					while (result.next()) {
						Date endWeek = result.getDate("EndWeek");
						BigDecimal ot = result.getBigDecimal("Overtime");
						BigDecimal ft = result.getBigDecimal("Flextime");
						int sheetId = result.getInt("sheet_id");
						EditableTimesheet t = new EditableTimesheet();
						t.setEmployee(e);
						t.setEndWeek(endWeek.toLocalDate());
						t.setTimesheetId(sheetId);
						sheetList.add(t);
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
		System.out.println("Error in get rows by timesheet");
		ex.printStackTrace();
		return null;
	}
		return sheetList;
	}
	
	/**
	 * Updates a passed in timesheet with the passed in values in
	 * the database.
	 * @param t the timesheet to update.
	 */
	public void updateTimesheet(EditableTimesheet t) {
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                	System.out.println("t: " + t);
                	System.out.println("emp: " + t.getEmployee());
                	int empNum = t.getEmployee().getEmpNumber();
                	String endWeek = t.getWeekEnding();
                	Date sqlDate = Date.valueOf(endWeek);
                	BigDecimal ot = t.getOvertime();
                	BigDecimal ft = t.getFlextime();
                	int sheetId = t.getTimesheetId();
                    stmt = connection.prepareStatement("UPDATE TimesheetsT "
                            + "SET EmployeeNumber = ?, EndWeek = ?, "
                            + "Overtime = ?, Flextime = ? "
                            + "WHERE sheet_id = ?;");
                    stmt.setInt(1, empNum);
                    stmt.setDate(2, sqlDate);
                    stmt.setBigDecimal(3, ot);
                    stmt.setBigDecimal(4, ft);
                    stmt.setInt(5, sheetId);
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
            System.out.println("Error in merge " + t);
            ex.printStackTrace();
        }
	}
	
	/**
	 * REST endpoint for updating a timesheet.
	 * @param saltString the authentication salt string path parameter
	 * @param empNum query parameter for the employee number
	 * @param endWeek query parameter for the end week
	 * @param ot query parameter for the over time
	 * @param ft query parameter for the flex time
	 * @param sheetId path parameter for the timesheet ID
	 */
	@Path("/updateTimesheet/{saltString}/{sheetId}")
	@PUT
	public void updateTimesheetREST(
	        @PathParam("saltString") String saltString,
	        @QueryParam("empNum") Integer empNum,
			@QueryParam("endWeek") String endWeek,
			@QueryParam("ot") BigDecimal ot,
			@QueryParam("ft") BigDecimal ft,
			@PathParam("sheetId") Integer sheetId) {
		if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
			EditableTimesheet t = new EditableTimesheet();
			t.setOvertime(ot);
			t.setFlextime(ft);
			t.setTimesheetId(sheetId);
			EditableEmployee e = employeeManager
			        .getEmployeeByNumber(empNum);
			t.setEmployee(e);
			DateTimeFormatter formatter = DateTimeFormatter
			        .ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate
			        .parse(endWeek, formatter);
			localDate = localDate.with(TemporalAdjusters
			        .nextOrSame(DayOfWeek.FRIDAY));
			t.setEndWeek(localDate);
			updateTimesheet(t);
		}
	}
	
	/**
	 * REST endpoint for adding a timesheet to an employee.
	 * @param saltString the authentication salt string path parameter
	 * @param empNum query parameter for the employee number
	 * @param endWeek query parameter for the endweek
	 * @param ot query parameter for the over time
	 * @param ft query parameter for the flex time
	 */
	@Path("/addTimesheet/{saltString}")
	@POST
	public void addTimesheetREST(@PathParam("saltString") String saltString,
	        @QueryParam("empNum") Integer empNum,
			@QueryParam("endWeek") String endWeek,
			@QueryParam("ot") BigDecimal ot,
			@QueryParam("ft") BigDecimal ft) {
		if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
			EditableTimesheet t = new EditableTimesheet();
			t.setOvertime(ot);
			t.setFlextime(ft);
			EditableEmployee e = employeeManager.getEmployeeByNumber(empNum);
			t.setEmployee(e);
			DateTimeFormatter formatter = DateTimeFormatter
			        .ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(endWeek, formatter);
			localDate = localDate.with(TemporalAdjusters
			        .nextOrSame(DayOfWeek.FRIDAY));
			t.setEndWeek(localDate);
			addTimesheet(t);
		}
	}
	
	/**
	 * Adds a timesheet to the system.
	 * @param t the timesheet to be added.
	 */
	public void addTimesheet(EditableTimesheet t) {
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                	int empNum = t.getEmployee().getEmpNumber();
                	String endWeek = t.getWeekEnding();
                	Date sqlDate = Date.valueOf(endWeek);
                	BigDecimal ot = t.getOvertime();
                	BigDecimal ft = t.getFlextime();
                    stmt = connection.prepareStatement(
                            "INSERT INTO TimesheetsT "
                            + "(EmployeeNumber, EndWeek, Overtime, Flextime) "
                         + "VALUES (?, ?, ?, ?)");
                    stmt.setInt(1, empNum);
                    stmt.setDate(2, sqlDate);
                    stmt.setBigDecimal(3, ot);
                    stmt.setBigDecimal(4, ft);
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
            System.out.println("Error in persist " + t);
            ex.printStackTrace();
        }

	}
	
	/**
	 * Find a timesheet based on employee and date.
	 * @param e the employee this timesheet belongs to.
	 * @param d the date of the timesheet we're looking for.
	 * @return the editable timesheet found.
	 */
	public EditableTimesheet findTimesheet(EditableEmployee e, LocalDate d) {
		ArrayList<EditableTimesheet> sheetList = new
		        ArrayList<EditableTimesheet>();
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			try {
				connection = dataSource.getConnection();
				try {
					int empId = e.getEmpNumber();
					
					stmt = connection.prepareStatement("SELECT * FROM "
					        + "TimesheetsT WHERE "
							+ "EmployeeNumber = ? AND EndWeek = ?;");
					stmt.setInt(1, empId);
					stmt.setDate(2, Date.valueOf(d));
					ResultSet result = stmt.executeQuery();
					while(result.next()) {
						Date endWeek = result.getDate("EndWeek");
						BigDecimal ot = result.getBigDecimal("Overtime");
						BigDecimal ft = result.getBigDecimal("Flextime");
						int sheetId = result.getInt("sheet_id");
						EditableTimesheet t = new EditableTimesheet();
						t.setEmployee(e);
						t.setEndWeek(endWeek.toLocalDate());
						t.setTimesheetId(sheetId);
						sheetList.add(t);
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
		System.out.println("Error in get rows by timesheet");
		ex.printStackTrace();
		return null;
	}
		return sheetList.get(0);
	}

}
