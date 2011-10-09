package org.ozsoft.photobook.web;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.ozsoft.photobook.entities.Photo;
import org.ozsoft.photobook.services.PhotoService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class PhotoBean implements Serializable {
    
    private static final long serialVersionUID = 8048420881379979384L;

    private static final Logger LOG = Logger.getLogger(PhotoBean.class);
    
    @EJB
    private PhotoService photoService;
    
    public List<Photo> getPhotos() {
        return photoService.findAll();
    }
    
    public StreamedContent getPhotoContent(long id) {
        StreamedContent content = null;
        try {
            OutputStream os = new FileOutputStream("D:/Temp/out.jpg");
            IOUtils.copy(photoService.getContent(id), os);
            os.close();
            content = new DefaultStreamedContent(photoService.getContent(id));
        } catch (SQLException e) {
            LOG.error("Could not get content of photo with ID " + id, e);
        } catch (IOException e) {
            LOG.error("Could not stream content of photo with ID " + id, e);
        }
        return content;
    }
    
    public void handleFileUpload(FileUploadEvent e) {
        UploadedFile file = e.getFile();
        String filename = file.getFileName();
        Photo photo = new Photo();
        photo.setName(filename);
        photoService.store(photo);
        InputStream is = null;        
        try {
            is = new BufferedInputStream(file.getInputstream());
            photoService.setContent(photo.getId(), is);
            is.close();
            LOG.debug(String.format("Photo uploaded: '%s' (%d bytes)", file.getFileName(), file.getSize()));
        } catch (IOException ex) {
            LOG.error(String.format("Could not set photo content from file '%s'", filename), ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
