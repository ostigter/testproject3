package org.example.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class ZipTest {
    
    public static void main(String[] args) {
        recompressZipFile(new File("test_in.zip"),  new File("test_out.zip"));
        recompressZipFile(new File("test_out.zip"), new File("test_out2.zip"));
    }
    
    private static void recompressZipFile(File inFile, File outFile) {
        listZipFile(inFile);
        System.out.format("Recompressing file '%s':\n", inFile);
        ZipInputStream zis = null;
        ZipOutputStream zos = null;
        try {
            zis = new ZipInputStream(new FileInputStream(inFile));
            zos = new ZipOutputStream(new FileOutputStream(outFile));
            ZipEntry inEntry = null;
            while ((inEntry = zis.getNextEntry()) != null) {
                String name = inEntry.getName();
                long size = inEntry.getSize();
                System.out.format("  %s (%d bytes)\n", name, size);
                ZipEntry outEntry = new ZipEntry(name);
                outEntry.setMethod(inEntry.getMethod());
                outEntry.setComment(inEntry.getComment());
                outEntry.setTime(inEntry.getTime());
                zos.putNextEntry(outEntry);
                IOUtils.copy(zis, zos);
            }
            zos.close();
            zis.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(zis);
        }
    }
    
    private static void listZipFile(File file) {
        System.out.format("Listing file '%s':\n", file);
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry>  entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                long size = entry.getSize();
                System.out.format("  %s (%d bytes)\n", name, size);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        
    }

}
