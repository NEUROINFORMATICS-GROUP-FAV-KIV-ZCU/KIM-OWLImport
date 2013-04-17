package cz.zcu.kiv.eeg.owlimport.gui.dialog;


import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Extension filter for JFileChooser. Filters files by their extensions. Directories are also included in the list.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExtensionFilter extends FileFilter {
	public static final String EXT_SEPARATOR = ".";

	private final String title;

	private final Set<String> extensionList;


	public ExtensionFilter(String filterTitle, String[] extensions) {
		title = filterTitle;
		extensionList = new HashSet<>();
		Collections.addAll(extensionList, extensions);
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String ext = getExtension(f);

		if (ext == null) {
			return false;
		} else {
			return extensionList.contains(ext);
		}
	}

	@Override
	public String getDescription() {
		StringBuilder str = new StringBuilder(title);

		if (extensionList.size() > 0) {
			str.append(" (");
			boolean first = true;
			for (String s : extensionList) {
				if (!first) {
					str.append(", ");
				} else {
					first = false;
				}
				str.append("*.");
				str.append(s);
			}
			str.append(")");
		}

		return str.toString();
	}


	public boolean hasSingleExtension() {
		return extensionList.size() == 1;
	}

	public String getSingleExtension() {
		String ext = null;
		Iterator<String> itr = extensionList.iterator();
		if (itr.hasNext()) {
			ext = itr.next();
		}
		return ext;
	}


	public File fixFileExtension(File file) {
		if (hasSingleExtension() && getExtension(file) == null) {
			String ext = getSingleExtension();
			file = new File(String.format("%s%s%s", file.getPath(), EXT_SEPARATOR, ext));
		}

		return file;
	}


	private String getExtension(File file) {
		String name = file.getName();
		String ext = null;

		int pos = name.lastIndexOf(EXT_SEPARATOR);
		if (pos > 0 && pos < name.length() - 1) {
			ext = name.substring(pos + 1).toLowerCase();
		}

		return ext;
	}
}
