package de.mxro.string.filter;


/**
 * Implementiert eine Liste von String-Operationen.
 * Es wird immer ein String uebergeben, veraendert und dann zurueckgeliefter (Funktion perform).
 * @author mer
 *
 */
public abstract class Filter {
	/**
	 * Endmakrer der Liste. Liefert Eingabe unveraendert zurueck.
	 */
	public static final Filter identity = new Identity(null);
	
	private final Filter next;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Filter other = (Filter) obj;
		if (this.next == null) {
			if (other.next != null)
				return false;
		} else if (!this.next.equals(other.next))
			return false;
		return true;
	}

	public final Filter getNext() {
		return this.next;
	}
	
	/**
	 * Fuehrt bestimmte Operationen auf dem String s aus und liefert das Ergebnis zurueck.
	 * @param s
	 * @return
	 */
	public abstract String perform(String s);

	protected Filter(final Filter next) {
		super();
		this.next = next;
	}
	
	/**
	 * Ersetzt den Regulaeren Ausdruck find durch replaceBy.
	 * @param find
	 * @param replaceBy
	 * @param next
	 * @return
	 */
	public static Filter regExReplace(final String find, final String replaceBy, Filter next) {
		return new RegExReplace(find, replaceBy, next);
	}
	
	/**
	 * Gibt den Text zurueck, der sich zwischen den Regulaeren Ausdruecken begin und end befindet.
	 * Filter.regExBetween("<head>", "</head>", Filter.identity) gibt beispielsweise den head eines HTML-Doumentes zurueck.
	 * @param begin
	 * @param end
	 * @param next
	 * @return
	 */
	public static Filter regExBetween(final String begin, final String end, Filter next) {
		return new RegExBetween(begin, end, next);
	}
	
	/**
	 * Fuehrt das Ergebnis zweier Filter zusammen.
	 * Filter.concatenate(Filter.identity, Filter.identiy, Filter.identity) wuerde beispielsweise den Text verdoppeln.
	 * @param first
	 * @param second
	 * @param next
	 * @return
	 */
	public static Filter concatenate(Filter first, Filter second, Filter next) {
		return new Concatenate(first, second, next);
	}
	
	/**
	 * Wendet  eine xslt-Transformation auf den uebergebenen String an.
	 * Der String sollte daher moeglichst ein xml-Dokument beinhalten oder zumindest HTML.
	 * Zum Parsen wird JTidy verwendet.
	 * @param xslt
	 * @param next
	 * @return
	 */
	//public static Filter xslt(String xslt, Filter next) {
	//	return new Xslt(xslt, next);
	//}
	
	/**
	 * Wandelt alle Grossbuchstabe in Kleinbuchstaben  um.
	 * @param next
	 * @return
	 */
	public static Filter lowerCase(Filter next) {
		return new LowerCase(next);
	}
	
	/**
	 * Rntfernt Elemente aus einem String. Zurueck bleiben die Inhalte.
	 * @param next
	 * @return
	 */
	public static Filter removeTags(Filter next) {
		return Filter.regExReplace("<[^>]*>", "", next); // dieser Ausdruck ist noch nicht ideal! > koennte irgendwo als Taginhalt auftauchen, 
		    										  // aber fuer diese Anwendung genau genug
	}
	
	/**
	 * Entfernt allen laut xml-Spefifikation irrelevanten whitespace.
	 * @param next
	 * @return
	 */
	public static Filter removeWhitespace(Filter next) {
		return Filter.regExReplace("[\\n\\t]+", "", Filter.regExReplace("  +", " ", next));
	}
	
	/**
	 * gibt den Titel eines HTML-Dokumentes zurueck.
	 * @param next
	 * @return
	 */
	public static Filter onlyTitle(Filter next) {
		return Filter.regExBetween("<title[^>]*>", "</title", next);
	}
	
	/**
	 * gibt den Inhalt des body-Elementes zurueck.
	 * @param next
	 * @return
	 */
	public static Filter onlyBody(Filter next) {
		return Filter.regExBetween("<body[^>]*>", "</body", next);
	}
	
	/**
	 * appends text to the given string
	 * @param next
	 * @return
	 */
	public static Filter append(String text, Filter next) {
		return new Append(text, next);
	}
	
	
	
	
}
