package de.mxro.filesystem;

import de.mxro.utils.URI;


public interface AddressMapper {
	/**
	 * comment
	 * @param uri
	 * @return
	 */
	public URI map(URI uri);
	public URI demap(URI uri);
}
