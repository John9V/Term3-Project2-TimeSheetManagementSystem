//package test.employeetest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//
//import ca.bcit.infosys.employee.Credentials;
//import ca.bcit.infosys.employee.Employee;
//import ca.bcit.infosys.employee.EmployeeListBean;
//
//class TestEmployeeList {
//	
//	EmployeeListBean elb = new EmployeeListBean();
//
//	@Test
//	void testGetEmployees() {
//		List<Employee> list = elb.getEmployees();
//		assertTrue(list.get(0).getName().equals("Scott"));
//	}
//	
//	@Test
//	void testGetEmployee() {
//		assertTrue(elb.getEmployee("Scott").getName().equals("Scott"));
//	}
//	
//	@Test
//	void testGetEmployeeByUsername() {
//		assertTrue(elb.getEmployeeByUserName("sreid").getName().equals("Scott"));
//	}
//	
//	@Test
//	void testisAdmin() {
//		assertFalse(elb.isAdmin(elb.getEmployee("Scott")));
//		assertTrue(elb.isAdmin(elb.getEmployee("Bruce")));
//	}
//	
//	@Test
//	void testVerifyUser() {
//		Credentials c = new Credentials();
//		c.setUserName("jvaganov");
//		c.setPassword("password1");
//		assertTrue(elb.verifyUser(c));
//	}
//	
//	@Test
//	void testDeleteEmployee() {
//		elb.deleteEmployee(elb.getEmployee("Scott"));
//		assertTrue(elb.getEmployee("Scott") == null);
//	}
//	
//	@Test
//	void testAddEmployee() {
//		Employee newEmp = new Employee("Test", 5, "test5");
//		elb.addEmployee(newEmp);
//		assertTrue(elb.getEmployee("Test") == newEmp);
//	}
//	
////	@Test
////	void testSave() {
////		for (int i = 0; i < elb.getEmployees().size(); i++) {
////            if (elb.getEmployees().get(i).isDeletable()) {
////            	assertFalse(elb.getEmployees().get(i).isDeletable());
////            } else {
////            	assertFalse(elb.getEmployees().get(i).isEditable());
////            }
////        }
////	}
//
//}
