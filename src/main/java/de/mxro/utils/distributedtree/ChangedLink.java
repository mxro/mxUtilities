/**
 * 
 */
package de.mxro.utils.distributedtree;


public class ChangedLink {
	public final String oldLink;
	public final String newLink;
	
	public ChangedLink(final String oldLink, final String newLink) {
		super();
		this.oldLink = oldLink;
		this.newLink = newLink;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ChangedLink))
			return false;
		final ChangedLink other = (ChangedLink) obj;
		
		if (this.newLink != null) {
			if (this.newLink.equals(other.newLink)) return true;
		}
		
		if (this.oldLink != null) {
			if (this.oldLink.equals(other.oldLink)) return true;
		}
		
		if (this.oldLink == null && other.oldLink == null 
				&& this.newLink == null && other.newLink == null) return true;
		
		return false;
	}




	@Override
	public String toString() {
		return this.oldLink+" -> "+this.newLink;
	}
	
}