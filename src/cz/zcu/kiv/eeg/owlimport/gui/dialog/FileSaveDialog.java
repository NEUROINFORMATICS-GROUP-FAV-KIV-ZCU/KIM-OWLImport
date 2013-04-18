package cz.zcu.kiv.eeg.owlimport.gui.dialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Save file dialog.
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSaveDialog extends FileDialog {
	/**
	 * Initializes a default dialog with default current dir group.
	 */
	public FileSaveDialog() {
		super();
	}

	/**
	 * Initializes a dialog and restores current directory for specified form group.
	 * @param lastDirGroup Last current directory group.
	 */
	public FileSaveDialog(String lastDirGroup) {
		super(lastDirGroup);
	}

	/**
	 * Shows save file dialog.
	 * @param root Component root.
	 * @return Dialog result.
	 */
	@Override
	public int showDialog(JComponent root) {
		return chooser.showSaveDialog(root);
	}

	/**
	 * Gets currently selected file and stores the current directory.
	 *
	 * Ugly side-effect, but it is the simplest way how to achieve directory saving.
	 *
	 * Additionally, if the selected filter is for single extension and user entered file name without the extension,
	 * extension is appended to the file name.
	 * @return Selected file.
	 */
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
