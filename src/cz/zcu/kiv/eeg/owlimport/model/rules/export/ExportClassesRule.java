package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import org.openrdf.query.GraphQueryResult;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportClassesRule extends AbstractEmptyParamsRule {
	private static final String EXPORT_QUERY = "CONSTRUCT {Cls} prop {Val}  " +
			"FROM {Cls} rdf:type {Type}; prop {Val} " +
			"WHERE Type = owl:Class";

	public ExportClassesRule(String ruleTitle) {
		super(ruleTitle);
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
