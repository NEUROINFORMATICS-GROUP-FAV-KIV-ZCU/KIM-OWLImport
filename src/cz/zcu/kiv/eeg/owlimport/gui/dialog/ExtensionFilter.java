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


	/**
	 * Creates a new filter with given title, filtering specified extensions.
	 * @param filterTitle Title of filter.
	 * @param extensions List of allowed extensions. Extensions has to be in lowercase.
	 */
	public ExtensionFilter(String filterTitle, String[] extensions) {
		title = filterTitle;
		extensionList = new HashSet<>();
		Collections.addAll(extensionList, extensions);
	}

	/**
	 * Checks if the given file is a directory or a file with corresponding extension.
	 * @param f File.
	 * @return {@code true} if the file matches the conditions.
	 */
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

	/**
	 * Formats the filter label - joins title and list of extensions.
	 * @return Label.
	 */
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


	/**
	 * Checks if the filter filters only one extension.
	 * @return {@code true} if the filter has only one extension specified.
	 */
	public boolean hasSingleExtension() {
		return extensionList.size() == 1;
	}

	/**
	 * Gets the first extension of the filter. Intended only for filters with single extension as the first extension
	 * in hash set cannot be predicted unless there is only one item.
	 * @return
	 */
	public String getSingleExtension() {
		String ext = null;
		Iterator<String> itr = extensionList.iterator();
		if (itr.hasNext()) {
			ext = itr.next();
		}
		return ext;
	}


	/**
	 * If the specified file has no extension and this filter contains only one extension, appends the filter's extension
	 * to original filename and returns a new instance of file. Otherwise the file is kept intact and returned.
	 * @param file File metadata.
	 * @return File with fixed filename.
	 */
	public File fixFileExtension(File file) {
		if (hasSingleExtension() && getExtension(file) == null) {
			String ext = getSingleExtension();
			file = new File(String.format("%s%s%s", file.getPath(), EXT_SEPARATOR, ext));
		}

		return file;
	}


	/**
	 * Extracts the file extension.
	 * @param file File.
	 * @return File extension or {@code null} if the extension cannot be determined.
	 */
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
