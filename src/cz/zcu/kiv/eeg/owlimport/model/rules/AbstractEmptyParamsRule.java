package cz.zcu.kiv.eeg.owlimport.model.rules;

import cz.zcu.kiv.eeg.owlimport.gui.IRuleParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.rules.EmptyParamsComponent;

/**
 * Base rule implementation for rules without params.
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractEmptyParamsRule extends AbstractRule {
	/** Empty rule params. */
	private IRuleParams parameters;

	/**
	 * Creates a rule without params with given title.
	 * @param ruleTitle Title of the rule.
	 */
	public AbstractEmptyParamsRule(String ruleTitle) {
		super(ruleTitle, new EmptyRuleParams());
	}

	/**
	 * Creates a GUI panel which informs the user that the rule has no parameters.
	 * @return {@code EmptyParamsComponent}.
	 */
	@Override
	public IRuleParamsComponent createGuiComponent() {
		return new EmptyParamsComponent();
	}

	/**
	 * Sets the rule parameters.
	 * @param params Rule parameters.
	 */
	@Override
	public void setRuleParams(IRuleParams params) {
		parameters = params;
	}

	/**
	 * Gets the rule parameters.
	 * @return Current rule parameters.
	 */
	@Override
	public IRuleParams getRuleParams() {
		return parameters;
	}
}
