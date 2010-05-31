package de.mxro.filesystem.ext;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;

import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.Utils;
import de.mxro.utils.log.UserError;

public abstract class PackageIncludedFolder {
	public abstract String getName();
	
	public boolean installTo(Folder folder) {
		final URL url2 = this.getClass().getResource(Utils.assertAtEnd(this.getName(), '/'));
		
		if (url2 == null) {
			UserError.singelton.log("Could not find folder: "+this.getClass().getCanonicalName()+"/"+this.getName(), UserError.Priority.NORMAL);
			return false;
		}
		final URI destURI = URIImpl.create(Utils.assertAtEnd(url2.toString(), '/'));
		UserError.singelton.log(this, "installTo: To folder: "+folder.getURI()+ " from url: "+destURI, UserError.Priority.INFORMATION);
	
//		try {
			if (destURI.isAbsolute()) {
				if (destURI.getFile() != null && destURI.getFile().exists()) {
					UserError.singelton.log(this, "installTo: Tries to copy files from local file system. from file: "+destURI.toString(), UserError.Priority.INFORMATION);
					if ( folder.importFile(destURI, Utils.lastElement(destURI.toString(), "/")) == null) {
						UserError.singelton.log("PackageIncludedFolder: Could not import file: "+destURI);
					}
					return true;
				}
			}
//		} catch (final Exception e) {
//			UserError.singelton.log(this, "PackageIncludedFolder: Error copying files locally: "+destURI.toString(), UserError.Priority.INFORMATION);
//			UserError.singelton.log(e);
//		}
		
		try {
			UserError.singelton.log(this, "installTo: Tries to copy files from jar. folder='"+folder.getURI().toString()+"'", UserError.Priority.INFORMATION);	
			final URLConnection conn = url2.openConnection();
			
			if (conn == null) {
				UserError.singelton.log("PackageIncludedFolder: No connection possible "+this.getClass().getCanonicalName()+"/"+this.getName(), UserError.Priority.NORMAL);
				return false;
			}
			if (conn instanceof JarURLConnection) {
				final JarEntry entry = ((JarURLConnection) conn).getJarEntry();
				
				return Utils.copyJarToFile(folder, entry, ((JarURLConnection) conn).getJarFile());
			}
			
			UserError.singelton.log("PackageIncludedFolder: Format not supported: "+conn+" "+this.getClass().getCanonicalName()+"/"+this.getName(), UserError.Priority.NORMAL);
			return false;
		
		} catch (final IOException e) {
			UserError.singelton.log(e);
			return false;
		}
	}
	
	public boolean installTo(java.io.File parent) {
		if (!parent.isDirectory())
			throw new IllegalArgumentException("Destination must be a folder");
		Folder folder;
		try {
			folder = FileSystemObject.newLocalRootFolder(URIImpl.fromFile(parent));
		} catch (final URISyntaxException e1) {
			UserError.singelton.log(e1);
			return false;
		}
		if (folder == null) {
			UserError.singelton.log("Could not create folder: "+parent.getPath(), UserError.Priority.NORMAL);
			return false;
		}
		return this.installTo(folder);
	}
}
