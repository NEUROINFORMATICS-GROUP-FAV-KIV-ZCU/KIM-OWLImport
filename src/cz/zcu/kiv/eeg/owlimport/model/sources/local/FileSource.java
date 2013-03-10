package cz.zcu.kiv.eeg.owlimport.model.sources.local;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.SourceImportException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSource extends AbstractSource {
	private static final String EL_FILE = "file";

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


	@Override
	public void saveLocation(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(EL_FILE);
		writer.writeCharacters(file.getPath());
		writer.writeEndElement();
	}
}
