package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;

public interface Server<R> {
	
	public void loadResource(String uri, AsyncCallback<String> callback);
	
	public void createResource(String uri, String resourceXML, AsyncCallback<Boolean> callback);
	
	public void processChanges(ChangeQueue<R> changeQueue, AsyncCallback<ChangeQueue<R>> successfulChanges);
	
}
