package de.mxro.filesystem.test;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import de.mxro.filesystem.FileSystemAddressMapper;
import de.mxro.filesystem.ext.LocalFileSystem;
import de.mxro.filesystem.ext.VirtualRealAddressMapper;
import de.mxro.utils.URIImpl;

public class VirtualFileSystemTests {
	
	@Test
	public void test_virtual_filesystem_mappter() throws URISyntaxException {
		VirtualRealAddressMapper mapper = new VirtualRealAddressMapper(new URIImpl("file:///C:/Users/Max/Documents/My Dropbox/SyncLinnk"), new URIImpl("http://www.mxro.de/SL"));
		FileSystemAddressMapper fileSystem = new FileSystemAddressMapper(new LocalFileSystem(), mapper);
		
		Assert.assertEquals(URIImpl.create("file:///C:/Users/Max/Documents/My%20Dropbox/doc1.xml"), mapper.demap(URIImpl.create("http://www.mxro.de/SL/doc1.xml")));
		
		
	}
}
