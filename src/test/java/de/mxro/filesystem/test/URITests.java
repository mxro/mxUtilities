package de.mxro.filesystem.test;

import org.junit.Assert;
import org.junit.Test;

import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;

public class URITests {
	
	
	@Test
	public void test_uri_getFolder() {
		URI uri = URIImpl.create("http://www.mxro.de/SL/SyncLinnk.xml.xml");
		
		Assert.assertEquals(URIImpl.create("http://www.mxro.de/SL/"), uri.getFolder());
	//	System.out.println(uri.getFolder());
	}
	
}
