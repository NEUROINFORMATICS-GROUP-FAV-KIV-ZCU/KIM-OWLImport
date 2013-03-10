package cz.zcu.kiv.eeg.owlimport.project;

import cz.zcu.kiv.eeg.owlimport.model.RuleManager;
import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleParams;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceFactory;
import cz.zcu.kiv.eeg.owlimport.model.sources.ISourceParams;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class ProjectReader {
	private SourceManager sourceManager;

	private RuleManager ruleManager;

	private File file;


	public ProjectReader(SourceManager srcManager, RuleManager rlManager, File inputFile) {
		sourceManager = srcManager;
		ruleManager = rlManager;
		file = inputFile;
	}


	public List<AbstractSource> load() throws ProjectReadException {
		List<AbstractSource> sources = new LinkedList<>();
		try {
			InputStream in = openFile();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(in);

			reader.nextTag();
			reader.require(XMLStreamConstants.START_ELEMENT, null, Elements.SOURCES);
			while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
				sources.add(loadSource(reader));
			}

			in.close();
			return sources;
		} catch (IOException e) {
			throw new ProjectReadException("Error while reading project file.", e);
		} catch (XMLStreamException e) {
			throw new ProjectReadException("Error while reading XML data.", e);
		}
	}


	private InputStream openFile() throws FileNotFoundException {
		FileInputStream fs = new FileInputStream(file);
		BufferedInputStream bfs = new BufferedInputStream(fs);
		return bfs;
	}

	private String sourceTitle;

	private String sourceBaseUri;

	private ISourceParams sourceParams;

	private List<AbstractRule> sourceRules;

	private AbstractSource loadSource(XMLStreamReader reader) throws XMLStreamException {
		reader.require(XMLStreamConstants.START_ELEMENT, null, Elements.SOURCE);
		String type = reader.getAttributeValue(null, Attributes.SOURCE_TYPE);
		ISourceFactory factory = sourceManager.getFactoryFor(type);
		sourceTitle = sourceBaseUri = null;
		sourceParams = null;
		sourceRules = new LinkedList<>();
		while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
			loadSourceMember(reader, factory);
		}

		AbstractSource source = factory.createSource(sourceTitle, sourceBaseUri, sourceParams);
		for (AbstractRule rule : sourceRules) {
			source.addRule(rule);
		}

		return source;
	}

	private void loadSourceMember(XMLStreamReader reader, ISourceFactory factory) throws XMLStreamException {
		String elName = reader.getLocalName();
		if (elName.equals(Elements.SOURCE_TITLE)) {
			sourceTitle = XmlReaderUtils.loadElementText(reader);
		} else if (elName.equals(Elements.SOURCE_BASE_URI)) {
			sourceBaseUri = XmlReaderUtils.loadElementText(reader);
		} else if (elName.equals(Elements.SOURCE_LOCATION)) {
			sourceParams = factory.loadParams(reader);
		} else if (elName.equals(Elements.RULES)) {
			loadSourceRules(reader);
		} else {
			throw XmlReaderUtils.unexpectedElementException(reader);
		}
	}

	private String ruleTitle;

	private IRuleParams ruleParams;

	private void loadSourceRules(XMLStreamReader reader) throws XMLStreamException {
		while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
			reader.require(XMLStreamConstants.START_ELEMENT, null, Elements.RULE);
			String type = reader.getAttributeValue(null, Attributes.RULE_TYPE);
			IRuleFactory factory = ruleManager.getFactoryFor(type);
			ruleTitle = null;
			ruleParams = null;
			while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
				loadRuleMember(reader, factory);
			}

			AbstractRule rule = factory.createRule(ruleTitle);
			rule.setRuleParams(ruleParams);
			sourceRules.add(rule);
		}
	}

	private void loadRuleMember(XMLStreamReader reader, IRuleFactory factory) throws XMLStreamException {
		String elName = reader.getLocalName();
		if (elName.equals(Elements.RULE_TITLE)) {
			ruleTitle = XmlReaderUtils.loadElementText(reader);
		} else if (elName.equals(Elements.RULE_PARAMS)) {
			ruleParams = factory.loadParams(reader);
		} else {
			throw XmlReaderUtils.unexpectedElementException(reader);
		}
	}


}
