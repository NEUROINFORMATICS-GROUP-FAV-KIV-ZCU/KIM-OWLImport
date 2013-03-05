package cz.zcu.kiv.eeg.owlimport.model.rules;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import org.openrdf.query.GraphQueryResult;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractRule {
	private AbstractSource source;

	private final String title;


	public AbstractRule(String ruleTitle) {
		title = ruleTitle;
	}

	public final void setSource(AbstractSource src) {
		source = src;
	}

	public final AbstractSource getSource() {
		return source;
	}


	public final String getTitle() {
		return title;
	}

	public abstract GraphQueryResult getStatements();

	protected final RepositoryWrapper getRepository() {
		return source.getRepository();
	}

	@Override
	public final String toString() {
		return getTitle();
	}
}
