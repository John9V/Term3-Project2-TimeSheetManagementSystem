package bcit.infosys.student.timesheetrow;

import java.util.List;
import javax.sql.DataSource;

import bcit.infosys.student.timesheet.EditableTimesheet;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
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
public class RowManagerBean implements Serializable {
	
	/**
	 * Connector to the database.
	 */
	@Resource(mappedName = "java:jboss/datasources/MySQLDS")
	private DataSource dataSource;
	
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
	
	/**
	 * Method to delete a timesheet row.
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
                	int sheetId = tsr.getOwnerSheetId();
                    stmt = connection.prepareStatement(
                            "INSERT INTO rowsT (ProjectID, WorkPackage, Sat,"
                            + " Sun, Mon, Tue, Wed, Thu, Fri, Notes, sheet_id) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    stmt.setInt(1, id);
                    stmt.setString(2, "WPKG");
                    stmt.setBigDecimal(3, bigd);
                    stmt.setBigDecimal(4, bigd);
                    stmt.setBigDecimal(5, bigd);
                    stmt.setBigDecimal(6, bigd);
                    stmt.setBigDecimal(7, bigd);
                    stmt.setBigDecimal(8, bigd);
                    stmt.setBigDecimal(9, bigd);
                    stmt.setString(10, "enter comments");
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
