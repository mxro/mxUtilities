package de.mxro.string.filter;

/**
 * Fuehrt das Ergebnis zweier Filter zusammen.
 * Filter.concatenate(Filter.identity, Filter.identiy, Filter.identity) wuerde beispielsweise den Text verdoppeln.
 * @author mer
 *
 */
public class Concatenate extends Filter {
	
	private final Filter filter1;
	private final Filter filter2;
	
	@Override
	public String perform(String s) {
		return this.getNext().perform(this.filter1.perform(s)+ this.filter2.perform(s));
	}

	protected Concatenate(final Filter filter1, final Filter filter2, Filter next) {
		super(next);
		this.filter1 = filter1;
		this.filter2 = filter2;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Concatenate other = (Concatenate) obj;
		if (this.filter1 == null) {
			if (other.filter1 != null)
				return false;
		} else if (!this.filter1.equals(other.filter1))
			return false;
		if (this.filter2 == null) {
			if (other.filter2 != null)
				return false;
		} else if (!this.filter2.equals(other.filter2))
			return false;
		return super.equals(obj);
	}
	
	
}
