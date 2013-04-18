package cz.zcu.kiv.eeg.owlimport.gui.dialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract dialog wrapper.
 *
 * Implements ability to restore last selected directory for dialogs specified by their group. This enables user to
 * easily import files from one directory and export rules and visibility to another one without having to manually
 * searching for both directories.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class FileDialog {
	public static final int CONFIRM_OPTION = JFileChooser.APPROVE_OPTION;
	/** Map of current dirs. */
	private final static Map<String, File> currentDirMap = new HashMap<>();
	/** Dialog's current dir group. */
	private String dirGroup;

	protected JFileChooser chooser;

	/**
	 * Initializes a default dialog with default current dir group.
	 */
	public FileDialog() {
		initializeChooser();
		restoreCurrentDir("");
	}

	/**
	 * Initializes a dialog and restores current directory for specified form group.
	 * @param lastDirGroup Last current directory group.
	 */
	public FileDialog(String lastDirGroup) {
		initializeChooser();
		restoreCurrentDir(lastDirGroup);
	}

	/**
	 * Initializes JFileChooser.
	 */
	private void initializeChooser() {
		chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
	}

	/**
	 * Restores current directory from specified group.
	 * @param lastDirGroup Group name.
	 */
	private void restoreCurrentDir(String lastDirGroup) {
		dirGroup = lastDirGroup;
		if (currentDirMap.containsKey(lastDirGroup)) {
			chooser.setCurrentDirectory(currentDirMap.get(lastDirGroup));
		} else {
			chooser.setCurrentDirectory(new File("."));
		}
	}

	/**
	 * Adds a filter for common ontologies and sets it as default.
	 */
	public void addOntologyExtensionFilter() {
		addExtensionFilter("Ontology Files", new String[]{
				"owl", "xml", "ttl", "nt", "n3"
		}, true);
	}

	/**
	 * Adds a filter for specified extensions.
	 * @param title Title of the filter.
	 * @param extensions List of allowed extensions.
	 */
	public void addExtensionFilter(String title, String[] extensions) {
		addExtensionFilter(title, extensions, false);
	}

	/**
	 * Adds a filter for specified extensions and sets it as default.
	 * @param title Title of the filter.
	 * @param extensions List of allowed extensions.
	 */
	public void addExtensionFilter(String title, String[] extensions, boolean isDefault) {
		FileFilter filter = new ExtensionFilter(title, extensions);
		chooser.addChoosableFileFilter(filter);
		if (isDefault) {
			chooser.setFileFilter(filter);
		}
	}

	/**
	 * Gets currently selected file and stores the current directory.
	 *
	 * Ugly side-effect, but it is the simplest way how to achieve directory saving.
	 * @return Selected file.
	 */
	public File getSelectedFile() {
		currentDirMap.put(dirGroup, chooser.getSelectedFile());
		return chooser.getSelectedFile();
	}

	/**
	 * Shows the dialog. Dialog type should be determined by the implementing class.
	 * @param root Component root.
	 * @return Dialog result.
	 */
	public abstract int showDialog(JComponent root);

}
