package cz.zcu.kiv.eeg.owlimport.model.sources.local;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.SourceImportException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;

/**
 * Local file ontology source.
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSource extends AbstractSource {
	private File file;

	/**
	 * Creates a new source with the given ontology file.
	 * @param srcTitle Source title.
	 * @param owlBaseUrl Base URL.
	 * @param localFile Ontology file.
	 */
	public FileSource(String srcTitle, String owlBaseUrl, File localFile) {
		super(srcTitle, owlBaseUrl);

		file = localFile;
	}

	/**
	 * Imports the ontology file to the given repository.
	 * @param repository Repository.
	 * @throws SourceImportException when the file could not be imported.
	 */
	@Override
	public void importToRepository(RepositoryWrapper repository) throws SourceImportException {
		try {
			repository.importFile(file, getBaseUrl());
		} catch (Exception e) {
			throw importException(e);
		}
	}

	/**
	 * Raises a new import exception.
	 * @param cause Causing exception.
	 * @return New exception.
	 */
	private SourceImportException importException(Exception cause) {
		return new SourceImportException("Error while importing local file to repository.", cause);
	}


	/**
	 * Writes the file path to XML file.
	 * @param writer XML writer.
	 * @throws XMLStreamException when the parameters could not be written.
	 */
	@Override
	public void saveLocation(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(FileSourceParams.EL_FILE);
		writer.writeCharacters(file.getPath());
		writer.writeEndElement();
	}
}
