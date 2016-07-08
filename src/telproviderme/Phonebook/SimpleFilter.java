/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme.Phonebook;

import javax.microedition.rms.RecordFilter;
//import telproviderme.SimpleRecord;

/**
 *
 * @author ruslan
 */
/**
 * This class implements the RecordFilter interface. It works on the records
 * created by SimpleRecord. It filters on first name and/or last name.
 */
public class SimpleFilter implements RecordFilter {

    // first and last names on which to filter
    private String first;

    private String last;

    /**
     * Public constructor: stores the first and last names on which to filter.
     * Stores first/last names as lower case so that filters are are
     * case-insensitive.
     */
    public SimpleFilter(String f, String l) {
        first = f.toLowerCase();
        last = l.toLowerCase();
    }

    /**
     * Takes a record, (r), and checks to see if it matches the first and last
     * name set in our constructor.
     *
     * Extracts the first and last names from the record, converts them to lower
     * case, then compares them with the values extracted from the record.
     *
     * return true if record matches, false otherwise
     */
    public boolean matches(byte[] r) {

        String f = SimpleRecord.getFirstName(r).toLowerCase();
        String l = SimpleRecord.getLastName(r).toLowerCase();

        return f.startsWith(first) && l.startsWith(last);
    }
}
