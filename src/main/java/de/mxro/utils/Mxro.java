package de.mxro.utils;

import java.net.URISyntaxException;

import de.mxro.filesystem.FileSystemObject;
import de.mxro.filesystem.Folder;

public class Mxro {
	private static Folder utilityAppsFolder;
	
	public static Folder getUtilityAppsFolder() {
		if (utilityAppsFolder == null) {
			try {
				utilityAppsFolder = FileSystemObject.newLocalRootFolder(URIImpl.fromFile(new java.io.File("").getAbsoluteFile()));
			} catch (final URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return utilityAppsFolder;
	}
	
	public static void setUtilityAppsFolder(Folder folder) {
		utilityAppsFolder = folder;
	}

}
