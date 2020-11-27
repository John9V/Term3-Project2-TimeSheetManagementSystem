package bcit.infosys.student.timesheetrow;

import java.util.List;
import javax.sql.DataSource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import bcit.infosys.student.employee.EmployeeBean;
import bcit.infosys.student.timesheet.EditableTimesheet;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class which interacts with the database for the timesheet rows of 
 * our timesheet system.
 * @author Ivan Vaganov
 * @author Scott Reid
 */
@Named("rowManagerBean")
@ConversationScoped
@Path("/rows")
public class RowManagerBean implements Serializable {
	
	/**
	 * Connector to the database.
	 */
	@Resource(mappedName = "java:jboss/datasources/MySQLDS")
	private DataSource dataSource;
	@Inject EmployeeBean employeeBean;
	
	@Path("/number/{saltString}/{timesheetNum}")
    @GET
    @Produces("application/json")
	public List<EditableTimesheetRow> getRowsREST(@PathParam("saltString") String saltString, @PathParam("timesheetNum") Integer timesheetNum) {
		if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
			return null;
		}
		
		EditableTimesheet t = new EditableTimesheet();
		t.setTimesheetId(timesheetNum);
		List<EditableTimesheetRow> rows = getRows(t);
		return rows;
	}
	
	/**
	 * Getter for the rows associated with a particular timesheet.
	 * @param sheet the timesheet to display rows for
	 * @return a list of rows associated with the passed in timesheet
	 */
	public List<EditableTimesheetRow> getRows(EditableTimesheet sheet) {
		ArrayList<EditableTimesheetRow> rowList = new
		        ArrayList<EditableTimesheetRow>();
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			try {
				connection = dataSource.getConnection();
				try {
					 int sheetId = sheet.getTimesheetId();
					
				stmt = connection.prepareStatement("SELECT * "
				        + "FROM rowsT WHERE "
						+ "sheet_id = ?;");
				stmt.setInt(1, sheetId);
				ResultSet result = stmt.executeQuery();
				while (result.next()) {
					int id = result.getInt("ProjectID");
					String wp = result.getString("WorkPackage");
					BigDecimal mon = result.getBigDecimal("Mon");
					BigDecimal tue = result.getBigDecimal("Tue");
					BigDecimal wed = result.getBigDecimal("Wed");
					BigDecimal thu = result.getBigDecimal("Thu");
					BigDecimal fri = result.getBigDecimal("Fri");
					BigDecimal sat = result.getBigDecimal("Sat");
					BigDecimal sun = result.getBigDecimal("Sun");
					BigDecimal[] hours = {sat, sun, mon, tue, wed, thu, fri};
					String comments = result.getNString("Notes");
					int rowId = result.getInt("rows_id");
					EditableTimesheetRow tempTSR = new EditableTimesheetRow(
					        id, wp, hours, comments, sheetId);
					tempTSR.setRowId(rowId);
					rowList.add(tempTSR);
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
		return rowList;
	}
	
	@Path("/updateRow/{saltString}/{rowId}")
	@PUT
	public void updateRowREST(@PathParam("saltString") String saltString, @PathParam("rowId") Integer rowId, 
			@QueryParam("projectId") Integer projectId,
			@QueryParam("wp") String wp,
			@QueryParam("sat") BigDecimal sat,
			@QueryParam("sun") BigDecimal sun,
			@QueryParam("mon") BigDecimal mon,
			@QueryParam("tue") BigDecimal tue,
			@QueryParam("wed") BigDecimal wed,
			@QueryParam("thu") BigDecimal thu,
			@QueryParam("fri") BigDecimal fri,
			@QueryParam("notes") String notes) {
		if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
			EditableTimesheetRow tsr = new EditableTimesheetRow();
			tsr.setRowId(rowId);
			tsr.setProjectID(projectId);
			tsr.setNotes(notes);
			tsr.setWorkPackage(wp);
			BigDecimal[] arr = tsr.getHoursForWeek();
			arr[0] = sat;
			arr[1] = sun;
			arr[2] = mon;
			arr[3] = tue;
			arr[4] = wed;
			arr[5] = thu;
			arr[6] = fri;
			tsr.setHoursForWeek(arr);
			updateRow(tsr);
		}
	}
	
	/**
	 * Update method for timesheet rows.
	 * @param tsr is the row which needs to be updated.
	 */
	public void updateRow(EditableTimesheetRow tsr) {
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                	int id = tsr.getProjectID();
                	String wp = tsr.getWorkPackage();
                	BigDecimal[] arr = tsr.getHoursForWeek();
                	BigDecimal sat = arr[0];
                	BigDecimal sun = arr[1];
                	BigDecimal mon = arr[2];
                	BigDecimal tue = arr[3];
                	BigDecimal wed = arr[4];
                	BigDecimal thu = arr[5];
                	BigDecimal fri = arr[6];
                	String notes = tsr.getNotes();
                	int rowId = tsr.getRowId();
                    stmt = connection.prepareStatement("UPDATE rowsT "
                            + "SET ProjectID = ?, WorkPackage = ?, "
                            + "Sat = ?, Sun = ?, Mon = ?, "
                            + "Tue = ?, Wed = ?, "
                            + "Thu = ?, Fri = ?, Notes = ? "
                            + "WHERE rows_id = ?;");
                    stmt.setInt(1, id);
                    stmt.setString(2, wp);
                    stmt.setBigDecimal(3, sat);
                    stmt.setBigDecimal(4, sun);
                    stmt.setBigDecimal(5, mon);
                    stmt.setBigDecimal(6, tue);
                    stmt.setBigDecimal(7, wed);
                    stmt.setBigDecimal(8, thu);
                    stmt.setBigDecimal(9, fri);
                    stmt.setString(10, notes);
                    stmt.setInt(11, rowId);
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
            System.out.println("Error in merge " + tsr);
            ex.printStackTrace();
        }
	}
	
	@Path("/delete/{saltString}/{number}")
    @DELETE
	public void deleteRow(@PathParam("saltString") String saltString, @PathParam("number") Integer number) {
		if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
			EditableTimesheetRow tsr = new EditableTimesheetRow();
			tsr.setRowId(number);
			deleteRow(tsr);
		}
	}
	
	/**
	 * Method to delete a timesheet row.-
	 * @param tsr is the row to be deleted.
	 */
	public void deleteRow(EditableTimesheetRow tsr) {
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                	int rowId = tsr.getRowId();
                    stmt = connection.prepareStatement(
                            "DELETE FROM rowsT WHERE rows_id = ?;");
                    stmt.setInt(1, rowId);
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
            System.out.println("Error in remove " + tsr);
            ex.printStackTrace();
        }
	}
	
	@Path("/addRow/{saltString}")
	@POST
	public void addRowREST(@PathParam("saltString") String saltString,
			@QueryParam("projectId") Integer projectId,
			@QueryParam("wp") String wp,
			@QueryParam("sat") BigDecimal sat,
			@QueryParam("sun") BigDecimal sun,
			@QueryParam("mon") BigDecimal mon,
			@QueryParam("tue") BigDecimal tue,
			@QueryParam("wed") BigDecimal wed,
			@QueryParam("thu") BigDecimal thu,
			@QueryParam("fri") BigDecimal fri,
			@QueryParam("notes") String notes,
			@QueryParam("sheedId") String sheetId
			) {
		if (!saltString.equals(employeeBean.getSaltString())) {
			System.out.println("Not authorized.");
		} else {
			EditableTimesheetRow tsr = new EditableTimesheetRow();
			tsr.setProjectID(projectId);
			tsr.setNotes(notes);
			tsr.setWorkPackage(wp);
			BigDecimal[] arr = tsr.getHoursForWeek();
			arr[0] = sat;
			arr[1] = sun;
			arr[2] = mon;
			arr[3] = tue;
			arr[4] = wed;
			arr[5] = thu;
			arr[6] = fri;
			tsr.setHoursForWeek(arr);
			addTimesheetRow(tsr);
		}
	}
	
	/**
	 * Method to add a row to the database.
	 * @param tsr the row to be added.
	 */
	public void addTimesheetRow(EditableTimesheetRow tsr) {
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
            try {
                connection = dataSource.getConnection();
                try {
                	int id = tsr.getProjectID();
                	BigDecimal bigd = new BigDecimal(0);
                	
                	String wp = "WPKG";
                	BigDecimal[] arr = tsr.getHoursForWeek();
                	BigDecimal sat = bigd;
                	BigDecimal sun = bigd;
                	BigDecimal mon = bigd;
                	BigDecimal tue = bigd;
                	BigDecimal wed = bigd;
                	BigDecimal thu = bigd;
                	BigDecimal fri = bigd;
                	if(arr != null) {
                		sat = arr[0];
                    	sun = arr[1];
                    	mon = arr[2];
                    	tue = arr[3];
                    	wed = arr[4];
                    	thu = arr[5];
                    	fri = arr[6];
                    	wp = tsr.getWorkPackage();
                	}
                	
                	int sheetId = tsr.getOwnerSheetId();
                	String comments = tsr.getNotes();
                	if(comments == null) {
                		comments = "enter comments";
                	}
                    stmt = connection.prepareStatement(
                            "INSERT INTO rowsT (ProjectID, WorkPackage, Sat,"
                            + " Sun, Mon, Tue, Wed, Thu, Fri, Notes, sheet_id) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    stmt.setInt(1, id);
                    stmt.setString(2, wp);
                    stmt.setBigDecimal(3, sat);
                    stmt.setBigDecimal(4, sun);
                    stmt.setBigDecimal(5, mon);
                    stmt.setBigDecimal(6, tue);
                    stmt.setBigDecimal(7, wed);
                    stmt.setBigDecimal(8, thu);
                    stmt.setBigDecimal(9, fri);
                    stmt.setString(10, comments);
                    stmt.setInt(11, sheetId);
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
            System.out.println("Error in persist " + tsr);
            ex.printStackTrace();
        }

	}

}
