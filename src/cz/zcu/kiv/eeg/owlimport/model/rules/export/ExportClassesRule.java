package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import org.openrdf.query.GraphQueryResult;

/**
 * ExportClassesRule exports all classes defined in the ontology.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportClassesRule extends AbstractEmptyParamsRule {
	private static final String EXPORT_QUERY = "CONSTRUCT {Cls} prop {Val}  " +
			"FROM {Cls} rdf:type {Type}; prop {Val} " +
			"WHERE Type = owl:Class";

	/**
	 * Initializes a new rule with given title.
	 * @param ruleTitle Rule title.
	 */
	public ExportClassesRule(String ruleTitle) {
		super(ruleTitle);
	}

	/**
	 * Exports classes definitions from ontology.
	 * @return Iteration of class definition statements.
	 * @throws RuleExportException when the query execution fails.
	 */
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
