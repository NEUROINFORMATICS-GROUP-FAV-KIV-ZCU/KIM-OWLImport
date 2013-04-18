package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * Factory for {@code ExportInstancesRule}.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportInstancesRuleFactory extends AbstractEmptyParamsRuleFactory {
	/**
	 * Returns created rule class metadata.
	 * @return {@code ExportInstancesRule} metadata.
	 */
	@Override
	public Class getCreatedClass() {
		return ExportInstancesRule.class;
	}

	/**
	 * Creates a new rule.
	 * @param title Title.
	 * @return Created rule.
	 */
	@Override
	public AbstractRule createRule(String title) {
		return new ExportInstancesRule(title);
	}

	/**
	 * Formats factory name.
	 * @return Factory name.
	 */
	public String toString() {
		return "Export All Instances";
	}
}
