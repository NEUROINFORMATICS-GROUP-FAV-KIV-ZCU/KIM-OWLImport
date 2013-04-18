package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import org.openrdf.model.Value;
import org.openrdf.query.GraphQueryResult;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code ProtonAlignRule} aligns the ontology to PROTON Ontology Top Module.
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProtonAlignRule extends AbstractEmptyParamsRule {
	private static final String EXPORT_QUERY = "CONSTRUCT {Class} rdf:type {_protontObj} " +
			"FROM CONTEXT _context {Class} rdf:type {ClassType}; [sesame:directSubClassOf {Super}] " +
			"WHERE ClassType = owl:Class AND NOT BOUND(Super)";

	private static final String PARAM_CONTEXT = "_context";

	private static final String PARAM_PROTON_OBJECT_URI = "_protontObj";

	private static final String PROTON_OBJECT_URI = "http://proton.semanticweb.org/2006/05/protont#Object";

	/**
	 * Initializes a new rule with given title.
	 * @param ruleTitle Rule title.
	 */
	public ProtonAlignRule(String ruleTitle) {
		super(ruleTitle);
	}

	/**
	 * Creates subclass of protont:Object statements for all classes which don't have any defined super class.
	 * These classes are class hierarchy roots, so the whole ontology will be aligned to PROTON Ontology Top Module.
	 * @return Iteration of subclass definition statements.
	 * @throws RuleExportException when the query execution fails.
	 */
	@Override
	public GraphQueryResult getStatements() throws RuleExportException {
		try {
			RepositoryWrapper repository = getRepository();
			Map<String, Value> params = new HashMap<>();
			params.put(PARAM_CONTEXT, repository.createUri(repository.getContext()));
			params.put(PARAM_PROTON_OBJECT_URI, repository.createUri(PROTON_OBJECT_URI));
			return repository.executeGraphQuery(EXPORT_QUERY, params, false);
		} catch (Exception e) {
			throw new RuleExportException("Error while exporting rules.", e);
		}
	}
}
