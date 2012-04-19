package org.ozsoft.photomanager.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.ozsoft.photomanager.entities.Photo;

public class ThumbnailPanel extends JPanel {

    private static final long serialVersionUID = -4359094295804999630L;

    private final List<Photo> photos;

//    private final JScrollPane scrollPane;

    public ThumbnailPanel() {
        setBorder(new LineBorder(Color.BLACK));
        
        photos = new ArrayList<Photo>();

//        scrollPane = new JScrollPane(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        add(scrollPane);

        int width = getWidth();
        int height = getHeight();
        System.out.println("width = " + width);
        System.out.println("height = " + height);
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

}
