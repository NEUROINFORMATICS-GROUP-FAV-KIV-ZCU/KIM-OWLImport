package cz.zcu.kiv.eeg.owlimport.model.sources.remote;

import cz.zcu.kiv.eeg.owlimport.gui.ISourceParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.sources.UrlParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Factory for remote ontologies identified by their URL.
 * @author Jan Smitka <jan@smitka.org>
 */
public class UrlSourceFactory implements ISourceFactory {
	private static final String FACTORY_TITLE = "Remote URL";

	/**
	 * Returns the class of sources created by the factory.
	 * @return Class instance.
	 */
	@Override
	public Class getCreatedClass() {
		return UrlSource.class;
	}

	/**
	 * Creates a new source.
	 * @param title Source title.
	 * @param baseUrl Source base URL.
	 * @param parameters Source location parameters.
	 * @return New remote source.
	 */
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

	/**
	 * Loads source parameters from XML file and creates new instance of {@code UrlSourceParams}.
	 * @param reader XML reader.
	 * @return {@code UrlSourceParams}
	 * @throws XMLStreamException when the params cannot be loaded.
	 */
	@Override
	public ISourceParams loadParams(XMLStreamReader reader) throws XMLStreamException {
		UrlSourceParams params = new UrlSourceParams();
		params.loadXml(reader);
		return params;
	}

	/**
	 * Creates a GUI panel which allows user to enter remote URL.
	 * @return {@code UrlSourceParams}.
	 */
	@Override
	public ISourceParamsComponent createGuiComponent() {
		return new UrlParamsComponent();
	}

	/**
	 * Returns a title of the factory.
	 * @return Factory title.
	 */
	@Override
	public String toString() {
		return FACTORY_TITLE;
	}
}
