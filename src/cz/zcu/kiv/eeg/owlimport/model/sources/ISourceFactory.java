package cz.zcu.kiv.eeg.owlimport.model.sources;

import cz.zcu.kiv.eeg.owlimport.gui.ISourceParamsComponent;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public interface ISourceFactory {
	public Class getCreatedClass();

	public AbstractSource createSource(String title, String baseUrl, ISourceParams parameters);

	public ISourceParams loadParams(XMLStreamReader reader) throws XMLStreamException;

	public ISourceParamsComponent createGuiComponent();
}
