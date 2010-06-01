package sr.projectx.web;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sr.projectx.entities.User;
import sr.projectx.services.UserService;

public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4701709787440016631L;
	
	@Resource(name = "UserService")
	private UserService userService;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		Writer writer = response.getWriter();
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setEmail("admin@projectx.sr");
		userService.create(user);
		writer.write("<b>User created!</b>");
	}

}
