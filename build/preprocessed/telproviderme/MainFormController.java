/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package telproviderme;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import ua.telnumberident.ruslan.*;

/**
 *
 * @author ruslan
 */
public class MainFormController implements CommandListener{
    private final MIDlet application;
    private final Form mainForm;
    private final Command cmdExit;
    private Display display;
    
    public MainFormController (MIDlet application) {
        this.application = application;
        cmdExit = new Command("Exit", Command.EXIT, 0);
        TelephoneNumberIdentUA instance = TelephoneNumberIdentUA.createInstance();
        mainForm = new MainForm("List", this.application, instance);        
        mainForm.addCommand(cmdExit);
        mainForm.setCommandListener(this);
    }
    
    public void startApp() {
        display = Display.getDisplay(application);
        display.setCurrent(mainForm);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdExit) {
            destroyApp(true);;
            application.notifyDestroyed();
        }
    }        
}
