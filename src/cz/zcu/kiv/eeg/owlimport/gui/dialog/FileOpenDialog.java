package cz.zcu.kiv.eeg.owlimport.gui.dialog;

import javax.swing.*;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileOpenDialog extends FileDialog {
	public FileOpenDialog() {
		super();
	}

	public FileOpenDialog(String lastDirGroup) {
		super(lastDirGroup);
	}

	@Override
	public int showDialog(JComponent root) {
		return chooser.showOpenDialog(root);
	}
}
