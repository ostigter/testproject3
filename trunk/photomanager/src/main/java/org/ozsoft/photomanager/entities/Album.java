package org.ozsoft.photomanager.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Album extends BaseEntity {

    private static final long serialVersionUID = 1092771597760283621L;

    @Basic
    private String name;
    
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @OneToMany
    private List<Photo> photos;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Photo coverPhoto;
    
    public Album() {
        setPhotos(new ArrayList<Photo>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Photo getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(Photo coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
    
}
