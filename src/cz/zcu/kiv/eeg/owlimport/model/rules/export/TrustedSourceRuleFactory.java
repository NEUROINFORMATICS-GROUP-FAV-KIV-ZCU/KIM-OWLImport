package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * Factory for {@code TrustedSourceRule}.
 * @author Jan Smitka <jan@smitka.org>
 */
public class TrustedSourceRuleFactory extends AbstractEmptyParamsRuleFactory {
	/**
	 * Returns created rule class metadata.
	 * @return {@code TrustedSourceRuleFactory} metadata.
	 */
	@Override
	public Class getCreatedClass() {
		return TrustedSourceRule.class;
	}

	/**
	 * Creates a new rule.
	 * @param title Title.
	 * @return Created rule.
	 */
	@Override
	public AbstractRule createRule(String title) {
		return new TrustedSourceRule(title);
	}

	/**
	 * Formats factory name.
	 * @return Factory name.
	 */
	@Override
	public String toString() {
		return "Generate Trusted Source for All Entities";
	}
}
