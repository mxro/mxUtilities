package de.mxro.filesystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import de.mxro.utils.URI;



public interface FileSystem {
	
	public abstract Folder createFolder(URI uri);

	public abstract File createFile(URI uri);

	public abstract FileOutputStream getFileOutputStream(URI uri);

	public abstract FileInputStream getFileInputStream(URI uri);

	public abstract File getFile(URI uri);

	public abstract Folder getFolder(URI uri);
	
	public abstract Vector<FileSystemObject> getChildren(URI uri);
	
	public abstract FileSystemObject getObject(URI uri);

	public abstract boolean deleteFile(URI uri);

	public abstract boolean deleteFolder(URI uri);
	
	public abstract java.io.File makeLokal(URI uri);
	
	/**
	 * retruns true if filesystem is accessible on local computer
	 * paths like file://...
	 * @return
	 */
	public abstract boolean isLocal();

}
