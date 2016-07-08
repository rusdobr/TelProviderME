/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import ua.telnumberident.ruslan.*;
import javax.microedition.pim.*;
import java.util.Enumeration;
import javax.microedition.io.*;

/**
 *
 * @author ruslan
 */
public class MainForm extends Form implements CommandListener, ItemStateListener{
    
    private ChoiceGroup mode;
    
    private PIM pim;
    
    private MIDlet parent;
            
    MainForm(String title, MIDlet parent, ITelephoneProviderIdent api) {
        super(title);
        this.parent = parent;
        /*
        pim = PIM.getInstance();
        String[] eventLists = pim.listPIMLists(PIM.CONTACT_LIST);
        String[]  items;
        if (eventLists.length > 0) {
            items = new String[eventLists.length];
            //EventList list = openDefaultEventList(PIM.READ_ONLY);
            for (int i = 0; i< eventLists.length; ++i){
                items[i] = eventLists[i];
            }
        } else {
            items = new String[1];
            items[0] = "No items found";
        }
        */
        /*
        PhoneMaskItem[] maskList = api.search(new PhoneNumber());
        String[]  items = new String[maskList.length];
        for (int i = 0; i< maskList.length; ++i){
            PhoneMaskItem item = maskList[i];
            items[i] = item.toString();
        }
        */
        String items[] = loadContacts();
        mode = new ChoiceGroup("Title",
                               Choice.EXCLUSIVE,
                               items,
                               null);
        append(mode);
        //Command select = new Command("Select", Command.OK, 0);
        addCommand(new Command("Select", Command.OK, 0));
        //setCommandListener(this);
        setItemStateListener(this);
        Display.getDisplay(parent).setCurrent(this);
    }
    
    public EventList openDefaultEventList(int mode) {
        EventList eventList = null;
        if (mode != PIM.READ_ONLY &&
            mode != PIM.WRITE_ONLY &&
            mode != PIM.READ_WRITE) {
            return null;
        }
        try {
            // Open the default event PIM list in the specified mode
            eventList = (EventList) pim.openPIMList(PIM.CONTACT_LIST, mode);
        } catch (PIMException pe) {
            // process PIM exception
            eventList = null;
        }
        return eventList;
    }
    
    public String[] loadContacts() {
        String[] resultItems = new String[1];
        System.out.println("into show contacts1");
        try {
            javax.microedition.pim.ContactList addressbook = null;
            javax.microedition.pim.ContactList simaddressbook = null;
            PIM pim = PIM.getInstance();
            System.out.println("into show contacts2");
            String[] allContactLists = PIM.getInstance().listPIMLists(PIM.CONTACT_LIST);
            System.out.println("into show contacts3");
            for (int ctr = 0; ctr < allContactLists.length; ctr++) {
                pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, allContactLists[ctr]);
                addressbook = (javax.microedition.pim.ContactList) (pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY));
                Contact contact = null;
                Enumeration items = null;
                items = addressbook.items();
                resultItems = new String[2];
                int i = 0;
                while (items.hasMoreElements() && i++ < resultItems.length) {
                    contact = (Contact) items.nextElement();
                    //int telCount = contact.countValues(Contact.TEL);
                    int nameCount = contact.countValues(Contact.FORMATTED_NAME);
                    if (/*telCount > 0 && */ nameCount > 0) {
                        String contactName = contact.getString(Contact.FORMATTED_NAME, 0);// + ":" + contact.getString(Contact.TEL, 0);
                        resultItems[i-1] = contactName;
                        System.out.println(contactName);
                    }
                }
                break;
            }
        } catch (Exception e) {
            resultItems[0] = e.toString();
        }
        return resultItems;
    }
    
    
    public void commandAction(Command c, Displayable d) 
    {
        System.out.println("Command" + c.toString());
    }
    public void itemStateChanged(Item item) {
        if(item == mode) {
            System.out.println("You have selected " + mode.getString(mode.getSelectedIndex()) + ".");
            try {
                parent.platformRequest("tel:+380501925955");
                } catch (ConnectionNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
        }
    }
    
    private void run() {/*
        String ussdCode = "*101#";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(ussdToCallableUri(ussdCode));
        try{
            startActivity(intent);
        } catch (SecurityException e){
            e.printStackTrace();
        }*/
    }
/*
    private Uri ussdToCallableUri(String ussd) {

        String uriString = "";

        if(!ussd.startsWith("tel:"))
            uriString += "tel:";

        for(char c : ussd.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }*/
}
