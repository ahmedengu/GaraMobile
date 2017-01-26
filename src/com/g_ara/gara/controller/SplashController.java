package com.g_ara.gara.controller;

import com.codename1.components.SpanLabel;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.location.LocationManager;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ahmedengu.
 */
public class SplashController {
    public static void refreshSplash(Form f, Container loadingCnt, StateMachine stateMachine) {
        boolean isNotOnline = am_i_online();
        boolean isNotGps = (LocationManager.getLocationManager().isGPSDetectionSupported() && !LocationManager.getLocationManager().isGPSEnabled());
        if (isNotOnline || isNotGps) {
            SpanLabel message = new SpanLabel("Gara require" + ((isNotOnline && isNotGps) ? " Internet Connection & GPS" : ((isNotOnline) ? " Internet Connection" : " GPS")));
            message.setUIID("ButtonGroupFirst");
            Button retry = new Button("Retry");
            retry.setUIID("ButtonGroupLast");
            FontImage.setMaterialIcon(retry, FontImage.MATERIAL_REFRESH);

            Container centerMiddle = FlowLayout.encloseCenterMiddle(BoxLayout.encloseY(message, retry));

            retry.addActionListener(evt -> {
                loadingCnt.setHidden(false);
                centerMiddle.remove();
                f.revalidate();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Display.getInstance().callSerially(() -> refreshSplash(f, loadingCnt, stateMachine));
                    }
                }, 1000);
            });
            f.add(BorderLayout.CENTER, centerMiddle);
            loadingCnt.setHidden(true);
            f.revalidate();
        } else if (Preferences.get("sessionToken", "").length() > 0) {
            try {
                ParseUser user = ParseUser.fetchBySession(Preferences.get("sessionToken", ""));
                if (user.isAuthenticated()) {
                    stateMachine.showForm("Home", null);
                } else {
                    stateMachine.showForm("Login", null);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            stateMachine.showForm("Login", null);
        }
    }

    public static boolean am_i_online() {
        boolean online = false;
        String net = NetworkManager.getInstance().getCurrentAccessPoint();
        if (net == null || net == "" || net.equals(null)) {
            online = false;
        } else {
            online = true;
        }
        return online;
    }
}
