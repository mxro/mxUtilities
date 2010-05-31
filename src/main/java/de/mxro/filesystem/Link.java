package de.mxro.filesystem;

import java.io.InputStream;
import java.io.OutputStream;

import de.mxro.utils.URI;


public abstract class Link {
	
	public abstract OutputStream getOutputStream();
	public abstract InputStream getInputStream();
//	public abstract boolean makeLocal();
	public abstract URI getLocalURI();
//	public abstract boolean updateFromLocal();
	public abstract boolean exists();
	
}
