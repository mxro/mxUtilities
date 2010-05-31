package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;

public interface ServerConnection<R> {
	
	/**
	 * returns true if this server can work with the given URI
	 * @param uri
	 */
	public boolean acceptResource(String uri);
	
	public void connectResource(String uri, AsyncCallback<ResourceConnection<R>> callback);
	
	public void createResource(String uri, R resource, AsyncCallback<ResourceConnection<R>> callback);
	
	/**
	 * sends all the changes for the document specified by URI
	 * holds program unitl changes are sent.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean commitResource(String uri);
	
	public boolean addResourceChangeCallback(ResourceChangeCallback<R> resourceChangeCallback);
	
    public void doChange(String uri, Change<R> change, AsyncCallback<Boolean> callback);
	
	public void undoChange(String uri, Change<R> change, AsyncCallback<Boolean> callback);
	
}
