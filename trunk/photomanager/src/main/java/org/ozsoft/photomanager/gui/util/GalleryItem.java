package org.ozsoft.photomanager.gui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * Base class for gallery items.
 * 
 * @author Oscar Stigter
 * 
 * @param <T>
 *            The item type.
 */
public abstract class GalleryItem<T extends GalleryItem<?>> extends JPanel implements MouseListener {

    private static final long serialVersionUID = 8225825322087633479L;

    private final GalleryPanel<T> parent;

    private boolean isSelected = false;

    /**
     * Constructor.
     * 
     * @param parent
     *            The gallery panel.
     */
    public GalleryItem(GalleryPanel<T> parent) {
        this.parent = parent;
        addMouseListener(this);
    }

    /**
     * Indicates whether this item is currently selected.
     * 
     * @return <code>true</code> if selected, otherwise <code>false</code>.
     */
    public final boolean isSelected() {
        return isSelected;
    }

    /**
     * Controls whether this item is currently selected.
     * 
     * @param isSelected
     *            <code>true</code> for selected, otherwise <code>false</code>.
     */
    public final void setSelected(boolean isSelected) {
        if (isSelected) {
            if (!this.isSelected) {
                this.isSelected = true;
                doSelected();
            }
        } else {
            if (this.isSelected) {
                this.isSelected = false;
                doUnselected();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!isSelected) {
                parent.itemSelected(this);
            }
            if (e.getClickCount() == 2) {
                parent.itemOpened(this);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not implemented.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not implemented.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Not implemented.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Not implemented.
    }

    /**
     * Handles the selection of this item.
     */
    protected abstract void doSelected();

    /**
     * Handles the unselection of this item.
     */
    protected abstract void doUnselected();

}
