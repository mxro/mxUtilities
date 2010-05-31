package de.mxro.filesystem.v01;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import de.mxro.filesystem.File;
import de.mxro.filesystem.Folder;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.domain.NotYetSupportedException;

//@XStreamAlias("v01.includedfile")
public class IncludedFile extends File {
	
	protected ByteArrayOutputStream data;
	
	public IncludedFile(String name, Folder owner) throws URISyntaxException {
		super(name, owner);
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.data.toByteArray());
	}

	@Override
	public OutputStream getOutputStream() {
		this.data = new ByteArrayOutputStream();
		return this.data;
	}

	@Override
	public boolean delete() {
		// nothing to do ...
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

	

	@Override
	public URI getURI() {
		try {
			return new URIImpl(this.getOwner().getURI().getPath() + "" + this.getName());
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
