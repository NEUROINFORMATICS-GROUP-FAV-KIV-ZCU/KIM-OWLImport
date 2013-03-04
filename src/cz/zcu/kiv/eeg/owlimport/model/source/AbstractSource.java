package cz.zcu.kiv.eeg.owlimport.model.source;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractSource {
	private String title;

	private String baseUrl;

	private RepositoryWrapper repository;

	public AbstractSource(String srcTitle, String owlBaseUrl) {
		title = srcTitle;
		baseUrl = owlBaseUrl;
	}


	public final String getTitle() {
		return title;
	}

	public final String getBaseUrl() {
		return baseUrl;
	}

	public abstract void importToRepository(RepositoryWrapper repository) throws SourceImportException;

	public final void attachRepository(RepositoryWrapper repositoryWrapper) {
		repository = repositoryWrapper;
	}

	public final RepositoryWrapper getRepository() {
		return repository;
	}


	@Override
	public final String toString() {
		return getTitle();
	}
}
