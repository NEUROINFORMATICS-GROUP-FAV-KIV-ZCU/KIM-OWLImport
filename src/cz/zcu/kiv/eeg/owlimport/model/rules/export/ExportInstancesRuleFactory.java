package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportInstancesRuleFactory extends AbstractEmptyParamsRuleFactory {
	@Override
	public Class getCreatedClass() {
		return ExportInstancesRule.class;
	}

	@Override
	public AbstractRule createRule(String title) {
		return new ExportInstancesRule(title);
	}


	public String toString() {
		return "Export All Instances";
	}
}
