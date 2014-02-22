package org.ozsoft.photomanager.gui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public abstract class GalleryItem extends JPanel implements MouseListener {

	private static final long serialVersionUID = 8225825322087633479L;

	private final GalleryListener listener;

	private boolean isSelected = false;

	public GalleryItem(GalleryListener listener) {
		this.listener = listener;
		addMouseListener(this);
	}

	public final boolean isSelected() {
		return isSelected;
	}

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
				listener.itemSelected(this);
			}
			if (e.getClickCount() == 2) {
				listener.itemOpened(this);
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
	 * Handles the selected of this item.
	 */
	protected abstract void doSelected();

	/**
	 * Handles the unselected of this item.
	 */
	protected abstract void doUnselected();

}
