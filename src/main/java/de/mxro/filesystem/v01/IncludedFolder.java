package de.mxro.filesystem.v01;

import java.net.URISyntaxException;
import java.util.Vector;

import de.mxro.filesystem.File;
import de.mxro.filesystem.FileSystem;
import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.FileSystemObjectHasNoOwnerException;
import de.mxro.filesystem.Folder;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.domain.NotYetSupportedException;
import de.mxro.utils.log.UserError;


public class IncludedFolder extends Folder {

	private Vector<FileSystemObject> objects;
	
	@Override
	protected FileSystemObject getObject(String name) {
		
		for (final FileSystemObject o : this.getObjects()) {
			if (o.getName().equals(name))
				return o;
		}
		return null;
	}
	
	public boolean deleteObject(String name) {
		return this.objects.remove(this.getObject(name));
	}
	
	@Override
	public Vector<FileSystemObject> getObjects() {
		if (this.objects == null) {
			this.objects = new Vector<FileSystemObject>();
		}
		return this.objects;
	}
	
	public IncludedFolder(URIImpl uri, Folder owner, FileSystem fileSystem) {
		super(uri, owner, fileSystem);
	}

	public IncludedFolder(String name, Folder owner) throws URISyntaxException {
		super(name, owner);
		this.objects = new Vector<FileSystemObject>();
	}

	@Override
	public File createFile(String name)  {
		File newfile;
		try {
			newfile = FileSystemObject.newIncludedFile(name, this);
		} catch (URISyntaxException e) {
			UserError.singelton.log(e);
			return null;
		}
		if (!this.getObjects().contains(newfile)) {
			this.getObjects().add(newfile);
			return newfile;
		}
		return null;
	}

	@Override
	public Folder createFolder(String name)  {
		Folder newfolder;
		try {
			newfolder = FileSystemObject.newIncludedFolder(name,this);
		} catch (URISyntaxException e) {
			UserError.singelton.log(e);
			return null;
		}
		if (!this.getObjects().contains(newfolder)) {
			this.getObjects().add(newfolder);
			return newfolder;
		}
		return null;
	}


	@Override
	public File get(URI url) {
		final FileSystemObject res = this.getObject(url.toString());
		if (res instanceof File)
			return (File) res;
		return null;
	}

	public void clear() {
		this.getObjects().clear();
	}

	@Override
	public URI getURI() {
		try {
			if (this.getOwner() != null)
				return new URIImpl(this.getOwner().getURI().getPath()+this.getName()+"/");
		} catch (final java.net.URISyntaxException e) { de.mxro.utils.log.UserError.singelton.log(e); }
		throw new FileSystemObjectHasNoOwnerException();
	}

	@Override
	public File importFile(URI uri, String importName, boolean override) {
		//throw new NotYetSupportedException();
		return null;
	}

	@Override
	public boolean delete() {
		// nothing to do
		return true;
	}
	

	@Override
	public java.io.File makeLocal() {
		throw new NotYetSupportedException();
	}

	@Override
	public boolean updateFromLocal() {
		throw new NotYetSupportedException();
	}
}
