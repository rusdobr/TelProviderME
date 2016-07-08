/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme;

import javax.microedition.midlet.*;


/**
 * @author ruslan
 */
public class Midlet extends MIDlet {

    
    private MainFormController controller;
    
    public Midlet() {
        controller = new MainFormController(this);
    } 
    
    public void startApp() {
        controller.startApp();
    }
    
    public void pauseApp() {
        controller.pauseApp();
    }
    
    public void destroyApp(boolean unconditional) {
        controller.destroyApp(unconditional);
    }
}
