package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class TrustedSourceRuleFactory extends AbstractEmptyParamsRuleFactory {
	@Override
	public Class getCreatedClass() {
		return TrustedSourceRule.class;
	}

	@Override
	public AbstractRule createRule(String title) {
		return new TrustedSourceRule(title);
	}

	@Override
	public String toString() {
		return "Generate Trusted Source for All Entities";
	}
}
