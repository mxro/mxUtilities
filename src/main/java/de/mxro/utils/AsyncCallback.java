package de.mxro.utils;

public interface AsyncCallback<O> {

	public void onSuccess(O result);
	
	public void onFailure(Throwable exepction);
}
