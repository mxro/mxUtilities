package de.mxro.utils.application;

import de.mxro.utils.progress.Progress;
import de.mxro.utils.progress.SwingProgress;

public abstract class Application {
	private final BackgroundProcess backgroundProcess=new BackgroundProcess();
	private transient java.io.File executable;
	
	public final BackgroundProcess getBackgroundProcess() {
		return this.backgroundProcess;
	}
	
	
	public java.io.File getExecutable() {
		return this.executable;
	}

	public void setExecutable(java.io.File executable) {
		this.executable = executable;
	}
	
	
	public abstract void quit();
	public abstract void open();
	public abstract void open(java.io.File file);
	
	public Progress getProgress() {
		if (this.progress == null) {
			this.progress = new SwingProgress(null);
			de.mxro.utils.Utils.centerComponent((SwingProgress) this.progress, null);
		}
		return this.progress;
	}
	
	private transient Progress progress; 
	
	public abstract java.io.File getSystemDesktop();
	public abstract java.io.File getSystemDocumentsDirectory();
}
