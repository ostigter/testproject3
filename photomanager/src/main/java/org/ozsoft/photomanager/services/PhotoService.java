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

/**
 * Service managing the albums and photos.
 * 
 * @author Oscar Stigter
 */
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
            LOGGER.error("Could not store album in database", e);
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
            LOGGER.error("Could not store photo in database", e);
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
                    LOGGER.error(String.format("Could not retrieve content of photo [%d] from database", id));
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

        LOGGER.debug(String.format("Added [%d] photos to album [%s]", photos.size(), album));

        storeAlbum(album);
    }

    /**
     * Uploads a photo from file and stores it in the database.
     * 
     * @param file
     *            The photo file.
     * 
     * @return The stored photo.
     */
    private Photo uploadPhoto(File file) {
        String path = file.getAbsolutePath();
        LOGGER.debug(String.format("Store photo from file '%s'", path));
        InputStream is = null;
        Photo photo = null;
        try {
            LOGGER.trace("Extracting EXIF metadata");
            int width = -1;
            int height = -1;
            Date date = null;
            String extension = null;
            int p = file.getName().lastIndexOf('.');
            if (p >= 0) {
                extension = file.getName().substring(p + 1).toLowerCase();
            } else {
                LOGGER.warn(String.format("Storing image file without extension: []", path));
                extension = "jpg";
            }
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory dir = metadata.getDirectory(JpegDirectory.class);
            if (dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_WIDTH)) {
                width = dir.getInt(JpegDirectory.TAG_JPEG_IMAGE_WIDTH);
                LOGGER.debug("### Width: " + width);
            } else {
                LOGGER.warn(String.format("No width set in EXIF metadata of photo [%s]", path));
            }
            if (dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT)) {
                height = dir.getInt(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT);
                LOGGER.debug("### Height: " + height);
            } else {
                LOGGER.warn(String.format("No height set in EXIF metadata of photo [%s]", path));
            }
            dir = metadata.getDirectory(ExifSubIFDDirectory.class);
            if (dir.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                date = dir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            } else {
                date = new Date(file.lastModified());
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

            // Store content in database.
            storeContent(file, photo);

            // Create and store thumbnail in database.
            storeThumbnail(photo);

        } catch (MetadataException e) {
            LOGGER.error(String.format("Could not read metadata from photo [%s]", path), e);

        } catch (ImageProcessingException e) {
            LOGGER.error(String.format("Failed to parse image from file '%s'", file), e);

        } catch (IOException e) {
            LOGGER.error(String.format("Failed to read file '%s'", file), e);

        } finally {
            IOUtils.closeQuietly(is);
        }

        return photo;
    }

    private void storeThumbnail(Photo photo) {
        long photoId = photo.getId();
        LOGGER.trace(String.format("Creating thumbnail of photo [%d]", photoId));
        try {
            BufferedImage image = ImageIO.read(retrieveContent(photoId));
            int orgWidth = image.getWidth();
            int orgHeight = image.getHeight();
            int width = (orgWidth > orgHeight) ? THUMBNAIL_WIDTH
                    : (int) (THUMBNAIL_WIDTH * ((double) orgWidth / (double) orgHeight));
            int height = (orgHeight > orgWidth) ? THUMBNAIL_WIDTH
                    : (int) (THUMBNAIL_WIDTH * ((double) orgHeight / (double) orgWidth));
            BufferedImage thumbnail = new BufferedImage(width, height, image.getType());
            Graphics2D g = thumbnail.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, width, height, 0, 0, orgWidth, orgHeight, null);
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);
            photo.setThumbnail(baos.toByteArray());
            baos.close();
            LOGGER.debug(String.format("Store thumbnail of photo [%d]", photoId));
            storePhoto(photo);
        } catch (IOException e) {
            LOGGER.error(String.format("Could not create thumbnail of photo [%d]", photoId), e);
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
