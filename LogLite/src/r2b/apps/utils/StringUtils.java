package r2b.apps.utils;


public final class StringUtils {
	
	/**
	 * Replace whitespaces and non visible characteres
	 * @param text
	 * @return
	 */
	public static String replaceAllWithespacesAndNonVisibleCharacteres(final String text) {
		return text.replaceAll("\\s+",""); 	  
	}

}
