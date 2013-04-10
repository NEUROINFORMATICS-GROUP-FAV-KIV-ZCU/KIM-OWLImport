package cz.zcu.kiv.eeg.owlimport.model.rules;

import cz.zcu.kiv.eeg.owlimport.gui.IRuleParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.rules.EmptyParamsComponent;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractEmptyParamsRule extends AbstractRule {
	private IRuleParams parameters;

	public AbstractEmptyParamsRule(String ruleTitle) {
		super(ruleTitle, new EmptyRuleParams());
	}

	@Override
	public IRuleParamsComponent createGuiComponent() {
		return new EmptyParamsComponent();
	}

	@Override
	public void setRuleParams(IRuleParams params) {
		parameters = params;
	}

	@Override
	public IRuleParams getRuleParams() {
		return parameters;
	}
}
