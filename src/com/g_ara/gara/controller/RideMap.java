package com.g_ara.gara.controller;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.util.Resources;
import com.parse4cn1.ParseObject;
import userclasses.StateMachine;

import java.util.List;

import static userclasses.StateMachine.data;
import static userclasses.StateMachine.hideBlocking;
import static userclasses.StateMachine.showBlocking;

/**
 * Created by ahmedengu.
 */
public class RideMap {
    public static void beforeRideMapForm(Form f, Resources resources, StateMachine stateMachine, Button cancel) {
        UserController.addUserSideMenu(f);
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
        new MapController((resources), f).initMap((List<ParseObject>) data.get("rides"), stateMachine,f);
    }
    public static void postRideMapForm(Form f) {
        showBlocking();
        hideBlocking();
        f.repaint();
        f.revalidate();
    }
}
