package cz.zcu.kiv.eeg.owlimport.gui.model;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;

import javax.swing.*;

/**
 * Model for the list of rules in ontology source.
 * @author Jan Smitka <jan@smitka.org>
 */
public class RuleListModel extends AbstractListModel<AbstractRule> {
	private AbstractSource source;

	/**
	 * Initializes list model bound to the specified ontology source.
	 * @param src Ontology source with defined rules.
	 */
	public RuleListModel(AbstractSource src) {
		source = src;
		attach();
	}

	/**
	 * Registers the list model listener.
	 */
	public void attach() {
		source.setRulesListener(new AbstractSource.RuleListListener() {
			@Override
			public void ruleAdded(AbstractRule rule, int index) {
				fireIntervalAdded(RuleListModel.this, index, index);
			}

			@Override
			public void ruleRemoved(int index) {
				fireIntervalRemoved(RuleListModel.this, index, index);
			}
		});
	}

	/**
	 * Unregisters the list model listener.
	 */
	public void detach() {
		source.setRulesListener(null);
	}

	/**
	 * Gets the number of rules defined for the source.
	 * @return Number of rules.
	 */
	@Override
	public int getSize() {
		return source.getRulesCount();
	}

	/**
	 * Gets the rule with given index.
	 * @param index Index in the list.
	 * @return Rule instance.
	 */
	@Override
	public AbstractRule getElementAt(int index) {
		return source.getRule(index);
	}
}
