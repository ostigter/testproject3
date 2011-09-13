package org.ozsoft.photobook.services;

import org.ozsoft.photobook.entities.Photo;

public interface PhotoService {
	
	long create(Photo photo);
	
	Photo retrieve(long id);
	
	void update(Photo photo);
	
	void delete(Photo photo);

}
