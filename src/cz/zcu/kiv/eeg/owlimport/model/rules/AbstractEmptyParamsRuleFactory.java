package cz.zcu.kiv.eeg.owlimport.model.rules;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Base factory implementation for rules with no parameters.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractEmptyParamsRuleFactory implements IRuleFactory {
	/**
	 * Creates a new instance of {@code EmptyRuleParams}.
	 * @param reader XML reader.
	 * @return Empty parameters set.
	 * @throws XMLStreamException never thrown.
	 */
	@Override
	public IRuleParams loadParams(XMLStreamReader reader) throws XMLStreamException {
		reader.nextTag();
		reader.require(XMLStreamConstants.END_ELEMENT, null, null);

		return new EmptyRuleParams();
	}
}
