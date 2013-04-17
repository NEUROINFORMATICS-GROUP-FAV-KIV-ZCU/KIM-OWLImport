package cz.zcu.kiv.eeg.owlimport.model.sources.local;

import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;
import cz.zcu.kiv.eeg.owlimport.project.XmlReaderUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;

/**
 * Parameters collection for local file sources.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSourceParams implements ISourceParams {
	public static final String EL_FILE = "file";

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


	@Override
	public void loadXml(XMLStreamReader reader) throws XMLStreamException {
		while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
			if (reader.getName().getLocalPart().equals(EL_FILE)) {
				file = new File(XmlReaderUtils.loadElementText(reader));
			} else {
				throw XmlReaderUtils.unexpectedElementException(reader, EL_FILE);
			}
		}
	}
}
