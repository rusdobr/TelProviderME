/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import telproviderme.Phonebook.PhonebookRecord;

/**
 *
 * @author ruslan
 */
public class NewEntryForm extends Form {

    private final TextField e_lastName;

    private final TextField e_firstName;

    private final TextField e_phoneNum;

    NewEntryForm(String title) {
        super(title);

        e_firstName = new TextField("First name:", "", PhonebookRecord.FN_LEN,
                TextField.ANY);
        e_lastName = new TextField("Last name:", "", PhonebookRecord.LN_LEN, TextField.ANY);
        e_phoneNum = new TextField("Phone Number", "", PhonebookRecord.PN_LEN,
                TextField.PHONENUMBER);
        append(e_firstName);
        append(e_lastName);
        append(e_phoneNum);
    }

    public void clean() {
        e_firstName.delete(0, e_firstName.size());
        e_lastName.delete(0, e_lastName.size());
        e_phoneNum.delete(0, e_phoneNum.size());
    }

    public PhonebookRecord getPhonebookRecord() {
        return new PhonebookRecord(e_firstName.getString(),
                e_lastName.getString(),
                e_phoneNum.getString());
    }
}
