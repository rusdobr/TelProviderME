/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme.Phonebook;

import javax.microedition.rms.RecordComparator;

/**
 *
 * @author ruslan
 */
/**
 * This class implements the RecordComparator interface. It works on the records
 * created by SimpleRecord. It sorts on either first name or last name.
 */
public class SimpleComparator implements RecordComparator {

    /**
     * Sorting values (sort by first or last name)
     */
    public final static int SORT_BY_FIRST_NAME = 1;

    public final static int SORT_BY_LAST_NAME = 2;

    /**
     * Sort order. Set by constructor.
     */
    private int sortOrder = -1;

    /**
     * Public constructor: sets the sort order to be used for this
     * instantiation.
     *
     * Sanitize s: if it is not one of the valid sort codes, set it to
     * SORT_BY_LAST_NAME silently. s the desired sort order
     */
    public SimpleComparator(int s) {
        switch (s) {
            case SORT_BY_FIRST_NAME:
            case SORT_BY_LAST_NAME:
                this.sortOrder = s;
                break;
            default:
                this.sortOrder = SORT_BY_LAST_NAME;
                break;
        }
    }

    /**
     * This is the compare method. It takes two records, and depending on the
     * sort order extracts and lexicographically compares the subfields as two
     * Strings.
     *
     * r1 First record to compare r2 Second record to compare return one of the
     * following:
     *
     * RecordComparator.PRECEDES if r1 is lexicographically less than r2
     * RecordComparator.FOLLOWS if r1 is lexicographically greater than r2
     * RecordComparator.EQUIVALENT if r1 and r2 are lexicographically equivalent
     */
    public int compare(byte[] r1, byte[] r2) {

        String n1 = null;
        String n2 = null;

        // Based on sortOrder, extract the correct fields
        // from the record and convert them to lower case
        // so that we can perform a case-insensitive compare.
        if (sortOrder == SORT_BY_FIRST_NAME) {
            n1 = SimpleRecord.getFirstName(r1).toLowerCase();
            n2 = SimpleRecord.getFirstName(r2).toLowerCase();
        } else if (sortOrder == SORT_BY_LAST_NAME) {
            n1 = SimpleRecord.getLastName(r1).toLowerCase();
            n2 = SimpleRecord.getLastName(r2).toLowerCase();
        }

        int n = n1.compareTo(n2);
        if (n < 0) {
            return RecordComparator.PRECEDES;
        }
        if (n > 0) {
            return RecordComparator.FOLLOWS;
        }

        return RecordComparator.EQUIVALENT;
    }
}
