package com.g_ara.gara.controller;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.util.Resources;
import com.parse4cn1.ParseObject;

import java.util.List;

import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class RideMap {
    static MapController mapController;

    public static void beforeRideMapForm(Form f, Resources resources, Button cancel) {
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
        mapController = new MapController((resources), f, f.getName());
        UserController.addUserSideMenu(f);

    }

    public static void postRideMapForm(Form f) {
        mapController.initMap((List<ParseObject>) data.get("rides"), f);
    }
}
