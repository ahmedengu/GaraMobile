package com.g_ara.gara.controller;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.util.Resources;
import com.parse4cn1.ParseObject;
import userclasses.StateMachine;

import java.util.List;

import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class RideMap {
    public static void beforeRideMapForm(Form f, Resources resources, StateMachine stateMachine, Button cancel) {
        FontImage.setMaterialIcon(cancel,FontImage.MATERIAL_CANCEL);
        List<ParseObject> rides = (List<ParseObject>) data.get("rides");
        new MapController((resources),f).initMap( rides, stateMachine);
    }

}
