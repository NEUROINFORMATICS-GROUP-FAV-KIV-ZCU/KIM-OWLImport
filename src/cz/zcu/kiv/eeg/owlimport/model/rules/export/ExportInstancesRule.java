package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import info.aduna.iteration.Iteration;
import org.openrdf.model.Statement;

/**
 * ExportInstancesRule exports definition of all instances defined in the ontology.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ExportInstancesRule extends AbstractEmptyParamsRule {
	private static final String ENTITIES_QUERY =
		"CONSTRUCT {Entity} Property {Value} " +
				"FROM {Entity} rdf:type {Type}; Property {Value}, {Type} sesame:directType {ClassType}" +
				"WHERE ClassType = owl:Class";

	/**
	 * Initializes a new rule with given title.
	 * @param ruleTitle Rule title.
	 */
	public ExportInstancesRule(String ruleTitle) {
		super(ruleTitle);
	}

	/**
	 * Exports definition of all instances from ontology.
	 * @return Iteration of instance definition statements.
	 * @throws RuleExportException when the query execution fails.
	 */
	@Override
	public Iteration<Statement, ? extends Exception> getStatements() throws RuleExportException {
		try {
			RepositoryWrapper repository = getRepository();
			return repository.executeGraphQuery(ENTITIES_QUERY);
		} catch (Exception e) {
			throw new RuleExportException("Error while exporting instances.", e);
		}
	}
}
