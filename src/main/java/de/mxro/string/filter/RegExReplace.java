package de.mxro.string.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Ersetzt den Regulaeren Ausdruck find durch replaceBy.
 * @author mer
 *
 */
//@XStreamAlias("regexreplace")
public class RegExReplace extends Filter {
	
	private final String find;
	private final String replaceBy;
	private transient Pattern pattern;
	
	private final Pattern getPattern() {
		if (this.pattern == null) {
			this.pattern = Pattern.compile(this.find, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.CANON_EQ); 		
		}
		return this.pattern;
	}

	@Override
	public String perform(String s) {
		final Matcher matcher = this.getPattern().matcher(s);
		return this.getNext().perform(matcher.replaceAll(this.replaceBy));
	}

	protected RegExReplace(final String find, final String replaceBy, Filter next) {
		super(next);
		this.find = find;
		this.replaceBy = replaceBy;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final RegExReplace other = (RegExReplace) obj;
		if (this.find == null) {
			if (other.find != null)
				return false;
		} else if (!this.find.equals(other.find))
			return false;
		if (this.replaceBy == null) {
			if (other.replaceBy != null)
				return false;
		} else if (!this.replaceBy.equals(other.replaceBy))
			return false;
		return super.equals(obj);
	}

	
	
}
