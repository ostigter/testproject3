package org.ozsoft.photobook.repositories;

import org.ozsoft.photobook.entities.Photo;

public class PhotoRepository extends Repository<Photo> {

	protected PhotoRepository() {
		super(Photo.class);
	}

}
