package test.employeetest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;

class TestCredential {

	@Test
	void testCredentialCreation() {
		Credentials c = new Credentials();
		c.setUserName("john");
        c.setPassword("password");
        assertTrue(c.getPassword().equals("password"));
        assertTrue(c.getUserName().equals("john"));
	}

}
