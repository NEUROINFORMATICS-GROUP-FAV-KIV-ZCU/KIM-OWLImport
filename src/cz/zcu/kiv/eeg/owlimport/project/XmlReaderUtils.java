package cz.zcu.kiv.eeg.owlimport.project;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class XmlReaderUtils {

	public static String loadElementText(XMLStreamReader reader) throws XMLStreamException {
		reader.next();
		reader.require(XMLStreamConstants.CHARACTERS, null, null);
		String text = reader.getText();
		reader.next();
		reader.require(XMLStreamConstants.END_ELEMENT, null, null);
		return text;
	}

	public static XMLStreamException unexpectedElementException(XMLStreamReader reader, String expected) {
		return new XMLStreamException(String.format("Unexpected element. Expected %s, got %s.", expected, reader.getName().toString()));
	}

	public static XMLStreamException unexpectedElementException(XMLStreamReader reader) {
		return new XMLStreamException(String.format("Unexpected element: %s.", reader.getName().toString()));
	}
}
