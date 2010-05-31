package de.mxro.utils.drm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import de.mxro.filesystem.File;
import de.mxro.filesystem.Folder;
import de.mxro.utils.AsyncCallback;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;

/**
 * Can provide helpful routines for servers working on the folder system
 * 
 * @author mroh004
 *
 */
public class FolderServerHelper  {

	final Folder folder;
	
	public final void createResource(String uri, String resourceXML,
			AsyncCallback<Boolean> callback) {
		URI resourceuri = URIImpl.create(uri);
		
		if (resourceuri == null) {
			callback.onFailure(new URISyntaxException(uri, "FolderServerImpl: Could not read URI."));
			return;
		}
		
		URI ownerfolderuri = resourceuri.getFolder();
		if (ownerfolderuri == null) {
			callback.onFailure(new IOException("FolderServerImpl: Cannot determine owning folder of :'"+resourceuri+"'"));
			return;
		}
		
		Folder ownerFolder = folder.getFolder(ownerfolderuri);
		if (ownerFolder == null) {
			callback.onFailure(new IOException("FolderServerImpl: Cannot open folder :'"+ownerfolderuri+"'"));
			return;
		}
		
		File newFile = folder.forceFile(resourceuri.getFileName());
		if (newFile == null) {
			callback.onFailure(new IOException("FolderServerImpl: Cannot create file :'"+resourceuri.getFileName()+"' \n"+
					"   in '"+ownerfolderuri+"'"));
			return;
		}
		
		OutputStream os = newFile.getOutputStream();
		if (os == null) {
			callback.onFailure(new IOException("FolderServerImpl: Cannot write file :'"+resourceuri.getFileName()+"' \n"+
					"   in '"+ownerfolderuri+"'"));
			return;
		}
		
		try {
			os.write(resourceXML.getBytes());
			callback.onSuccess(true);
		} catch (IOException e) {
			callback.onFailure(e);
			return;
		}
		
	}

	public final void loadResource(String uri, AsyncCallback<String> callback) {
		File file = folder.getFile(URIImpl.create( uri));
		if (file == null) {
			callback.onFailure(new IOException("FolderServerImpl: Could not find resource '"+uri+"'")); 
			return;
		}
		
		InputStream is = file.getInputStream();
		if (is == null) {
			callback.onFailure(new IOException("FolderServerImpl: Could not open resource '"+uri+"'")); 
			return;
		}
		
		try {
			String resource = de.mxro.utils.Utils.fromInputStream(is);
			if (resource == null) {
				callback.onFailure(new IOException("FolderServerImpl: Could not read resource '"+uri+"'"));
				return;
			}
			callback.onSuccess(resource);
			return;
		} catch (IOException e) {
			callback.onFailure(e);
			return;
		}
		
	}

	

	public FolderServerHelper(Folder folder) {
		super();
		this.folder = folder;
	}

	
	
}
