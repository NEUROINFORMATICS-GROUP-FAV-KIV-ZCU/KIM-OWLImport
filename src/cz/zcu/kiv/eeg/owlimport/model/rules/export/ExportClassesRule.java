package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.gui.IRuleParamsComponent;
import cz.zcu.kiv.eeg.owlimport.gui.rules.EmptyParamsComponent;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.EmptyRuleParams;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import org.openrdf.query.GraphQueryResult;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportClassesRule extends AbstractRule {
	private static final String EXPORT_QUERY = "CONSTRUCT {Cls} prop {Val}  " +
			"FROM {Cls} rdf:type {Type}; prop {Val} " +
			"WHERE Type = owl:Class";

	private IRuleParams parameters;


	public ExportClassesRule(String ruleTitle) {
		super(ruleTitle, new EmptyRuleParams());
	}

	@Override
	public void setRuleParams(IRuleParams params) {
		parameters = params;
	}

	@Override
	public IRuleParams getRuleParams() {
		return parameters;
	}

	@Override
	public IRuleParamsComponent createGuiComponent() {
		return new EmptyParamsComponent();
	}

	@Override
	public GraphQueryResult getStatements() throws RuleExportException {
		try {
			RepositoryWrapper repository = getRepository();
			return repository.executeGraphQuery(EXPORT_QUERY);
		} catch (Exception e) {
			throw new RuleExportException("Error while exporting the rule.", e);
		}
	}
}
