package org.ozsoft.photomanager.services;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.entities.Photo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.drew.metadata.jpeg.JpegDirectory;

public class PhotoService {

    /** Thumbnail width in pixels. */
    private static final int THUMBNAIL_WIDTH = 150;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(PhotoService.class);

    /** Entitiy manager. */
    private final EntityManager em;

    /**
     * Constructor.
     */
    public PhotoService() {
        em = PersistenceService.getEntityManager();
    }

    /**
     * Stores an album.
     * 
     * @param album
     *            The album.
     */
    public void storeAlbum(Album album) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (album.getId() == null) {
                em.persist(album);
            } else {
                em.merge(album);
            }
            tx.commit();
            LOGGER.debug("Stored album with ID " + album.getId());
        } catch (PersistenceException e) {
            tx.rollback();
            LOGGER.error("Error storing album", e);
        }
    }

    public List<Album> listAlbums() {
        return em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
    }

    /**
     * Retrieve an album by its ID.
     * 
     * @param id
     *            The album ID.
     * 
     * @return The album if found, otherwise null.
     */
    public Album retrieveAlbum(long id) {
        return em.find(Album.class, id);
    }

    /**
     * Stores a photo.
     * 
     * @param photo
     *            The photo.
     */
    public void storePhoto(Photo photo) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (photo.getId() == null) {
                em.persist(photo);
            } else {
                em.merge(photo);
            }
            tx.commit();
            LOGGER.debug("Stored photo with ID " + photo.getId());
        } catch (PersistenceException e) {
            tx.rollback();
            LOGGER.error("Error storing photo", e);
        }
    }

    /**
     * Retrieves a photo by its ID.
     * 
     * @param id
     *            The photo ID.
     * 
     * @return The photo if found, otherwise null.
     */
    public Photo retrievePhoto(long id) {
        return em.find(Photo.class, id);
    }

    public byte[] retrieveThumbnail(long id) {
        byte[] thumbnail = null;
        Photo photo = retrievePhoto(id);
        if (photo != null) {
            thumbnail = photo.getThumbnail();
        }
        return thumbnail;
    }

    public InputStream retrieveContent(long id) {
        InputStream is = null;
        Photo photo = retrievePhoto(id);
        if (photo != null) {
            Session session = PersistenceService.getSession();
            session.refresh(photo);
            Blob blob = photo.getContent();
            if (blob != null) {
                try {
                    is = blob.getBinaryStream();
                } catch (SQLException e) {
                    LOGGER.error(String.format("Could not retrieve content of photo [%d]\n", id));
                }
            }
        }
        return is;
    }

    public void uploadPhotos(Album album, File[] files) {
        List<Photo> photos = album.getPhotos();
        for (File file : files) {
            Photo photo = uploadPhoto(file);
            photos.add(photo);
        }

        // In no album cover photo set, use first photo.
        if (album.getCoverPhoto() == null && !photos.isEmpty()) {
            album.setCoverPhoto(photos.get(0));
        }

        storeAlbum(album);
    }

    private Photo uploadPhoto(File sourceFile) {
        LOGGER.debug(String.format("Store photo from file '%s'", sourceFile.getAbsolutePath()));
        InputStream is = null;
        Photo photo = null;
        try {
            try {
                LOGGER.debug("Extracting EXIF metadata");
                int width = -1;
                int height = -1;
                Date date = null;
                String extension = null;
                int p = sourceFile.getName().lastIndexOf('.');
                if (p >= 0) {
                    extension = sourceFile.getName().substring(p + 1).toLowerCase();
                } else {
                    LOGGER.warn("Storing image file without extension: " + sourceFile.getAbsolutePath());
                    extension = "jpg";
                }
                Metadata metadata = ImageMetadataReader.readMetadata(sourceFile);
                Directory dir = metadata.getDirectory(JpegDirectory.class);
                try {
                    if (dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_WIDTH)) {
                        width = dir.getInt(JpegDirectory.TAG_JPEG_IMAGE_WIDTH);
                        LOGGER.debug("Width: " + width);
                    } else {
                        LOGGER.warn("No width set in EXIF metadata");
                    }
                    if (dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT)) {
                        height = dir.getInt(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT);
                        LOGGER.debug("Height: " + height);
                    } else {
                        LOGGER.warn("No height set in EXIF metadata");
                    }
                } catch (MetadataException e1) {
                    LOGGER.error(e1);
                }
                dir = metadata.getDirectory(ExifSubIFDDirectory.class);
                try {
                    if (dir.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                        date = dir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    } else {
                        date = new Date(sourceFile.lastModified());
                    }
                    LOGGER.debug("Date: " + date);
                    if (dir.containsTag(ExifThumbnailDirectory.TAG_ORIENTATION)) {
                        int orientation = dir.getInt(ExifThumbnailDirectory.TAG_ORIENTATION);
                        LOGGER.debug("Orientation: " + orientation);
                    }

                    // Store metadata in database.
                    photo = new Photo();
                    photo.setWidth(width);
                    photo.setHeight(height);
                    photo.setDate(date);
                    photo.setFileType(extension);
                    storePhoto(photo);

                    storeThumbnail(sourceFile, photo);

                    storeContent(sourceFile, photo);

                } catch (MetadataException e1) {
                    LOGGER.error(e1);
                }
            } catch (ImageProcessingException e1) {
                LOGGER.error(e1);
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Failed to upload photo from file '%s'", sourceFile), e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return photo;
    }

    private void storeThumbnail(File photoFile, Photo photo) {
        LOGGER.debug("Creating thumbnail of photo " + photo.getId());
        try {
            BufferedImage image = ImageIO.read(photoFile);
            int orgWidth = image.getWidth();
            int orgHeight = image.getHeight();
            int width = (orgWidth > orgHeight) ? THUMBNAIL_WIDTH : (int) (THUMBNAIL_WIDTH * ((double) orgWidth / (double) orgHeight));
            int height = (orgHeight > orgWidth) ? THUMBNAIL_WIDTH : (int) (THUMBNAIL_WIDTH * ((double) orgHeight / (double) orgWidth));
            BufferedImage thumbnail = new BufferedImage((int) width, (int) height, image.getType());
            Graphics2D g = thumbnail.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, width, height, 0, 0, orgWidth, orgHeight, null);
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);
            photo.setThumbnail(baos.toByteArray());
            baos.close();
            LOGGER.debug("Storing thumbnail of photo " + photo.getId());
            storePhoto(photo);
        } catch (IOException e) {
            LOGGER.error("Could not create thumbnail of photo " + photoFile, e);
        }
    }

    private void storeContent(File file, Photo photo) {
        long id = photo.getId();
        LOGGER.debug("Storing content of photo " + id);
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            Session session = PersistenceService.getSession();
            session.refresh(photo);
            Blob blob = session.getLobHelper().createBlob(is, is.available());
            photo.setContent(blob);
            storePhoto(photo);
        } catch (IOException e) {
            LOGGER.error("Could not store content of photo " + id);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
