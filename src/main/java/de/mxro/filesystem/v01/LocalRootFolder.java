package de.mxro.filesystem.v01;



import java.net.URISyntaxException;

import de.mxro.filesystem.FileSystem;
import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;
import de.mxro.filesystem.RootFolder;
import de.mxro.filesystem.ext.LocalFileSystem;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.log.UserError;

//@XStreamAlias("v01.localrootfolder")
public class LocalRootFolder extends LocalFolder implements RootFolder {
	
	// transient 
	protected URI rootURI;
	

	@Override
	public Folder getOwner() {
		
		try {
			if (this.rootURI.getFile().getParentFile() == null)
				return null;
			
			return FileSystemObject.newLocalRootFolder(URIImpl.fromFile( this.rootURI.getFile().getParentFile() ));
		} catch (final URISyntaxException e) {
			UserError.singelton.log(e);
			return null;
		}
	}
	
	public LocalRootFolder(URI interface1) {
		this(interface1, LocalFileSystem.singelton);
	}
	
	public LocalRootFolder(URI interface1, FileSystem fileSystem) {
		super(interface1, null, fileSystem);
	
		
			//de.mxro.UserError.singelton.log(Utils.assertAtEnd(rootURI.getFile().getAbsolutePath(), '/'));
		
		this.rootURI = interface1;//URI.create("file://"+Utils.assertAtEnd(rootURI.getFile().getAbsolutePath(), '/'));
		
		if (this.rootURI == null)
			throw new IllegalArgumentException("bad rootURI!");
		
		if (!( this.rootURI.getFile().exists()))
			throw new IllegalArgumentException("rootURI does not point to existing file!\n"+
					"  URI: '"+this.rootURI+"'");
		
		// Test whether url compatible ...
		
		try {
			interface1.toURL();
		} catch (final Exception e) { 
			UserError.singelton.log(e); 
			throw new IllegalArgumentException("rootURI must be valid URL ("+interface1.toString()+")"); 
		}
	}

	public URI getAbsoluteURI() {
		return this.getURI();
	}
	
	@Override
	public URI getURI() {	
		if (this.rootURI == null )
			throw new IllegalArgumentException("LocalRootFolder: please set rootURI after creation!" );
		if ( ! this.rootURI.getFile().exists() ) {
			//throw new IllegalArgumentException("LocalRootFolder: rootURI does not exist ("+this.rootURI.toString()+")!");
			this.rootURI.getFile().mkdirs();
		}
		return this.rootURI;
	}

	public void setRootURI(URIImpl rootURI) {
		this.rootURI = rootURI;
		if (!( rootURI.getFile().exists()))
			throw new IllegalArgumentException("rootURI must exist! "+rootURI.toString());
		
		try {
			rootURI.toURL();
		} catch (final java.net.MalformedURLException e) { 
			e.printStackTrace(); 
			throw new IllegalArgumentException("rootURI must be valid URL ("+rootURI.toString()+")"); 
		}
	}
	
	
	

}
