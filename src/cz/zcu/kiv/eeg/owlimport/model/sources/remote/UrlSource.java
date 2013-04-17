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
 * @author Jan Smitka <jan@smitka.org>
 */
public class UrlSource extends AbstractSource {

	private final String url;

	public UrlSource(String srcTitle, String owlBaseUrl, String owlUrl) {
		super(srcTitle, owlBaseUrl);

		url = owlUrl;
	}

	@Override
	public void importToRepository(RepositoryWrapper repository) throws SourceImportException {
		try {
			repository.importUrl(url, getBaseUrl());
		} catch (IOException|RepositoryException|RDFParseException e) {
			throw new SourceImportException("Remote URL could not be imported to the repository.", e);
		}
	}

	@Override
	public void saveLocation(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(UrlSourceParams.EL_URL);
		writer.writeCharacters(url);
		writer.writeEndElement();
	}
}
