/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme.Phonebook;

/**
 *
 * @author ruslan
 */
/**
 * This class provides static methods that allow us to hide the format of a
 * record. N.B. no synchronized access is provided
 */
public final class SimpleRecord {
    
    private final static int ID_INDEX = 0;
    private final static int ID_LENGTH = 10;

    private final static int FIELD_LEN = 20;
    
    private final static int FIRST_NAME_INDEX = ID_INDEX + ID_LENGTH;

    private final static int LAST_NAME_INDEX = FIRST_NAME_INDEX + FIELD_LEN;
    
    private final static int PHONE_INDEX = LAST_NAME_INDEX + FIELD_LEN;
    
    private final static int PROVIDER_INDEX = PHONE_INDEX + FIELD_LEN;
    
    private final static int AMOUNT_INDEX = PROVIDER_INDEX + FIELD_LEN;
    private final static int AMOUNT_LENGTH = 10;

    private final static int MAX_REC_LEN = AMOUNT_INDEX + AMOUNT_LENGTH;

    private static StringBuffer recBuf = new StringBuffer(MAX_REC_LEN);

    // Don't let anyone instantiate this class
    private SimpleRecord() {
    }

    // Clear internal buffer
    private static void clearBuf() {
        for (int i = 0; i < MAX_REC_LEN; i++) {
            recBuf.insert(i, ' ');
        }
        recBuf.setLength(MAX_REC_LEN);
    }

    /**
     * Takes component parts and return a record suitable for our address book.
     *
     * return byte[] the newly created record first record field: first name
     * last record field: last name num record field: phone number
     */
    public static byte[] createRecord(Integer id, String first, String last, String num, String provider, Integer amount) {
        clearBuf();
        recBuf.insert(ID_INDEX, id);
        recBuf.insert(FIRST_NAME_INDEX, first);
        recBuf.insert(LAST_NAME_INDEX, last);
        recBuf.insert(PHONE_INDEX, num);
        recBuf.insert(PROVIDER_INDEX, provider);
        recBuf.insert(AMOUNT_INDEX, amount);
        recBuf.setLength(MAX_REC_LEN);
        return recBuf.toString().getBytes();
    }

    /**
     * Extracts the ID field from a record. return Integer representing unique identifier 
     * of inserted record
     */
    public static Integer getId(byte[] b) {
        return Integer.valueOf(new String(b, ID_INDEX, ID_LENGTH).trim());
    }
    
    /**
     * Extracts the first name field from a record. return String contains the
     * first name field b the record to parse
     */
    public static String getFirstName(byte[] b) {
        return new String(b, FIRST_NAME_INDEX, FIELD_LEN).trim();
    }

    /**
     * Extracts the last name field from a record. return String contains the
     * last name field b the record to parse
     */
    public static String getLastName(byte[] b) {
        return new String(b, LAST_NAME_INDEX, FIELD_LEN).trim();
    }

    /**
     * Extracts the phone number field from a record. return String contains the
     * phone number field b the record to parse
     */
    public static String getPhoneNum(byte[] b) {
        return new String(b, PHONE_INDEX, FIELD_LEN).trim();
    }
    /**
     * Extracts the first name field from a record. return String contains the
     * first name field b the record to parse
     */
    public static String getProvider(byte[] b) {
        return new String(b, PROVIDER_INDEX, FIELD_LEN).trim();
    }
    
    public static Integer getAmount(byte[] b) {
        return Integer.valueOf(new String(b, AMOUNT_INDEX, AMOUNT_LENGTH).trim());
    }
}
