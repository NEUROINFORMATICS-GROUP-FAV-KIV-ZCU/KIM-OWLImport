package cz.zcu.kiv.eeg.owlimport;

import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.SourceImportException;
import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.LocalRepositoryManager;
import org.openrdf.rio.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class RepositoryManager {
	private static final String DEFAULT_NAMESPACE = "http://eeg.kiv.zcu.cz/#";
	private static final String DEFAULT_CONFIG = "config/owlim.ttl";

	private static final String REPOSITORY_URI = "http://www.openrdf.org/config/repository#Repository";
	private static final String REPOSITORY_ID_URI = "http://www.openrdf.org/config/repository#repositoryID";

	private LocalRepositoryManager repositoryManager;

	private Graph repositoryConfigTemplate;
	private Resource repositoryNode;

	private List<RepositoryWrapper> createdRepositories;

	public RepositoryManager() throws RepositoryManagerException {
		createdRepositories = new LinkedList<>();
		InputStream configStream = getClass().getResourceAsStream(DEFAULT_CONFIG);
		try {
			loadConfigurationTemplate(configStream);

			repositoryManager = new LocalRepositoryManager(new File("."));
			repositoryManager.initialize();
		} catch (RDFParseException e) {
			throw new RepositoryManagerException("An error occurred while parsing the configuration file.");
		} catch (IOException e) {
			throw new RepositoryManagerException("An error occurred while reading the configuration file.", e);
		} catch (RDFHandlerException e) {
			throw new RepositoryManagerException("An error occurred while creating the configuration graph.", e);
		} catch (RepositoryException e) {
			throw new RepositoryManagerException("An error occurred while initializing the repository manager.", e);
		}
	}

	private void loadConfigurationTemplate(InputStream stream) throws RDFParseException, IOException, RDFHandlerException {
		repositoryConfigTemplate = loadGraph(stream);

		findRepositoryNode();
		findRepositoryIdNode();
	}

	private Graph loadGraph(InputStream stream) throws RDFParseException, IOException, RDFHandlerException {
		Graph config = new GraphImpl();
		RDFParser parser = Rio.createParser(RDFFormat.TURTLE);
		parser.setRDFHandler(new GraphLoadRdfHandler(config));
		parser.parse(stream, DEFAULT_NAMESPACE);
		return config;
	}

	private void findRepositoryNode() {
		Iterator<Statement> iterator = repositoryConfigTemplate.match(null, RDF.TYPE, new URIImpl(REPOSITORY_URI));
		if (iterator.hasNext()) {
			repositoryNode = iterator.next().getSubject();
		}
	}

	private Statement findRepositoryIdNode() {
		Iterator<Statement> iterator = repositoryConfigTemplate.match(repositoryNode, new URIImpl(REPOSITORY_ID_URI), null);
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			throw new InvalidStateException("Repository ID cannot be found in template.");
		}
	}

	public RepositoryWrapper createRepository(String name) throws RepositoryManagerException {
		try {
			String id = repositoryManager.getNewRepositoryID(name);
			RepositoryConfig config = createRepositoryConfig(id);
			repositoryManager.addRepositoryConfig(config);
			RepositoryWrapper wrapper = new RepositoryWrapper(repositoryManager.getRepository(id));
			createdRepositories.add(wrapper);
			return wrapper;
		} catch (RepositoryException e) {
			throw new RepositoryManagerException(e);
		} catch (RepositoryConfigException e) {
			throw new RepositoryManagerException(e);
		}
	}


	public RepositoryConfig createRepositoryConfig(String id) throws RepositoryConfigException {
		Statement oldId = findRepositoryIdNode();
		ValueFactory valueFactory = repositoryConfigTemplate.getValueFactory();
		Statement newId = valueFactory.createStatement(oldId.getSubject(), oldId.getPredicate(),
				valueFactory.createLiteral(id), oldId.getContext());
		boolean removed = repositoryConfigTemplate.remove(oldId);
		repositoryConfigTemplate.add(newId);

		return RepositoryConfig.create(repositoryConfigTemplate, repositoryNode);
	}

	public RepositoryWrapper getRepository(String id) throws RepositoryManagerException {
		try {
			return new RepositoryWrapper(repositoryManager.getRepository(id));
		} catch (RepositoryConfigException e) {
			throw new RepositoryManagerException(e);
		} catch (RepositoryException e) {
			throw new RepositoryManagerException(e);
		}
	}


	public void importSource(AbstractSource source) throws SourceImportException {
		try {
			RepositoryWrapper repository = createRepository(source.getTitle());
			source.importToRepository(repository);
			source.attachRepository(repository);
		} catch (RepositoryManagerException e) {
			throw new SourceImportException("Error while importing sources.", e);
		}
	}



	public void close() {
		for (RepositoryWrapper repository : createdRepositories) {
			try {
				repository.close();
			} catch (RepositoryException e) {
				// log error and ignore
			}
		}
		repositoryManager.shutDown();
	}

}
