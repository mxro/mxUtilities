package de.mxro.utils.drm;

public interface Change<R> {

	public static enum Type {
		REVERSIBLE, 
		
		/**
		 * this is done automatically
		 */
		IMPLICIT,
		
		/**
		 * just skip it
		 */
		SKIP,
		
		/**
		 * this is a stopper
		 * 
		 */
		IRREVERSIBLE
	}
	
	public abstract boolean doOnResource(R doc);

	public abstract boolean undoOnResource(R doc);

}