package de.mxro.utils.drm;


public interface ResourceChangeCallback<R> {
	public void changeDone(Change<R> change);
	
	public void changeUndone(Change<R> change);
}
