package com.g_ara.gara.controller;

import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.maps.Coord;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseGeoPoint;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import static com.g_ara.gara.model.Constants.CURRENT_LOCATION_ICON;
import static com.g_ara.gara.model.Constants.RED_LOCATION_ICON;
import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class DriveSummary {

    public static void confirmAction(StateMachine stateMachine) {
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
            trip.put("cost", data.get("cost"));
            trip.put("toll", data.get("toll"));
            trip.put("seats", data.get("seats"));
            trip.put("notes", data.get("notes"));

            trip.save();
            current.put("trip", trip);
            current.remove("tripRequest");
            UserController.currentParseUserSave();

            data.put("active", trip);
            stateMachine.showForm("Home", null);
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void beforeDriveSummaryForm(Container summary, Resources resources) {
        summary.add(new Label("Distance: " + MapController.distanceInKilometers(MapController.getLocationCoord(), MapController.getDestCoord())));
        summary.add(new Label("Cost per kilo: " + data.get("cost")));
        summary.add(new Label("Tolls cost: " + data.get("toll")));
        summary.add(new Label("available seats: " + data.get("seats")));
        summary.add(new SpanLabel("notes:\n" + data.get("notes")));
        summary.add(new Label("Car: " + ((ParseObject) data.get("car")).getString("name")));
        Container container = new Container(new BorderLayout());

        MapContainer map = new MapController(resources, container).map;
        map.addMarker(CURRENT_LOCATION_ICON(), MapController.getLocationCoord(), "Hi marker", "Optional long description", null);
        map.addMarker(RED_LOCATION_ICON(), MapController.getDestCoord(), "Hi marker", "Optional long description", null);
        map.zoom(MapController.getLocationCoord(), 5);
        map.setScrollableY(false);
        map.addPath(MapController.decode(MapController.getRoutesEncoded(MapController.getLocationCoord(), MapController.getDestCoord())));
        summary.add(container);
    }


}
