package cz.zcu.kiv.eeg.owlimport.model.rules;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public interface IRuleFactory {
	public Class getCreatedClass();

	public AbstractRule createRule(String title);

	public IRuleParams loadParams(XMLStreamReader reader) throws XMLStreamException;
}
