package cz.zcu.kiv.eeg.owlimport.model.sources;

import cz.zcu.kiv.eeg.owlimport.gui.ISourceParamsComponent;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Interface for ontology source factories. Handles correct creation and initialization of sources with given location
 * parameters.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public interface ISourceFactory {
	/**
	 * Returns the class of sources created by the factory.
	 * @return Class metadata.
	 */
	public Class getCreatedClass();

	/**
	 * Creates a new source.
	 * @param title Source title.
	 * @param baseUrl Source base URL.
	 * @param parameters Source location parameters.
	 * @return New ontology source.
	 */
	public AbstractSource createSource(String title, String baseUrl, ISourceParams parameters);

	/**
	 * Loads parameters from XML files and create a new ISourceParams instance.
	 * @param reader XML reader.
	 * @return Source parameters.
	 * @throws XMLStreamException when the parameters could not be read or the XML is malformed.
	 */
	public ISourceParams loadParams(XMLStreamReader reader) throws XMLStreamException;

	/**
	 * Creates a GUI component for input of source parameters.
	 * @return New GUI component.
	 */
	public ISourceParamsComponent createGuiComponent();
}
