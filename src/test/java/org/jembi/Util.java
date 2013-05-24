package org.jembi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {
	
	public static String getResourceAsString(String resource) throws IOException {
		InputStream is = Util.class.getClassLoader().getResourceAsStream(resource);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while((line = reader.readLine()) != null ) {
	        stringBuilder.append(line);
	        stringBuilder.append(ls);
	    }

	    return stringBuilder.toString();
	}

}
