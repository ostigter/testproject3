package org.ozsoft.backup;

import org.junit.Test;

public class ProjectTest {
    
    @Test
    public void backup() {
        Project project = new Project("Project1");
        project.addSourceFolder("test/resources/folder1");
        project.addSourceFolder("test/resources/folder2");
        project.setDestinationFolder("target/test/backups");
        project.createBackup();
    }

}
