package cz.zcu.kiv.eeg.owlimport.gui.dialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class FileDialog {
	public static final int CONFIRM_OPTION = JFileChooser.APPROVE_OPTION;


	private final static Map<String, File> workdirMap = new HashMap<>();

	private String dirGroup;

	protected JFileChooser chooser;

	public FileDialog() {
		initializeChooser();
		restoreCurrentDir("");
	}

	public FileDialog(String lastDirGroup) {
		initializeChooser();
		restoreCurrentDir(lastDirGroup);
	}

	private void initializeChooser() {
		chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);


	}

	private void restoreCurrentDir(String lastDirGroup) {
		dirGroup = lastDirGroup;
		if (workdirMap.containsKey(lastDirGroup)) {
			chooser.setCurrentDirectory(workdirMap.get(lastDirGroup));
		} else {
			chooser.setCurrentDirectory(new File("."));
		}
	}

	public void addOntologyExtensionFilter() {
		addExtensionFilter("Ontology Files", new String[]{
				"owl", "xml", "ttl", "nt", "n3"
		}, true);
	}

	public void addExtensionFilter(String title, String[] extensions) {
		addExtensionFilter(title, extensions, false);
	}

	public void addExtensionFilter(String title, String[] extensions, boolean isDefault) {
		FileFilter filter = new ExtensionFilter(title, extensions);
		chooser.addChoosableFileFilter(filter);
		if (isDefault) {
			chooser.setFileFilter(filter);
		}
	}

	public File getSelectedFile() {
		workdirMap.put(dirGroup, chooser.getSelectedFile());
		return chooser.getSelectedFile();
	}


	public abstract int showDialog(JComponent root);

}
