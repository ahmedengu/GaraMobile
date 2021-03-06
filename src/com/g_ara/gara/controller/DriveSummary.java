package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.maps.Coord;
import com.codename1.ui.*;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseGeoPoint;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import static com.g_ara.gara.controller.MapController.draw2MarkerMap;
import static com.g_ara.gara.controller.UserController.getUserEmptyObject;
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class DriveSummary {

    public static void confirmAction(StateMachine stateMachine) {
        try {
            ParseObject trip = ParseObject.create("Trip");
            ParseUser current = ParseUser.getCurrent();
            trip.put("driver", getUserEmptyObject());
            Coord locationCoord = MapController.getLocationCoord();
            trip.put("from", locationCoord.getLatitude() + "," + locationCoord.getLongitude());
            Coord destCoord = MapController.getDestCoord();
            trip.put("to", new ParseGeoPoint(destCoord.getLatitude(), destCoord.getLongitude()));
            trip.put("car", ((ParseObject) data.get("car")));
            trip.put("active", true);
            trip.put("cost", data.get("cost"));
            trip.put("toll", data.get("toll"));
            trip.put("seats", data.get("seats"));
            trip.put("notes", data.get("notes"));
            trip.put("groups", data.get("groups"));
            trip.put("autoAccept", data.get("autoAccept"));
            trip.put("groupUser", data.get("groupUser"));
            showBlocking();
            trip.save();
            current.put("trip", trip);
            current.remove("tripRequest");
            current.save();

            data.put("active", trip);
            hideBlocking();
            stateMachine.showForm("Home", null);
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void beforeDriveSummaryForm(Container summary, Button cancel, Button confirm, Form f) {
        UserController.addUserSideMenu(f);
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
        FontImage.setMaterialIcon(confirm, FontImage.MATERIAL_DONE);

        summary.add(new Label("Car: " + ((ParseObject) data.get("car")).getString("name")));
        summary.add(new Label("seats: " + data.get("seats")));
        summary.add(new Label("Tolls cost: " + data.get("toll")));
        summary.add(new Label("Cost per kilo: " + data.get("cost")));
        summary.add(new Label("Auto Accept: " + ((((int) data.get("autoAccept")) == 0) ? "No" : "Yes")));
    }

    public static void postDriveSummaryForm(Container summary, Form f) {
        showBlocking();
        Coord locationCoord = MapController.getLocationCoord();
        Coord destCoord = MapController.getDestCoord();
        summary.add(new Label("Distance: " + (int) MapController.distanceInKilometers(locationCoord, destCoord)));
        draw2MarkerMap(locationCoord, destCoord, f, f.getName());
        hideBlocking();
        f.repaint();
        f.revalidate();

        f.getContentPane().revalidate();
    }
}
