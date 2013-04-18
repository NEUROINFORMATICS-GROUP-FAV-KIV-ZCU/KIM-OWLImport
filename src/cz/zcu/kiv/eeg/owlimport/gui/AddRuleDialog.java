package cz.zcu.kiv.eeg.owlimport.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import cz.zcu.kiv.eeg.owlimport.model.RuleManager;
import cz.zcu.kiv.eeg.owlimport.model.rules.AbstractRule;
import cz.zcu.kiv.eeg.owlimport.model.rules.IRuleFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dialog for new rule creation.
 * @author Jan Smitka <jan@smitka.org>
 */
public class AddRuleDialog extends InputDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField titleField;
	private JComboBox ruleTypeBox;

	private RuleManager ruleManager;


	/**
	 * Initializes the dialog.
	 * @param rlManager Rule manager with available rule factories.
	 */
	public AddRuleDialog(RuleManager rlManager) {
		$$$setupUI$$$();

		initializeLayout();

		ruleManager = rlManager;

		registerButtonActions();
		registerCancelActions();

		fillRuleTypeBox();
	}

	/**
	 * Initializes the dialog layout.
	 */
	private void initializeLayout() {
		setContentPane(contentPane);
		setModal(true);
		pack();

		setTitle("Add New Rule");
		setIcon("icons/add.png");
		setResizable(false);
	}

	/**
	 * Registers OK and Cancel button actions.
	 */
	private void registerButtonActions() {
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
	}

	/**
	 * Populates the rule type combo box with available factories.
	 */
	private void fillRuleTypeBox() {
		for (IRuleFactory factory : ruleManager.getFactories()) {
			ruleTypeBox.addItem(factory);
		}
	}

	/**
	 * Creates the rule.
	 * @return Newly created rule.
	 */
	public AbstractRule createRule() {
		IRuleFactory factory = (IRuleFactory) ruleTypeBox.getSelectedItem();
		return factory.createRule(titleField.getText());
	}


	/**
	 * Validates parameters and closes the dialog.
	 */
	@Override
	protected void onOK() {
		try {
			validateParams();
			setDialogResultOk();
			dispose();
		} catch (ValidationException e) {
			handleValidationException(e);
		}
	}

	/**
	 * Validates rule parameters.
	 * @throws ValidationException when the validation fails.
	 */
	private void validateParams() throws ValidationException {
		if (titleField.getText().length() == 0) {
			throw new ValidationException("You have to enter rule title.", titleField);
		}
		if (ruleTypeBox.getSelectedItem() == null) {
			throw new ValidationException("You have to select type of rule.", ruleTypeBox);
		}
	}


	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, new Dimension(450, -1), null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
		panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		buttonOK = new JButton();
		buttonOK.setText("OK");
		panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		buttonCancel = new JButton();
		buttonCancel.setText("Cancel");
		panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText("Rule Title:");
		panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer2 = new Spacer();
		panel3.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		titleField = new JTextField();
		panel3.add(titleField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Rule Type:");
		panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		ruleTypeBox = new JComboBox();
		panel3.add(ruleTypeBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}
}
