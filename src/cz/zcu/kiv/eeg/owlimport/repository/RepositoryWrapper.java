package cz.zcu.kiv.eeg.owlimport.repository;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jan Smitka <jan@smitka.org>
 *
 * Tested queries:

CONSTRUCT {Super} rdf:type {Type}
FROM CONTEXT <http://eeg.kiv.zcu.cz/test>
[{Class} sesame:directSubClassOf {Super}], {Super} rdf:type {Type}; rdfs:subClassOf {Root}
WHERE NOT BOUND(Class) AND Root = <http://eeg.kiv.zcu.cz/test#NegativeComponent>

CONSTRUCT {Entity} prop {Value}
FROM {Entity} prop {Value}; rdf:type {Class}, {prop} rdf:type {PropType}
WHERE PropType = owl:DatatypeProperty AND Class = <http://kiv.zcu.cz/eegbase#Hardware>

CONSTRUCT {Prop} rdfs:domain {Domain}
FROM {Prop} rdf:type {PropType}; [rdfs:domain {Domain}]
WHERE PropType = owl:DatatypeProperty

 */
public class RepositoryWrapper {
	private Repository repository;

	private RepositoryConnection conn;

	private ValueFactory valueFactory;

	private String context;

	private static final String SELECT_PROPERTIES_QUERY = "SELECT Prop " +
			"FROM {Prop} rdf:type {PropType} WHERE PropType = owl:DatatypeProperty";


	private List<String> propertiesList;


	public RepositoryWrapper(Repository repo) throws RepositoryException {
		repository = repo;
		conn = repo.getConnection();
		conn.setAutoCommit(false);
		valueFactory = repo.getValueFactory();
	}

	public String getContext() {
		return context;
	}

	public void importFile(File file, String baseUrl) throws RepositoryException, RDFParseException, IOException {
		importFile(file, baseUrl, baseUrl);
	}


	public void importFile(File file, String baseUrl, String context) throws RepositoryException, RDFParseException, IOException {
		conn.add(file, baseUrl, RDFFormat.forFileName(file.getName(), RDFFormat.RDFXML), valueFactory.createURI(context));
		conn.commit();
		this.context = context;
		cleanCache();
	}

	public void importUrl(String owlUrl, String baseUrl) throws IOException, RepositoryException, RDFParseException {
		importUrl(owlUrl, baseUrl, baseUrl);
	}

	public void importUrl(String owlUrl, String baseUrl, String context) throws IOException, RepositoryException, RDFParseException {
		URL url = new URL(owlUrl);
		conn.add(url, baseUrl, RDFFormat.forFileName(url.getFile(), RDFFormat.RDFXML), valueFactory.createURI(context));
		conn.commit();
		this.context = context;
		cleanCache();
	}

	private void cleanCache() {
		propertiesList = null;
	}


	public List<String> getAllProperties() throws MalformedQueryException, RepositoryException, QueryEvaluationException {
		if (propertiesList == null) {
			propertiesList = new LinkedList<>();
			TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SERQL, SELECT_PROPERTIES_QUERY);
			TupleQueryResult res = query.evaluate();
			while (res.hasNext()) {
				BindingSet row = res.next();
				propertiesList.add(row.getValue("Prop").stringValue());
			}
			res.close();
		}
		return propertiesList;
	}

	public GraphQueryResult executeGraphQuery(String query) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
		return executeGraphQuery(query, new HashMap<String, Value>(), false);
	}

	public GraphQueryResult executeGraphQuery(String query, Map<String, Value> params) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
		return executeGraphQuery(query, params, false);
	}

	public GraphQueryResult executeGraphQuery(String query, Map<String, Value> params, boolean includeInferred) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
		GraphQuery graphQuery = conn.prepareGraphQuery(QueryLanguage.SERQL, query);
		graphQuery.setIncludeInferred(includeInferred);
		for (Map.Entry<String, Value> param : params.entrySet()) {
			graphQuery.setBinding(param.getKey(), param.getValue());
		}
		return graphQuery.evaluate();
	}

	public void close() throws RepositoryException {
		conn.close();
		repository.shutDown();
	}



	public URI createUri(String uri) {
		return valueFactory.createURI(uri);
	}

	public Literal createLiteral(String str) {
		return valueFactory.createLiteral(str);
	}

	public Literal createLiteral(int num) {
		return valueFactory.createLiteral(num);
	}

	public Literal createLiteral(long num) {
		return valueFactory.createLiteral(num);
	}
}
