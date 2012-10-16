package org.ozsoft.photomanager.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class GalleryLayout implements LayoutManager {
    
    @Override
    public void layoutContainer(Container container) {
        container.setLocation(0, 0);
        int maxWidth = container.getWidth();
        int x = 0;
        int y = 0;
        for (Component component : container.getComponents()) {
            Dimension size = component.getPreferredSize();
            int width = (int) Math.floor(size.getWidth());
            int height = (int) Math.floor(size.getHeight());
            if ((x + width) > maxWidth) {
                x = 0;
                y += height;
                component.setBounds(x, y, width, height);
            } else {
                component.setBounds(x, y, width, height);
                x += width;
            }
        }
    }

    @Override
    public void addLayoutComponent(String directions, Component component) {
        // Not implemented.
    }

    @Override
    public void removeLayoutComponent(Component component) {
        // Not implemented.
    }

    @Override
    public Dimension preferredLayoutSize(Container container) {
        return new Dimension(100, 100);
    }

    @Override
    public Dimension minimumLayoutSize(Container container) {
        // Not implemented.
        return new Dimension(100, 100);
    }

}
