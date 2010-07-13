package org.ozsoft.backup;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BackupTool {
    
    private static final File PROJECTS_FILE = new File("projects.dat");
    
    private final Map<String, Project> projects;
    
    public BackupTool() {
        projects = new TreeMap<String, Project>();
        readProjects();
        writeProjects();
    }

    public static void main(String[] args) {
        new BackupTool();
    }

    private void readProjects() {
    }
    
    private void writeProjects() {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(PROJECTS_FILE));
            dos.writeInt(projects.size());
            for (Project project : projects.values()) {
                dos.writeUTF(project.getName());
                Set<String> sourceFolders = project.getSourceFolders();
                dos.writeInt(sourceFolders.size());
                for (String folder : sourceFolders) {
                    dos.writeUTF(folder);
                }
                dos.writeUTF(project.getDestinationFolder());
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    // Best effort; ignore.
                }
            }
        }
        
    }
    
}
