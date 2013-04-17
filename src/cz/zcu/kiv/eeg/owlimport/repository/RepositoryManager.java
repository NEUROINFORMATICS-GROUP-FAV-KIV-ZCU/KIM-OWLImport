package cz.zcu.kiv.eeg.owlimport.repository;

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
 * Repository manager handles creation of semantic repositories and import of detached ontology sources.
 *
 * @author Jan Smitka <jan@smitka.org>
 */
public class RepositoryManager {
	/** Directory where the repository metadata is stored. */
	private static final String REPOSITORIES_DIR = "repositories";

	/** Default namespace. */
	private static final String DEFAULT_NAMESPACE = "http://eeg.kiv.zcu.cz/#";
	/** Default configuration file path. */
	private static final String DEFAULT_CONFIG = "config/owlim.ttl";

	/** Repository element URI in configuration graph. */
	private static final String REPOSITORY_URI = "http://www.openrdf.org/config/repository#Repository";
	/** Repository ID element URI in configuration graph. */
	private static final String REPOSITORY_ID_URI = "http://www.openrdf.org/config/repository#repositoryID";

	/** Underlying repository manager. */
	private LocalRepositoryManager repositoryManager;

	/** RDF graph of repository configuration template. */
	private Graph repositoryConfigTemplate;
	/** Repository node in configuration graph. */
	private Resource repositoryNode;

	/** List of created and managed repositories. */
	private List<RepositoryWrapper> createdRepositories;

	/**
	 * Creates a new repository manager, loads configuration template and initializes new Sesame local repository manager.
	 * @throws RepositoryManagerException
	 */
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

	/**
	 * Loads repository configuration template from given stream.
	 * @param stream Stream with RDF graph of configuration.
	 * @throws RDFParseException when the configuration could not be parsed.
	 * @throws IOException when the configuration could not be loaded.
	 * @throws RDFHandlerException when the configuration graph could not be created.
	 */
	private void loadConfigurationTemplate(InputStream stream) throws RDFParseException, IOException, RDFHandlerException {
		repositoryConfigTemplate = loadGraph(stream);

		findRepositoryNode();
	}

	/**
	 * Loads the RDF graph from given stream.
	 * @param stream Stream with RDF graph data.
	 * @return Loaded graph.
	 * @throws RDFParseException when the graph could not be parsed.
	 * @throws IOException when the stream could not be read.
	 * @throws RDFHandlerException when the graph could not be created.
	 */
	private Graph loadGraph(InputStream stream) throws RDFParseException, IOException, RDFHandlerException {
		Graph config = new GraphImpl();
		RDFParser parser = Rio.createParser(RDFFormat.TURTLE);
		parser.setRDFHandler(new GraphLoadRdfHandler(config));
		parser.parse(stream, DEFAULT_NAMESPACE);
		return config;
	}

	/**
	 * Finds the repository node in configuration graph.
	 */
	private void findRepositoryNode() {
		Iterator<Statement> iterator = repositoryConfigTemplate.match(null, RDF.TYPE, new URIImpl(REPOSITORY_URI));
		if (iterator.hasNext()) {
			repositoryNode = iterator.next().getSubject();
		}
	}

	/**
	 * Finds the repository ID statement in the repository node.
	 * @return Repository ID statement.
	 */
	private Statement findRepositoryIdNode() {
		Iterator<Statement> iterator = repositoryConfigTemplate.match(repositoryNode, new URIImpl(REPOSITORY_ID_URI), null);
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			throw new InvalidStateException("Repository ID cannot be found in template.");
		}
	}


	/**
	 * Creates a new repository with given ID.
	 * @param id Repository ID.
	 * @return Wrapper for the created repository.
	 * @throws RepositoryManagerException when the repository could not be created.
	 */
	public RepositoryWrapper createRepository(String id) throws RepositoryManagerException {
		try {
			String uniqId = repositoryManager.getNewRepositoryID(id);
			RepositoryConfig config = createRepositoryConfig(uniqId);
			repositoryManager.addRepositoryConfig(config);
			RepositoryWrapper wrapper = new RepositoryWrapper(repositoryManager.getRepository(uniqId));
			createdRepositories.add(wrapper);
			return wrapper;
		} catch (RepositoryException|RepositoryConfigException e) {
			throw new RepositoryManagerException(e);
		}
	}


	/**
	 * Creates a new repository configuration.
	 * @param id Repository ID.
	 * @return Configuration with given repository ID.
	 * @throws RepositoryConfigException when the configuration could not be created.
	 */
	private RepositoryConfig createRepositoryConfig(String id) throws RepositoryConfigException {
		Statement oldId = findRepositoryIdNode();
		ValueFactory valueFactory = repositoryConfigTemplate.getValueFactory();
		Statement newId = valueFactory.createStatement(oldId.getSubject(), oldId.getPredicate(),
				valueFactory.createLiteral(id), oldId.getContext());
		repositoryConfigTemplate.remove(oldId);
		repositoryConfigTemplate.add(newId);

		return RepositoryConfig.create(repositoryConfigTemplate, repositoryNode);
	}

	/**
	 * Gets the repository wrapper for previously created repository with the given ID.
	 * @param id Repository ID.
	 * @return Repository wrapper.
	 * @throws RepositoryManagerException when the repository cannot be found.
	 */
	public RepositoryWrapper getRepository(String id) throws RepositoryManagerException {
		try {
			return new RepositoryWrapper(repositoryManager.getRepository(id));
		} catch (RepositoryConfigException|RepositoryException e) {
			throw new RepositoryManagerException(e);
		}
	}


	/**
	 * Creates a new repository and imports the given source into it.
	 * @param source Ontology source.
	 * @throws SourceImportException when the source could not be imported or the repository could not be created.
	 */
	public void importSource(AbstractSource source) throws SourceImportException {
		try {
			RepositoryWrapper repository = createRepository(source.getTitle());
			source.importToRepository(repository);
			source.attachRepository(repository);
		} catch (RepositoryManagerException e) {
			throw new SourceImportException("Error while importing sources.", e);
		}
	}


	/**
	 * Import the list of given ontology sources.
	 * @param sources List of ontology sources.
	 * @throws SourceImportException when one of the sources could not be imported.
	 */
	public void importSources(List<AbstractSource> sources) throws SourceImportException {
		for (AbstractSource source : sources) {
			if (!source.hasRepositoryAttached()) {
				importSource(source);
			}
		}
	}


	/**
	 * Closes the repository manager and all created repositories. Cleans the created repository metadata.
	 */
	public void close() {
		for (RepositoryWrapper repository : createdRepositories) {
			try {
				repository.close();
			} catch (RepositoryException e) {
				// log error and ignore
			}
		}
		repositoryManager.shutDown();
		cleanupFiles();
	}

	/**
	 * Cleans up the repository metadata.
	 */
	private void cleanupFiles() {
		File repoDir = new File(REPOSITORIES_DIR);
		deleteFile(repoDir);
	}

	/**
	 * Recursively delete the given file or directory.
	 * @param file File to be deleted.
	 */
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deleteFile(f);
			}
		}
		file.delete();
	}

}
