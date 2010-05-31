package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;



/**
 * GUI Code should interact with this class rather than
 * resource class directly.
 * 
 * Manages connections to servers, etc.
 * 
 * @author mroh004
 *
 */
public interface ResourceManager<R> {
	
	public void doChange(Change<R> change, AsyncCallback<Boolean> callback);
	
	public void undoChange(Change<R> change, AsyncCallback<Boolean> callback);
	
	public ResourceConnection<R> getConnection();
	
	public R getResource();
	
	public void addDocumentChangeCallback(ResourceChangeCallback<R> resourceChangeCallback);
	
	public boolean commit();
}
