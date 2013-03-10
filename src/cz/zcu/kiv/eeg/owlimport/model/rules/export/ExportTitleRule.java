package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
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
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportTitleRule extends AbstractRule {
	private static final String EXPORT_QUERY = "CONSTRUCT {Entity} rdf:type {Type}; rdf:label {Title} " +
			"FROM {Entity} sesame:directType {Type}; _labelProp {Title}";

	private ExportTitleParams params;

	public ExportTitleRule(String ruleTitle) {
		super(ruleTitle, new ExportTitleParams());
	}

	@Override
	protected void setRuleParams(IRuleParams ruleParams) {
		params = (ExportTitleParams) ruleParams;
	}

	@Override
	public IRuleParams getRuleParams() {
		return params;
	}

	@Override
	public IRuleParamsComponent createGuiComponent() {
		return new ExportTitleParamsComponent(params, getRepository());
	}

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
