package de.mxro.string.filter;


/**
 * Endmarke der Liste. Liefert String unveraendert zurueck.
 * @author mer
 *
 */
//@XStreamAlias("identity")
public class Identity extends Filter {

	@Override
	public String perform(String s) {
		return s;
	}

	protected Identity(Filter next) {
		super(next);
	}

	
	
}
