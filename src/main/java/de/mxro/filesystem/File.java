package de.mxro.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import de.mxro.utils.URI;
import de.mxro.utils.log.UserError;

public class File extends FileSystemObject {
	

	public File(URI uri, Folder owner, FileSystem fileSystem) {
		super(uri, owner, fileSystem);
	}

	public File(String name, Folder owner) throws URISyntaxException {
		super(name, owner);
	}

	public void fromStream(InputStream is) {
		try {
			
	        final OutputStream out = this.getOutputStream();
	    
	        // Transfer bytes from in to out
	        final byte[] buf = new byte[1024];
	        int len;
	        while ((len = is.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	       // in.close();
	        out.close();
	       
	    } catch (final IOException e) {
	    	de.mxro.utils.log.UserError.singelton.showError("Error importing files.", e);
	    }
	}
	public InputStream getInputStream() {
		return this.getFileSystem().getFileInputStream(this.getURI());
	}
	public OutputStream getOutputStream() {
		return this.getFileSystem().getFileOutputStream(this.getURI());
	}


	@Override
	public java.io.File makeLocal() {
		return this.getFileSystem().makeLokal(this.getURI());
	}

	@Override
	public boolean updateFromLocal() {
		UserError.singelton.log("File.updateFromLocal: Not supported yet", UserError.Priority.HIGH);
		return false;
	}
	
	
}
