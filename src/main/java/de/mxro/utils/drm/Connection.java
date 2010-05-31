package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;

/**
 * Connection to ONE document on a server
 * 
 * @author mroh004
 *
 */
public interface Connection<R> {

	public void doChange(Change<R> change, AsyncCallback<Boolean> callback);
	
	public void undoChange(Change<R> change, AsyncCallback<Boolean> callback);
	
	public boolean addDocumentChangeCallback(ResourceChangeCallback<R> resourceChangeCallback);
	
	public ResourceServer<R> getResourceServer();
}
