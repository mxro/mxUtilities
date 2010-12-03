package de.mxro.filesystem.ext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Vector;

import de.mxro.filesystem.File;
import de.mxro.filesystem.FileSystem;
import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;
import de.mxro.filesystem.v01.LocalFile;
import de.mxro.filesystem.v01.LocalFolder;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.Utils;
import de.mxro.utils.log.UserError;

public class LocalFileSystem implements FileSystem {
	
	public LocalFileSystem() {
		super();
	}
	
	public Folder createFolder(URI uri) {

		if (uri.getFile().mkdirs() || // mkdir would just create one dir
		     uri.getFile().exists()) {
			Folder owner = this.getFolder(uri.getOwner());
			if (owner == null) {
				UserError.singelton.log(this, "createFolder: Could not find owner folder of '"+uri+"'", UserError.Priority.NORMAL);
			}
			
			return new LocalFolder(uri, owner, this);
		}
		
		UserError.singelton.log(this.getClass().getName()+".createFolder: Could not create folder '"+uri.toString(), UserError.Priority.NORMAL);
		return null;
	}
	
	
	public File createFile(URI uri) {
		try {
			//System.out.println("create File: "+uri.toString());
			
			// assert that path exists
			
			if (!uri.getFolder().getFile().exists() && !uri.getFolder().getFile().mkdirs()) {
				UserError.singelton.log("LocalFileSystem.createFile: Could not create" +
						" file "+uri.toString()+" because folder '"+uri.getFolder().toString()+"' cannot be created.", 
						UserError.Priority.HIGH);
			}
			
			if( uri.getFile().createNewFile() ||
			   uri.getFile().exists()) {
				return new LocalFile(uri, null, this);
			}
		} catch (final IOException e) { 
			UserError.singelton.log(e); 
			
		}
		return null;  
	}
	
	
	public FileOutputStream getFileOutputStream(URI uri) {
		try {
			
			return new FileOutputStream(uri.getFile());
		} catch (final FileNotFoundException e) { return null; }
	}
	
	
	public FileInputStream getFileInputStream(URI uri) {
		if (! uri.getFile().exists()) {
			de.mxro.utils.log.UserError.singelton.log(this, "getFileInputStream: Could not create input stream for "+uri+
					" because file does not exist.", UserError.Priority.NORMAL);
			return null;
		}
		try {
			return new FileInputStream(uri.getFile());
		} catch (final FileNotFoundException e) { return null; }
	}
	
	
	public File getFile(URI uri) {
		try {
			final java.io.File file = uri.getFile();	
			if (file.exists()) {
				final Folder root = FileSystemObject.newLocalRootFolder( URIImpl.fromFile((file.getParentFile()))) ;
				return FileSystemObject.newLocalFile(file.getName(), root);
			} 
				
			return null;
			
		} catch (final java.net.URISyntaxException e) { e.printStackTrace();  return null;}
	}
	
	
	public Folder getFolder(URI uri) {
		
		final java.io.File file = uri.getFile();
		//System.out.println("XXXXXXXXXXXX "+file);
		if (file != null && file.exists() && file.isDirectory()) {
			try {
				return FileSystemObject.newLocalRootFolder(URIImpl.create(Utils.assertAtEnd(URIImpl.fromFile(uri.getFile().getAbsoluteFile()).toString(), '/')));
			} catch (final URISyntaxException e) {
				UserError.singelton.log(e);
				return null;
			}
		}
		
		return null;
		
	}
	
	
	public boolean deleteFile(URI uri) {
		return uri.getFile().delete();
	}
	
	static private boolean deleteDirectory(java.io.File path) {
	    if( path.exists() ) {
	      final java.io.File[] files = path.listFiles();
	      for (final java.io.File element : files) {
	         if(element.isDirectory()) {
	           deleteDirectory(element);
	         }
	         else {
	           element.delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
	/* (non-Javadoc)
	 * @see de.mxro.filesystem.FileAccess#deleteFolder(java.net.URL)
	 */
	public boolean deleteFolder(URI uri) {
		return deleteDirectory(uri.getFile());
	}
	
	
	
	public static FileSystem singelton = new LocalFileSystem();



	public Vector<FileSystemObject> getChildren(URI uri) {
		final java.io.File file = uri.getFile().getAbsoluteFile();
		final Vector<FileSystemObject> children = new Vector<FileSystemObject>();
		for (final java.io.File f : file.listFiles()) {
			
				try {
					if (f.isFile()) {
						children.add(new LocalFile(URIImpl.fromFile(f), null, this));
					} else if (f.isDirectory()) {
						children.add(new LocalFolder(URIImpl.fromFile(f), null, this));
					}
				} catch (URISyntaxException e) {
					UserError.singelton.log(e);
				}
			
		}
		return children;
	}


	public FileSystemObject getObject(URI uri) {
		if (uri == null) return null;
		if (uri.getFile() == null) return null;
		if (!uri.getFile().exists()) return null;
		
		if (uri.getFile().isDirectory()) return this.getFolder(uri);
		
		if (uri.getFile().isFile()) return this.getFile(uri);
		//UserError.singelton.log("LocalFileSystem.getObject: neither file nor directory: "+uri.toString(), UserError.Priority.LOW);
		return null;
	}

	public java.io.File makeLokal(URI uri) {
		File file = this.getFile(uri);
		if (file == null) return null;
		return uri.getFile();
	}

	public boolean isLocal() {
		return true;
	}
	
	
	
}
