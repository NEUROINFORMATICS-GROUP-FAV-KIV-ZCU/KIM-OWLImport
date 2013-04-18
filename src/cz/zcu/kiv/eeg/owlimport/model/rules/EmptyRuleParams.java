package cz.zcu.kiv.eeg.owlimport.model.rules;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Parameters object for rules with no params.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class EmptyRuleParams implements IRuleParams {
	/**
	 * No parameters to save, state of XML writer is not changed.
	 * @param writer XML writer.
	 * @throws XMLStreamException never thrown.
	 */
	@Override
	public void saveXml(XMLStreamWriter writer) throws XMLStreamException {
		// empty
	}

	/**
	 * No parameters to load, state of XML reader is not changed.
	 * @param reader XML reader.
	 * @throws XMLStreamException never thrown.
	 */
	@Override
	public void loadXml(XMLStreamReader reader) throws XMLStreamException {
		// empty
	}
}
