package cz.zcu.kiv.eeg.owlimport.project;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Utilities to simplify the process of stream XML reading.
 * @author Jan Smitka <jan@smitka.org>
 */
public class XmlReaderUtils {
	/**
	 * Load text of the current element.
	 * @param reader XML reader.
	 * @return Text of the element.
	 * @throws XMLStreamException when the text cannot be read or XML is malformed.
	 */
	public static String loadElementText(XMLStreamReader reader) throws XMLStreamException {
		reader.next();
		reader.require(XMLStreamConstants.CHARACTERS, null, null);
		String text = reader.getText();
		reader.next();
		reader.require(XMLStreamConstants.END_ELEMENT, null, null);
		return text;
	}

	/**
	 * Raises a new unexpected element exception, reporting the single expected element.
	 * @param reader Reader.
	 * @param expected Expected element.
	 * @return Exception.
	 */
	public static XMLStreamException unexpectedElementException(XMLStreamReader reader, String expected) {
		return new XMLStreamException(String.format("Unexpected element. Expected %s, got %s.", expected, reader.getName().toString()));
	}

	/**
	 * Raises a new unexpected element exception.
	 * @param reader XML reader.
	 * @return Exception.
	 */
	public static XMLStreamException unexpectedElementException(XMLStreamReader reader) {
		return new XMLStreamException(String.format("Unexpected element: %s.", reader.getName().toString()));
	}
}
