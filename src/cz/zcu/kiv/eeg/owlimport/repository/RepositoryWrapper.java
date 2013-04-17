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
 * Wrapper for OWLIM repositories.
 *
 * Provides simplified API for RDF import and SeRQL querying.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class RepositoryWrapper {
	/** Underlying repository. */
	private Repository repository;

	/** Connection for the repository. */
	private RepositoryConnection conn;

	/** Value factory. */
	private ValueFactory valueFactory;

	/** Context URL of imported statements. */
	private String context;

	/** Query which selects all defined datatype properties. */
	private static final String SELECT_PROPERTIES_QUERY = "SELECT Prop " +
			"FROM {Prop} rdf:type {PropType} WHERE PropType = owl:DatatypeProperty";

	/** Cached list of datatype properties. */
	private List<String> propertiesList;


	/**
	 * Creates the wrapper above the specified repository and initializes the repository connection.
	 * @param repo OWLIM repository.
	 * @throws RepositoryException when the connection with repository fails.
	 */
	public RepositoryWrapper(Repository repo) throws RepositoryException {
		repository = repo;
		conn = repo.getConnection();
		conn.setAutoCommit(false);
		valueFactory = repo.getValueFactory();
	}

	/**
	 * Gets the context of imported statements.
	 * @return String containing context URL.
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Imports the specified OWL file to the repository. Given base URL will be used as context of imported statements.
	 * @param file File to be imported.
	 * @param baseUrl Base URL of the specified ontology.
	 * @throws RepositoryException when the import to repository fails.
	 * @throws RDFParseException when the specified file could not be parsed.
	 * @throws IOException when the specified file could not be read.
	 */
	public void importFile(File file, String baseUrl) throws RepositoryException, RDFParseException, IOException {
		importFile(file, baseUrl, baseUrl);
	}


	/**
	 * Imports the specified OWL file to the repository with given context URL.
	 * @param file File to be imported.
	 * @param baseUrl Base URL of the specified ontology.
	 * @param context String with context URL of imported statements.
	 * @throws RepositoryException when the import to repository fails.
	 * @throws RDFParseException when the specified file could not be parsed.
	 * @throws IOException when the specified file could not be read.
	 */
	public void importFile(File file, String baseUrl, String context) throws RepositoryException, RDFParseException, IOException {
		conn.add(file, baseUrl, RDFFormat.forFileName(file.getName(), RDFFormat.RDFXML), valueFactory.createURI(context));
		conn.commit();
		this.context = context;
		cleanCache();
	}

	/**
	 * Imports the OWL from specified URL to the repository. Given base URL will be used as context of imported statements.
	 * @param owlUrl OWL file URL.
	 * @param baseUrl Base URL of the given file.
	 * @throws IOException when the remote file could not be fetched.
	 * @throws RepositoryException when the import to repository fails.
	 * @throws RDFParseException when the specified file could not be parsed.
	 */
	public void importUrl(String owlUrl, String baseUrl) throws IOException, RepositoryException, RDFParseException {
		importUrl(owlUrl, baseUrl, baseUrl);
	}

	/**
	 * Imports the OWL from specified URL to the repository with given context URL.
	 * @param owlUrl OWL file URL.
	 * @param baseUrl Base URL of the given file.
	 * @param context String with context URL of imported statements.
	 * @throws IOException when the remote file could not be fetched.
	 * @throws RepositoryException when the import to repository fails.
	 * @throws RDFParseException when the specified file could not be parsed.
	 */
	public void importUrl(String owlUrl, String baseUrl, String context) throws IOException, RepositoryException, RDFParseException {
		URL url = new URL(owlUrl);
		conn.add(url, baseUrl, RDFFormat.forFileName(url.getFile(), RDFFormat.RDFXML), valueFactory.createURI(context));
		conn.commit();
		this.context = context;
		cleanCache();
	}


	/**
	 * Cleans the repository wrapper cache.
	 */
	private void cleanCache() {
		propertiesList = null;
	}


	/**
	 * Gets the list of all defined datatype properties.
	 * @return List with URLs of defined datatype properties.
	 * @throws MalformedQueryException when the query could not be parsed by the repository.
	 * @throws RepositoryException when the repository query fails.
	 * @throws QueryEvaluationException when the query could not be evaluated.
	 */
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

	/**
	 * Executes the SeRQL graph query with no parameters. Inferred statements will not be included.
	 * @param query SeRQL CONSTRUCT query.
	 * @return Query result.
	 * @throws MalformedQueryException when the query could not be parsed by the repository.
	 * @throws RepositoryException when the repository query fails.
	 * @throws QueryEvaluationException when the query could not be evaluated.
	 */
	public GraphQueryResult executeGraphQuery(String query) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
		return executeGraphQuery(query, new HashMap<String, Value>(), false);
	}

	/**
	 * Executes the SeRQL graph query with given parameters. Inferred statements will not be included.
	 * @param query SeRQL construct query.
	 * @param params Query parameters.
	 * @return Query result.
	 * @throws MalformedQueryException when the query could not be parsed by the repository.
	 * @throws RepositoryException when the repository query fails.
	 * @throws QueryEvaluationException when the query could not be evaluated.
	 */
	public GraphQueryResult executeGraphQuery(String query, Map<String, Value> params) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
		return executeGraphQuery(query, params, false);
	}

	/**
	 * Executes the SeRQL graph query with given parameters.
	 * @param query SeRQL construct query.
	 * @param params Query parameters.
	 * @param includeInferred Include inferred statements?
	 * @return Query result.
	 * @throws MalformedQueryException when the query could not be parsed by the repository.
	 * @throws RepositoryException when the repository query fails.
	 * @throws QueryEvaluationException when the query could not be evaluated.
	 */
	public GraphQueryResult executeGraphQuery(String query, Map<String, Value> params, boolean includeInferred) throws QueryEvaluationException, MalformedQueryException, RepositoryException {
		GraphQuery graphQuery = conn.prepareGraphQuery(QueryLanguage.SERQL, query);
		graphQuery.setIncludeInferred(includeInferred);
		for (Map.Entry<String, Value> param : params.entrySet()) {
			graphQuery.setBinding(param.getKey(), param.getValue());
		}
		return graphQuery.evaluate();
	}

	/**
	 * Closes the repository connection and shuts down the repository.
	 * @throws RepositoryException when the repository could not be shut down.
	 */
	public void close() throws RepositoryException {
		conn.close();
		repository.shutDown();
	}


	/**
	 * Creates URI resource from string.
	 * @param uri URI string.
	 * @return URI resource object.
	 */
	public URI createUri(String uri) {
		return valueFactory.createURI(uri);
	}

	/**
	 * Creates string value literal.
	 * @param str String value.
	 * @return String literal object.
	 */
	public Literal createLiteral(String str) {
		return valueFactory.createLiteral(str);
	}

	/**
	 * Creates numeric value literal.
	 * @param num Integer value.
	 * @return Numeric literal object.
	 */
	public Literal createLiteral(int num) {
		return valueFactory.createLiteral(num);
	}

	/**
	 * Creates numeric value literal
	 * @param num Long integer value.
	 * @return Numeric literal object.
	 */
	public Literal createLiteral(long num) {
		return valueFactory.createLiteral(num);
	}
}
