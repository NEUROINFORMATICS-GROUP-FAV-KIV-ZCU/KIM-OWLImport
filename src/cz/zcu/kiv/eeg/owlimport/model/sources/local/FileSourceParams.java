package cz.zcu.kiv.eeg.owlimport.model.sources.local;

import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import java.io.File;

/**
 * Parameters collection for local file sources.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSourceParams implements ISourceParams {
	/** Locally stored file. */
	private File file;

	/**
	 * Gets the reference of the sources file.
	 * @return OWL file reference.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the reference to
	 * @param file Reference to file containing OWL data.
	 */
	public void setFile(File file) {
		this.file = file;
	}
}
