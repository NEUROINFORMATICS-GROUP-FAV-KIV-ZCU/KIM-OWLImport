package cz.zcu.kiv.eeg.owlimport.project;

import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.repository.RepositoryWrapper;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.*;
import java.util.List;

/**
 * Visibility generator handles generating the visiblity.nt file for KIM web UI.
 *
 * Visiblity rules are written in N-Triples format.
 * @author Jan Smitka <jan@smitka.org>
 */
public class VisibilityGenerator {
	/**
	 * Query to export visiblity rules.
	 */
	private static final String EXPORT_VISIBLITY_QUERY = "CONSTRUCT DISTINCT {Class} <http://www.ontotext.com/kim/2006/05/kimso#visibilityLevel1> {\"\"} " +
			"FROM {Class} rdf:type {ClassType} WHERE ClassType = owl:Class OR ClassType = owl:ObjectProperty OR ClassType = owl:DatatypeProperty";

	/** Output file. */
	private File outputFile;

	/** Output stream. */
	private OutputStream outputStream;

	/** RDF output writer. */
	private RDFWriter rdfWriter;

	/**
	 * Initializes visibility generator. Output will be written to the given file.
	 * @param file Output file.
	 */
	public VisibilityGenerator(File file) {
		outputFile = file;
	}


	/**
	 * Creates a generic export exception.
	 * @param cause Causing exception.
	 * @return Exception.
	 */
	private ExportException exportException(Throwable cause) {
		return new ExportException("An error occurred while generating visibility for one of the sources. The resulting file might be incomplete.", cause);
	}

	/**
	 * Generates and writes out visibility rules for the given sources.
	 * @param sources List of sources.
	 * @throws ExportException when the visibility cannot be generated.
	 */
	public void generateVisibility(List<AbstractSource> sources) throws ExportException{
		openFile();
		for (AbstractSource source : sources) {
			generateVisibilityForSource(source);
		}
		closeFile();
	}

	/**
	 * Generates and writes out visibility rules of the given source.
	 * @param source Source.
	 * @throws ExportException when the visibility rules cannot be generated.
	 */
	private void generateVisibilityForSource(AbstractSource source) throws ExportException {
		RepositoryWrapper repository = source.getRepository();

		try {
			GraphQueryResult queryResult = repository.executeGraphQuery(EXPORT_VISIBLITY_QUERY);
			writeStatements(queryResult);
		} catch (QueryEvaluationException|MalformedQueryException|RepositoryException|RDFHandlerException e) {
			throw exportException(e);
		}
	}

	/**
	 * Writes out the generated statements.
	 * @param queryResult Result of export query.
	 * @throws RDFHandlerException
	 * @throws QueryEvaluationException
	 */
	private void writeStatements(GraphQueryResult queryResult) throws RDFHandlerException, QueryEvaluationException {
		while (queryResult.hasNext()) {
			rdfWriter.handleStatement(queryResult.next());
		}

		queryResult.close();
	}

	/**
	 * Opens the output file and initializes RDF writer.
	 * @throws ExportException when the file cannot be opened.
	 */
	private void openFile() throws ExportException {
		try {
			outputStream = createStream();
			rdfWriter = Rio.createWriter(RDFFormat.NTRIPLES, outputStream);
			rdfWriter.startRDF();
		} catch (Exception e) {
			throw exportException(e);
		}
	}

	/**
	 * Creates the output stream.
	 * @return Output stream.
	 * @throws IOException
	 */
	private OutputStream createStream() throws IOException {
		FileOutputStream fileStream = new FileOutputStream(outputFile);
		return new BufferedOutputStream(fileStream);
	}


	/**
	 * Closes the output file.
	 * @throws ExportException when the file cannot be closed and finalized.
	 */
	private void closeFile() throws ExportException {
		try {
			rdfWriter.endRDF();
			outputStream.close();
		} catch (Exception e) {
			throw exportException(e);
		}
	}
}
