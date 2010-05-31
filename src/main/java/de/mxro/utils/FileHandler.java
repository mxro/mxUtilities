package de.mxro.utils;

public interface FileHandler {
	
	/**
	 * Uploads the specified file and returns the URL to the file
	 * @param file
	 * @return
	 */
	public String uploadFile(java.io.File file);
	
	public boolean canHandle(java.io.File file);
	
}
