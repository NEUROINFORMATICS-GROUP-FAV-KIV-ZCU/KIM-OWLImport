package cz.zcu.kiv.eeg.owlimport.model.rules;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class EmptyRuleParams implements IRuleParams {
	@Override
	public void saveXml(XMLStreamWriter writer) throws XMLStreamException {
		// empty
	}

	@Override
	public void loadXml(XMLStreamReader reader) throws XMLStreamException {
		// empty
	}
}
