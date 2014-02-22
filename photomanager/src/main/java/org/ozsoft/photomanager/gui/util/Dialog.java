package org.ozsoft.photomanager.gui.util;

import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Abstract base class for Swing dialogs.
 * 
 * @author Oscar Stigter
 */
public abstract class Dialog {

	public static final int OK = 0;

	public static final int CANCEL = 1;

	protected final JFrame owner;

	protected final JDialog dialog;

	protected int result = CANCEL;

	public Dialog(String title, JFrame owner) {
		this.owner = owner;
		dialog = new JDialog(owner, title, true);
		dialog.setLayout(new GridBagLayout());
		initUI();
	}

	public int show() {
		dialog.setLocationRelativeTo(owner);
		dialog.setVisible(true);
		return result;
	}

	public void hide() {
		dialog.setVisible(false);
	}

	public int getResult() {
		return result;
	}

	protected abstract void initUI();

}
