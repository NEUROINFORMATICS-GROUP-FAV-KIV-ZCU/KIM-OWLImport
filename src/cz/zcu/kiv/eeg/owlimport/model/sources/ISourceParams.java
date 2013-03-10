package cz.zcu.kiv.eeg.owlimport.model.sources;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Interface for exportable sources parameters.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public interface ISourceParams {


	public void loadXml(XMLStreamReader reader) throws XMLStreamException;
}
