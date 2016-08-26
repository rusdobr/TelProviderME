/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.TextField;
import telproviderme.Phonebook.PhonebookRecord;
import ua.telnumberident.ruslan.ITelephoneProviderIdent;
import ua.telnumberident.ruslan.PhoneProvider;

/**
 *
 * @author ruslan
 */
public class NewEntryForm extends Form implements ItemCommandListener{

    private final TextField e_lastName;

    private final TextField e_firstName;

    private final TextField e_phoneNum;
    
    private final ChoiceGroup e_operator;
    
    private PhoneProvider[] providers;


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

            
        e_operator.setItemCommandListener(this);
        append(e_firstName);
        append(e_lastName);
        append(e_phoneNum);
        append(e_operator);
    }

    public void clean() {
        e_firstName.delete(0, e_firstName.size());
        e_lastName.delete(0, e_lastName.size());
        e_phoneNum.delete(0, e_phoneNum.size());
        e_operator.setSelectedIndex(0, true);

    }

    public PhonebookRecord getPhonebookRecord() {        
        return new PhonebookRecord(e_firstName.getString(),
                e_lastName.getString(),
                e_phoneNum.getString(),
                providers[e_operator.getSelectedIndex()]);
    }

    public void commandAction(Command c, Item item) {
        throw new UnsupportedOperationException("Not supported yet." + c.getLabel()); //To change body of generated methods, choose Tools | Templates.
    }
}
