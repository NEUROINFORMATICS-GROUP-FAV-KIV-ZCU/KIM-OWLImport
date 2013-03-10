package cz.zcu.kiv.eeg.owlimport.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import cz.zcu.kiv.eeg.owlimport.RepositoryManager;
import cz.zcu.kiv.eeg.owlimport.gui.model.RuleListModel;
import cz.zcu.kiv.eeg.owlimport.gui.model.SourceListModel;
import cz.zcu.kiv.eeg.owlimport.model.ExportException;
import cz.zcu.kiv.eeg.owlimport.model.Exporter;
import cz.zcu.kiv.eeg.owlimport.model.RuleManager;
import cz.zcu.kiv.eeg.owlimport.model.SourceManager;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.sources.AbstractSource;
import cz.zcu.kiv.eeg.owlimport.model.sources.SourceImportException;
import cz.zcu.kiv.eeg.owlimport.project.ProjectReadException;
import cz.zcu.kiv.eeg.owlimport.project.ProjectWriteException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
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

	private RepositoryManager repositoryManager;

	private SourceManager sourceManager;

	private RuleManager ruleManager;

	private SourceListModel sourcesModel;

	private AbstractSource selectedSource;

	private RuleListModel rulesModel;

	public MainDialog(RepositoryManager repoManager, SourceManager srcManager, RuleManager rlManager) {
		sourceManager = srcManager;
		repositoryManager = repoManager;
		ruleManager = rlManager;

		initSourceList();

		importOWLButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImportSourceDialog dialog = new ImportSourceDialog(MainDialog.this.sourceManager);
				dialog.showInCenterOf($$$getRootComponent$$$());

				if (dialog.getDialogResult() == DialogResult.OK) {
					AbstractSource source = dialog.createSource();
					sourceManager.addSource(source);
					try {
						repositoryManager.importSource(source);
					} catch (SourceImportException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		sourceList.addListSelectionListener(new ListSingleSelectionAdapter(sourceList) {
			@Override
			public void selectionChanged(ListSelectionEvent e) {
				if (rulesModel != null) {
					rulesModel.detach();
				}
			}

			@Override
			public void selectionSelected(int selectedIndex, ListSelectionEvent e) {
				selectedSource = sourcesModel.getElementAt(selectedIndex);
				rulesModel = new RuleListModel(selectedSource);
				setRuleListModel(rulesModel);
			}

			@Override
			public void selectionCanceled(ListSelectionEvent e) {
				selectedSource = null;
				setRuleListModel(null);
			}
		});

		addRuleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
		});


		ruleList.addListSelectionListener(new ListSingleSelectionAdapter(ruleList) {
			@Override
			public void selectionSelected(int selectedIndex, ListSelectionEvent e) {
				ruleOptionsPanel.removeAll();

				AbstractRule rule = rulesModel.getElementAt(selectedIndex);
				IRuleParamsComponent opt = rule.getGuiComponent();
				JLabel title = new JLabel(rule.getTitle());
				title.setBorder(new EmptyBorder(0, 0, 10, 0));
				ruleOptionsPanel.add(title, BorderLayout.NORTH);
				ruleOptionsPanel.add(opt.getPanel(), BorderLayout.CENTER);
				opt.refresh();
				ruleOptionsPanel.revalidate();
				getFrame().pack();
				getFrame().repaint();
			}

			@Override
			public void selectionCanceled(ListSelectionEvent e) {
				ruleOptionsPanel.removeAll();
				ruleOptionsPanel.revalidate();
				getFrame().repaint();
			}
		});

		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);

				if (chooser.showSaveDialog($$$getRootComponent$$$()) == JFileChooser.APPROVE_OPTION) {
					Exporter export = new Exporter(chooser.getSelectedFile());
					try {
						export.openFile();

						for (AbstractSource source : sourceManager.getSources()) {
							export.writeSource(source);
						}

						export.closeFile();
					} catch (ExportException ex) {

					}
				}
			}
		});


		importEEGDatabaseOWL();


		saveProjectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);

				if (chooser.showSaveDialog($$$getRootComponent$$$()) == JFileChooser.APPROVE_OPTION) {
					try {
						sourceManager.saveProject(chooser.getSelectedFile());
					} catch (ProjectWriteException ex) {
						// handle error
					}
				}
			}
		});
		loadProjectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);

				if (chooser.showOpenDialog($$$getRootComponent$$$()) == JFileChooser.APPROVE_OPTION) {
					try {
						sourceManager.loadProject(chooser.getSelectedFile(), ruleManager);
					} catch (ProjectReadException ex) {
						// handle error
					}
				}
			}
		});
	}


	private JFrame getFrame() {
		return (JFrame) rootPanel.getTopLevelAncestor();
	}


	private void importEEGDatabaseOWL() {
		try {
			sourceManager.loadProject(new File("E:\\test.xml"), ruleManager);
			sourceManager.importSources(repositoryManager);
		} catch (ProjectReadException e) {
			e.printStackTrace();
		} catch (SourceImportException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	private void setRuleListModel(ListModel<AbstractRule> model) {
		ruleList.setModel(model);
		addRuleButton.setEnabled(model != null);
	}

	private void initSourceList() {
		sourcesModel = new SourceListModel(sourceManager);
		sourceList.setModel(sourcesModel);
	}

	public static void run(final RepositoryManager repoManager, final SourceManager srcManager, final RuleManager rlManager) {
		initLookAndFeel();

		JFrame frame = new JFrame("MainDialog");
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
		loadProjectButton.setText("Load Project");
		mainToolbar.add(loadProjectButton);
		importOWLButton = new JButton();
		importOWLButton.setText("Import OWL");
		mainToolbar.add(importOWLButton);
		final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
		mainToolbar.add(toolBar$Separator1);
		exportButton = new JButton();
		exportButton.setText("Export");
		mainToolbar.add(exportButton);
		final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
		mainToolbar.add(toolBar$Separator2);
		saveProjectButton = new JButton();
		saveProjectButton.setText("Save Project");
		mainToolbar.add(saveProjectButton);
		final JScrollPane scrollPane1 = new JScrollPane();
		rootPanel.add(scrollPane1, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(177, 128), null, 0, false));
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
		rootPanel.add(toolBar1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
		addRuleButton = new JButton();
		addRuleButton.setEnabled(false);
		addRuleButton.setText("Add Rule");
		toolBar1.add(addRuleButton);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return rootPanel;
	}
}
