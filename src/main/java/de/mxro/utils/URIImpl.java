package de.mxro.utils;

/* class copied out of gridlab library: http://www.gridlab.org/ */

/**
 * @author rob
 */
/** This class implements URIs. It is API compatible with java.net.URI.
 * However,
 * the standard Java class has a bug. The Java URI class does not work correctly
 * if you omit the hostname in a URI. For example: <BR>
 * file:// <hostname>/ <path><BR>
 * and <BR>
 * <hostname>== not set (empty string) <BR>
 * and <BR>
 * <path>== /bin/date <BR>
 * then the correct URI is <BR>
 * file:////bin/date <BR>
 * So four slashes in total after the "file:" <BR>
 * if the path would be a relative path such as foo/bar, the URI would be: <BR>
 * file:///foo/bar (three slashes because of the empty hostname). <BR>
 * However, the Java URI class getPath() method will return "/foo/bar" as the
 * path instead of "foo/bar"... <BR><BR>
 * <P>Also note that relative paths in URIs are a bit ambiguous, especially with the "any" scheme.
 * It depends on the protocol (scheme) used which root is used to resolve the path.
 * So a URI with the "file" scheme will be relative to the current working directory, while "ssh" might be
 * relative to $HOME (it depends on the settings of the ssh daemon of the remote machine).
 * A "ftp" URI might end up somewhere else, etc.
 * As far as we know, there is no good solution for this problem. So, try to use URIs with an absolute path
 * as much as possible, and be careful when you use URIs with relative paths.
 */

import java.io.Serializable;
import java.net.URISyntaxException;

import mx.gwtutils.MxroGWTUtils;

import de.mxro.utils.log.UserError;


//@XStreamConverter(URIConverter.class) 
//@XStreamAlias("v01.uri")
public class URIImpl implements Serializable, Comparable, URI {
	public static final long serialVersionUID = 1L;
	
	java.net.URI u;

    public URIImpl(String s) throws java.net.URISyntaxException {
        // de.mxro.UserError.singelton.log("String create of URI: " + s);
        this.u = new java.net.URI(URIEncoder.encodeUri(s));
    	//this.u = new java.net.URI(s);
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#addFileToFolder(java.lang.String)
	 */
    public URIImpl addFileToFolder(String name) throws URISyntaxException {
    	String uri = this.toString();
    	String uriPath = Utils.assertAtEnd(uri, '/');
    	return new URIImpl(uriPath+name);
    }
   
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#addFolderToFolder(java.lang.String)
	 */
    public URIImpl addFolderToFolder(String name) throws URISyntaxException {
    	String uri = this.toString();
    	String uriPath = Utils.assertAtEnd(uri, '/');
    	return new URIImpl(uriPath+name+"/");
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#changeExtension(java.lang.String)
	 */
    public URI changeExtension(String newExtension) {
    	if (this.getFolder().equals(this)) {
    		UserError.singelton.log(this, "changeExtension: cannot change extension of folder "+this.toString(), UserError.Priority.HIGH);
    		return URIImpl.create(this.toString());
    	}
    	String newURI = mx.gwtutils.MxroGWTUtils.removeExtension(this.toString()) + newExtension;
    	URI res;
		try {
			return new URIImpl(newURI);
		} catch (URISyntaxException e) {
			UserError.singelton.log(e);
			UserError.singelton.log(this, "changeExtension: cannot change extension for '"+this.toString()+"' to '"+newExtension+"'", UserError.Priority.NORMAL);
			return URIImpl.create(this.toString());
		}
    	
    }
    
    public static URIImpl fromJavaURI(java.net.URI uri) {
    	return URIImpl.create(uri.toString());
//    	try {
//			String newURI = uri.getPath();
//			if (uri.getScheme() != null) {
//				newURI = uri.getScheme() +":"+ newURI;
//			}
//    		final URI res =  new URI(newURI);
//    		
//			//de.mxro.UserError.singelton.log(newURI + " -> "+res.toString());
//    		return res;
//		} catch (final URISyntaxException e) {
//			de.mxro.UserError.singelton.log(e);
//		}
//		return null;
    }
    
    public static URIImpl fromFile(java.io.File file)  throws java.net.URISyntaxException {
    	//de.mxro.UserError.singelton.log("Uri from file: "+file.getAbsolutePath());
    	final String absPath = file.toURI().getPath();
    	String path = absPath;
    	if (
    			file.isDirectory() &&
    			!(absPath.charAt(absPath.length()-1) == '/')
    		) {
			path = absPath + "/";
		} else {
			path = absPath;
		}
    	//de.mxro.UserError.singelton.log("create uri from file: "+path+" "+file.isAbsolute());
    	if (file.isAbsolute())
			return new URIImpl("file://"+path);
		else
			return new URIImpl(path);
    	
    }
    
    public static URIImpl create(String s) {
        try {
			return new URIImpl(s);
		} catch (final URISyntaxException e) {
			UserError.singelton.log("URI.create: UriSyntaxException for "+s, UserError.Priority.HIGH);
			return null;
		}
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getFileName()
	 */
    public String getFileName() {
    	String uri = this.toString();
    	if (uri.length() == 0) return "";
    	int lastIndex = uri.lastIndexOf("/");
    	if (lastIndex == uri.length()) return "";
    	return uri.substring(lastIndex+1);
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getLastElement()
	 */
    public String getLastElement() {
    	return mx.gwtutils.MxroGWTUtils.lastElement(this.getPath(), "/");
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getElement(int)
	 */
    public String getElement(int index) {
    	return mx.gwtutils.MxroGWTUtils.nthElement(this.getPath(), "/", index);
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#isLocal()
	 */
    public boolean isLocal() {
        if (this.u.getHost() == null)
			return true;

        if (this.u.getHost().equals("localhost"))
			return true;

        /*
         if (IPUtils.getLocalHostName().equals(u.getHost())) {
         return true;
         }
         */
        return false;
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getPath()
	 */
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

    public String getPath() {
        final String path = this.u.getPath();

        //		System.err.println("IPgetPath: uri = " + u);
        //        System.err.println("IPgetPath: path = " + path);
        if ((this.u.getScheme() == null) && (this.u.getHost() == null))
			//		System.err.println("IPgetPath: path = " + path);
            return path;

        /*
         * if (u.getHost() == null) { if (path.startsWith("/")) { path =
         * path.substring(1); } }
         *
         * if (path.startsWith("//")) { path = path.substring(1); }
         */
        
        
        //path = path.substring(1);

        //		System.err.println("IPgetPath: path = " + path);
        return path;
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#compareTo(java.lang.Object)
	 */
    public int compareTo(Object other) {
        if (other instanceof java.net.URI)
			return this.u.compareTo((java.net.URI) other);

        if (other instanceof URIImpl)
			return this.u.compareTo(((URIImpl) other).u);

        return -1;
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof URIImpl))
			return false;

        final URI other = (URI) arg0;
        
        if (this.toString().equals(other.toString())) return true;
        
        if (other.getScheme() == null || other.getScheme().equals("any") ) {
            String tmpURIString = this.getScheme() + "://";
            tmpURIString += ((other.getUserInfo() == null) ? "" : other
                .getUserInfo());
            tmpURIString += ((other.getHost() == null) ? "" : other.getHost());
            tmpURIString += ((other.getPort() == -1) ? "" : (":" + other
                .getPort()));
            tmpURIString += ("/" + other.getPath());

            //	        System.err.println("URI equals: created tmp URI: " + tmpURIString + ", orig was: " + other + ", compare with: " + u + ".");
            final boolean res = this.u.toString().equals(tmpURIString);

            //	        System.err.println("result of URI equals = " + res);
            return res;
        }

        return this.u.equals(arg0);
    }

    
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getFile()
	 */
    public java.io.File getFile() {
    	java.io.File res;
    	if (this.getPath() == null || this.getPath().equals("")) {
    		//UserError.singelton.log(this, "getFile: Path was null for "+this, UserError.Priority.NORMAL);
    		
    		return null;
    	}
    	
    	// maybe we want to find a windows share starting with "\\[Server]"
    	if (this.getPath().startsWith("//", 0)) {
    		final java.io.File file = new java.io.File("\\\\"+this.getPath().substring(2));
    		//System.out.println(file);
    		return file;
    	}
		res = new java.io.File(this.getPath());
		
    	
    	return res.getAbsoluteFile();
    	/*try {
    		return new java.io.File(this.normalize().toURL().getFile()) ;	
    	} catch (java.net.MalformedURLException e) { e.printStackTrace(); }
    	return null;*/
    	
    	
    	//try {
    	
    		
    		/*if (this.isAbsolute()) 
    			return  new java.io.File("/"+java.net.URLDecoder.decode(this.getPath(), "UTF-8"));
    		return  new java.io.File(java.net.URLDecoder.decode(this.getPath(), "UTF-8"));*/
    	//} catch (UnsupportedEncodingException e) { e.printStackTrace(); }
    	//return null;
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#isCompatible(java.lang.String)
	 */
    public boolean isCompatible(String scheme) {
        if ((this.getScheme() == null) || this.getScheme().equals("any"))
			return true;

        if (this.getScheme().equals(scheme))
			return true;

        return false;
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#debugPrint()
	 */
    public void debugPrint() {
        System.err.println("URI: scheme = " + this.getScheme() + ", host = "
            + this.getHost() + ", port = " + this.getPort() + ", path = " + this.getPath());
        System.err.println("underlying: " + this.u);
    }
    
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getAuthority()
	 */
    public String getAuthority() {
        return this.u.getAuthority();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getFragment()
	 */
    public String getFragment() {
        return this.u.getFragment();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getHost()
	 */
    public String getHost() {
        return this.u.getHost();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getPort()
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

    public int getPort() {
        return this.u.getPort();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getPort(int)
	 */
    public int getPort(int defaultPort) {
        final int port = this.getPort();

        if (port < 0)
			return defaultPort;

        return port;
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getQuery()
	 */
    public String getQuery() {
        return this.u.getQuery();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getRawAuthority()
	 */
    public String getRawAuthority() {
        return this.u.getRawAuthority();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getRawFragment()
	 */
    public String getRawFragment() {
        return this.u.getRawFragment();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getRawPath()
	 */
    public String getRawPath() {
        return this.u.getRawPath();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getRawQuery()
	 */
    public String getRawQuery() {
        return this.u.getRawQuery();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getRawSchemeSpecificPart()
	 */
    public String getRawSchemeSpecificPart() {
        return this.u.getRawSchemeSpecificPart();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getRawUserInfo()
	 */
    public String getRawUserInfo() {
        return this.u.getRawUserInfo();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getScheme()
	 */
    public String getScheme() {
        final String scheme = this.u.getScheme();

        /*
         if (scheme == null) {
         if (getHost() == null) {
         return "file";
         } else {
         return "any";
         }
         }
         */
        return scheme;
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getSchemeSpecificPart()
	 */
    public String getSchemeSpecificPart() {
        return this.u.getSchemeSpecificPart();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getUserInfo()
	 */
    public String getUserInfo() {
        return this.u.getUserInfo();
    }

    @Override
	public int hashCode() {
        return this.u.hashCode();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#isAbsolute()
	 */
    public boolean isAbsolute() {
        return this.u.isAbsolute();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#isOpaque()
	 */
    public boolean isOpaque() {
        return this.u.isOpaque();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#normalize()
	 */
    public URI normalize() {
        return URIImpl.fromJavaURI(this.u.normalize());
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#parseServerAuthority()
	 */
    public URI parseServerAuthority() throws java.net.URISyntaxException {
        return URIImpl.fromJavaURI(this.u.parseServerAuthority());
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#relativize(java.net.URI)
	 */
    public URI relativize(java.net.URI arg0) {
        return this.relativize(URIImpl.fromJavaURI(arg0));
        
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#relativize(de.mxro.gwt.URIInterface)
	 */
    public URIImpl relativize(URI arg0) {
        
        
        try {
			if (this.getScheme() != null && this.getScheme().equals(arg0.getScheme())) {
//				final String basePath = this.getPath();
//		        final String relPath = arg0.getPath();
				//return new URI(de.mxro.Utils.relativize(basePath, relPath));
				String meWithoutScheme = this.toString().replaceFirst(this.getScheme()+"://", "");
				String arg0WithoutScheme = arg0.toString().replaceFirst(arg0.getScheme()+"://", "");
				java.net.URI meJavaURI = new java.net.URI(meWithoutScheme);
				java.net.URI arg0JavaURI = new java.net.URI(arg0WithoutScheme);
				java.net.URI relativized = meJavaURI.relativize(arg0JavaURI);
				// Java could relativize
				if (!relativized.equals(arg0JavaURI)) {
					return URIImpl.fromJavaURI(relativized);
				}
				// try something else
				String relativizedPath = de.mxro.utils.Utils.relativize(meWithoutScheme, arg0WithoutScheme);
				if (!relativizedPath.equals(arg0WithoutScheme)) {
					return URIImpl.create(relativizedPath);
				}
				// couldnt relativize
				return URIImpl.create(arg0.toString());
			} else
				return URIImpl.fromJavaURI(this.u.relativize(arg0.toJavaURI()));
		} catch (final URISyntaxException e) {
			de.mxro.utils.log.UserError.singelton.log(e);
			return null;
		}
    }
    

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#resolve(java.lang.String)
	 */
    public URI resolve(String arg0) {
        return URIImpl.fromJavaURI(this.u.resolve(arg0));
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#resolve(java.net.URI)
	 */
    public URI resolve(java.net.URI arg0) {
        return URIImpl.fromJavaURI(this.u.resolve(arg0));
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#resolve(de.mxro.gwt.URIInterface)
	 */
    public URIImpl resolve(URI arg0) {
        if (arg0 == null) return null;
    	final java.net.URI juri = this.u.resolve(arg0.toJavaURI());
        
        String newURI = URIImpl.fromJavaURI(juri).toString();
        // fix for directories
        if (arg0.toString().length() > 0 && arg0.toString().charAt(arg0.toString().length()-1) == '/') {
        	newURI = Utils.assertAtEnd(newURI, '/');
        }
        
        // fix for file:/dir/... to file:///
        if (newURI.startsWith("file:/") && !newURI.startsWith("file:///")) {
        	newURI = newURI.replaceFirst("file:/", "file:///");
        }
        
        return URIImpl.create(newURI);
        
//        
////      bugfix for samba drives
//        if (this.getPath().startsWith("//")) {
//        	newURI = "/"+newURI;
//        }
//        
//        if (juri.getScheme() != null) {
//			newURI = juri.getScheme()+"://"+newURI;
//		}
//        
//       
//    	return URI.create(newURI);
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#getFolder()
	 */
    public URIImpl getFolder() {
    	String uri = this.toString();
    	int lastIndex = uri.lastIndexOf("/");
    	if (lastIndex < 0) return URIImpl.create("");
    	// when URI is already a folder return ownerfolder
    	if (lastIndex == uri.length()-1) 
    		lastIndex = uri.substring(0, uri.length()-2).lastIndexOf("/");
    	URIImpl res;
		
		try {
			res = new URIImpl(uri.substring(0, lastIndex + 1));
		} catch (URISyntaxException e) {
			return null;
		}
	
    	return res;
    	
//    	String newURI = "";
//        if (this.getScheme() != null) {
//			newURI = this.getScheme()+":/";
//		}
//        final String path = this.getPath();
//        final String remainingPath = Utils.assertAtEnd(de.mxro.Utils.removeLastElement(path, "/"), '/');
//        newURI = newURI+remainingPath;
//        return URI.create(newURI);
        
    }
    
    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#toASCIIString()
	 */
    public String toASCIIString() {
        return this.u.toASCIIString();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#toString()
	 */
    @Override
    /**
     * converts URI to string
     */
    public String toString() {
        if (this.getScheme() == null ||
        		this.getScheme().equals("null") ||
        		this.getScheme().equals("any"))
			return this.u.getPath();
    	return this.u.toString();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#toURL()
	 */
    public java.net.URL toURL() throws java.net.MalformedURLException {
        return this.u.toURL();
    }

    /* (non-Javadoc)
	 * @see de.mxro.gwt.URIInterface#toJavaURI()
	 */
    public java.net.URI toJavaURI() {
        return this.u;
    }

	
	public URI getOwner() {
		String uriAsString = this.toString();
		String uriOfOwner = MxroGWTUtils.removeLastElement(uriAsString, "/");
		
		return URIImpl.create(uriOfOwner);
	}

}
