/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import telproviderme.Phonebook.DepositRecord;
import ua.telnumberident.ruslan.ITelephoneProviderIdent;
import ua.telnumberident.ruslan.PhoneNumber;
import ua.telnumberident.ruslan.PhoneProvider;

/**
 *
 * @author ruslan
 */
public class DepositForm extends Form{

    private final TextField e_phoneNum;
    
    private final TextField e_amount;
    
    private final TextField e_mobycode;
      
    private final ChoiceGroup e_operator;
    
    private final PhoneProvider[] providers;
    
    private PhoneNumber phoneNumber;
    
    private PhoneProvider phoneProvider;


    DepositForm(String title, ITelephoneProviderIdent phoneProvider) {
        super(title);
        e_phoneNum = new TextField("Phone Number", "", DepositRecord.PN_LEN,
                TextField.PHONENUMBER);
        providers = phoneProvider.getProviders();
        e_operator = new ChoiceGroup( "Operator", Choice.EXCLUSIVE);
        
        for(int  n = 0; n < providers.length; ++n ){
            // TODO : translation here
            e_operator.append(providers[n].toString(), null);
        }
        e_amount = new TextField("Amount:", "", DepositRecord.AMOUNT_LEN, TextField.NUMERIC);
        e_mobycode = new TextField("Mobycode:", "", DepositRecord.MOBYCODE_LEN, TextField.NUMERIC);
        
        append(e_phoneNum);
        append(e_operator);
        append(e_amount);
        append(e_mobycode);
    }

    private void clean() {
        //e_mobycode.delete(0, e_mobycode.size());
        e_amount.delete(0, e_amount.size());
        e_phoneNum.delete(0, e_phoneNum.size());
        e_operator.setSelectedIndex(0, true);
        phoneNumber = null;
        phoneProvider = null;

    }
    
    public void setDepositRecord(DepositRecord record){
        clean();
        if (record == null) {
            return;
        }
        if (record.getPhoneNumber() != null){
            phoneNumber = record.getPhoneNumber();
            e_phoneNum.setString(record.getPhoneNumber().toString());
            e_phoneNum.setConstraints((e_phoneNum.getConstraints() & TextField.PHONENUMBER) | TextField.UNEDITABLE);
        } else {
            e_phoneNum.setConstraints(e_phoneNum.getConstraints() | TextField.PHONENUMBER);            
        }
        
        if (record.getPhoneProvider() != null) {            
            for(int  n = 0; n < providers.length; ++n ){
                if (providers[n] == record.getPhoneProvider()) {
                    phoneProvider = record.getPhoneProvider();
                    e_operator.setSelectedIndex(n, true);
                }
            }
        }
    }

    public DepositRecord getDepositRecord() {        
        return new DepositRecord((phoneNumber != null ? phoneNumber : new PhoneNumber(e_phoneNum.getString())),
            phoneProvider != null ? phoneProvider : providers[e_operator.getSelectedIndex()],
            Integer.valueOf(e_amount.getString()),
            Integer.valueOf(e_mobycode.getString())
        );
    }

}
