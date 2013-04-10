package cz.zcu.kiv.eeg.owlimport.model.rules;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractEmptyParamsRuleFactory implements IRuleFactory {
	@Override
	public IRuleParams loadParams(XMLStreamReader reader) throws XMLStreamException {
		return new EmptyRuleParams();
	}
}
