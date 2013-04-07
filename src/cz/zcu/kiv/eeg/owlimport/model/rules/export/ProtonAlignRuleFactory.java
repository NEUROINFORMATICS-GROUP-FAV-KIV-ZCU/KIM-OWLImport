package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.EmptyRuleParams;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProtonAlignRuleFactory implements IRuleFactory {
	@Override
	public Class getCreatedClass() {
		return ProtonAlignRule.class;
	}

	@Override
	public AbstractRule createRule(String title) {
		return new ProtonAlignRule(title);
	}

	@Override
	public IRuleParams loadParams(XMLStreamReader reader) throws XMLStreamException {
		return new EmptyRuleParams();
	}


	@Override
	public String toString() {
		return "Align Root Classes to Proton Ontology";
	}
}
