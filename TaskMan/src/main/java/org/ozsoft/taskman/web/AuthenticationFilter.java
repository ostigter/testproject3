package org.ozsoft.taskman.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A filter for handling authentication of unauthenticated users and allowing
 * access for users that are authenticated already.
 * 
 * Configure the following init params in web.xml:
 * 
 * loginPage - the page to redirect to once a user logs in successfully.
 * (relative to the context path root)
 * 
 * allowedPaths - a ;-seperated list of paths (relative to the context path
 * root) that do not require authentication.
 * 
 * @author Oscar Stigter
 */
public class AuthenticationFilter implements Filter {

    /** Login page. */
    private String loginPage;

    /** Allowed paths (without authentication). */
    private String[] allowedPaths;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        loginPage = filterConfig.getInitParameter("loginPage");
        allowedPaths = filterConfig.getInitParameter("allowedPaths").split(";");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                // User is already logged in.
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        String path = request.getServletPath();

        // Allow login page.
        if (path.equals(loginPage)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Allow specific paths.
        for (String allowedPath : allowedPaths) {
            if (path.startsWith(allowedPath)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // In all other cases, go to login page.
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + loginPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // Empty implementation.
    }

}
