package net.sf.webdav.methods;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.webdav.MethodExecutor;
import net.sf.webdav.WebdavStatus;

import org.apache.log4j.Logger;

public class DoNotImplemented implements MethodExecutor {

	private static Logger log = Logger.getLogger("net.sf.webdav.methods");

	private boolean readOnly;

	public DoNotImplemented(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if (readOnly) {
			resp.sendError(WebdavStatus.SC_FORBIDDEN);
		} else
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		// TODO implement proppatch
	}

}
