package com.marchsabino.alchr.net;

import java.net.URL;

/**
 * Allows a connection between the library and the Old School Runescape Grand Exchange.
 *
 * @author Marcello Sabino
 */
public class Connection {

    /** The charset which the expected response is encoded with */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /** The base URL of Old School Runescape's Grand Exchange. */
    public static final String BASE_URL = "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=";

    private final URL url;

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */

    public Connection(URL url) {
    	this.url = url;
	}

	public URL getURL() {
    	return url;
	}

}
