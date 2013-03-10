package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleRuleFactory implements IRuleFactory {
	private static final String FACTORY_TITLE = "Export Title of Instances";

	@Override
	public AbstractRule createRule(String title) {
		return new ExportTitleRule(title);
	}

	@Override
	public String toString() {
		return FACTORY_TITLE;
	}
}
