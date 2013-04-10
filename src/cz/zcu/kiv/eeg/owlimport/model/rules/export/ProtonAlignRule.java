package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import org.openrdf.model.Value;
import org.openrdf.query.GraphQueryResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProtonAlignRule extends AbstractEmptyParamsRule {
	private static final String EXPORT_QUERY = "CONSTRUCT {Class} rdf:type {_protontObj} " +
			"FROM CONTEXT _context {Class} rdf:type {ClassType}; [sesame:directSubClassOf {Super}] " +
			"WHERE ClassType = owl:Class AND NOT BOUND(Super)";

	private static final String PARAM_CONTEXT = "_context";

	private static final String PARAM_PROTON_OBJECT_URI = "_protontObj";

	private static final String PROTON_OBJECT_URI = "http://proton.semanticweb.org/2006/05/protont#Object";


	public ProtonAlignRule(String ruleTitle) {
		super(ruleTitle);
	}

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
