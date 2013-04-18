package cz.zcu.kiv.eeg.owlimport.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import cz.zcu.kiv.eeg.owlimport.gui.dialog.FileOpenDialog;
import cz.zcu.kiv.eeg.owlimport.gui.dialog.FileSaveDialog;
import cz.zcu.kiv.eeg.owlimport.gui.model.RuleListModel;
import cz.zcu.kiv.eeg.owlimport.gui.model.SourceListModel;
import cz.zcu.kiv.eeg.owlimport.model.RuleManager;
import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.SourceImportException;
import cz.zcu.kiv.eeg.owlimport.project.*;
import cz.zcu.kiv.eeg.owlimport.repository.RepositoryManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main window of the program.
 * @author Jan Smitka <jan@smitka.org>
 */
public class MainDialog {
	private JList sourceList;
	private JList ruleList;
	private JPanel rootPanel;
	private JToolBar mainToolbar;
	private JButton importOWLButton;
	private JButton exportButton;
	private JButton addRuleButton;
	private JPanel ruleOptionsPanel;
	private JButton saveProjectButton;
	private JButton loadProjectButton;
	private JButton generateVisibilityButton;
	private JButton removeRuleButton;

	private RepositoryManager repositoryManager;

	private SourceManager sourceManager;

	private RuleManager ruleManager;

	private SourceListModel sourcesModel;

	private AbstractSource selectedSource;

	private RuleListModel rulesModel;

	private AbstractRule selectedRule;

	/**
	 * Initializes the window.
	 * @param repoManager Repository manager.
	 * @param srcManager Source manager.
	 * @param rlManager Rule manager.
	 */
	public MainDialog(RepositoryManager repoManager, SourceManager srcManager, RuleManager rlManager) {
		sourceManager = srcManager;
		repositoryManager = repoManager;
		ruleManager = rlManager;

		initSourceList();

		initializeSourceComponentsListeners();
		initializeRuleComponentsListeners();
		initializeProjectButtonsListeners();
	}

	/**
	 * Initializes the listeners for ontology source management components.
	 */
	private void initializeSourceComponentsListeners() {
		importOWLButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				importSource();
			}
		});
		sourceList.addListSelectionListener(new ListSingleSelectionAdapter(sourceList) {
			@Override
			public void selectionSelected(int selectedIndex, ListSelectionEvent e) {
				refreshRuleList(selectedIndex);
			}

			@Override
			public void selectionCanceled(ListSelectionEvent e) {
				clearRuleList();
			}
		});
	}

	/**
	 * Initializes listeners for rule management components.
	 */
	private void initializeRuleComponentsListeners() {
		addRuleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRule();
			}
		});

		removeRuleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedRule();
			}
		});


		ruleList.addListSelectionListener(new ListSingleSelectionAdapter(ruleList) {
			@Override
			public void selectionSelected(int selectedIndex, ListSelectionEvent e) {
				createRuleOptionsPanel(selectedIndex);
			}

			@Override
			public void selectionCanceled(ListSelectionEvent e) {
				removeRuleOptionsPanel();
			}
		});
	}

	/**
	 * Initializes listeners for project management buttons.
	 */
	private void initializeProjectButtonsListeners() {
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportSources();
			}
		});


		saveProjectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveProject();
			}
		});

		loadProjectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadProject();
			}
		});

		generateVisibilityButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateVisibility();
			}
		});
	}

	/**
	 * Shows import source dialog and imports the source to repository.
	 */
	private void importSource() {
		ImportSourceDialog dialog = new ImportSourceDialog(this.sourceManager);
		dialog.showInCenterOf($$$getRootComponent$$$());

		if (dialog.getDialogResult() == DialogResult.OK) {
			AbstractSource source = dialog.createSource();
			sourceManager.addSource(source);
			try {
				repositoryManager.importSource(source);
			} catch (SourceImportException ex) {
				handleError(ex);
			}
		}
	}

	/**
	 * Clears the rule list.
	 */
	private void clearRuleList() {
		selectedSource = null;
		setRuleListModel(null);
	}

	/**
	 * Populates the rule list from currently selected source.
	 * @param selectedIndex Selected index.
	 */
	private void refreshRuleList(int selectedIndex) {
		if (rulesModel != null) {
			rulesModel.detach();
		}

		selectedSource = sourcesModel.getElementAt(selectedIndex);
		rulesModel = new RuleListModel(selectedSource);
		setRuleListModel(rulesModel);
	}

	/**
	 * Shows add rule dialog and adds the new rule to the currently selected source.
	 */
	private void addRule() {
		if (selectedSource == null) {
			return;
		}

		AddRuleDialog dialog = new AddRuleDialog(ruleManager);
		dialog.showInCenterOf($$$getRootComponent$$$());

		if (dialog.getDialogResult() == DialogResult.OK) {
			AbstractRule rule = dialog.createRule();
			selectedSource.addRule(rule);
		}
	}


	/**
	 * Creates rule options panel for rule at specified index.
	 * @param selectedIndex Selected index.
	 */
	private void createRuleOptionsPanel(int selectedIndex) {
		ruleOptionsPanel.removeAll();

		selectedRule = rulesModel.getElementAt(selectedIndex);
		IRuleParamsComponent opt = selectedRule.getGuiComponent();
		JLabel title = new JLabel(selectedRule.getTitle());
		title.setBorder(new EmptyBorder(0, 0, 10, 0));
		ruleOptionsPanel.add(title, BorderLayout.NORTH);
		ruleOptionsPanel.add(opt.getPanel(), BorderLayout.CENTER);
		opt.refresh();
		ruleOptionsPanel.revalidate();
		getFrame().pack();
		getFrame().repaint();

		removeRuleButton.setEnabled(true);
	}

	/**
	 * Removes the rule options panel.
	 */
	private void removeRuleOptionsPanel() {
		ruleOptionsPanel.removeAll();
		ruleOptionsPanel.revalidate();
		getFrame().repaint();

		selectedRule = null;
		removeRuleButton.setEnabled(false);
	}

	/**
	 * Exports currently defined sources and their rules.
	 */
	private void exportSources() {
		FileSaveDialog dialog = new FileSaveDialog("export");
		dialog.addExtensionFilter("Turtle Files", new String[]{"ttl"}, true);

		if (dialog.showDialog($$$getRootComponent$$$()) == FileSaveDialog.CONFIRM_OPTION) {
			Exporter export = new Exporter(dialog.getSelectedFile());
			try {
				export.writeSources(sourceManager.getSources());
			} catch (ExportException ex) {
				handleError(ex);
			}
		}
	}

	/**
	 * Saves project.
	 */
	private void saveProject() {
		FileSaveDialog dialog = new FileSaveDialog("export");
		dialog.addExtensionFilter("XML Files", new String[]{"xml"}, true);

		if (dialog.showDialog($$$getRootComponent$$$()) == FileSaveDialog.CONFIRM_OPTION) {
			try {
				sourceManager.saveProject(dialog.getSelectedFile());
			} catch (ProjectWriteException ex) {
				handleError(ex);
			}
		}
	}

	/**
	 * Loads previously saved projects.
	 */
	private void loadProject() {
		FileOpenDialog dialog = new FileOpenDialog("open");
		dialog.addExtensionFilter("XML Files", new String[]{"xml"}, true);

		if (dialog.showDialog($$$getRootComponent$$$()) == FileOpenDialog.CONFIRM_OPTION) {
			try {
				sourceManager.loadProject(dialog.getSelectedFile(), ruleManager);
				repositoryManager.importSources(sourceManager.getSources());
			} catch (ProjectReadException | SourceImportException ex) {
				handleError(ex);
			}
		}
	}

	/**
	 * Asks user for file path and generates visibility file.
	 */
	private void generateVisibility() {
		FileSaveDialog dialog = new FileSaveDialog("save");
		dialog.addExtensionFilter("N-Triples Files", new String[]{"nt"}, true);

		if (dialog.showDialog($$$getRootComponent$$$()) == FileSaveDialog.CONFIRM_OPTION) {
			VisibilityGenerator visibilityGenerator = new VisibilityGenerator(dialog.getSelectedFile());
			try {
				visibilityGenerator.generateVisibility(sourceManager.getSources());
			} catch (ExportException ex) {
				handleError(ex);
			}
		}
	}

	/**
	 * Removes the currently selected rule.
	 */
	private void removeSelectedRule() {
		if (selectedRule != null) {
			selectedSource.removeRule(selectedRule);
		}
	}

	/**
	 * Handles error and shows an error message to user.
	 * @param e Exception.
	 */
	private void handleError(Exception e) {
		JOptionPane.showMessageDialog($$$getRootComponent$$$(), formatErrorMessage(e), "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Formats the exception message.
	 * @param e Exception.
	 * @return String containing exception message and messages of all exception causes.
	 */
	private String formatErrorMessage(Exception e) {
		StringBuilder str = new StringBuilder(e.getMessage());
		Throwable cause = e.getCause();
		while (cause != null) {
			str.append(System.getProperty("line.separator"));
			str.append("Caused by: ");
			str.append(cause.getMessage());
			cause = cause.getCause();
		}

		return str.toString();
	}

	/**
	 * Gets the window frame.
	 * @return Window frame.
	 */
	private JFrame getFrame() {
		return (JFrame) rootPanel.getTopLevelAncestor();
	}

	/**
	 * Sets the rule list model and enables/disables addRuleButton and removeRuleButton.
	 * @param model
	 */
	private void setRuleListModel(ListModel<AbstractRule> model) {
		ruleList.setModel(model);
		addRuleButton.setEnabled(model != null);
		removeRuleButton.setEnabled(model != null);
	}

	/**
	 * Initializes the source list.
	 */
	private void initSourceList() {
		sourcesModel = new SourceListModel(sourceManager);
		sourceList.setModel(sourcesModel);
	}

	/**
	 * Runs the application.
	 * @param repoManager Repository manager.
	 * @param srcManager Source manager.
	 * @param rlManager Rule manager.
	 */
	public static void run(final RepositoryManager repoManager, final SourceManager srcManager, final RuleManager rlManager) {
		initLookAndFeel();

		JFrame frame = new JFrame("KIM-OWLImport");
		ImageIcon icon = new ImageIcon(MainDialog.class.getResource("icons/kim-owlimport.png"));
		frame.setIconImage(icon.getImage());
		frame.setContentPane(new MainDialog(repoManager, srcManager, rlManager).rootPanel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				repoManager.close();
			}
		});
	}

	/**
	 * Initializes the application system look and feel.
	 */
	private static void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			// ignore the missing system look and feel - it's just the look and feel, nothing groundbreaking
		}
	}


	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		rootPanel = new JPanel();
		rootPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
		mainToolbar = new JToolBar();
		mainToolbar.setFloatable(false);
		mainToolbar.setMargin(new Insets(0, 3, 0, 0));
		mainToolbar.setRollover(true);
		mainToolbar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
		rootPanel.add(mainToolbar, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
		loadProjectButton = new JButton();
		loadProjectButton.setIcon(new ImageIcon(getClass().getResource("/cz/zcu/kiv/eeg/owlimport/gui/icons/open.png")));
		loadProjectButton.setText("Load Project");
		mainToolbar.add(loadProjectButton);
		final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
		mainToolbar.add(toolBar$Separator1);
		exportButton = new JButton();
		exportButton.setIcon(new ImageIcon(getClass().getResource("/cz/zcu/kiv/eeg/owlimport/gui/icons/export.png")));
		exportButton.setText("Export");
		mainToolbar.add(exportButton);
		generateVisibilityButton = new JButton();
		generateVisibilityButton.setIcon(new ImageIcon(getClass().getResource("/cz/zcu/kiv/eeg/owlimport/gui/icons/visibility.png")));
		generateVisibilityButton.setText("Generate Visiblity");
		mainToolbar.add(generateVisibilityButton);
		final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
		mainToolbar.add(toolBar$Separator2);
		saveProjectButton = new JButton();
		saveProjectButton.setIcon(new ImageIcon(getClass().getResource("/cz/zcu/kiv/eeg/owlimport/gui/icons/save.png")));
		saveProjectButton.setText("Save Project");
		mainToolbar.add(saveProjectButton);
		final JScrollPane scrollPane1 = new JScrollPane();
		rootPanel.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(177, 128), null, 0, false));
		sourceList = new JList();
		sourceList.setSelectionMode(0);
		scrollPane1.setViewportView(sourceList);
		final JScrollPane scrollPane2 = new JScrollPane();
		rootPanel.add(scrollPane2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(199, 128), null, 0, false));
		ruleList = new JList();
		ruleList.setSelectionMode(0);
		scrollPane2.setViewportView(ruleList);
		final JScrollPane scrollPane3 = new JScrollPane();
		rootPanel.add(scrollPane3, new GridConstraints(1, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(500, 300), null, 0, false));
		ruleOptionsPanel = new JPanel();
		ruleOptionsPanel.setLayout(new BorderLayout(0, 0));
		scrollPane3.setViewportView(ruleOptionsPanel);
		ruleOptionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
		final JToolBar toolBar1 = new JToolBar();
		toolBar1.setFloatable(false);
		rootPanel.add(toolBar1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
		addRuleButton = new JButton();
		addRuleButton.setEnabled(false);
		addRuleButton.setIcon(new ImageIcon(getClass().getResource("/cz/zcu/kiv/eeg/owlimport/gui/icons/add.png")));
		addRuleButton.setText("Add Rule");
		toolBar1.add(addRuleButton);
		removeRuleButton = new JButton();
		removeRuleButton.setEnabled(false);
		removeRuleButton.setIcon(new ImageIcon(getClass().getResource("/cz/zcu/kiv/eeg/owlimport/gui/icons/remove.png")));
		removeRuleButton.setText("Remove Rule");
		toolBar1.add(removeRuleButton);
		final JToolBar toolBar2 = new JToolBar();
		toolBar2.setFloatable(false);
		rootPanel.add(toolBar2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
		importOWLButton = new JButton();
		importOWLButton.setIcon(new ImageIcon(getClass().getResource("/cz/zcu/kiv/eeg/owlimport/gui/icons/add.png")));
		importOWLButton.setText("Import OWL");
		toolBar2.add(importOWLButton);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return rootPanel;
	}
}
