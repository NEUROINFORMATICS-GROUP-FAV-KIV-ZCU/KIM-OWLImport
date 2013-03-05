package cz.zcu.kiv.eeg.owlimport.gui.model;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;

import javax.swing.*;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class RuleListModel extends AbstractListModel<AbstractRule> {
	private AbstractSource source;


	public RuleListModel(AbstractSource src) {
		source = src;
		attach();
	}

	public void attach() {
		source.setRulesListener(new AbstractSource.RuleListListener() {
			@Override
			public void ruleAdded(AbstractRule rule, int index) {
				fireIntervalAdded(rule, index, index);
			}
		});
	}

	public void detach() {
		source.setRulesListener(null);
	}


	@Override
	public int getSize() {
		return source.getRulesCount();
	}


	@Override
	public AbstractRule getElementAt(int index) {
		return source.getRule(index);
	}
}
