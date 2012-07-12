package org.jembi.rhea;

public class Util {
	
	/**
	 * Split an id string into the id type and the id number
	 * @param id_str the id string to split
	 * @return an array with the first value being the id type and the second being the id number
	 */
	public static String[] splitIdentifer(String id_str) {
		int index = id_str.indexOf('-');
		String idType = id_str.substring(0, index);
		String id = id_str.substring(index + 1);
		String[] ret = new String[2];
		ret[0] = idType;
		ret[1] = id;
		return ret;
	}

}
