package org.ozsoft.toyshop.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static final String PAGE_HEADER = "<html><head><title>HelloWorld</title></head><body>";
    private static final String PAGE_FOOTER = "</body></html>";

    private static final long serialVersionUID = -7874340533831274336L;

    @Inject
    private UserController userController;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.write(PAGE_HEADER);
        writer.write(String.format("<h1>%s</h1>", userController.getGreeting()));
        writer.write(PAGE_FOOTER);
    }
}
