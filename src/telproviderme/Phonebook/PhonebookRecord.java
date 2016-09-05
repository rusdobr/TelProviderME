/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme.Phonebook;

import ua.telnumberident.ruslan.PhoneProvider;

/**
 *
 * @author ruslan
 */
public class PhonebookRecord {

    public static final int FN_LEN = 10;

    public static final int LN_LEN = 20;

    public static final int PN_LEN = 15;
    
    public static final int AMOUNT_LEN = 10;

    private final String firstName;

    public int getId(){
        return id.intValue();
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Integer getAmount(){
        return amount;
    }
    private final Integer id;
    private final String lastName;
    private final String phoneNumber;
    private final PhoneProvider phoneProvider;
    private final Integer amount;

    public PhoneProvider getPhoneProvider() {
        return phoneProvider;
    }

    public PhonebookRecord(Integer id, String firstName, String lastName, String phoneNumber, PhoneProvider phoneProvider, Integer amount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.phoneProvider = phoneProvider;
        this.amount = amount;
    }
}
