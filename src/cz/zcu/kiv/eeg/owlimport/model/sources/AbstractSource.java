package cz.zcu.kiv.eeg.owlimport.model.sources;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of ontology source with export and transformation rules.
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractSource {
	/** Source title. */
	private String title;
	/** Base URL of the ontology. */
	private String baseUrl;
	/** Repository of the source. */
	private RepositoryWrapper repository;
	/** List of rules. */
	private List<AbstractRule> rules;
	/** Rule list listener. */
	private RuleListListener rulesListener;

	/**
	 * Initializes basic source parameters.
	 * @param srcTitle Source title.
	 * @param owlBaseUrl Ontology base URL.
	 */
	public AbstractSource(String srcTitle, String owlBaseUrl) {
		title = srcTitle;
		baseUrl = owlBaseUrl;

		rules = new ArrayList<>();
	}

	/**
	 * Gets the currently registered rule list listener.
	 * @return Rule list listener.
	 */
	public RuleListListener getRulesListener() {
		return rulesListener;
	}

	/**
	 * Sets the rule list listener. Only one listener can be registered at the same time.
	 *
	 * The listener is notified when the list of source rules is modified.
	 * @param listener
	 */
	public void setRulesListener(RuleListListener listener) {
		rulesListener = listener;
	}

	/**
	 * Gets the source title.
	 * @return Source title.
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * Gets the source ontology base URL.
	 * @return Base URL.
	 */
	public final String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Imports the ontology to the given repository.
	 *
	 * This method is called by repository manager after the repository for source is created.
	 * @param repository Repository.
	 * @throws SourceImportException when the source cannot be imported.
	 */
	public abstract void importToRepository(RepositoryWrapper repository) throws SourceImportException;

	/**
	 * Saves the source location parameters to
	 * @param writer XML writer.
	 * @throws XMLStreamException
	 */
	public abstract void saveLocation(XMLStreamWriter writer) throws XMLStreamException;

	/**
	 * Attach the source to semantic repository with imported statements. This method is called by repository manager
	 * after the ontology has been imported to the specified repository.
	 * @param repositoryWrapper Repository.
	 */
	public final void attachRepository(RepositoryWrapper repositoryWrapper) {
		repository = repositoryWrapper;
	}

	/**
	 * Checks whether the source is attached to repository and therefore if the source has already been imported.
	 * @return {@code true} if the source is attached to repository.
	 */
	public final boolean hasRepositoryAttached() {
		return (repository != null);
	}

	/**
	 * Gets the repository with source ontology.
	 * @return Repository.
	 */
	public final RepositoryWrapper getRepository() {
		return repository;
	}


	/**
	 * Adds a rule to the source.
	 * @param rule Rule.
	 */
	public final void addRule(AbstractRule rule) {
		int newIndex = rules.size();
		rule.setSource(this);
		rules.add(rule);
		if (rulesListener != null) {
			rulesListener.ruleAdded(rule, newIndex);
		}
	}

	/**
	 * Removes the rule with given index from the source.
	 * @param index Index of the rule in the list.
	 */
	public final void removeRule(int index) {
		rules.remove(index);
		if (rulesListener != null) {
			rulesListener.ruleRemoved(index);
		}
	}


	/**
	 * Removes the specified rule from the rule list.
	 * @param rule Rule to be removed.
	 */
	public final void removeRule(AbstractRule rule) {
		int index = rules.indexOf(rule);
		if (index >= 0) {
			removeRule(index);
		}
	}

	/**
	 * Gets the count of rules of the source.
	 * @return
	 */
	public final int getRulesCount() {
		return rules.size();
	}

	/**
	 * Gets the rule from the list at specified index.
	 * @param index Index of the rule.
	 * @return Rule at given index.
	 */
	public final AbstractRule getRule(int index) {
		return rules.get(index);
	}

	/**
	 * Gets the list of all rules.
	 *
	 * Beware: the list of rules is mutable and listener events is not triggered when the collection is modified.
	 * @return List of rules.
	 */
	public final List<AbstractRule> getRules() {
		return rules;
	}

	/**
	 * Formats the source title.
	 * @return Source title.
	 */
	@Override
	public final String toString() {
		return getTitle();
	}


	/**
	 * RuleListListener is notified when the internal collection of rules is modified through the source.
	 */
	public interface RuleListListener {
		/**
		 * Occurs when a new rule is added to the source.
		 * @param rule New rule.
		 * @param index Index of the new rule.
		 */
		public void ruleAdded(AbstractRule rule, int index);

		/**
		 * Occurs when the rule is removed from the list.
		 * @param index Index of removed rule.
		 */
		public void ruleRemoved(int index);
	}
}
