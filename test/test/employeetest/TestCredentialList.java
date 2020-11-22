//package test.employeetest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.Test;
//
//import ca.bcit.infosys.employee.Credentials;
//import ca.bcit.infosys.employee.CredentialsListBean;
//
//class TestCredentialList {
//	
//	private CredentialsListBean clb = new CredentialsListBean();
//	Credentials c = new Credentials();
//	
//	@Test
//	void testValidCredentials() {
//		c.setUserName("jvaganov");
//		c.setPassword("password1");
//		assertTrue(clb.validCredentials(c));
//	}
//	
//	@Test
//	void testAddCredentials() {
//		c.setUserName("test");
//		c.setPassword("test");
//		clb.addCredentials(c);
//		assertTrue(clb.findCredentials("test").getPassword().contentEquals("test"));
//	}
//	
//	@Test
//	void testUpdateCredentials() {
//		c.setUserName("test");
//		c.setPassword("test1");
//		clb.addCredentials(c);
//		assertTrue(clb.findCredentials("test").getPassword().contentEquals("test1"));
//	}
//
//	@Test
//	void testDeleteCredentials() {
//		clb.deleteCredentials(clb.findCredentials("jvaganov"));
//		assertTrue(clb.findCredentials("jvaganov") == null);
//	}
//	
//	@Test
//	void testFindCredentials() {
//		assertTrue(clb.findCredentials("jvaganov").getPassword().contentEquals("password1"));
//	}
//	
//}
