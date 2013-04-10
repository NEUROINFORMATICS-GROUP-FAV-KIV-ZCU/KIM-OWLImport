package cz.zcu.kiv.eeg.owlimport.model.rules.export;

import cz.zcu.kiv.eeg.owlimport.RepositoryWrapper;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractEmptyParamsRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.RuleExportException;
import info.aduna.iteration.Iteration;
import info.aduna.iteration.IteratorIteration;
import info.aduna.iteration.UnionIteration;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.query.QueryEvaluationException;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class TrustedSourceRule extends AbstractEmptyParamsRule {
	private static final String RDF_TYPE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static final String TRUSTED_SOURCE_CLASS_URI = "http://proton.semanticweb.org/2006/05/protons#Trusted";

	private static final String SOURCE_BASE_URI = "http://kiv.zcu.cz/eeg/KIM/sources";

	private static final String ENTITIES_QUERY =
			"CONSTRUCT {Entity} <http://proton.semanticweb.org/2006/05/protons#generatedBy> {_sourceUri} " +
			"FROM {Entity} rdf:type {Type}, {Type} sesame:directType {ClassType}" +
			"WHERE ClassType = owl:Class";

	private static final String SOURCE_URI_PARAM = "_sourceUri";

	public TrustedSourceRule(String ruleTitle) {
		super(ruleTitle);
	}

	@Override
	public Iteration<Statement, ? extends Exception> getStatements() throws RuleExportException {
		GraphImpl graph = new GraphImpl();
		ValueFactory graphValueFactory = graph.getValueFactory();

		String sourceUri = String.format("%s#%s", SOURCE_BASE_URI, createUriPart(getSource().getTitle()));
		graph.add(graphValueFactory.createURI(sourceUri), graphValueFactory.createURI(RDF_TYPE_URI), graphValueFactory.createURI(TRUSTED_SOURCE_CLASS_URI));

		Iteration<Statement, QueryEvaluationException> sourceDef =  new IteratorIteration<>(graph.iterator());

		RepositoryWrapper repository = getRepository();
		Map<String, Value> queryParams = new HashMap<String, Value>();
		queryParams.put(SOURCE_URI_PARAM, repository.createLiteral(sourceUri));

		Iteration<Statement, QueryEvaluationException> entities;
		try {
			entities = repository.executeGraphQuery(ENTITIES_QUERY, queryParams, true);
		} catch (Exception e) {
			throw new RuleExportException("Error while exporting entities from repository.", e);
		}

		return new UnionIteration<Statement, QueryEvaluationException>(sourceDef, entities);
	}


	private String createUriPart(String title) {
		String noAccents = removeAccents(title);
		return noAccents.replaceAll("[^A-Za-z0-9_\\.-]", "-").replaceAll("-{2,}", "-");
	}


	private String removeAccents(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
