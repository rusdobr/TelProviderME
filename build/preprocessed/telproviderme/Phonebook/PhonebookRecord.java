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
public class PhonebookRecord {

    public static final int FN_LEN = 10;

    public static final int LN_LEN = 20;

    public static final int PN_LEN = 15;

    private final String firstName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    private final String lastName;
    private final String phoneNumber;

    public PhonebookRecord(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
