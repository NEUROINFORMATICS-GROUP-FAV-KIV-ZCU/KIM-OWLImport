package cz.zcu.kiv.eeg.owlimport.model.sources;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Interface for exportable source location parameters.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public interface ISourceParams {
	/**
	 * Loads the source parameters from XML file.
	 * @param reader XML reader
	 * @throws XMLStreamException when the XML could not be loaded or parsed.
	 */
	public void loadXml(XMLStreamReader reader) throws XMLStreamException;
}
