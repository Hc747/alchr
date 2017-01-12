package com.marchsabino.alchr.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Allows a connection between the library and the Old School Runescape Grand Exchange.
 *
 * @author Marcello Sabino
 */
public class Connection extends URLConnection {

    /** The charset which the expected response is encoded with */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /** The base URL of Old School Runescape's Grand Exchange. */
    public static final String BASE_URL = "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=";

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    public Connection(URL url) {
        super(url);
    }

    @Override
    public void connect() throws IOException {

    }
}
