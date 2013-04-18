package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.gui.IRuleParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.rules.ExportTitleParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import org.openrdf.model.Value;
import org.openrdf.query.GraphQueryResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Exports all instances with specified property and uses the property value as entity label.
 *
 * This rule is intended as an example of ontology transformation.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleRule extends AbstractRule {
	private static final String EXPORT_QUERY = "CONSTRUCT {Entity} rdf:type {Type}; rdf:label {Title} " +
			"FROM {Entity} sesame:directType {Type}; _labelProp {Title}";

	private ExportTitleParams params;

	/**
	 * Initializes a new rule with given title and empty parameters.
	 * @param ruleTitle Rule title.
	 */
	public ExportTitleRule(String ruleTitle) {
		super(ruleTitle, new ExportTitleParams());
	}

	/**
	 * Sets the rule parameters.
	 * @param ruleParams {@code ExportTitleParams} instance.
	 */
	@Override
	public void setRuleParams(IRuleParams ruleParams) {
		params = (ExportTitleParams) ruleParams;
	}

	/**
	 * Gets the rule parameters.
	 * @return Current {@code ExportTitleParams} instance.
	 */
	@Override
	public IRuleParams getRuleParams() {
		return params;
	}

	/**
	 * Creates a GUI panel which allows user to specify which datatype property will be used as the entity label.
	 * @return GUI component.
	 */
	@Override
	public IRuleParamsComponent createGuiComponent() {
		return new ExportTitleParamsComponent(params, getRepository());
	}

	/**
	 * Exports entity titles from specified property.
	 * @return Iteration of entity title statements.
	 * @throws RuleExportException when the query execution fails.
	 */
	@Override
	public GraphQueryResult getStatements() throws RuleExportException {
		try {
			RepositoryWrapper repository = getRepository();
			Map<String, Value> queryParams = new HashMap<String, Value>();
			queryParams.put("_labelProp", repository.createUri(params.getLabelProp()));

			return repository.executeGraphQuery(EXPORT_QUERY, queryParams);
		} catch (Exception e) {
			throw new RuleExportException("Error while exporting the rule.", e);
		}
	}
}
