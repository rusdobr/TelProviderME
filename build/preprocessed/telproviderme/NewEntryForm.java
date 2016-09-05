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
import telproviderme.Phonebook.PhonebookRecord;
import ua.telnumberident.ruslan.ITelephoneProviderIdent;
import ua.telnumberident.ruslan.PhoneProvider;

/**
 *
 * @author ruslan
 */
public class NewEntryForm extends Form{

    private final TextField e_lastName;

    private final TextField e_firstName;

    private final TextField e_phoneNum;
    
    private final ChoiceGroup e_operator;
    
    private final PhoneProvider[] providers;
    
    private final TextField e_amount;

    NewEntryForm(String title, ITelephoneProviderIdent phoneProvider) {
        super(title);
        providers = phoneProvider.getProviders();
        
        e_firstName = new TextField("First name:", "", PhonebookRecord.FN_LEN,
                TextField.ANY);
        e_lastName = new TextField("Last name:", "", PhonebookRecord.LN_LEN, TextField.ANY);
        e_phoneNum = new TextField("Phone Number", "", PhonebookRecord.PN_LEN,
                TextField.PHONENUMBER);
                
        e_operator = new ChoiceGroup( "Operator", Choice.EXCLUSIVE);
        
        for(int  n = 0; n < providers.length; ++n ){
            // TODO : translation here
            e_operator.append(providers[n].toString(), null);
        }
        e_amount = new TextField("Default:", "", PhonebookRecord.AMOUNT_LEN, TextField.NUMERIC);
        
        append(e_firstName);
        append(e_lastName);
        append(e_phoneNum);
        append(e_operator);
        append(e_amount);
    }

    public void clean() {
        e_firstName.delete(0, e_firstName.size());
        e_lastName.delete(0, e_lastName.size());
        e_phoneNum.delete(0, e_phoneNum.size());
        e_amount.delete(0, e_amount.size());
        e_operator.setSelectedIndex(0, true);

    }

    public PhonebookRecord getPhonebookRecord() {
        return new PhonebookRecord(null, e_firstName.getString(),
                e_lastName.getString(),
                e_phoneNum.getString(),
                providers[e_operator.getSelectedIndex()],
                Integer.valueOf(e_amount.getString(),
                PhonebookRecord.IS_FAVORITE)
        );
    }
    
}
