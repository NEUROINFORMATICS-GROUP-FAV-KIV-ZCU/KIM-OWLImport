package cz.zcu.kiv.eeg.owlimport.model.sources.remote;

import cz.zcu.kiv.eeg.owlimport.gui.ISourceParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.sources.UrlParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class UrlSourceFactory implements ISourceFactory {
	private static final String FACTORY_TITLE = "Remote URL";


	@Override
	public Class getCreatedClass() {
		return UrlSource.class;
	}

	@Override
	public AbstractSource createSource(String title, String baseUrl, ISourceParams parameters) {
		if (!(parameters instanceof UrlSourceParams)) {
			throw invalidParametersSetException();
		}

		UrlSourceParams urlParams = (UrlSourceParams) parameters;
		return new UrlSource(title, baseUrl, urlParams.getUrl());
	}

	private IllegalArgumentException invalidParametersSetException() {
		return new IllegalArgumentException("Specified parameters set is not an instance of UrlSourceParams.");
	}

	@Override
	public ISourceParams loadParams(XMLStreamReader reader) throws XMLStreamException {
		UrlSourceParams params = new UrlSourceParams();
		params.loadXml(reader);
		return params;
	}

	@Override
	public ISourceParamsComponent createGuiComponent() {
		return new UrlParamsComponent();
	}

	@Override
	public String toString() {
		return FACTORY_TITLE;
	}
}
