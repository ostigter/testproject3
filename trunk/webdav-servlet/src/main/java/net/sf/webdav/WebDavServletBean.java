package net.sf.webdav;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.webdav.exceptions.UnauthenticatedException;
import net.sf.webdav.exceptions.WebdavException;
import net.sf.webdav.methods.DoCopy;
import net.sf.webdav.methods.DoDelete;
import net.sf.webdav.methods.DoGet;
import net.sf.webdav.methods.DoHead;
import net.sf.webdav.methods.DoLock;
import net.sf.webdav.methods.DoMkcol;
import net.sf.webdav.methods.DoMove;
import net.sf.webdav.methods.DoNotImplemented;
import net.sf.webdav.methods.DoOptions;
import net.sf.webdav.methods.DoPropfind;
import net.sf.webdav.methods.DoPut;

import org.apache.log4j.Logger;

public class WebDavServletBean extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log =
        Logger.getLogger(WebDavServletBean.class);

    private static final boolean readOnly = false;
    private ResourceLocks resLocks;
    private WebdavStore store;
    private Map<String, MethodExecutor> methodMap = new HashMap<String, MethodExecutor>();

    public WebDavServletBean() {
        this.resLocks = new ResourceLocks();
        try {
            MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException();
        }
    }

    public void init(WebdavStore store, String dftIndexFile, String insteadOf404,
    		int nocontentLenghHeaders, boolean lazyFolderCreationOnPut) throws ServletException {

        this.store = store;

        MimeTyper mimeTyper = new MimeTyper() {
            public String getMimeType(String path) {
                return "text/plain";
            }
        };

        register("GET", new DoGet(store, dftIndexFile, insteadOf404, resLocks,
                mimeTyper, nocontentLenghHeaders));
        register("HEAD", new DoHead(store, dftIndexFile, insteadOf404,
                resLocks, mimeTyper, nocontentLenghHeaders));
        DoDelete doDelete = (DoDelete) register("DELETE", new DoDelete(store,
                resLocks, readOnly));
        DoCopy doCopy = (DoCopy) register("COPY", new DoCopy(store, resLocks,
                doDelete, readOnly));
        register("LOCK", new DoLock(store, resLocks, readOnly));
        register("MOVE", new DoMove(resLocks, doDelete, doCopy, readOnly));
        register("MKCOL", new DoMkcol(store, resLocks, readOnly));
        register("OPTIONS", new DoOptions(store, resLocks));
        register("PUT", new DoPut(store, resLocks, readOnly,
                lazyFolderCreationOnPut));
        register("PROPFIND", new DoPropfind(store, resLocks, readOnly,
                mimeTyper));
        register("PROPPATCH", new DoNotImplemented(readOnly));
        register("*NO*IMPL*", new DoNotImplemented(readOnly));
    }

    private MethodExecutor register(String methodName, MethodExecutor method) {
        methodMap.put(methodName, method);
        return method;
    }

    /**
     * Handles the special WebDAV methods.
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {

        String methodName = req.getMethod();

        debugRequest(methodName, req);

        try {
            store.begin(req.getUserPrincipal());
            store.checkAuthentication();
            resp.setStatus(WebdavStatus.SC_OK);

            try {
                MethodExecutor methodExecutor = (MethodExecutor) methodMap.get(methodName);
                if (methodExecutor == null) {
                    methodExecutor = (MethodExecutor) methodMap.get("*NO*IMPL*");
                }
                methodExecutor.execute(req, resp);

                store.commit();
            } catch (IOException e) {
                java.io.StringWriter sw = new java.io.StringWriter();
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                log.error("IOException: " + sw.toString());
                resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
                store.rollback();
                throw new ServletException(e);
            }

        } catch (UnauthenticatedException e) {
            resp.sendError(WebdavStatus.SC_FORBIDDEN);
        } catch (WebdavException e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            log.error("WebdavException: " + sw.toString());
            throw new ServletException(e);
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            log.error("Exception: " + sw.toString());
        }

    }

    @SuppressWarnings("unchecked")
	private void debugRequest(String methodName, HttpServletRequest req) {
        log.debug(String.format("Request: %s %s", methodName, req.getRequestURI()));
        Enumeration e = req.getHeaderNames();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            log.debug(String.format("%s: %s", s, req.getHeader(s)));
        }
        e = req.getAttributeNames();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            log.debug("Attribute: " + s + " " + req.getAttribute(s));
        }
        e = req.getParameterNames();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            log.debug("Parameter: " + s + " " + req.getParameter(s));
        }
//        try {
//			BufferedReader reader = req.getReader();
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				sb.append(line).append('\n');
//			}
//			String requestBody = sb.toString();
//			if (requestBody.length() != 0) {
//				log.debug("Request body:\n" + requestBody);
//			}
//        } catch (Exception ex) {
//        	log.error(ex);
//        }
    }

}
