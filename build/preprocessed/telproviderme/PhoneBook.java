/*
 * Copyright (c) 2000-2001 Sun Microsystems, Inc. All Rights Reserved.
 */
package telproviderme;

import java.util.Hashtable;
import java.util.Vector;
import telproviderme.Phonebook.*;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import telproviderme.deposit.PortmoneScript;
import ua.telnumberident.ruslan.ITelephoneProviderIdent;
import ua.telnumberident.ruslan.PhoneNumber;
import ua.telnumberident.ruslan.PhoneProvider;
import ua.telnumberident.ruslan.TelephoneNumberIdentUA;

/**
 * This MIDlet implements a simple address book with the following
 * functionality: browsing, entry, deletion, and searching (both on device and
 * over the network).
 */
public class PhoneBook extends MIDlet implements CommandListener,
        ItemStateListener {

    private RecordStore addrBook;

    final private static int ERROR = 0;

    final private static int INFO = 1;

    private Display display;

    private Alert alert;

    private Command cmdAdd;
    
    private Command cmdDeposit;
    
    private Command cmdBack;

    private Command cmdCancel;

    private Command cmdDial;

    private Command cmdExit;

    private Command cmdSelect;

    private Command cmdSearchNetwork;

    private Command cmdSearchLocal;

    private List mainScr;

    private final static int SEARCH_INDEX = 0;
    private final static int NEW_INDEX = 1;
    private final static int BROWSE_INDEX = 2;
    private final static int FAVORITES_INDEX = 3;
    private final static int OPTIONS_INDEX = 4;
    private final static int OPTIONS_DEPOSIT = 5;
    private final static int TESTDATA_INDEX = 6;
    // TODO : add translation
    private String[] mainScrChoices = {"Search", "Add New", "Browse", "Favorites",
        "Options", "Deposit", "Testdata"};

    private Form searchScr;

    private TextField s_lastName;

    private TextField s_firstName;

    private NewEntryForm entryScr;
    
    private DepositForm depositScr;

    private List nameScr;

    private Vector phoneRecords;

    private Form optionScr;

    private ChoiceGroup sortChoice;

    private TextBox dialScr;
    
    private TextBox depositText;

    private int sortOrder = 1;
    
    private ITelephoneProviderIdent phoneProvider;
   

    /**
     * Public no-argument constructor. Called by the system to instantiate our
     * class. Caches reference to the display, allocate commands, and tries to
     * open the address book.
     */
    public PhoneBook() {
        
        phoneProvider = TelephoneNumberIdentUA.createInstance();
        
        display = Display.getDisplay(this);

        cmdAdd = new Command("Add", Command.OK, 1);
        cmdDeposit = new Command("Run", Command.OK, 1);
        cmdBack = new Command("Back", Command.BACK, 2);
        cmdCancel = new Command("Cancel", Command.BACK, 2);
        cmdDial = new Command("Dial", Command.OK, 1);
        cmdExit = new Command("Exit", Command.EXIT, 2);
        cmdSelect = new Command("Select", Command.OK, 1);
        cmdSearchNetwork = new Command("Network", Command.SCREEN, 4);
        cmdSearchLocal = new Command("Local", Command.SCREEN, 3);

        alert = new Alert("", "", null, AlertType.INFO);
        alert.setTimeout(2000);
        try {
            addrBook = RecordStore.openRecordStore("TheAddressBook", true);
        } catch (RecordStoreException e) {
            addrBook = null;
        }
    }

    /**
     * Called by the system to start our MIDlet. If the open of the address book
     * fails, display an alert and continue.
     *
     */
    protected void startApp() {
        if (addrBook == null) {
            displayAlert(ERROR, "Could not open address book", null);
        } else {
            genMainScr();
        }
    }

    /**
     * Called by the system to pause our MIDlet. No actions required by our
     * MIDlet.
     */
    protected void pauseApp() {
    }

    /**
     * Called by the system to end our MIDlet. No actions required by our
     * MIDlet.
     */
    protected void destroyApp(boolean unconditional) {
        if (addrBook != null) {
            try {
                addrBook.closeRecordStore();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Display an Alert on the screen
     *
     * @param type One of the following: ERROR, INFO
     * @param msg Message to display
     * @param s screen to change to after displaying alert. if null, revert to
     * main screen
     */
    private void displayAlert(int type, String msg, Screen s) {
        alert.setString(msg);

        switch (type) {
            case ERROR:
                alert.setTitle("Error!");
                alert.setType(AlertType.ERROR);
                break;
            case INFO:
                alert.setTitle("Info");
                alert.setType(AlertType.INFO);
                break;
        }
        display.setCurrent(alert, s == null ? display.getCurrent() : s);
    }

    /**
     * Notify the system that we are exiting.
     */
    private void midletExit() {
        destroyApp(false);
        notifyDestroyed();
    }

    /**
     * Create the first screen of our MIDlet. This screen is a list.
     */
    private Screen genMainScr() {
        if (mainScr == null) {
            mainScr = new List("Menu", List.IMPLICIT, mainScrChoices, null);
            mainScr.addCommand(cmdSelect);
            mainScr.addCommand(cmdExit);
            mainScr.setCommandListener(this);
        }
        display.setCurrent(mainScr);
        return mainScr;
    }

    /**
     * Sort order option screen. Allows us to set sort order to either sorting
     * by last name (default), or first name.
     */
    private Screen genOptionScr() {
        if (optionScr == null) {
            optionScr = new Form("Options");
            optionScr.addCommand(cmdBack);
            optionScr.setCommandListener(this);

            sortChoice = new ChoiceGroup("Sort by", Choice.EXCLUSIVE);
            sortChoice.append("First name", null);
            sortChoice.append("Last name", null);
            sortChoice.setSelectedIndex(sortOrder, true);
            optionScr.append(sortChoice);
            optionScr.setItemStateListener(this);
        }
        display.setCurrent(optionScr);
        return optionScr;
    }

    /**
     * Search screen.
     *
     * Displays two <code>TextField</code>s: one for first name, and one for
     * last name. These are used for searching the address book.
     *
     * @see AddressBookMIDlet#genNameScr
     */
    private Screen genSearchScr() {
        if (searchScr == null) {
            searchScr = new Form("Search");
            searchScr.addCommand(cmdBack);
            searchScr.addCommand(cmdSearchNetwork);
            searchScr.addCommand(cmdSearchLocal);
            searchScr.setCommandListener(this);
            s_firstName = new TextField("First name:", "", PhonebookRecord.FN_LEN,
                    TextField.ANY);
            s_lastName = new TextField("Last name:", "", PhonebookRecord.LN_LEN, TextField.ANY);
            searchScr.append(s_firstName);
            searchScr.append(s_lastName);
        }

        s_firstName.delete(0, s_firstName.size());
        s_lastName.delete(0, s_lastName.size());
        display.setCurrent(searchScr);
        return searchScr;
    }

    /**
     * Name/Phone number entry screen
     *
     * Displays three <code>TextField</code>s: one for first name, one for last
     * name, and one for phone number. These are used to capture data to add to
     * the address book.
     *
     */
    private Screen genEntryScr() {
        if (entryScr == null) {
            entryScr = new NewEntryForm("Add new", phoneProvider);
            entryScr.addCommand(cmdCancel);
            entryScr.addCommand(cmdAdd);
            entryScr.setCommandListener(this);
        }
        entryScr.clean();
        display.setCurrent(entryScr);
        return entryScr;
    }

    /**
     * Name/Phone number deposit screen
     */
    private Screen genDepositScr(boolean useSelected) {
        if (depositScr == null) {
            depositScr = new DepositForm("Deposit phone", phoneProvider);
            depositScr.addCommand(cmdCancel);
            depositScr.addCommand(cmdDeposit);
            depositScr.setCommandListener(this);
        }
        DepositRecord depositRec = null;
        if (useSelected) {
            depositRec = (DepositRecord) phoneRecords.elementAt(nameScr
                .getSelectedIndex());
        }        
        depositScr.setDepositRecord(depositRec);
        display.setCurrent(depositScr);
        return depositScr;
    }
    
    
    /**
     * Generates a list of first/last/phone numbers. Can be called as a result
     * of a browse command (genBrowseScr) or a search command (genSearchScr).
     *
     * title title of this screen (since it can be called from a browse or a
     * search command. f if not null, first name to search on l if not null,
     * last name to search on
     */
    private Screen genNameScr(String title, String f, String l, boolean local) {
        SimpleComparator sc;
        SimpleFilter sf = null;
        RecordEnumeration re;
        phoneRecords = null;

        if (local) {
            sc = new SimpleComparator(
                    sortOrder == 0 ? SimpleComparator.SORT_BY_FIRST_NAME
                            : SimpleComparator.SORT_BY_LAST_NAME);

            if (f != null || l != null) {
                sf = new SimpleFilter(f, l);
            }

            try {
                re = addrBook.enumerateRecords(sf, sc, false);
            } catch (Exception e) {
                displayAlert(ERROR, "Could not create enumeration: " + e, null);
                return null;
            }
        } else {
            re = new NetworkQuery(f, l, sortOrder);
        }

        nameScr = null;
        if (re.hasNextElement()) {
            nameScr = new List(title, List.IMPLICIT);
            nameScr.addCommand(cmdBack);
            nameScr.addCommand(cmdDial);
            nameScr.addCommand(cmdDeposit);
            nameScr.setCommandListener(this);
            phoneRecords = new Vector(6);

            try {
                while (re.hasNextElement()) {
                    byte[] b = re.nextRecord();
                    String phoneNumber = SimpleRecord.getPhoneNum(b);
                    String provider = SimpleRecord.getProvider(b);
                    // TODO : translate provider name
                    nameScr.append(phoneNumber + " "
                            + SimpleRecord.getLastName(b) + " "
                            + SimpleRecord.getPhoneNum(b) + " "
                            + provider, null);
                    phoneRecords.addElement(new DepositRecord(new PhoneNumber(phoneNumber), new PhoneProvider(provider), null, null));
                }
            } catch (Exception e) {
                displayAlert(ERROR, "Error while building name list: " + e,
                        null);
                return null;
            }
            display.setCurrent(nameScr);

        } else {
            displayAlert(INFO, "No names found", null);
        }

        return nameScr;
    }

    /**
     * Generate a screen with which to dial the phone. Note: this may or may not
     * be implemented on a given implementation.
     */
    private void genDialScr() {
        DepositRecord elem = (DepositRecord) phoneRecords.elementAt(nameScr.getSelectedIndex());
        dialScr = new TextBox("Dialing", elem.getPhoneNumber().toString(), PhonebookRecord.PN_LEN, TextField.PHONENUMBER);
        dialScr.addCommand(cmdCancel);
        dialScr.setCommandListener(this);
        display.setCurrent(dialScr);
    }

    /**
     * Add an entry to the address book. Called after the user selects the
     * addCmd while in the genEntryScr screen.
     */
    private void addEntry() {
        PhonebookRecord record = entryScr.getPhonebookRecord();
        String f = record.getFirstName();
        String l = record.getLastName();
        String p = record.getPhoneNumber();
        PhoneProvider provider = record.getPhoneProvider();

        _addPhoneRecord(f, l, p, provider);
    }
    
    /**
     * Add an entry to the address book. Called after the user selects the
     * addCmd while in the genEntryScr screen.
     */
    private void addDeposit() {
        /*
        Hashtable hashtable = new Hashtable();
 
   // Adding key-value pairs to Hashtable
   hashtable.put("A", "Apple");
   hashtable.put("B", "Orange");
   hashtable.put("C", "Mango");
   hashtable.put("D", "Banana");
   hashtable.put("E", "Grapes");
   boolean keyFlag1 = hashtable.containsKey("A");
   Hashtable op = new Hashtable();
   op.put(PhoneProvider.VODAFONE, "1");
   op.put("" + PhoneProvider.KIEVSTAR, "1");
   PhoneProvider mts = new PhoneProvider(PhoneProvider.VODAFONE);
    boolean keyFlag2 = op.containsKey(PhoneProvider.VODAFONE);
    boolean keyFlag3 = op.containsKey("" + PhoneProvider.VODAFONE);
    boolean keyFlag4 = op.containsKey(PhoneProvider.KIEVSTAR);
    boolean keyFlag5 = op.containsKey(mts.toString());
    String buffer = "Key A exists in Hashtable?: " + keyFlag1 + ":" + keyFlag2 + ":" + keyFlag3+ ":" + keyFlag4 + ":" + keyFlag5;
        */
        DepositRecord record = depositScr.getDepositRecord();
        PortmoneScript script = new PortmoneScript();
        depositText = new TextBox(
                "Deposit",
                //buffer,
                script.get(record.getPhoneProvider(), record.getPhoneNumber(), record.getAmount(), record.getMobyCode()),
                PortmoneScript.MAX_BUFFER_LEN*2,
                TextField.ANY);
        depositText.addCommand(cmdCancel);
        depositText.setCommandListener(this);
        display.setCurrent(depositText);
    }        

    private void _addPhoneRecord(String f, String l, String p, PhoneProvider provider) {
        byte[] b = SimpleRecord.createRecord(f, l, p, provider.toString());
        try {
            addrBook.addRecord(b, 0, b.length);
            displayAlert(INFO, "Record added", mainScr);
        } catch (RecordStoreException rse) {
            displayAlert(ERROR, "Could not add record" + rse, mainScr);
        }
    }

    /**
     * *************************************************************************
     * This method implements a state machine that drives the MIDlet from one
     * state (screen) to the next.
     */
    public void commandAction(Command c, Displayable d) {        
        if (d == mainScr) {
            processMainScrAction(c);
        } else if (d == nameScr) {
            // Handle a screen with names displayed, either
            // from a browse or a search
            if (c == cmdBack) {
                // display main screen
                genMainScr();
            } else if (c == cmdDial) {
                // dial the phone screen
                genDialScr();
            } else if (c == cmdDeposit) {
                // dial the phone screen
                genDepositScr(true);
            }
        } else if (d == entryScr) {
            // Handle the name entry screen
            if (c == cmdCancel) {
                // display main screen
                genMainScr();
            } else if (c == cmdAdd) {
                // display name entry screen
                addEntry();
            }
        } else if (d == optionScr) {
            // Handle the option screen
            if (c == cmdBack) {
                // display main screen
                genMainScr();
            }
        } else if (d == searchScr) {
            // Handle the search screen
            if (c == cmdBack) {
                // display main screen
                genMainScr();
            } else if (c == cmdSearchNetwork || c == cmdSearchLocal) {

                // display search of local addr book
                genNameScr("Search Result", s_firstName.getString(), s_lastName
                        .getString(), c == cmdSearchLocal);
            }
        } else if (d == dialScr || d == depositText) {
            if (c == cmdCancel) {
                // display main screen
                genMainScr();
            }
        } else if (d == depositScr) {
            if (c == cmdCancel) {
                genMainScr();
            } else if (c == cmdDeposit) {
                addDeposit();
            }
        }
    }

    private void processMainScrAction(Command c) {
        // Handle main sceen
        if (c == cmdExit) {
            midletExit(); // exit
        } else if ((c == List.SELECT_COMMAND) || (c == cmdSelect)) {
            switch (mainScr.getSelectedIndex()) {
                case SEARCH_INDEX:
                    // display search screen
                    genSearchScr();
                    break;
                case NEW_INDEX:
                    // display name entry screen
                    genEntryScr();
                    break;
                case BROWSE_INDEX:
                    // display all names
                    genNameScr("Browse", null, null, true);
                    break;
                case FAVORITES_INDEX:
                    // display favorites
                    genNameScr("Favorites", null, null, true);
                    break;
                case OPTIONS_INDEX:
                    // display option screen
                    genOptionScr();
                    break;
                case OPTIONS_DEPOSIT:
                    // display deposit screen
                    genDepositScr(false);
                    break;
                case TESTDATA_INDEX:
                    // display option screen
                    addTestData();
                    break;

                default:
                    displayAlert(ERROR, "Unexpected index!", mainScr);
            }
        }
    }

    /**
     * Gets called when the user is viewing the sort options in the optionScr.
     * Takes the new selected index and changes the sort order (how names are
     * displayed from a search or a browse).
     *
     * item An item list
     */
    public void itemStateChanged(Item item) {
        if (item == sortChoice) {
            sortOrder = sortChoice.getSelectedIndex();
        }
    }

    private void addTestData() {
        PhoneProvider[] providers = phoneProvider.getProviders();
        for( int n = 0; n <  providers.length; ++n){
            _addPhoneRecord("FirstName " + n, "LastName " + n, "3805019200" + (n*n), providers[n]);
        }
    }
}
