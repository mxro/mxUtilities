package de.mxro.utils.log.impl;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class SwtUserErrorDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTextPane jTextPaneError = null;

	private JButton jButtonOk = null;

	
	public void ready() {
		this.setVisible(false);
		//this.
	}
	
	public SwtUserErrorDialog(boolean modal,
			Frame owner,
			SwtUserError swtError, 
			String message, 
			String debugInfo) throws HeadlessException {
		super(owner, modal);
		this.initialize();
		this.jTextPaneError.setText(message);
	}

	/**
	 * @param owner
	 */
	public SwtUserErrorDialog(Frame owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(326, 193);
		this.setTitle("Error");
		this.setContentPane(this.getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(new BorderLayout());
			this.jContentPane.add(this.getJTextPaneError(), BorderLayout.CENTER);
			this.jContentPane.add(this.getJButtonOk(), BorderLayout.SOUTH);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jTextPaneError	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPaneError() {
		if (this.jTextPaneError == null) {
			this.jTextPaneError = new JTextPane();
		}
		return this.jTextPaneError;
	}

	/**
	 * This method initializes jButtonOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOk() {
		if (this.jButtonOk == null) {
			this.jButtonOk = new JButton();
			this.jButtonOk.setText("Okay");
			this.jButtonOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SwtUserErrorDialog.this.ready();
				}
			});
		}
		return this.jButtonOk;
	}

}  //  @jve:decl-index=0:visual-constraint="146,67"
