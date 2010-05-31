package de.mxro.utils.drm;

import de.mxro.utils.AsyncCallback;

public class ConnectionImpl<R> implements ResourceConnection<R> {

	final ServerConnection<R> server;
	final String resourceuri;
	
	
	public boolean addResourceChangeCallback(ResourceChangeCallback<R> resourceChangeCallback) {
		
		return false;
	}

	
	public void doChange(Change<R> change, AsyncCallback<Boolean> callback) {
		this.server.doChange(resourceuri, change, callback);
	}

	
	public ServerConnection<R> getResourceServer() {
		return server;
	}

	
	public void undoChange(Change<R> change, AsyncCallback<Boolean> callback) {
		this.server.undoChange(resourceuri, change, callback);
	}

	public ConnectionImpl(ServerConnection<R> server, String resourceuri) {
		super();
		this.server = server;
		this.resourceuri = resourceuri;
	}

	
	public String getURI() {
		
		return resourceuri;
	}

	
	public boolean commit() {
		return this.server.commitResource(resourceuri);
	}

	
	
}
