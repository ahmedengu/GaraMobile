package com.g_ara.gara.controller;

import com.codename1.components.SpanLabel;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.util.Timer;
import java.util.TimerTask;

import static userclasses.StateMachine.showForm;

/**
 * Created by ahmedengu.
 */
public class SplashController {
    public static void refreshSplash(Form f, Container loadingCnt, StateMachine stateMachine) {
        boolean isNotOnline = !am_i_online();
        boolean isNotGps = (LocationManager.getLocationManager().isGPSDetectionSupported() && !LocationManager.getLocationManager().isGPSEnabled());
        if (isNotOnline || isNotGps) {
            errorMessageReload(f, loadingCnt, stateMachine, "Gara require" + ((isNotOnline && isNotGps) ? " Internet Connection & GPS" : ((isNotOnline) ? " Internet Connection" : " GPS")));
        } else {
            try {
                final Location currentLocationSync = LocationManager.getLocationManager().getCurrentLocationSync(10);
                MapController.setLocationCoord(new Coord(currentLocationSync.getLatitude(), currentLocationSync.getLongitude()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Preferences.get("sessionToken", "").length() > 0) {
                try {
                    ParseUser user = ParseUser.fetchBySession(Preferences.get("sessionToken", ""));
                    if (user.isAuthenticated()) {
                        showForm(Preferences.get("nId", "Home"));
                        Preferences.delete("nId");
                    } else {
                        showForm("Login");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    if (e.getCode() == 209) {
                        Preferences.delete("sessionToken");
                        showForm("Login");
                    } else
                        errorMessageReload(f, loadingCnt, stateMachine, e.getMessage());
                }
            } else {
                showForm("Login");
            }
        }
    }

    public static void errorMessageReload(final Form f, final Container loadingCnt, final StateMachine stateMachine, String txt) {
        SpanLabel message = new SpanLabel(txt);
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
    }

    public static boolean am_i_online() {
        boolean online = false;
        String net = NetworkManager.getInstance().getCurrentAccessPoint();
        if (net == null || net == "" || net.equals(null)) {
            online = false;
        } else {
            online = true;
        }
        return Display.getInstance().isSimulator() || online;
    }
}
