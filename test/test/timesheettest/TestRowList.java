//package test.timesheettest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//
//import bcit.infosys.student.timesheetrow.RowListBean;
//import ca.bcit.infosys.system.HRSystem;
//import ca.bcit.infosys.timesheet.Timesheet;
//import ca.bcit.infosys.timesheet.TimesheetCollectionBean;
//import ca.bcit.infosys.timesheet.TimesheetRow;
//
//class TestRowList {
//	
////	RowListBean rlb = new RowListBean();
////	TimesheetCollectionBean tbc = new TimesheetCollectionBean();
////	Timesheet newTs = new Timesheet();
////
////	@Test
////	void testGetRows() {
////		tbc.addTimesheet(newTs);
////		TimesheetRow newTsr = new TimesheetRow();
////    	newTsr.setOwnerTimesheet(newTs);
////    	rlb.addTimesheetRow(newTsr);
////		List<TimesheetRow> rows = rlb.getRows(newTs);
////		assertTrue(newTsr == rows.get(0));
////	}
////	
////	@Test
////	void testGetCurrentTimesheet() {
////		tbc.addTimesheet(newTs);
////		TimesheetRow newTsr = new TimesheetRow();
////    	newTsr.setOwnerTimesheet(newTs);
////    	rlb.addTimesheetRow(newTsr);
////    	assertTrue(newTs == rlb.getCurrentTimeSheet(newTsr));
////	}
////	
////	@Test
////	void testFindRow() {
////		tbc.addTimesheet(newTs);
////		TimesheetRow newTsr = new TimesheetRow();
////		newTsr.setProjectID(1);
////		newTsr.setWorkPackage("test");
////    	newTsr.setOwnerTimesheet(newTs);
////    	rlb.addTimesheetRow(newTsr);
////    	assertTrue(newTsr == rlb.findRow(1, "test"));
////	}
////	
////	@Test
////	void testDeleteRow() {
////		tbc.addTimesheet(newTs);
////		TimesheetRow newTsr = new TimesheetRow();
////		newTsr.setProjectID(1);
////		newTsr.setWorkPackage("test");
////    	newTsr.setOwnerTimesheet(newTs);
////    	rlb.addTimesheetRow(newTsr);
////    	rlb.deleteRow(newTsr);
////    	assertTrue(rlb.findRow(1, "test") == null);
////	}
////	
////	@Test
////	void testUpdateRow() {
////		tbc.addTimesheet(newTs);
////		TimesheetRow newTsr = new TimesheetRow();
////		newTsr.setProjectID(1);
////		newTsr.setWorkPackage("test");
////    	newTsr.setOwnerTimesheet(newTs);
////    	rlb.addTimesheetRow(newTsr);
////    	
////    	TimesheetRow secondTsr = new TimesheetRow();
////    	secondTsr.setProjectID(1);
////    	secondTsr.setWorkPackage("test");
////    	secondTsr.setOwnerTimesheet(newTs);
////    	rlb.updateRow(secondTsr);
////    	
////    	assertTrue(secondTsr == rlb.findRow(1, "test"));
////	}
////
////	@Test
////	void testSave() {
////		tbc.addTimesheet(newTs);
////		TimesheetRow newTsr = new TimesheetRow();
////    	newTsr.setOwnerTimesheet(newTs);
////    	rlb.addTimesheetRow(newTsr);
////		
////		rlb.save(newTs);
////				
////		for (int i = 0; i < rlb.getRows().size(); i++) {
////            if (rlb.getRows().get(i).isDeletable()) {
////            	assertFalse(rlb.getRows().get(i).isDeletable());
////            } else {
////            	assertFalse(rlb.getRows().get(i).isEditable());
////            }
////        }
////	}
//
//}
