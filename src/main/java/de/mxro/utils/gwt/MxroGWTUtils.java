package de.mxro.utils.gwt;

import de.mxro.utils.log.UserError;

public class MxroGWTUtils {
	public static String removeFirstElement(String s, String separator) {
		final String[] list = s.split(separator);
		if (list.length == 0) {
			UserError.singelton.log("mxro.Utils: String used for removeFirstElement() is empty.", UserError.Priority.NORMAL);
			return null;
		}
		if (list.length == 1)
			return "";
		if (list.length == 2)
			return list[1];
		String res=list[1];
		for (int i=2;i<list.length;i++) {
			res += "/"+list[i];
		}
		
		return res;
	}

	public static char[] allowedCharacters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
	        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
	        's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
	        '_', '-', '.',
	        '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        '0'	                                      
	};

	public static String getSimpleName(String forName) {
		final String n = forName;
		if (n.length() > 0) {
			String simple="";
			for (int i = 0; i<n.length(); i++) {
				boolean found = false;
				for (final char element : allowedCharacters) {
					found = found || n.charAt(i) == element ||
					n.charAt(i) == Character.toUpperCase(element);
				}
				if (found) {
					simple = simple + n.charAt(i);
				} else {
					simple = simple + '_';
				}
			}
			return simple;
		} else
			return n;
	}
}
