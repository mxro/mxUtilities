package de.mxro.filesystem;

import java.net.URISyntaxException;

import de.mxro.filesystem.v01.IncludedFile;
import de.mxro.filesystem.v01.IncludedFolder;
import de.mxro.filesystem.v01.IncludedRootFolder;
import de.mxro.filesystem.v01.LocalFile;
import de.mxro.filesystem.v01.LocalFolder;
import de.mxro.filesystem.v01.LocalRootFolder;
import de.mxro.utils.URI;
import de.mxro.utils.URIImpl;
import de.mxro.utils.log.UserError;


public abstract class FileSystemObject implements Comparable<FileSystemObject> {
	
	/**
	 * just for compability
	 */
	@Deprecated
	private final String name="";
	
	private final Folder owner;
	private final FileSystem fileSystem;
	private final URIImpl uri;
	
	public Folder getOwner() {
		return this.owner;
	}

	public int compareTo(FileSystemObject arg0) {
		if (arg0 instanceof FileSystemObject)
			return (arg0).getName().compareTo(this.getName());
		return 0;
	}

	public String getName() { 	
		return uri.getLastElement();
	}
	
	

	public FileSystemObject(String name, Folder owner) throws URISyntaxException {
		this(owner.getURI().addFileToFolder(name), owner, owner.getFileSystem());
	}
	
	public FileSystemObject(final URI uri, final Folder owner, final FileSystem fileSystem) {
		super();
		this.uri = (URIImpl) uri;
		this.owner = owner;
		this.fileSystem = fileSystem;
	};
	
	public boolean delete() {
		return this.fileSystem.deleteFile(this.getURI());
	}
	
	public  java.net.URL getURL() {  
			try {
				return this.getURI().toURL(); 
			} catch (final java.net.MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
	
	public URI getURI() {
		return uri;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FileSystemObject))
			return false;
		return ((FileSystemObject) obj).getName().equals(this.getName()) &&
			this.getURI().equals( ((FileSystemObject) obj).getURI() )/* &&
			this.getOwner().equals(((FileSystemObject) obj).getOwner())*/;
	}
	
	public static Folder newLocalFolder(String name,  Folder owner) throws URISyntaxException {
		return new LocalFolder(name, owner);
	}
	
	public static File newLocalFile(java.io.File file) {
		Folder folder;
		try {
			folder = newLocalRootFolder(URIImpl.fromFile(file.getParentFile()));
		} catch (final URISyntaxException e) {
			UserError.singelton.log(e);
			return null;
		}
		if (folder == null) {
			UserError.singelton.log(FileSystemObject.class, "newLocalFile: Could not create folder for : "+file, UserError.Priority.HIGH);
			return null;
		}
		return folder.forceFile(file.getName());
	}
	
	public static File newLocalFile(String name,  Folder owner) throws URISyntaxException {
		return new LocalFile(name, owner);
	}
	
	/*public static File newLocalFile(String path) {
		java.io.File file = new java.io.File(path);
		return new LocalFile(file.getName(), new LocalRootFolder(file.getAbsolutePath()));
	}*/
	
	public static Folder newLocalRootFolder(URI interface1) {
		return new LocalRootFolder(interface1);
	}
	
	public static Folder newLocalRootFolder(Folder folder) {
		//System.out.println("here "+folder.getURI());
		if (folder instanceof LocalFolder)
			return new LocalRootFolder(folder.getURI());
		return null;
	}
	
	/**
	 * creates new folder and imports all sub-files
	 * 
	 * @param folder
	 * @return
	 */
	@Deprecated
	public static Folder importLocalRootFolder(java.io.File folder) {
		assert folder.exists() : "FileSystemObject.importLocalRootFolder: Folder must exist! "+folder.getAbsolutePath();
		assert folder.isDirectory() : "FileSystemObject.importLocalRootFolder: Folder must be directory! "+folder.getAbsolutePath();
		
		
		try {
			final Folder res = newLocalRootFolder(URIImpl.fromFile(folder));
			/*for (java.io.File a : folder.listFiles()) {
				res.importFile(URI.fromFile(a.getAbsoluteFile()));
				// de.mxro.UserError.singelton.log(res.importFile(URI.fromFile(a)).getName());
			}*/
			//de.mxro.UserError.singelton.log(ObjectXMLTransformer.singelton.toXML(res));
			return res;
		} catch (final URISyntaxException e) {
			de.mxro.utils.log.UserError.singelton.log(e);
			return null;
		}		
		
	}
	
	public static Folder newIncludedFolder(String name, Folder owner) throws URISyntaxException {
		return new IncludedFolder(name, owner);
	}
	
	public static File newIncludedFile(String name, Folder owner) throws URISyntaxException {
		return new IncludedFile(name, owner);
	}
	
	public static Folder newIncludedRootFolder(String rootURL)  {
		return IncludedRootFolder.createInstance();
	}
	
	public static Folder currentDir() {
		final java.io.File currentDir = new java.io.File("");
		try {
			return newLocalRootFolder(URIImpl.fromFile(currentDir.getAbsoluteFile()));
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * returns true if object is located at the
	 * local computer. path: file://...
	 * @return
	 */
	public boolean isLocal() {
		return this.getFileSystem().isLocal();
	}
	
	/** erzeugt ggf eine temporaere Kopie der Datei, die von einem
	 * externen Programm geoeffnet werden kann
	 * @return
	 */
	public abstract java.io.File makeLocal();
	
	
	/**
	 * speichert den Inhalt der lokalen Datei wieder im Objekt (entfaellt, wenn
	 * Datei sowieso lokal
	 * @return
	 */
	public abstract boolean updateFromLocal();
	
	public FileSystem getFileSystem() {
		return fileSystem;
	}
}
