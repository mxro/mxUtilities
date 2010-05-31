package de.mxro.filesystem.v01;

import java.net.URISyntaxException;

import de.mxro.filesystem.FileSystem;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;

public class IncludedRootFolder extends IncludedFolder {

	public static IncludedRootFolder createInstance() {
		
		
			return new IncludedRootFolder(URIImpl.create("file://root/"), new IncludedFileSystem());
		
	}
	
	
	
	public IncludedRootFolder(URIImpl uri, FileSystem fileSystem) {
		super(uri, null, fileSystem);
	}

	
	public String getAbsoluteURL() {
		return "";
	}
	
	@Override
	public java.net.URL getURL() {
		try {
			return new java.net.URL("file://");
		} catch (final java.net.MalformedURLException e) { e.printStackTrace(); }
		return null;
	}

	@Override
	public URI getURI() {
		try {
			return new URIImpl("");
		} catch (final URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
