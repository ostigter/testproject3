/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.webdav;

import java.io.File;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;

import net.sf.webdav.exceptions.WebdavException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Servlet which provides support for WebDAV level 2.
 * 
 * the original class is org.apache.catalina.servlets.WebdavServlet by Remy
 * Maucherat, which was heavily changed
 * 
 * @author Remy Maucherat
 */

public class WebdavServlet extends WebDavServletBean {

    private static final String ROOTPATH_PARAMETER = "rootpath";

    public void init() throws ServletException {
        // Parameters from web.xml
        String clazzName = getServletConfig().getInitParameter(
                "ResourceHandlerImplementation");
        if (clazzName == null || clazzName.equals("")) {
            clazzName = LocalFileSystemStore.class.getName();
        }

        File root = getFileRoot();
        WebdavStore webdavStore = constructStore(clazzName, root);
        boolean lazyFolderCreationOnPut = true;
        String dftIndexFile = "";
        String insteadOf404 = "";
        int noContentLengthHeader = 0;

        super.init(webdavStore, dftIndexFile, insteadOf404,
                noContentLengthHeader, lazyFolderCreationOnPut);
    }

    protected WebdavStore constructStore(String clazzName, File root) {
        WebdavStore webdavStore;
        try {
            Class clazz = WebdavServlet.class.getClassLoader().loadClass(
                    clazzName);

            Constructor ctor = clazz.getConstructor(new Class[] { File.class });

            webdavStore = (WebdavStore) ctor.newInstance(new Object[] { root });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("some problem making store component", e);
        }
        return webdavStore;
    }

    private File getFileRoot() {
        String rootPath = "data";
        if (rootPath == null) {
            throw new WebdavException("missing parameter: "
                    + ROOTPATH_PARAMETER);
        }
        if (rootPath.equals("*WAR-FILE-ROOT*")) {
            String file = LocalFileSystemStore.class.getProtectionDomain()
            .getCodeSource().getLocation().getFile().replace('\\', '/');
            if (file.charAt(0) == '/'
                && System.getProperty("os.name").indexOf("Windows") != -1) {
                file = file.substring(1, file.length());
            }

            int ix = file.indexOf("/WEB-INF/");
            if (ix != -1) {
                rootPath = file.substring(0, ix).replace('/',
                        File.separatorChar);
            } else {
                throw new WebdavException(
                        "Could not determine root of war file. Can't extract from path '"
                        + file + "' for this web container");
            }
        }
        return new File(rootPath);
    }

}
