package org.ozsoft.photobook.repositories;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.ozsoft.photobook.entities.Photo;

@ManagedBean(eager = true)
@ApplicationScoped
public class PhotoRepository extends Repository<Photo> {

    public PhotoRepository() {
        super(Photo.class);
    }

}
