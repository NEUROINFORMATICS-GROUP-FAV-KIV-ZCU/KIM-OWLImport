package cz.zcu.kiv.eeg.owlimport.model;

import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * RuleManager manages list of rule factories.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class RuleManager {
	/** List of rule factories. */
	private List<IRuleFactory> factories;

	/**
	 * Initializes an empty RuleManager.
	 */
	public RuleManager() {
		factories = new LinkedList<>();
	}

	/**
	 * Registers a new rule factory.
	 * @param factory New factory.
	 */
	public void registerFactory(IRuleFactory factory) {
		factories.add(factory);
	}

	/**
	 * Gets the list of new factories.
	 * @return List of all factories.
	 */
	public List<IRuleFactory> getFactories() {
		return factories;
	}

	/**
	 * Gets the factory for rules of specified class.
	 * @param className Fully qualified class name of the rule.
	 * @return Rule factory or {@code null} when the factory for the given class is not found.
	 */
	public IRuleFactory getFactoryFor(String className) {
		for (IRuleFactory factory : factories) {
			if (factory.getCreatedClass().getName().equals(className)) {
				return factory;
			}
		}
		return null;
	}
}
