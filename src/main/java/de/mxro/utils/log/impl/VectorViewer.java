package de.mxro.utils.log.impl;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public abstract class VectorViewer<E> extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JTextPane jTextPane = null;

	private JLabel jLabel1 = null;

	private JButton jButtonOkay = null;

	private JScrollPane jScrollPane = null;

	public void showVector(Vector<E> v) {
		String res = "";
		for (final E e: v) {			
			res = res + "\n"+this.renderElement(e);
		}
		this.jTextPane.setText(res);
		this.setVisible(true);
	}
	
	public abstract String renderElement(E e);
	
	/**
	 * @param owner
	 */
	public VectorViewer(Frame owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(385, 348);
		this.setContentPane(this.getJContentPane());
		this.setTitle("View");
		this.setModal(true);
		de.mxro.utils.Utils.centerComponent(this, null);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jLabel1 = new JLabel();
			this.jLabel1.setBounds(new Rectangle(15, 15, 76, 16));
			this.jLabel1.setText("Report:");
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.jLabel1, null);
			this.jContentPane.add(this.getJButtonOkay(), null);
			this.jContentPane.add(this.getJScrollPane(), null);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane() {
		if (this.jTextPane == null) {
			this.jTextPane = new JTextPane();
			this.jTextPane.setPreferredSize(new Dimension(0, 800));
			
		}
		return this.jTextPane;
	}

	/**
	 * This method initializes jButtonOkay	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOkay() {
		if (this.jButtonOkay == null) {
			this.jButtonOkay = new JButton();
			this.jButtonOkay.setBounds(new Rectangle(285, 285, 75, 29));
			this.jButtonOkay.setText("Okay");
			this.jButtonOkay.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					VectorViewer.this.setVisible(false);
				}
			});
		}
		return this.jButtonOkay;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (this.jScrollPane == null) {
			this.jScrollPane = new JScrollPane();
			this.jScrollPane.setBounds(new Rectangle(15, 45, 346, 226));
			this.jScrollPane.setViewportView(this.getJTextPane());
			this.jScrollPane.setHorizontalScrollBarPolicy(
                      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		return this.jScrollPane;
	}

}  //  @jve:decl-index=0:visual-constraint="97,13"
