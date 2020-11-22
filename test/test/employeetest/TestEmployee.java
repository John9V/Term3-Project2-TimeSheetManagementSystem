package test.employeetest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.TimesheetRow;

class TestEmployee {

	@Test
	void testEmployeeCreation() {
		Employee e = new Employee("Scott", 1, "e1");
        assertTrue(e.getName().equals("Scott"));
        assertTrue(e.getUserName().equals("e1"));
        assertTrue(e.getEmpNumber() == 1);
        e.setName("John");
        e.setUserName("j1");
        e.setEmpNumber(2);
        assertTrue(e.getName().equals("John"));
        assertTrue(e.getUserName().equals("j1"));
        assertTrue(e.getEmpNumber() == 2);
	}
	
}
