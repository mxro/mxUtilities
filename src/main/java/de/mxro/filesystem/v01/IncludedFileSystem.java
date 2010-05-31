package de.mxro.filesystem.v01;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import de.mxro.filesystem.File;
import de.mxro.filesystem.FileSystem;
import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;
import de.mxro.utils.URI;

//@XStreamAlias("v01.includedfilesystem")
public class IncludedFileSystem implements FileSystem {

	public File createFile(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public Folder createFolder(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteFile(URI uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteFolder(URI uri) {
		// TODO Auto-generated method stub
		return false;
	}

	public Vector<FileSystemObject> getChildren(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public File getFile(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInputStream getFileInputStream(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileOutputStream getFileOutputStream(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public Folder getFolder(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileSystemObject getObject(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public java.io.File makeLokal(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLocal() {
		return false;
	}

	
}
