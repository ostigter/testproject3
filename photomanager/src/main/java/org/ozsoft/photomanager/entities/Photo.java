package org.ozsoft.photomanager.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Photo extends BaseEntity {

    private static final long serialVersionUID = -300563798008125626L;
    
    @Basic
    private int width;
    
    @Basic
    private int height;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Basic
    private String fileType;
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
