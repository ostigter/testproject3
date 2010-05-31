package sr.projectx.services;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Test;

import sr.projectx.entities.User;

public class UserServiceImplTest {
	
	@Test
	public void test() throws NamingException {
        Properties p = new Properties();

        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");

//        p.put("database", "new://Resource?type=DataSource");
//        p.put("database.JdbcDriver", "org.hsqldb.jdbcDriver");
//        p.put("database.JdbcUrl", "jdbc:hsqldb:mem:projectx");
//        p.put("database.JtaManaged", "true");
//
//        p.put("databaseUnmanaged", "new://Resource?type=DataSource");
//        p.put("databaseUnmanaged.JdbcDriver", "org.hsqldb.jdbcDriver");
//        p.put("databaseUnmanaged.JdbcUrl", "jdbc:hsqldb:mem:projectx");
//        p.put("databaseUnmanaged.JtaManaged", "false");

        Context context = new InitialContext(p);

        UserService userService = (UserService) context.lookup("UserServiceLocal");

		User user = new User();
		user.setUsername("o.stigter");
		user.setPassword("appel");
		user.setEmail("oscar.stigter@gmail.com");
		userService.create(user);
		
		userService.close();
	}

}
