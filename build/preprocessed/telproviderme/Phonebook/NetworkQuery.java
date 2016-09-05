/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme.Phonebook;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.rms.RecordEnumeration;

/**
 *
 * @author ruslan
 */

/*
 * Class to query a network service for address book entries and parse the
 * result. Uses HttpConnection to fetch the entries from a server.
 * 
 * The http request is made using a base url provided by the caller with the
 * query arguments for last name and first name encoded in the query parameters
 * of the URL.
 */
public class NetworkQuery implements RecordEnumeration {

    private StringBuffer buffer = new StringBuffer(80);

    private String[] fields = new String[7];

    private String empty = new String();

    private Vector results = new Vector(20);

    private Enumeration resultsEnumeration;

    final static String baseurl = "http://127.0.0.1:8080/Book/netaddr";

    /**
     * Create a RecordEnumeration from the network.
     *
     * Query a network service for addresses matching the specified criteria.
     * The base URL of the service has the query parameters appended. The
     * request is made and the contents parsed into a Vector which is used as
     * the basis of the RecordEnumeration. lastname the last name to search for
     * firstname the first name to search for sortorder the order in which to
     * sort 1 is by last name, 0 is by first name
     */
    public NetworkQuery(String firstname, String lastname, int sortorder) {
        HttpConnection c = null;
                
        int ch;
        InputStream is = null;
        InputStreamReader reader;
        String url;

        // Format the complete URL to request
        buffer.setLength(0);
        buffer.append(baseurl);
        buffer.append("?last=");
        buffer.append((lastname != null) ? lastname : empty);
        buffer.append("&first=");
        buffer.append((firstname != null) ? firstname : empty);
        buffer.append("&sort=" + sortorder);

        url = buffer.toString();

        // Open the connection to the service
        try {
            c = open(url);
            results.removeAllElements();

            /*
       * Open the InputStream and construct a reader to convert from bytes
       * to chars.
             */
            is = c.openInputStream();
            reader = new InputStreamReader(is);
            while (true) {
                int i = 0;
                fields[0] = empty;
                fields[1] = empty;
                fields[2] = empty;
                fields[3] = empty;
                fields[4] = empty;
                fields[5] = empty;
                fields[6] = empty;
                do {
                    buffer.setLength(0);
                    while ((ch = reader.read()) != -1 && (ch != ',')
                            && (ch != '\n')) {
                        if (ch == '\r') {
                            continue;
                        }
                        buffer.append((char) ch);
                    }

                    if (ch == -1) {
                        throw new EOFException();
                    }

                    if (buffer.length() > 0) {
                        if (i < fields.length) {
                            fields[i++] = buffer.toString();
                        }
                    }
                } while (ch != '\n');

                if (fields[0].length() > 0) {
                    results.addElement(SimpleRecord.createRecord(Integer.valueOf(fields[0]),
                            fields[1], fields[2], fields[3], fields[4], Integer.valueOf(fields[5]), fields[6]));
                }
            }
        } catch (Exception e) {

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        resultsEnumeration = results.elements();
    }

    /**
     * Read the HTTP headers and the data using HttpConnection. Check the
     * response code to ensure successful open.
     *
     * Connector.open is used to open url and a HttpConnection is returned. The
     * HTTP headers are read and processed. url the URL to open throws
     * IOException for any network related exception
     */
    private HttpConnection open(String url) throws IOException {
        HttpConnection c;
        int status = -1;

        // Open the connection and check for redirects
        while (true) {
            c = (HttpConnection) Connector.open(url);

            // Get the status code,
            // causing the connection to be made
            status = c.getResponseCode();

            if ((status == HttpConnection.HTTP_TEMP_REDIRECT)
                    || (status == HttpConnection.HTTP_MOVED_TEMP)
                    || (status == HttpConnection.HTTP_MOVED_PERM)) {

                // Get the new location and close the connection
                url = c.getHeaderField("location");
                c.close();
            } else {
                break;
            }
        }

        // Only HTTP_OK (200) means the content is returned.
        if (status != HttpConnection.HTTP_OK) {
            c.close();
            throw new IOException("Response status not OK");
        }
        return c;
    }

    /**
     * Returns true if more elements exist in enumeration.
     */
    public boolean hasNextElement() {
        return resultsEnumeration.hasMoreElements();
    }

    /**
     * Returns a copy of the next record in this enumeration,
     */
    public byte[] nextRecord() {
        return (byte[]) resultsEnumeration.nextElement();
    }

    /**
     * The following are simply stubs that we don't implement...
     */
    public boolean hasPreviousElement() {
        return false;
    }

    public void destroy() {
    }

    public boolean isKeptUpdated() {
        return false;
    }

    public void keepUpdated(boolean b) {
        return;
    }

    public int nextRecordId() {
        return 0;
    }

    public int numRecords() {
        return 0;
    }

    public byte[] previousRecord() {
        return null;
    }

    public int previousRecordId() {
        return 0;
    }

    public void rebuild() {
        return;
    }

    public void reset() {
        return;
    }
}
