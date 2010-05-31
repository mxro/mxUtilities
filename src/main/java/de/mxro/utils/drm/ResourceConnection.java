package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;

/**
 * Connection to ONE document on a server
 * 
 * @author mroh004
 *
 */
public interface ResourceConnection<R> {

	public String getURI();
	
	public void doChange(Change<R> change, AsyncCallback<Boolean> callback);
	
	public void undoChange(Change<R> change, AsyncCallback<Boolean> callback);
	
	public boolean addResourceChangeCallback(ResourceChangeCallback<R> resourceChangeCallback);
	
	/**
	 * forces to send all the changes for this resource
	 * @return
	 */
	public boolean commit();
	
	public ServerConnection<R> getResourceServer();
}
