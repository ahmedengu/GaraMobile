package com.g_ara.gara;


import com.codename1.analytics.AnalyticsService;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.g_ara.gara.controller.UserController;
import com.parse4cn1.ParseException;
import userclasses.StateMachine;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose
 * of building native mobile applications using Java.
 */
public class Gara {

    private Form current;

    public void init(Object context) {
        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature, uncomment if you have a pro subscription
        // Log.bindCrashProtection(true);
    }

    public void start() {
        if (Preferences.get("Analytics", true)) {
            AnalyticsService.setAppsMode(true);
            AnalyticsService.init("UA-80287405-1", "g-ara.com");
        }
        Display.getInstance().lockOrientation(true);
        NetworkManager.getInstance().updateThreadCount(2);

        if (current != null) {
            current.show();
            return;
        }
        new StateMachine("/theme");
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
        if (current instanceof Dialog) {
            ((Dialog) current).dispose();
            current = Display.getInstance().getCurrent();
        }
        try {
            UserController.currentParseUserSave();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
    }

}
