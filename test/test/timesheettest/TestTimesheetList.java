//package test.timesheettest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//
//import ca.bcit.infosys.employee.Employee;
//import ca.bcit.infosys.employee.EmployeeListBean;
//import ca.bcit.infosys.system.HRSystem;
//import ca.bcit.infosys.timesheet.Timesheet;
//import ca.bcit.infosys.timesheet.TimesheetCollectionBean;
//import ca.bcit.infosys.timesheet.TimesheetRow;
//
//class TestTimesheetList {
//
//	
//	@Test
//	void testGetTimesheets() {
//		TimesheetCollectionBean tbc = new TimesheetCollectionBean();
//		EmployeeListBean elb = new EmployeeListBean();
//		Employee e = elb.getEmployeeByUserName("blink");
//		String name = tbc.getTimesheets().get(0).getEmployee().getUserName();
//		assertTrue(name.equals("blink"));
//	}
//	
//	@Test
//	void testGetTimesheetsEmployee() {
//		TimesheetCollectionBean tbc = new TimesheetCollectionBean();
//		EmployeeListBean elb = new EmployeeListBean();
//		Employee e = elb.getEmployeeByUserName("blink");
//		String name = tbc.getTimesheets(e).get(0).getEmployee().getUserName();
//		System.out.println(name);
//		assertTrue(name.equals("blink"));
//	}
//	
//	@Test
//	void testAddTimesheet() {
//		TimesheetCollectionBean tbc = new TimesheetCollectionBean();
//		EmployeeListBean elb = new EmployeeListBean();
//		Employee e = elb.getEmployeeByUserName("blink");
//		
//		Timesheet newTs = new Timesheet();
//    	tbc.addTimesheet(newTs);
//    	newTs.setEmployee(e);
//    	newTs.getDetails();
//		
//		Timesheet newTsAdded = tbc.getTimesheets(e).get(1);
//		assertTrue(newTsAdded == newTs);
//	}
//
//}
