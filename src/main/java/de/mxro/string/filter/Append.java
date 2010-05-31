package de.mxro.string.filter;

public class Append extends Filter {

	private final String text;
	
	
	
	public Append(final String text, Filter next) {
		super(next);
		this.text = text;
	}



	@Override
	public String perform(String s) {
		return s + this.text;
	}

}
