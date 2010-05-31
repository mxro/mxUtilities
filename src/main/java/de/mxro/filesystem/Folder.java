package de.mxro.filesystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Vector;

import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.Utils;
import de.mxro.utils.log.UserError;

public class Folder extends FileSystemObject {
	
	
	public Folder(URI uri, Folder owner, FileSystem fileSystem) {
		super(uri, owner, fileSystem);
	}

	public Folder(String name, Folder owner) throws URISyntaxException {
		super(name, owner);
		
	}
	
	/**
	 * tries to find an fileobject corresponding to (absolute or relative) uri
	 * if item lies under current dir (r.g. folder/file.txt) the correct folder for that file 
	 * is found this.get("folder") == file.getParent
	 * 
	 * this is not the case if path has to be backward resolved (e.g. ../text.txt)!
	 * 
	 * @param uri
	 * @return
	 */
	public FileSystemObject get(URI uri) {
		if (uri == null) {
			UserError.singelton.log("LocalFolder.get: passed URI was null: "+this.getURI(), UserError.Priority.HIGH);
			throw new IllegalArgumentException("uri must not be null");
		}
		
		if (this.getURI() == null) {
			UserError.singelton.log(this, "get: URI of containing folder is null", UserError.Priority.HIGH);
			return null;
		}
		
		if (this.getFileSystem() == null) {
			UserError.singelton.log(this, "get: folder has no filesystem!", UserError.Priority.HIGH);
			return null;
		}
		
		if (uri.getPath() == null) {
			UserError.singelton.log("LocalFolder.get: passed URIs path was null:"+uri+" folder: "+this.getURI(), UserError.Priority.HIGH);
			return null;
		}
		
		if (uri.toString().equals(""))
			return this;
		if (uri.getPath().equals("."))
			return this;
		
		URI resolved = this.getURI().resolve(uri);
		if (resolved == null) return null;
		
		return this.getFileSystem().getObject(resolved);
	}
	public Folder createFolder(String name) {
		try {
			return this.getFileSystem().createFolder(this.getURI().addFolderToFolder(name));
		} catch (URISyntaxException e) {
		UserError.singelton.log("Folder.createFolder: illegale name: "+name, UserError.Priority.HIGH);
		return null;
		}
	}
	
	public File createFile(String name)  {
		try {
			return this.getFileSystem().createFile(this.getURI().addFileToFolder(name));
		} catch (URISyntaxException e) {
			UserError.singelton.log("Folder.createFile: illegale name: "+name, UserError.Priority.HIGH);
			return null;
		}
	}
	
	public final FileSystemObject importFile(URI uri, String importName) {
		return this.importFile(uri, importName, true);
	}
	public FileSystemObject importFile(URI uri, String importName, boolean override) {
		if (!uri.getFile().exists()) {
			UserError.singelton.log(this,"importFile: Can only import local files '"+uri.toString()+"'", UserError.Priority.HIGH);
			return null;
		}
		
		final java.io.File file = uri.getFile().getAbsoluteFile();
		if (file.isDirectory()) {
			Folder newFolder; 
			if (override) {
				newFolder = this.forceFolder(importName);
			} else {
				
					newFolder = this.createFolder(this.getUniqueFileName(importName));
				
			}
			if (newFolder == null) {
				UserError.singelton.log(this, "importFile: Cannot create Folder: "+importName, UserError.Priority.HIGH);
				return null;
			}
			for (final java.io.File f : file.listFiles()) {
				try {
					newFolder.importFile(URIImpl.fromFile(f.getAbsoluteFile()), override);
				} catch (final URISyntaxException e) {
					de.mxro.utils.log.UserError.singelton.showError("Error importing file.", e);
					return null;
				}
			}
			return newFolder;
		};
		
		final File newFile = this.forceFile(importName);
		if (newFile == null) {
			UserError.singelton.log("LocalFolder: File '"+uri.toString()+"' could not be created " +
					"as '"+importName+"' at '"+this.getURI().toString()+"'", UserError.Priority.NORMAL);
			return null;
		}
		try {
			final InputStream in = new FileInputStream(file);
	        if (!newFile.getURI().toString().equals(uri.toString())) {
				Utils.streamCopy(newFile.getOutputStream(), in);
			}
	        in.close();
	       
	        return newFile;
	    } catch (final IOException e) {
	    	de.mxro.utils.log.UserError.singelton.showError("Error importing files.", e);
	    	return null;
	    }
	
		
	}
	
	public FileSystemObject importObject(FileSystemObject object) throws IOException {
		return this.importObject(object, true);
	}
	
	public FileSystemObject importObject(FileSystemObject object, boolean override) throws IOException {
		if (object instanceof File)
			return this.importFile((File) object, override);
		else
		if (object instanceof Folder) {
			
			final Folder newFolder = this.importFolder((Folder) object, override);
			
			return newFolder;
		}
		UserError.singelton.log("FileSystemObjct.importObject: object was not a file or a folder : '"+object.getURI()+"'", UserError.Priority.HIGH);
		return null;
	}
	
	public File importFile(File file, boolean override) throws IOException {
		assert file != null : "Folder.importfile: dont pass null Files!";
		File newFile;
		if (override) {
			newFile = this.forceFile(file.getName());
		} else {
			
				newFile = this.createFile(this.getUniqueFileName(file.getName()));
			
		}	
		
		//if (file.equals(newFile)) {
		//	return file;
		//}
		
		if (newFile == null) {
			de.mxro.utils.log.UserError.singelton.log(this, "importFile: import of "+file.getName()+ "failed", UserError.Priority.HIGH);
			return null;
		}
		if (file.getInputStream() == null) {
			de.mxro.utils.log.UserError.singelton.log(this, "Could not find source file "+file.getURI()+ "", UserError.Priority.HIGH);
			return null;
		}
		/*if (newFile.getOutputStream() == null) {
			de.mxro.UserError.singelton.log("FileSystemObject.importFile: Could open destination file: "+newFile.getURI()+ "", UserError.Priority.HIGH);
			return null;
		}*/
		if (!newFile.equals(file)) {
			//System.out.println("Must copy");
			Utils.streamCopy(newFile.getOutputStream(), file.getInputStream());
		}
			
		return newFile;
	}
	
	public Folder importFolder(Folder folder) throws IOException {
		return this.importFolder(folder, true);
	}
	
	public Vector<FileSystemObject> getObjects() {
		return this.getFileSystem().getChildren(this.getURI());
	}
	
	public Folder importFolder(Folder folder, boolean override) throws IOException {
		assert folder != null : "Folder.importfolder: dont pass null Files!";
		assert folder.getName() != "" : "Folder.importfolder: Folder should have name!";
		
		Folder newFolder;
		if (override) {
			newFolder = this.forceFolder(folder.getName());
		} else {
			
				newFolder = this.createFolder(this.getUniqueFileName(folder.getName()));
			
		}
		
		if (newFolder == null) {
			de.mxro.utils.log.UserError.singelton.log("FileSystemObject.importFile: import of '"+folder.getName()+ "' failed", UserError.Priority.HIGH);
			return null;
		}
		for (final FileSystemObject object : folder.getObjects()) {
			
			newFolder.importObject(object, override);
		}
		return newFolder;
	}

	public FileSystemObject importFile(URI uri, boolean override) {
		final String filename = new java.io.File(uri.getPath()).getName();
		return this.importFile(uri, filename, override);
	}
	
	protected FileSystemObject getObject(URI uri) {
		return this.get(uri);
		//		System.out.println("Filesystemobject.getObject: "+uri.toString());
//		return this.getFileSystem().getObject(uri);
	}
	
	protected FileSystemObject getObject(String name) throws URISyntaxException {
			
		return this.getObject(this.getURI().addFileToFolder(name));
	}
	
	public Folder getFolder(URI uri) {
		
		//System.out.println("getFolder uri: "+uri);
		final FileSystemObject fileObject = this.getObject(uri);
		
		if (fileObject instanceof Folder)
			return (Folder) fileObject;
		return null;
	}
	
	/**
	 * returns folder if it exists if not it will be created before
	 * 
	 * @param uri
	 * @return
	 */
	public Folder forceFolder(String name) {
		assert name != "" : "Folder.forceFolder: Folder should hava a name";
		
		try {
			final Folder found = this.getFolder(new URIImpl(name));
			//System.out.println("name: "+name);
			if (found==null)
				return this.createFolder(name);
			
			return found;
		} catch (final URISyntaxException e) {
			de.mxro.utils.log.UserError.singelton.log(e);
			return null;
		}
	}
	
	public Folder forceFolder(URI uri) {
		final String path = uri.getPath();
		if (Utils.length(path, "/")==1)
			return this.forceFolder(path);
		final Folder newFolder = this.forceFolder(Utils.firstElement(path, "/"));
		
		if (newFolder == null) {
			UserError.singelton.log("Folder.forceFolder: utility folder could not be created: "+uri.getPath(), UserError.Priority.HIGH);
			return null;
		}
		try {
			return newFolder.forceFolder(new URIImpl(Utils.removeFirstElement(path, "/")));
		} catch (final URISyntaxException e) {
			de.mxro.utils.log.UserError.singelton.log(e);
			return null;
		}
	}
	
	/**
	 * returns file if it exists if not it will be created before
	 * 
	 * @param uri
	 * @return
	 */
	public File forceFile(String name) {
		try {
			if (this.getFile(new URIImpl(name))==null)
				return this.createFile(name);
			return this.getFile(new URIImpl(name));
		} catch (final URISyntaxException e) {
			de.mxro.utils.log.UserError.singelton.log(e);
			return null;
		}
	}
	
	public File getFile(URI uri) {
		final FileSystemObject fileObject = this.getObject(uri);
		if (fileObject instanceof File)
			return (File) fileObject;
		return null;
	}
	
	public URI resolveURI(URI uri) {
		return this.getURI().resolve(uri);
	}
	
	public URI relativizeURI(URI uri) {
		return this.getURI().relativize(uri);
	}
	
	public String getUniqueFileName(String base) {
		try {
			String add="";
			int i=0;
			if (Utils.getExtension(base).equals("")) {
				while (this.get(new URIImpl(base+add))!=null) {
					add = String.valueOf(++i);
				}
				return base+add;
			} else {
				while (this.get(new URIImpl(Utils.removeExtension(base)+add+"."+Utils.getExtension(base)))!=null) {
					add = String.valueOf(++i);
				}
				return Utils.removeExtension(base)+add+"."+Utils.getExtension(base);
			}
			
		} catch (final URISyntaxException e) {
			de.mxro.utils.log.UserError.singelton.log("Utils.getUniqueFileName: base string no valid uri: "+base);
			de.mxro.utils.log.UserError.singelton.log(e);
		}
		return null;
			
	}

	@Override
	public java.io.File makeLocal() {
		return this.getFileSystem().makeLokal(this.getURI());
		
	}

	@Override
	public boolean updateFromLocal() {
		UserError.singelton.log("Folder.updateFromLocal: notsupportedyet", UserError.Priority.HIGH);
		return false;
	}
	
	
	
}
