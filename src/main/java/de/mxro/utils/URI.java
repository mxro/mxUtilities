package de.mxro.utils;

import java.net.URISyntaxException;

public interface URI {

	/**
	 * this method tries to add a child to the current path
	 * e.g. uri www.mxro.de + name test.xml become www.mxro.de/test.xml
	 * @param name
	 * @return
	 */
	public abstract URI addFileToFolder(String name) throws URISyntaxException;

	/**
	 * tries to get the owner folder of the current path
	 * eg http://www.mxro.de/test/ would become
	 * http://www.mxro.de/
	 */
	public abstract URI getOwner();
	
	/**
	 * this method tries to add a child folder to the current path
	 * e.g. uri www.mxro.de + name test become www.mxro.de/test/
	 * @param name
	 * @return
	 */
	public abstract URI addFolderToFolder(String name)
			throws URISyntaxException;

	/**
	 * creates a new URI with a changed extension
	 * eg from test.xml --> new Extension .rdf --> test.rdf
	 * @param newExtension
	 * @return
	 */
	public abstract URI changeExtension(String newExtension);

	/**
	 * gives the element after the last '/'
	 * eg http://www.test.de/test.xml would return 'test.xml'
	 * @return
	 */
	public abstract String getFileName();

	public abstract String getLastElement();

	public abstract String getElement(int index);

	/** Check whether URI refers to the local machine */
	public abstract boolean isLocal();

	/** Check whether URI refers to the local machine. The difference between this call and isLocal is that
	 this call also checks if the host in the URI is equal to the hostname of the local mahince */
	/*public boolean refersToLocalHost() {
	    if (u.getHost() == null) {
	        return true;
	    }
	
	    if (u.getHost().equals("localhost")) {
	        return true;
	    }
	
	    if (IPUtils.getLocalHostName().equals(u.getHost())) {
	        return true;
	    }
	
	    return false;
	}*/

	public abstract String getPath();

	public abstract int compareTo(Object other);

	public abstract boolean equals(Object arg0);

	public abstract java.io.File getFile();

	/** Checks whether this URI is "compatible" with the given scheme
	 * If this URI has the same scheme, it is compatible.
	 * When this URI has "any" as scheme, or no scheme at all, it is also
	 * compatible.
	 *
	 * @param scheme the scheme to compare to
	 * @return true: the URIs are compatible
	 */
	public abstract boolean isCompatible(String scheme);

	public abstract void debugPrint();

	public abstract String getAuthority();

	public abstract String getFragment();

	public abstract String getHost();

	/** gets the host componnent of the URI.
	 * If the URI refers to the local host, this will try to get the local host name and return that.
	 * If that fails, "localhost" is returned.
	 *
	 * @return the host
	 */
	/*public String resolveHost() {
	    String host = getHost();
	
	    if (host == null) {
	        host = IPUtils.getLocalHostName();
	    }
	
	    if ((host == null) || (host.length() == 0)) {
	        return "localhost";
	    }
	
	    return host;
	}*/

	public abstract int getPort();

	/**
	 *
	 * @return returns the port specified in the URI, and default if none was specified.
	 */
	public abstract int getPort(int defaultPort);

	public abstract String getQuery();

	public abstract String getRawAuthority();

	public abstract String getRawFragment();

	public abstract String getRawPath();

	public abstract String getRawQuery();

	public abstract String getRawSchemeSpecificPart();

	public abstract String getRawUserInfo();

	public abstract String getScheme();

	public abstract String getSchemeSpecificPart();

	public abstract String getUserInfo();

	public abstract boolean isAbsolute();

	public abstract boolean isOpaque();

	public abstract URI normalize();

	public abstract URI parseServerAuthority()
			throws java.net.URISyntaxException;

	public abstract URI relativize(java.net.URI arg0);

	public abstract URI relativize(URI arg0);

	public abstract URI resolve(String arg0);

	public abstract URI resolve(java.net.URI arg0);

	public abstract URI resolve(URI arg0);

	public abstract URI getFolder();

	public abstract String toASCIIString();

	public abstract String toString();

	public abstract java.net.URL toURL() throws java.net.MalformedURLException;

	public abstract java.net.URI toJavaURI();

}