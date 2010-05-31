package de.mxro.filesystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import de.mxro.utils.URI;
import de.mxro.utils.log.UserError;

/**
 * Allows to access resources under different
 * URIs than where they are actually stored:
 * eg http://www.mysite.com/test.html can be accessed at
 * file:///data/test.html
 * 
 * @author mx
 *
 */
public class FileSystemAddressMapper implements FileSystem {

	private final AddressMapper mapper;
	private final FileSystem fileSystem;
	

	public FileSystemAddressMapper(FileSystem fileSystem, AddressMapper mapper) {
		super();
		this.mapper = mapper;
		this.fileSystem = fileSystem;
	}

	public File createFile(URI uri) {	
		File newFile = fileSystem.createFile(mapper.map(uri));
		if (newFile == null) {
			UserError.singelton.log(this.getClass().toString()+".createFile: Could not create file " +
					"in real system '"+mapper.map(uri)+"' \n" +
							"original: '"+uri, UserError.Priority.NORMAL);
			return null;
		}
		
		return new File(uri, this.getFolder(uri.getFolder()), this);
	}

	public Folder createFolder(URI uri) {
		Folder newFolder =fileSystem.createFolder(mapper.map(uri));
		if (newFolder == null) {
			UserError.singelton.log(this.getClass().toString()+".createFolder: Could not create file /n" +
					" in real system '"+mapper.map(uri)+"' \n" +
					" original: '"+uri, UserError.Priority.NORMAL);
			return null;
		}
		
		return new Folder(uri, this.getFolder(uri.getFolder()), this);
	}

	public boolean deleteFile(URI uri) {
		return fileSystem.deleteFile(mapper.map(uri));
	}

	public boolean deleteFolder(URI uri) {
		return fileSystem.deleteFolder(mapper.map(uri));
	}

	public Vector<FileSystemObject> getChildren(URI uri) {
		return fileSystem.getChildren(mapper.map(uri));
	}

	public File getFile(URI uri) {
		if (uri == null) return null;
		if (uri.toString().equals("")) return null;
		File file = fileSystem.getFile(mapper.map(uri));
		if (file == null) return null;
		return new File(uri, this.getFolder(uri.getFolder()), this);
	}

	public FileInputStream getFileInputStream(URI uri) {
		return fileSystem.getFileInputStream(mapper.map(uri));
	}

	public FileOutputStream getFileOutputStream(URI uri) {
		return fileSystem.getFileOutputStream(mapper.map(uri));
	}

	public Folder getFolder(URI uri) {
		//System.out.println(uri);
		if (uri == null) return null;
		if (uri.toString().equals("")) return null;
		Folder folder = fileSystem.getFolder(mapper.map(uri));
		if (folder == null) return null;
		
		
		return new Folder(uri, this.getFolder(uri.getFolder()), this);
	}

	public FileSystemObject getObject(URI uri) {
		if (uri == null) return null;
		FileSystemObject obj = fileSystem.getObject(mapper.map(uri));
		if (obj instanceof Folder) return this.getFolder(uri);
		if (obj instanceof File) return this.getFile(uri);
		return null;
	}

	public AddressMapper getMapper() {
		return mapper;
	}

	public java.io.File makeLokal(URI uri) {
		return fileSystem.makeLokal(mapper.map(uri));
	}

	public boolean isLocal() {
		return fileSystem.isLocal();
	}
	
}
