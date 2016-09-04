/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme.Phonebook;

import ua.telnumberident.ruslan.PhoneNumber;
import ua.telnumberident.ruslan.PhoneProvider;

/**
 *
 * @author ruslan
 */
public class DepositRecord {

    public static final int AMOUNT_LEN = 3;

    public static final int MOBYCODE_LEN = 4;

    public static final int PN_LEN = 15;

    
    private final Integer amount;
    private final Integer mobyCode;
    private final PhoneNumber phoneNumber;
    private final PhoneProvider phoneProvider;

    public PhoneProvider getPhoneProvider() {
        return phoneProvider;
    }
    
    public Integer getAmount(){
        return amount;
    }
    
    public Integer getMobyCode(){
        return mobyCode;
    }
    
    public PhoneNumber getPhoneNumber(){
        return phoneNumber;
    }

    public DepositRecord(PhoneNumber phoneNumber, PhoneProvider phoneProvider, Integer amount, Integer mobyCode) {
        this.amount = amount;
        this.mobyCode = mobyCode;
        this.phoneNumber = phoneNumber;
        this.phoneProvider = phoneProvider;
    }
}
