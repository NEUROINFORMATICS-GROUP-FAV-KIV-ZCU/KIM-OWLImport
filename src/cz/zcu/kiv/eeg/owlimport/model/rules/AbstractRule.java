package cz.zcu.kiv.eeg.owlimport.model.rules;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.gui.IRuleParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import info.aduna.iteration.Iteration;
import org.openrdf.model.Statement;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Abstract implementation of source export/transformation rule. Holds basic rule metadata.
 *
 * Implementing classes should handle querying the source repository and creating newly created triplets which will
 * be written to the output file.
 * @author Jan Smitka <jan@smitka.org>
 */
public abstract class AbstractRule {
	/** Source to which this rule is bound to. */
	private AbstractSource source;
	/** Rule title. */
	private final String title;
	/** Created GUI component. */
	private IRuleParamsComponent guiComponent;

	/**
	 * Creates a new rule with given title and initializes the rule parameters.
	 *
	 * Implementing classes should call this constructor with default parameters (usually an empty instance of {@code IRuleParams}.
	 * @param ruleTitle Rule title.
	 * @param params Default parameters for the rule.
	 */
	public AbstractRule(String ruleTitle, IRuleParams params) {
		title = ruleTitle;

		setRuleParams(params);
	}

	/**
	 * Sets the rule parameters. Implementing classes should handle typecasting from the generic interface type.
	 * @param params New rule parameters.
	 */
	public abstract void setRuleParams(IRuleParams params);

	/**
	 * Gets the rule parameters.
	 * @return Current rule parameters.
	 */
	public abstract IRuleParams getRuleParams();

	/**
	 * Creates an GUI panel which will allow user to specify the parameters of the rule.
	 * @return Class implementing {@code IRuleParamsComponent}.
	 */
	public abstract IRuleParamsComponent createGuiComponent();

	/**
	 * Creates and returns an iteration of new triplets.
	 *
	 * Triplets can be created by the CONSTRUCT query, or generated in the rule.
	 * @return Iteration of statements.
	 * @throws RuleExportException when the rule cannot be exported, e.g. query fails.
	 */
	public abstract Iteration<Statement, ? extends Exception> getStatements() throws RuleExportException;

	/**
	 * Sets the source which will be exported.
	 * @param src Source to be exported.
	 */
	public final void setSource(AbstractSource src) {
		source = src;
	}

	/**
	 * Gets the source which will be exported.
	 * @return Source instance.
	 */
	public final AbstractSource getSource() {
		return source;
	}

	/**
	 * Gets the rule title.
	 * @return Rule title.
	 */
	public final String getTitle() {
		return title;
	}


	/**
	 * Gets the GUI panel for parameter specification. It is lazily created first time this method is called.
	 * @return GUI panel.
	 */
	public final IRuleParamsComponent getGuiComponent() {
		if (guiComponent == null) {
			guiComponent = createGuiComponent();
		}
		return guiComponent;
	}

	/**
	 * Saves the rule parameters to XML file.
	 * @param writer XML writer.
	 * @throws XMLStreamException when the parameters cannot be written.
	 */
	public final void saveParams(XMLStreamWriter writer) throws XMLStreamException {
		getRuleParams().saveXml(writer);
	}

	/**
	 * Gets the source ontology repository for querying.
	 * @return Repository wrapper for the source.
	 */
	protected final RepositoryWrapper getRepository() {
		return source.getRepository();
	}

	/**
	 * Formats the rule title.
	 * @return Rule title.
	 */
	@Override
	public final String toString() {
		return getTitle();
	}
}
