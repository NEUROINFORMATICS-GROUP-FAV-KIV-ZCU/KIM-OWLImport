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
	public void saveXml(XMLStreamWriter writer) throws XMLStreamException;

	public void loadXml(XMLStreamReader reader) throws XMLStreamException;
}
