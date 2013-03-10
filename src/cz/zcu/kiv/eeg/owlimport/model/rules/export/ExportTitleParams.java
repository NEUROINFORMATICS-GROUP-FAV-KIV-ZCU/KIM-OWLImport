package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;
import cz.zcu.kiv.eeg.owlimport.project.XmlReaderUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleParams implements IRuleParams {
	private static final String EL_LABEL_PROP = "labelUri";

	private String labelProp;


	public String getLabelProp() {
		return labelProp;
	}

	public void setLabelProp(String labelProp) {
		this.labelProp = labelProp;
	}


	@Override
	public void saveXml(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(EL_LABEL_PROP);
		writer.writeCharacters(labelProp);
		writer.writeEndElement();
	}


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
