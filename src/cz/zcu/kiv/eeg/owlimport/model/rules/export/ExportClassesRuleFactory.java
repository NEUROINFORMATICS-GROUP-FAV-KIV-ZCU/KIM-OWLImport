package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportClassesRuleFactory extends AbstractEmptyParamsRuleFactory {
	@Override
	public Class getCreatedClass() {
		return ExportClassesRule.class;
	}

	@Override
	public AbstractRule createRule(String title) {
		return new ExportClassesRule(title);
	}


	@Override
	public String toString() {
		return "Export All Classes";
	}
}
