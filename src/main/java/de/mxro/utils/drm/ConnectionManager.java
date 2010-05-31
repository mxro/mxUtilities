package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;



/**
 * Finds the right connection to do changes on documents based
 * on the URI of the document
 * 
 * @author mroh004
 *
 */
public interface ConnectionManager<R> {
    
	public void connectResource(String foruri, AsyncCallback<ResourceConnection<R>> callback);
	public void createResource(String foruri, R resource, AsyncCallback<ResourceConnection<R>> callback);
	
}
