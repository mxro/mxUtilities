package de.mxro.filesystem;

import de.mxro.utils.URI;


public interface AddressMapper {
	public URI map(URI uri);
	public URI demap(URI uri);
}
