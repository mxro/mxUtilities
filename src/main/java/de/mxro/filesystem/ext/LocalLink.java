package de.mxro.filesystem.ext;


import java.io.InputStream;
import java.io.OutputStream;

import de.mxro.filesystem.File;
import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;
import de.mxro.filesystem.Link;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.log.UserError;



public class LocalLink extends Link {

	protected final URI uri;
	protected final Folder context;
	
	@Override
	public InputStream getInputStream() {
		
		return this.context.getFile(this.uri).getInputStream();
		
	}

	@Override
	public URI getLocalURI() {
		final File file = this.context.getFile(this.uri); 
		if (file == null) {
			UserError.singelton.log("LocalLink.getLocalURI: File "+this.uri+" is not in Folder: "+this.context.getURI(), UserError.Priority.HIGH);
			return null;
		}
		return file.getURI();
	}

	@Override
	public OutputStream getOutputStream() {
		return this.context.getFile(this.uri).getOutputStream();
	}

//	@Override
//	public boolean makeLocal() {
//		return true;
//	}

//	@Override
//	public boolean updateFromLocal() {
//		return true;
//	}
	
	/**
	 * constructor which does not pass context
	 * the current path will be created as context
	 * uri should be absolute!
	 **/
	public static LocalLink fromURI(final URI uri) throws java.net.URISyntaxException {
		final java.io.File file = uri.getFile();
		final Folder temproot = FileSystemObject.newLocalRootFolder(URIImpl.fromFile(file.getParentFile().getAbsoluteFile()));
		//UserError.singelton.log("LocalLink.fromURI create temp local folder: "+temproot.getURI().getPath());
		return new LocalLink(uri, temproot);
	}
	
	public static LocalLink fromURI(final URI uri, final Folder context) {
		return new LocalLink(uri, context);
	}

	protected LocalLink(final URI uri, final Folder context) {
		super();
		this.uri = uri;
		if (uri == null)
			throw new IllegalArgumentException("uri must not be null!");
		this.context = context;
		if (context==null) {
			de.mxro.utils.log.UserError.singelton.log("LocalLink created with null context.", UserError.Priority.HIGH);
		}
	}
	
	@Override
	public boolean exists() {
		return this.context.getFile(this.uri).getURI().getFile().exists();
	}

	@Override
	public String toString() {
		return this.getClass().getName()+": "+this.uri;
	}
	
	

}
