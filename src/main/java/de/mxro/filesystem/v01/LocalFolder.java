package de.mxro.filesystem.v01;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import de.mxro.filesystem.File;
import de.mxro.filesystem.FileSystem;
import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.Utils;
import de.mxro.utils.log.UserError;

//@XStreamAlias("v01.localfolder")
public class LocalFolder extends Folder {
	
	
	
	public LocalFolder(URI interface1, Folder owner, FileSystem fileSystem) {
		super(interface1, owner, fileSystem);
	}

	public LocalFolder(String name,  Folder owner) throws URISyntaxException {
		super(name,  owner);
	}

//	@Override
//	public File createFile(String name) {
//		final File newfile = FileSystemObject.newLocalFile(name,  this);
//		//System.out.println(newfile.getURI().getFile().getAbsolutePath());
//		if ( 
//				LocalFileSystem.singelton.createFile( newfile.getURI()) &&
//				!this.getObjects().contains(newfile) 
//				   )  {
//							this.getObjects().add(newfile);
//							return newfile;
//						}
//						
//		
//		return null;
//	}

//	@Override
//	public URI getURI() {
//		try {
//			if (this.getOwner() != null)
//				return new URI("file://"+Utils.assertAtEnd(this.getOwner().getURI().getPath(),'/')+this.getName()+"/");
//		
//			//throw new FileSystemObjectHasNoOwnerException();
//			//UserError.singelton.log("LocalFolder: has no owner: "+this.getName()+" "+this.getClass().getName(), UserError.Priority.HIGH);
//			return new URI(this.getName());
//		} catch (final java.net.URISyntaxException e) { de.mxro.UserError.singelton.log(e); }
//		return null;
//	}
	
//	@Override
//	public Folder createFolder(String name) {
//		final Folder newfolder = FileSystemObject.newLocalFolder(name,  this);
//		//System.out.println( "newfolder uri: "+newfolder.getURI().normalize().toString() );
//		//System.out.println( "newfolder uri: "+newfolder.getURI().getFile().toString() );
//		if (this.getObjects().contains(newfolder)) {
//			UserError.singelton.log("LocalFolder.createFolder: Folder "+name+" could not be created. Folder already in FileSystem.");
//			return null;
//		}
//		if ( LocalFileSystem.singelton.createFolder( newfolder.getURI() )  ) {  
//					   this.getObjects().add(newfolder);	 
//					   return newfolder;
//					}
//		
//		return null;
//	}
	

	
	
//	/**
//	 * tries to find an fileobject corresponding to (absolute or relative) uri
//	 * if item lies under current dir (r.g. folder/file.txt) the correct folder for that file 
//	 * is found this.get("folder") == file.getParent
//	 * 
//	 * this is not the case if path has to be backward resolved (e.g. ../text.txt)!
//	 * 
//	 * @param uri
//	 * @return
//	 */
//	@Override
//	public FileSystemObject get(URI uri) {
//		
//		if (uri == null) {
//			UserError.singelton.log("LocalFolder.get: passed URI was null: "+this.getURI(), UserError.Priority.HIGH);
//			throw new IllegalArgumentException("uri must not be null");
//		}
//		
//		if (uri.getPath() == null) {
//			UserError.singelton.log("LocalFolder.get: passed URIs path was null:"+uri+" folder: "+this.getURI(), UserError.Priority.HIGH);
//			return null;
//		}
//		if (uri.getPath().equals(""))
//			return this;
//		if (uri.getPath().equals("."))
//			return this;
//		
//		return this.getFileSystem().getObject(this.getURI().resolve(uri));
//		
////		try {
////			
//			
//			if (uri.isAbsolute()) {
//				if (!this.getURI().relativize(uri).equals(uri)) {
//					if (this.getOwner()!=null && !(this instanceof RootFolder))
//						return this.getOwner().getFolder(uri);
//					
//					if (uri.getFile().isDirectory())
//						return LocalFileSystem.singelton.getFolder(uri);
//					
//					return LocalFileSystem.singelton.getFile(uri);
//					
//				}
//				
//				return this.getFolder(this.getURI().relativize(uri));
//				
//				
//			} 
//				
//			final String path = uri.getPath();				
//			
//			if (Utils.length(path, "/")>0) {
//				
//				final FileSystemObject fileObject = this.getObject(Utils.firstElement(path, "/"));
//					
//				if (fileObject != null) {
//					
//					if (Utils.length(path, "/")==1) {
//						if (fileObject.getURI().getFile().exists())
//							return fileObject;
//						else { 
//							this.getObjects().remove(fileObject);
//							return null;
//						}
//					}
//					
//					final String remainingPath=Utils.removeFirstElement(path, "/");
//						
//					return ((LocalFolder) fileObject).get(new URI(remainingPath));
//					
//				} 
//					
//				final FileSystemObject res = LocalFileSystem.singelton.getFolder(this.getURI().resolve(uri));
//				
//				if (res!=null)
//					return res;
//				
//				return  LocalFileSystem.singelton.getFile(this.getURI().resolve(uri));
//					
//				
//			} 
//			
//			return null;
//		} catch (final java.net.URISyntaxException e) { de.mxro.UserError.singelton.log(e); }
//		return null;
//	
//	}

	@Override
	public FileSystemObject importFile(URI uri, String importName, boolean override) {
		final java.io.File file = uri.getFile().getAbsoluteFile();
		if (file.isDirectory()) {
			Folder newFolder; 
			if (override) {
				newFolder = this.forceFolder(importName);
			} else {
				
					newFolder = this.createFolder(this.getUniqueFileName(importName));
				
			}
			if (newFolder == null) {
				UserError.singelton.log("LocalFolder.importFile: Cannot create Folder: "+importName);
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
	    }
	
		
		return null;
	}

//	@Override
//	public boolean deleteObject(String name) {
//		if (this.getObject(name) == null) {
//			UserError.singelton.log("LocalFolder deleteObject: Tried to delete nonexistend object: "+name, UserError.Priority.NORMAL);
//			return true;
//		}
//		
//		if (this.getObject(name).delete())
//			return this.getObjects().remove(this.getObject(name));
//		return false;
//	}

	

//	@Override
//	public boolean delete() {
//		LocalFileSystem.singelton.deleteFolder(this.getURL());
//		return false;
//	}
	
	
	@Override
	public java.io.File makeLocal() {
		return this.getURI().getFile();
	}

	@Override
	public boolean updateFromLocal() {
		return true;
	}

}
