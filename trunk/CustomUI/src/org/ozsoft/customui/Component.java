package org.ozsoft.customui;

import java.awt.Graphics2D;

public interface Component {

    Component getParent();

    int getWidth();

    int getHeight();

    int getX();

    int getY();

    void doLayout(Graphics2D g);

    void paint(Graphics2D g);

}
