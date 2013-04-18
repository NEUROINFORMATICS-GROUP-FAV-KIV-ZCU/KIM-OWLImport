package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * Factory for {@code ProtonAlignRuleFactory}.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProtonAlignRuleFactory extends AbstractEmptyParamsRuleFactory {
	/**
	 * Returns created rule class metadata.
	 * @return {@code ProtonAlignRule} metadata.
	 */
	@Override
	public Class getCreatedClass() {
		return ProtonAlignRule.class;
	}

	/**
	 * Creates a new rule.
	 * @param title Title.
	 * @return Created rule.
	 */
	@Override
	public AbstractRule createRule(String title) {
		return new ProtonAlignRule(title);
	}

	/**
	 * Formats factory name.
	 * @return Factory name.
	 */
	@Override
	public String toString() {
		return "Align Root Classes to Proton Ontology";
	}
}
