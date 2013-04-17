package cz.zcu.kiv.eeg.owlimport.model.sources.remote;

import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.SourceImportException;
import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFParseException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

/**
 * Ontology source from remote URL.
 * @author Jan Smitka <jan@smitka.org>
 */
public class UrlSource extends AbstractSource {
	/** Remote URL. */
	private final String url;

	/**
	 * Creates a new source with given ontology URL.
	 * @param srcTitle Source title.
	 * @param owlBaseUrl Source base URI.
	 * @param owlUrl Remote URL.
	 */
	public UrlSource(String srcTitle, String owlBaseUrl, String owlUrl) {
		super(srcTitle, owlBaseUrl);

		url = owlUrl;
	}

	/**
	 * Imports the ontology from remote URL to given repository.
	 * @param repository Repository.
	 * @throws SourceImportException when the source cannot be imported.
	 */
	@Override
	public void importToRepository(RepositoryWrapper repository) throws SourceImportException {
		try {
			repository.importUrl(url, getBaseUrl());
		} catch (IOException|RepositoryException|RDFParseException e) {
			throw new SourceImportException("Remote URL could not be imported to the repository.", e);
		}
	}

	/**
	 * Saves the remote URL to XML file.
	 * @param writer XML writer.
	 * @throws XMLStreamException when the parameters cannot be written.
	 */
	@Override
	public void saveLocation(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(UrlSourceParams.EL_URL);
		writer.writeCharacters(url);
		writer.writeEndElement();
	}
}
