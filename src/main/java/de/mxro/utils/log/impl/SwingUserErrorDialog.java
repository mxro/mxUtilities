package de.mxro.utils.log.impl;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import de.mxro.utils.Utils;

public class SwingUserErrorDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTextPane jTextPaneError = null;

	private JButton jButtonOk = null;

	private JButton jButtonDetails = null;

	
	public void ready() {
		this.setVisible(false);
		//this.
	}
	
	public SwingUserErrorDialog(boolean modal,
			Frame owner,
			SwingUserError swtError, 
			String message, 
			String debugInfo) throws HeadlessException {
		super(owner, modal);
		this.initialize();
		this.jTextPaneError.setText(message);
		de.mxro.utils.Utils.centerComponent(this, null);
	}

	/**
	 * @param owner
	 */
	public SwingUserErrorDialog(Frame owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(407, 173);
		this.setContentPane(this.getJContentPane());
		this.setTitle("Error");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.getJButtonOk(), null);
			this.jContentPane.add(this.getJTextPaneError(), null);
			this.jContentPane.add(this.getJButtonDetails(), null);
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
			this.jTextPaneError.setBounds(new Rectangle(15, 11, 361, 95));
			this.jTextPaneError.setBackground(null);
			//jTextPaneError.setAlignmentY(Component.CENTER_ALIGNMENT);
			this.jTextPaneError.setEditable(false);
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
			this.jButtonOk.setBounds(new Rectangle(270, 120, 116, 30));
			this.jButtonOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SwingUserErrorDialog.this.ready();
				}
			});
		}
		return this.jButtonOk;
	}

	private static class LogViewer extends VectorViewer<String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String renderElement(String e) {
			
			return e;
		}

		public LogViewer(Frame owner) {
			super(owner);
		}
		
		
	}
	
	/**
	 * This method initializes jButtonDetails	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDetails() {
		if (this.jButtonDetails == null) {
			this.jButtonDetails = new JButton();
			this.jButtonDetails.setBounds(new Rectangle(15, 120, 116, 30));
			this.jButtonDetails.setText("Details");
			this.jButtonDetails.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final LogViewer viewer = new LogViewer(null);
					viewer.showVector(de.mxro.utils.log.UserError.singelton.logged);
				}
			});
		}
		return this.jButtonDetails;
	}

}  //  @jve:decl-index=0:visual-constraint="146,67"
