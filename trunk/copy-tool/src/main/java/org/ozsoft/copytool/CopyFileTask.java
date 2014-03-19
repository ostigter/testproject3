package org.ozsoft.copytool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.ozsoft.copytool.task.Task;
import org.ozsoft.copytool.task.TaskListener;

public class CopyFileTask extends Task {

    private static final int BUFFER_SIZE = 4096;

    private final File sourceFile;

    private final File destFile;

    public CopyFileTask(File sourceFile, File destFile, TaskListener listener) {
        super(listener);
        this.sourceFile = sourceFile;
        this.destFile = destFile;
    }

    @Override
    public void run() {
        long totalBytes = sourceFile.length();
        long copiedBytes = 0L;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = 0;
        try {
            is = new BufferedInputStream(new FileInputStream(sourceFile));
            os = new BufferedOutputStream(new FileOutputStream(destFile));
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
                copiedBytes += length;
                setProgress(copiedBytes, totalBytes);
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                }
            }
            os.close();
            is.close();
            destFile.setLastModified(sourceFile.lastModified());
            complete();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while copying file '%s' to '%s'", sourceFile, destFile), e);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
    }
}
