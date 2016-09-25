package com.g_ara.gara.controller;

import com.codename1.components.MultiButton;
import com.codename1.io.Preferences;
import com.codename1.messaging.Message;
import com.codename1.ui.Display;
import com.g_ara.gara.model.Constants;

/**
 * Created by ahmedengu.
 */
public class SettingsController {
    public static void analyticsState(boolean analytics, MultiButton components) {
        components.setSelected(analytics);
    }

    public static void analyticsAciton(MultiButton analytics) {
        boolean selected = !Preferences.get("Analytics", true);
        analyticsState(selected, analytics);
        Preferences.set("Analytics", selected);
    }

    public static void websiteAction() {
        Display.getInstance().execute(Constants.WEBSITE);
    }

    public static void rateAction() {
        new RatingWidget(RatingWidget.getAppstoreURL(), Constants.EMAIL);
    }

    public static void feedbackAction() {
        Message m = new Message("Body of message");
        Display.getInstance().sendMessage(new String[]{Constants.EMAIL}, "[" + Display.getInstance().getPlatformName() + "] Feedback ", m);
    }
}
