package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Factory for {@code ExportTitleRule}.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleRuleFactory implements IRuleFactory {
	private static final String FACTORY_TITLE = "Export Title of Instances";

	/**
	 * Returns created rule class metadata.
	 * @return {@code ExportTitleRule} metadata.
	 */
	@Override
	public Class getCreatedClass() {
		return ExportTitleRule.class;
	}

	/**
	 * Creates a new rule.
	 * @param title Title.
	 * @return Created rule.
	 */
	@Override
	public AbstractRule createRule(String title) {
		return new ExportTitleRule(title);
	}

	@Override
	public IRuleParams loadParams(XMLStreamReader reader) throws XMLStreamException {
		ExportTitleParams params = new ExportTitleParams();
		params.loadXml(reader);
		return params;
	}


	/**
	 * Formats factory name.
	 * @return Factory name.
	 */
	@Override
	public String toString() {
		return FACTORY_TITLE;
	}
}
