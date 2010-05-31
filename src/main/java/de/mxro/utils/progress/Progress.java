package de.mxro.utils.progress;

public interface Progress {

	public boolean isAborting();
	
	public void setMaximum(int maximum);
	
	public void setProgress(int progress);
	
	public void setMessage(String message);
	
	public void initProgress(String title);
	
	public void stopProgress();
}
