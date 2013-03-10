package cz.zcu.kiv.eeg.owlimport.model.sources;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractSource {
	private String title;

	private String baseUrl;

	private RepositoryWrapper repository;

	private List<AbstractRule> rules;

	private RuleListListener rulesListener;

	public AbstractSource(String srcTitle, String owlBaseUrl) {
		title = srcTitle;
		baseUrl = owlBaseUrl;

		rules = new ArrayList<>();
	}

	public RuleListListener getRulesListener() {
		return rulesListener;
	}


	public void setRulesListener(RuleListListener listener) {
		rulesListener = listener;
	}


	public final String getTitle() {
		return title;
	}

	public final String getBaseUrl() {
		return baseUrl;
	}

	public abstract void importToRepository(RepositoryWrapper repository) throws SourceImportException;

	public abstract void saveLocation(XMLStreamWriter writer) throws XMLStreamException;

	public final void attachRepository(RepositoryWrapper repositoryWrapper) {
		repository = repositoryWrapper;
	}

	public final RepositoryWrapper getRepository() {
		return repository;
	}


	public final void addRule(AbstractRule rule) {
		int newIndex = rules.size();
		rule.setSource(this);
		rules.add(rule);
		if (rulesListener != null) {
			rulesListener.ruleAdded(rule, newIndex);
		}
	}


	public final int getRulesCount() {
		return rules.size();
	}


	public final AbstractRule getRule(int index) {
		return rules.get(index);
	}


	public final List<AbstractRule> getRules() {
		return rules;
	}


	@Override
	public final String toString() {
		return getTitle();
	}



	public interface RuleListListener {
		public void ruleAdded(AbstractRule rule, int index);
	}
}
