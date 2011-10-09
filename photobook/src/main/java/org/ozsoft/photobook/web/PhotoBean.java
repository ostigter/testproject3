package org.ozsoft.photobook.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@RequestScoped
public class PhotoBean {
    
    private static final Logger LOG = Logger.getLogger(PhotoBean.class); 
    
    public void handleFileUpload(FileUploadEvent e) {
        UploadedFile file = e.getFile();
        String msg = String.format("File uploaded: '%s' (%d bytes)", file.getFileName(), file.getSize());
        System.out.println("*** Console: " + msg);
        LOG.info(msg);
    }

}
