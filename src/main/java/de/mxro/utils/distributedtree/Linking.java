package de.mxro.utils.distributedtree;

import java.util.Vector;

/**
 * An Interface for classes that contain links
 * to other nodes or files<br/>
 * 
 * Links can be transformed with
 * LinksInItemsTransformation 
 * 
 * @author mx
 *
 * @param <O>
 */
public interface Linking<O> {
	
	public static class LinkVector extends Vector<String> {
	
		private static final long serialVersionUID = 2472100465511583554L;
		
	}
	
	/**
	 * vector of all the links to nodes/files that this element is pointing to
	 */
	public LinkVector getLinkVector();
	
	/**
	 * creates a new instance of the element with updated links
	 * @param vector
	 * @return
	 */
	public O newInstanceFromLinkVector(LinkVector vector);
	
	/**
	 * 
	 * @return true if the links of this element are primilarly
	 * linked by the element and not a secondary link 
	 */
	public boolean isLinkOwner();
}
