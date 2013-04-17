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
 * @author Jan Smitka <jan@smitka.org>
 */
public class VisibilityGenerator {
	private static final String EXPORT_VISIBLITY_QUERY = "CONSTRUCT {Class} <http://www.ontotext.com/kim/2006/05/kimso#visibilityLevel1> {\"\"} " +
			"FROM CONTEXT _context {Class} sesame:directType {ClassType} WHERE ClassType = owl:Class";

	private File outputFile;

	private OutputStream outputStream;

	private RDFWriter rdfWriter;

	public VisibilityGenerator(File file) {
		outputFile = file;
	}


	private ExportException exportException(Throwable cause) {
		return new ExportException("An error occurred while generating visiblity for one of the sources. The resulting file might be incomplete.", cause);
	}

	public void generateVisiblity(List<AbstractSource> sources) throws ExportException{
		openFile();
		for (AbstractSource source : sources) {
			generateVisibilityForSource(source);
		}
		closeFile();
	}

	private void generateVisibilityForSource(AbstractSource source) throws ExportException {
		RepositoryWrapper repository = source.getRepository();

		try {
			GraphQueryResult queryResult = repository.executeGraphQuery(EXPORT_VISIBLITY_QUERY);
			writeStatements(queryResult);
		} catch (QueryEvaluationException|MalformedQueryException|RepositoryException|RDFHandlerException e) {
			throw exportException(e);
		}
	}

	private void writeStatements(GraphQueryResult queryResult) throws RDFHandlerException, QueryEvaluationException {
		while (queryResult.hasNext()) {
			rdfWriter.handleStatement(queryResult.next());
		}

		queryResult.close();
	}

	private void openFile() throws ExportException {
		try {
			outputStream = createStream();
			rdfWriter = Rio.createWriter(RDFFormat.NTRIPLES, outputStream);
			rdfWriter.startRDF();
		} catch (Exception e) {
			throw exportException(e);
		}
	}

	private OutputStream createStream() throws IOException {
		FileOutputStream fileStream = new FileOutputStream(outputFile);
		return new BufferedOutputStream(fileStream);
	}




	private void closeFile() throws ExportException {
		try {
			rdfWriter.endRDF();
			outputStream.close();
		} catch (Exception e) {
			throw exportException(e);
		}
	}
}
