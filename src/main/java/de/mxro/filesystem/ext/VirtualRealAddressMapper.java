package de.mxro.filesystem.ext;

import de.mxro.filesystem.AddressMapper;
import de.mxro.utils.URI;


/**
 * Allows to access resources under different
 * URIs than where they are actually stored:
 * eg http://www.mysite.com/test.html can be accessed at
 * file:///data/test.html
 * using
 * virtualURI: http://www.mysite.com/
 * realURI: file:///data/
 * 
 * @author mx
 *
 */
public class VirtualRealAddressMapper implements AddressMapper {
	
	private final URI virtualURI;
	private final URI realURI;
	private VirtualRealAddressMapper inverse;
	
	public URI map(URI uri) {
		URI relativizedByVirtualURI = null;
		if (uri.isAbsolute()) {
			relativizedByVirtualURI = virtualURI.relativize(uri);
			if (relativizedByVirtualURI == null) return null;
		} else // already relativized 
			{
			relativizedByVirtualURI = uri;
		}
		
		//System.out.println("realUri: "+realURI);
		URI resolvedByRealURI = realURI.resolve(relativizedByVirtualURI);
		//System.out.println("Resolved: "+resolvedByRealURI);
		return resolvedByRealURI;
	}

	
	
	public VirtualRealAddressMapper(URI virtualURI, URI realURI) {
		super();
		this.virtualURI = virtualURI;
		this.realURI = realURI;
		
	}

	

	public URI demap(URI uri) {
		
		return getInverse().map(uri);
	}



	public VirtualRealAddressMapper getInverse() {
		if (inverse == null) {
			inverse = new VirtualRealAddressMapper(realURI, virtualURI);
		}
		return inverse;
	}

	
	
}
