package cz.zcu.kiv.eeg.owlimport.model.source.local;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.source.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.source.SourceImportException;

import java.io.File;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSource extends AbstractSource {

	private File file;

	public FileSource(String srcTitle, String owlBaseUrl, File localFile) {
		super(srcTitle, owlBaseUrl);

		file = localFile;
	}

	@Override
	public void importToRepository(RepositoryWrapper repository) throws SourceImportException {
		try {
			repository.importFile(file, getBaseUrl());
		} catch (Exception e) {
			throw importException(e);
		}
	}


	private SourceImportException importException(Exception cause) {
		return new SourceImportException("Error while importing local file to repository.", cause);
	}
}
