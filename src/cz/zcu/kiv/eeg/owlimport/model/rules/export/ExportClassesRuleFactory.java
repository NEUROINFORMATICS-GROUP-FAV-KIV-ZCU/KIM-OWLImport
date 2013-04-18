package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * Factory for {@code ExportClassesRule}.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportClassesRuleFactory extends AbstractEmptyParamsRuleFactory {
	/**
	 * Returns created rule class metadata.
	 * @return {@code ExportClassesRule} metadata.
	 */
	@Override
	public Class getCreatedClass() {
		return ExportClassesRule.class;
	}

	/**
	 * Creates a new rule.
	 * @param title Title.
	 * @return Created rule.
	 */
	@Override
	public AbstractRule createRule(String title) {
		return new ExportClassesRule(title);
	}

	/**
	 * Formats factory name.
	 * @return Factory name.
	 */
	@Override
	public String toString() {
		return "Export All Classes";
	}
}
