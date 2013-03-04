package cz.zcu.kiv.eeg.owlimport;

import org.openrdf.query.GraphQueryResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Jan Smitka <jan@smitka.org>
 */
public class Main {


	public static void consoleMain(String[] args) {
		try {
			RepositoryManager manager = new RepositoryManager();

			RepositoryWrapper repo = manager.createRepository("test");
			repo.importFile(new File("../eegdatabase.owl"), "http://kiv.zcu.cz/eegbase#");

			ResultWriter writer = new ResultWriter();
			GraphQueryResult res = repo.exportInstances("http://kiv.zcu.cz/eegbase#", "http://kiv.zcu.cz/eegbase#title");
			File f = new File("../export.n3");
			OutputStream stream = new FileOutputStream(f);
			writer.exportStatements(res, stream);
			stream.close();

			manager.close();

		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

}
