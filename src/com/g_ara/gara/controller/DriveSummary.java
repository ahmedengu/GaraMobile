package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.maps.Coord;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseGeoPoint;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class DriveSummary {

    public static void confirmAction(StateMachine stateMachine, TextField toll, TextField cost) {
        try {
            ParseObject trip = ParseObject.create("Trip");
            ParseUser current = ParseUser.getCurrent();
            current.setDirty(false);
            trip.put("driver", current);
            Coord locationCoord = MapController.getLocationCoord();
            trip.put("from", locationCoord.getLatitude() + "," + locationCoord.getLongitude());
            Coord destCoord = MapController.getDestCoord();
            trip.put("to", new ParseGeoPoint(destCoord.getLatitude(), destCoord.getLongitude()));
            trip.put("car", ((ParseObject) data.get("car")));
            trip.put("active", true);
            trip.put("cost", Integer.parseInt(cost.getText().length() == 0 ? "0" : cost.getText()));
            trip.put("toll", Integer.parseInt(toll.getText().length() == 0 ? "0" : toll.getText()));

            trip.save();
            current.put("trip", trip);
            current.remove("tripRequest");
            current.save();


            data.put("active", trip);
            stateMachine.showForm("Home", null);
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void beforeDriveSummaryForm(Container summary) {

        summary.add(new Label(MapController.getLocationCoord().toString()));
        summary.add(new Label(MapController.getDestCoord().toString()));
        summary.add(new Label(((ParseObject) data.get("car")).getString("name")));
    }

}
