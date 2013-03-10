package cz.zcu.kiv.eeg.owlimport.model;

import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class RuleManager {
	private List<IRuleFactory> factories;


	public RuleManager() {
		factories = new LinkedList<>();
	}


	public void registerFactory(IRuleFactory factory) {
		factories.add(factory);
	}

	public List<IRuleFactory> getFactories() {
		return factories;
	}

	public IRuleFactory getFactoryFor(String className) {
		for (IRuleFactory factory : factories) {
			if (factory.getCreatedClass().getName().equals(className)) {
				return factory;
			}
		}
		return null;
	}
}
