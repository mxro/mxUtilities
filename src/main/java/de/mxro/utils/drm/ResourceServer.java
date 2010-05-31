package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;

public interface ResourceServer<R> {
	
	public void connectResource(String uri, AsyncCallback<ResourceManager<R>> callback);
	
	public void createResource(String uri, R resource, AsyncCallback<ResourceManager<R>> callback);
	
	
}
