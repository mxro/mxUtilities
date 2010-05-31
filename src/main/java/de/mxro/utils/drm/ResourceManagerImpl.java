package de.mxro.utils.drm;


import de.mxro.utils.AsyncCallback;

public class ResourceManagerImpl<R> implements ResourceManager<R> {

	final R resource;
	final ResourceConnection<R> connection;
	

	public void addDocumentChangeCallback(
			ResourceChangeCallback<R> resourceChangeCallback) {
		connection.addResourceChangeCallback(resourceChangeCallback);
	}

	
	public ResourceConnection<R> getConnection() {
		return connection;
	}

	
	public R getResource() {
		return resource;
	}

	

	public ResourceManagerImpl(R resource, ResourceConnection<R> connection) {
		super();
		this.resource = resource;
		this.connection = connection;
	}



	
	public void doChange(Change<R> change,
			AsyncCallback<Boolean> callback) {
		connection.doChange(change, callback);
	}




	public void undoChange(Change<R> change,
			AsyncCallback<Boolean> callback) {
		connection.undoChange(change, callback);
	}

	
	public boolean commit() {
		return connection.commit();
	}

	
	
}
