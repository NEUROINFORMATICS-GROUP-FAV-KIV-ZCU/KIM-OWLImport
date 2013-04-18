package cz.zcu.kiv.eeg.owlimport.model.rules;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Interface for rule factories. Handles rule creation and parameters loading from XML file.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public interface IRuleFactory {
	/**
	 * Returns the class of rules created by the factory.
	 * @return Class metadata.
	 */
	public Class getCreatedClass();

	/**
	 * Creates a new rule with the given title.
	 * @param title Title.
	 * @return Newly created rule.
	 */
	public AbstractRule createRule(String title);

	/**
	 * Loads rule parameters from XML and creates the correct instance of {@code IRuleParams}.
	 * @param reader XML reader.
	 * @return Parameters for rules created by the factory.
	 * @throws XMLStreamException when the parameters cannot be read or XML is malformed.
	 */
	public IRuleParams loadParams(XMLStreamReader reader) throws XMLStreamException;
}
