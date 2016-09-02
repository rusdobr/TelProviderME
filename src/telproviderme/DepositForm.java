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

    public void clean() {
        e_mobycode.delete(0, e_mobycode.size());
        e_amount.delete(0, e_amount.size());
        e_phoneNum.delete(0, e_phoneNum.size());
        e_operator.setSelectedIndex(0, true);

    }

    public DepositRecord getDepositRecord() {        
        return new DepositRecord(e_phoneNum.getString(),
            providers[e_operator.getSelectedIndex()],
            Integer.valueOf(e_amount.getString()),
            Integer.valueOf(e_mobycode.getString())
        );
    }

}
