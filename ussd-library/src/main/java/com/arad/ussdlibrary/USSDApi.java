/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.arad.ussdlibrary;

import java.util.HashMap;
import java.util.HashSet;


public interface USSDApi {
    void send(String text, USSDController.CallbackMessage callbackMessage);
    void cancel();
    void callUSSDInvoke(String ussdPhoneNumber, HashMap<String,HashSet<String>> map,
                        USSDController.CallbackInvoke callbackInvoke,int slot);
    void callUSSDInvoke(String ussdPhoneNumber, int simSlot, HashMap<String,HashSet<String>> map,
                        USSDController.CallbackInvoke callbackInvoke);
    void callUSSDOverlayInvoke(String ussdPhoneNumber, HashMap<String,HashSet<String>> map,
                               USSDController.CallbackInvoke callbackInvoke);
    void callUSSDOverlayInvoke(String ussdPhoneNumber, int simSlot, HashMap<String,HashSet<String>> map,
                               USSDController.CallbackInvoke callbackInvoke);
}
