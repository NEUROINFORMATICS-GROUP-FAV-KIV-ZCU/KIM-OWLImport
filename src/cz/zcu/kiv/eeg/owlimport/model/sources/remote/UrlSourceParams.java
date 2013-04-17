package cz.zcu.kiv.eeg.owlimport.model.sources.remote;

import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;
import cz.zcu.kiv.eeg.owlimport.project.XmlReaderUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Parameters for remote ontology source.
 * @author Jan Smitka <jan@smitka.org>
 */
public class UrlSourceParams implements ISourceParams {
	public static final String EL_URL = "url";

	/** Remote URL. */
	private String url;

	/**
	 * Gets the remote URL.
	 * @return String containing URL.
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * Sets the remote URL. The URL has to be fully-qualified.
	 * @param url New URL string.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Loads the parameters from XML file.
	 * @param reader XML reader
	 * @throws XMLStreamException when the parameters cannot be loaded.
	 */
	@Override
	public void loadXml(XMLStreamReader reader) throws XMLStreamException {
		while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
			if (reader.getName().getLocalPart().equals(EL_URL)) {
				url = XmlReaderUtils.loadElementText(reader);
			} else {
				throw XmlReaderUtils.unexpectedElementException(reader, EL_URL);
			}
		}
	}
}
