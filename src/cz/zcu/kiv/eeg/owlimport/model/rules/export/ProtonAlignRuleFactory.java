package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProtonAlignRuleFactory extends AbstractEmptyParamsRuleFactory {
	@Override
	public Class getCreatedClass() {
		return ProtonAlignRule.class;
	}

	@Override
	public AbstractRule createRule(String title) {
		return new ProtonAlignRule(title);
	}


	@Override
	public String toString() {
		return "Align Root Classes to Proton Ontology";
	}
}
