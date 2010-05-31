package de.mxro.string.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Gibt den Text zurueck, der sich zwischen den Regulaeren Ausdruecken begin und end befindet.
	 * regExBetween("<head>", "</head>", Filter.identity) gibt beispielsweise den head eines HTML-Doumentes zurueck.
 * @author mer
 *
 */
//@XStreamAlias("regexbetween")
public class RegExBetween extends Filter {

	private final String begin;
	private transient Pattern beginPattern;
	private final String end;
	private transient Pattern endPattern;
	
	private final Pattern getBeginPattern() {
		if (this.beginPattern == null) {
			this.beginPattern =  Pattern.compile(this.begin, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.CANON_EQ); 
		}
		return this.beginPattern;
	}

	private final Pattern getEndPattern() {
		if (this.endPattern == null) {
			this.endPattern =  Pattern.compile(this.end, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.CANON_EQ); 
		}
		return this.endPattern;
	}

	@Override
	public String perform(String s) {
		
		final Matcher beginMatcher = this.getBeginPattern().matcher(s);
		if (beginMatcher.find()) {
			final Matcher endMatcher = this.getEndPattern().matcher(s);
			if (endMatcher.find()) {
				if (endMatcher.start() > beginMatcher.end())
					return this.getNext().perform(s.substring(beginMatcher.end(), endMatcher.start()));
			}
		}
		return null;
	}

	protected RegExBetween(final String begin, final String end, Filter next) {
		super(next);
		this.begin = begin;
		this.end = end;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final RegExBetween other = (RegExBetween) obj;
		if (this.begin == null) {
			if (other.begin != null)
				return false;
		} else if (!this.begin.equals(other.begin))
			return false;
		if (this.end == null) {
			if (other.end != null)
				return false;
		} else if (!this.end.equals(other.end))
			return false;
		return super.equals(obj);
	}

	

	
}
