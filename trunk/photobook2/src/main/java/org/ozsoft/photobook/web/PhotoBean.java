package org.ozsoft.photobook.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.ozsoft.photobook.entities.Photo;
import org.ozsoft.photobook.repositories.PhotoRepository;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class PhotoBean implements Serializable {
    
	private static final long serialVersionUID = -7768597734064601558L;

	private static final Logger LOGGER = Logger.getLogger(PhotoBean.class);
    
    @ManagedProperty(value = "#{photoRepository}")
    private PhotoRepository photoRepository;
    
    public void setPhotoRepository(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
    
    public List<Photo> getPhotos() {
        return photoRepository.findAll();
    }

    public StreamedContent getPhotoContent(long id) {
        StreamedContent content = null;
        Photo photo = photoRepository.retrieveById(id);
        if (photo != null) {
            content = new DefaultStreamedContent(new ByteArrayInputStream(photo.getContent()), "image/jpeg");
        }
        return content;
    }
    
    public void handleFileUpload(FileUploadEvent e) {
    	System.out.println("*** handleFileUpload");
        UploadedFile file = e.getFile();
        String filename = file.getFileName();
        Photo photo = new Photo();
        InputStream is = null;        
        try {
            is = new BufferedInputStream(file.getInputstream());
            byte[] content = IOUtils.toByteArray(is);
            is.close();
            photo.setContent(content);
            photoRepository.store(photo);
            LOGGER.debug(String.format("Photo uploaded: '%s' (%d bytes)", file.getFileName(), file.getSize()));
        } catch (IOException ex) {
            LOGGER.error(String.format("Could not set photo content from file '%s'", filename), ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
