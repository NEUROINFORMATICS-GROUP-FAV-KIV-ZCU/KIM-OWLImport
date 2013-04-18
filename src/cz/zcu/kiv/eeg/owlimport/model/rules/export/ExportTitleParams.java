package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;
import cz.zcu.kiv.eeg.owlimport.project.XmlReaderUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Parameters for {@code ExportTitleRule}.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleParams implements IRuleParams {
	private static final String EL_LABEL_PROP = "labelUri";

	private String labelProp;

	/**
	 * Gets the URI of label property.
	 * @return String containing property URI.
	 */
	public String getLabelProp() {
		return labelProp;
	}

	/**
	 * Sets the URI of label property.
	 * @param labelProp String containing property URI.
	 */
	public void setLabelProp(String labelProp) {
		this.labelProp = labelProp;
	}

	/**
	 * Writes the parameters to XML file.
	 * @param writer XML writer.
	 * @throws XMLStreamException when the XML could not be written.
	 */
	@Override
	public void saveXml(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(EL_LABEL_PROP);
		writer.writeCharacters(labelProp);
		writer.writeEndElement();
	}


	/**
	 * Loads the parameters from XML file.
	 * @param reader XML reader.
	 * @throws XMLStreamException when the XML could not be read or XML is malformed.
	 */
	@Override
	public void loadXml(XMLStreamReader reader) throws XMLStreamException {
		while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
			if (reader.getName().getLocalPart().equals(EL_LABEL_PROP)) {
				labelProp = XmlReaderUtils.loadElementText(reader);
			} else {
				throw XmlReaderUtils.unexpectedElementException(reader, EL_LABEL_PROP);
			}
		}
	}
}
