package cz.zcu.kiv.eeg.owlimport.model.rules;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Interface for exportable rule parameters.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public interface IRuleParams {
	/**
	 * Saves rule parameters to XML file.
	 * @param writer XML writer.
	 * @throws XMLStreamException when the parameters cannot be written.
	 */
	public void saveXml(XMLStreamWriter writer) throws XMLStreamException;

	/**
	 * Loads rule parameters from XML file.
	 * @param reader XML reader.
	 * @throws XMLStreamException when the parameters cannot be read or XML is malformed.
	 */
	public void loadXml(XMLStreamReader reader) throws XMLStreamException;
}
