package de.mxro.filesystem.v01;

import java.net.URISyntaxException;

import de.mxro.filesystem.File;
import de.mxro.filesystem.FileSystem;
import de.mxro.filesystem.Folder;
import de.mxro.utils.URI;

//@XStreamAlias("v01.localfile")
public class LocalFile extends File {
	
	public LocalFile(URI uri, Folder owner, FileSystem fileSystem) {
		super(uri, owner, fileSystem);
		
	}

	public LocalFile(String name,  Folder owner) throws URISyntaxException {
		super(name,  owner);
		
	}


	

	@Override
	public java.io.File makeLocal() {
		return this.getURI().getFile();
	}

	@Override
	public boolean updateFromLocal() {
		return true;
	}

	
	
//	@Override
//	public URI getURI() {
//		try {
//			if (this.getOwner() != null)
//				return new URI("file://"+Utils.assertAtEnd(this.getOwner().getURI().getPath(), '/')+this.getName());
//		} catch (final java.net.URISyntaxException e) { e.printStackTrace(); }
//		throw new FileSystemObjectHasNoOwnerException();
//	}

	

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LocalFile))
			return false;
		//System.out.println(this.getURI().toString()+" == "+((LocalFile) obj).getURI().toString());
		//System.out.println(this.getURI().toString().equals(((LocalFile) obj).getURI().toString()));
		return this.getURI().toString().equals(((LocalFile) obj).getURI().toString());
	}
	
	

}
