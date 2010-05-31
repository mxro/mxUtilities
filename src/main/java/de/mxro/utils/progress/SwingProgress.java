package de.mxro.utils.progress;

import java.awt.Frame;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import de.mxro.utils.log.UserError;

public class SwingProgress extends JDialog implements Progress {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel jLabelInProgress = null;

	private JProgressBar jProgressBar = null;

	private JButton jButtonAbort = null;

	private JLabel jLabelMessage = null;
	
	private boolean aborting;
	
	

	public boolean isAborting() {
		return this.aborting;
	}
	
	public void setMaximum(int maximum) {
		this.getJProgressBar().setMaximum(maximum);
	}
	
	
	
	public void setProgress(int progress) {
		this.getJProgressBar().setValue(progress);
	}

	public void setMessage(String message) {
		this.jLabelMessage.setText(message);
		this.jLabelMessage.revalidate();
		this.jLabelMessage.paintImmediately(this.jLabelMessage.getBounds());
		
	}
	
	public void initProgress(String title) {
		this.aborting = false;
		this.jLabelMessage.setText("");
		this.jLabelInProgress.setText(title);
		//this.setModal(true);
		this.getJProgressBar().setValue(0);
		//this.setAlwaysOnTop(true);
		this.setVisible(true);
		
		this.validate();
	}
	
	public void stopProgress() {
		this.setVisible(false);
	}

	/**
	 * @param owner
	 */
	public SwingProgress(Frame owner) {
		super(owner);
		this.initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(316, 186);
		this.setTitle("Please Wait");
		this.setContentPane(this.getJContentPane());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jLabelMessage = new JLabel();
			this.jLabelMessage.setBounds(new Rectangle(30, 90, 253, 16));
			this.jLabelMessage.setText("JLabel");
			this.jLabelInProgress = new JLabel();
			this.jLabelInProgress.setText("in Progress ...");
			this.jLabelInProgress.setBounds(new Rectangle(30, 30, 186, 17));
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(null);
			this.jContentPane.add(this.jLabelInProgress, null);
			this.jContentPane.add(this.getJProgressBar(), null);
			this.jContentPane.add(this.getJButtonAbort(), null);
			this.jContentPane.add(this.jLabelMessage, null);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar() {
		if (this.jProgressBar == null) {
			this.jProgressBar = new JProgressBar();
			this.jProgressBar.setBounds(new Rectangle(30, 60, 254, 20));
		}
		return this.jProgressBar;
	}

	/**
	 * This method initializes jButtonAbort	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAbort() {
		if (this.jButtonAbort == null) {
			this.jButtonAbort = new JButton();
			this.jButtonAbort.setBounds(new Rectangle(210, 120, 75, 29));
			this.jButtonAbort.setText("Abort");
			this.jButtonAbort.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					de.mxro.utils.log.UserError.singelton.log(SwingProgress.this, "Abort Progress!", UserError.Priority.NORMAL);
					SwingProgress.this.aborting = true;
				}
			});
		}
		return this.jButtonAbort;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
