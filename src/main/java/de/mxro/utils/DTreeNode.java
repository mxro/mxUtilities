package de.mxro.utils;

import java.util.Vector;

import de.mxro.filesystem.File;
import de.mxro.filesystem.Folder;
import de.mxro.utils.distributedtree.ChangedLink;

/**
 * interface for nodes in distributed trees
 * eg xml documents where nodes in the xml document
 * refer to nodes in other xml documents
 * 
 * @author mx
 *
 */
public interface DTreeNode {
	/**
	 * 
	 * @return the folder in a virtual filesystem that the node is located in
	 */
	public Folder getFolder();
	
	/**
	 * 
	 * @return the file in a virtual filesystem where the data of the node is stored
	 */
	public File getFile();
	
	
	
	/**
	 * uris to child nodes
	 * @return
	 */
	public Vector<String> getChildNodes(); 
	
	/**
	 * update the links in the content of the node
	 * to other nodes or files they refer to
	 * 
	 * @param changedLinks
	 * @return true if update successful
	 */
	public boolean updateLinks(Vector<ChangedLink> changedLinks);
	
}
