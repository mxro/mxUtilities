package de.mxro.string.filter;

/**
 * Wandelt alle Grossbuchstabe in Kleinbuchstaben  um.
 * @author mer
 *
 */
public class LowerCase extends Filter {

	@Override
	public String perform(String s) {
		return this.getNext().perform(s.toLowerCase());
	}

	protected LowerCase(Filter next) {
		super(next);
	}
	

}
