package sr.projectx.services;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Test;

import sr.projectx.entities.User;

public class UserServiceImplTest {
	
	private static InitialContext initialContext;
	
	@BeforeClass
	public static void beforeClass() throws NamingException {
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.openejb.client.LocalInitialContextFactory");
		initialContext = new InitialContext(properties);
	}
	
	@Test
	public void test() {
//		UserService userService = new UserServiceImpl();
//		
//		User user = new User();
//		user.setUsername("o.stigter");
//		user.setPassword("appel");
//		user.setEmail("oscar.stigter@gmail.com");
//		userService.create(user);
//		
//		userService.close();
	}

}
