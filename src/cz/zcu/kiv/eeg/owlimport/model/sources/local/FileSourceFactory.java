package cz.zcu.kiv.eeg.owlimport.model.sources.local;

import cz.zcu.kiv.eeg.owlimport.gui.ISourceParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.sources.FileParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Factory for local file ontology sources.
 * @author Jan Smitka <jan@smitka.org>
 */
public class FileSourceFactory implements ISourceFactory {
	private static final String FACTORY_TITLE = "Local File";

	/**
	 * Returns the class of sources created by the factory.
	 * @return Class instance.
	 */
	@Override
	public Class getCreatedClass() {
		return FileSource.class;
	}

	/**
	 * Creates a new source.
	 * @param title Source title.
	 * @param baseUrl Source base URL.
	 * @param parameters Source location parameters containing source file.
	 * @return New local file source.
	 */
	@Override
	public AbstractSource createSource(String title, String baseUrl, ISourceParams parameters) {
		if (!(parameters instanceof FileSourceParams)) {
			throw invalidParametersSetException();
		}

		FileSourceParams fileParams = (FileSourceParams) parameters;
		return new FileSource(title, baseUrl, fileParams.getFile());
	}


	private IllegalArgumentException invalidParametersSetException() {
		return new IllegalArgumentException("Specified parameters set is not an instance of FileSourceParams.");
	}


	/**
	 * Creates a GUI panel which allows user to select a file with ontology.
	 * @return {@code FileParamsComponent}.
	 */
	@Override
	public ISourceParamsComponent createGuiComponent() {
		return new FileParamsComponent();
	}

	/**
	 * Loads parameters from XML file and creates a new instance of {@code FileSourceParams}
	 * @param reader XML reader.
	 * @return {@code FileSourceParams}.
	 * @throws XMLStreamException
	 */
	@Override
	public ISourceParams loadParams(XMLStreamReader reader) throws XMLStreamException {
		FileSourceParams params = new FileSourceParams();
		params.loadXml(reader);
		return params;
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
