package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;

import javax.xml.stream.XMLStreamException;
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
	public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement(EL_LABEL_PROP);
		writer.writeCharacters(labelProp);
		writer.writeEndElement();
	}
}
