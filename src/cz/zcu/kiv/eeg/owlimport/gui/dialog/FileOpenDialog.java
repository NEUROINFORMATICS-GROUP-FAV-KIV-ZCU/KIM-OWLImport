package cz.zcu.kiv.eeg.owlimport.gui.dialog;

import javax.swing.*;

/**
 * Open file dialog.
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileOpenDialog extends FileDialog {
	/**
	 * Initializes a default dialog with default current dir group.
	 */
	public FileOpenDialog() {
		super();
	}

	/**
	 * Initializes a dialog and restores current directory for specified form group.
	 * @param lastDirGroup Last current directory group.
	 */
	public FileOpenDialog(String lastDirGroup) {
		super(lastDirGroup);
	}

	/**
	 * Shows the open file dialog.
	 * @param root Component root.
	 * @return Dialog result.
	 */
	@Override
	public int showDialog(JComponent root) {
		return chooser.showOpenDialog(root);
	}
}
