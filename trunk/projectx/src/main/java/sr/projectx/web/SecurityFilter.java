package sr.projectx.web;

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
 * access for users that are already authenticated. <br />
 * <br />
 * 
 * Configure the following init params in web.xml:
 * <ul>
 * <li><code>loginPage</code>: login page to redirect to upon unauthenticated
 * access of a secured page (relative to the context path root)</li>
 * <li><code>allowedPaths</code>: a semi-colon seperated list of paths (relative
 * to the context path root) that do not require authentication</li>
 * </ul>
 * 
 * @author Oscar Stigter
 */
public class SecurityFilter implements Filter {

	/** Login page. */
	private String loginPage;

	/** Allowed paths without authentication. */
	private String[] allowedPaths;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		loginPage = filterConfig.getInitParameter("loginPage");
		if (loginPage == null) {
		    throw new IllegalStateException("Servlet init parameter 'loginPage' not set");
		}
		allowedPaths = filterConfig.getInitParameter("allowedPaths").split(";");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
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
