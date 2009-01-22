package ozmud;


/**
 * Generic utility class.
 *  
 * @author Oscar Stigter
 */
public abstract class Util {
	

	public static String replace(String source, String search, String replace) {
		if (source == null || source.length() == 0) {
			return source;
		}
		
		if (search == null) {
			throw new IllegalArgumentException("null search");
		}
		
		final int searchLength = search.length();
		
		if (searchLength == 0) {
			return source;
		}
		
	    final StringBuffer result = new StringBuffer();
	    int startIndex = 0;
	    int oldIndex = 0;
	    while ((oldIndex = source.indexOf(search, startIndex)) >= 0) {
	    	result.append(source.substring(startIndex, oldIndex));
	    	if (replace != null) {
	    		result.append(replace);
	    	}
	    	startIndex = oldIndex + searchLength;
	    }
	    result.append(source.substring(startIndex));
	    return result.toString();
	}
	
	
	public static String capitalize(String s) {
		if (s != null) {
			final int length = s.length();
			if (length > 0) {
				final StringBuilder sb = new StringBuilder(length);
				char c = s.charAt(0);
				if (c >= 'a' && c <= 'z') {
					c -= 32;
					sb.append(c);
					if (length > 1) {
						sb.append(s.substring(1));
					}
					s = sb.toString();
				}
			}
		}
		return s;
	}


}
