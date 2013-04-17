package cz.zcu.kiv.eeg.owlimport.gui.dialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSaveDialog extends FileDialog {
	public FileSaveDialog() {
		super();
	}

	public FileSaveDialog(String lastDirGroup) {
		super(lastDirGroup);
	}

	@Override
	public int showDialog(JComponent root) {
		return chooser.showSaveDialog(root);
	}


	@Override
	public File getSelectedFile() {
		File file = super.getSelectedFile();
		FileFilter filter = chooser.getFileFilter();
		if (filter instanceof ExtensionFilter) {
			file = ((ExtensionFilter) filter).fixFileExtension(file);
		}

		return file;
	}
}
