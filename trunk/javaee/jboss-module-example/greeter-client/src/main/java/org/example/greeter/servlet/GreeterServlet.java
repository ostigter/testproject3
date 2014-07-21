package org.example.greeter.servlet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.greeter.GreeterUtil;

@WebServlet("/greeting")
public class GreeterServlet extends HttpServlet {

    private static final long serialVersionUID = -5031444846839580270L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write(String.format("Greeting: '%s'", GreeterUtil.getGreeting()));
    }
}
