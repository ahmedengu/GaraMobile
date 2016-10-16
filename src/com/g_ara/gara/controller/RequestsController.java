package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.maps.Coord;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.parse4cn1.*;

import java.util.ArrayList;
import java.util.List;

import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class RequestsController {
    public static void beforeRequestsForm(Form f, Resources resources) {
        try {
            List<ParseObject> results = new ArrayList<>();
            if (data.get("active") != null && ((ParseObject) data.get("active")).getClassName().equals("Trip")) {
                ParseQuery<ParseObject> q = ParseQuery.getQuery("TripRequest");
                q.include("user");
                q.whereEqualTo("trip", ((ParseObject) data.get("active"))).whereEqualTo("accept", -1).whereEqualTo("inactive", false);
                results = q.find();
            }

            MapContainer map = new MapController(resources, f).map;
            for (int i = 0; i < results.size(); i++) {
                final ParseObject object = results.get(i);
                ParseGeoPoint location = object.getParseObject("user").getParseGeoPoint("location");
                map.addMarker(FontImage.createMaterial(FontImage.MATERIAL_PERSON_PIN_CIRCLE, new Style()).toEncodedImage(), new Coord(location.getLatitude(), location.getLongitude()), "", "", evt -> {
                    Dialog dialog = new Dialog("Requests");
                    dialog.setLayout(new BorderLayout());
                    Label label = new Label("User: " + object.getParseObject("user").getString("username"));
                    Button cancel = new Button("Reject");
                    cancel.addActionListener(evt1 -> {
                        object.put("accept", 0);
                        try {
                            object.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            ToastBar.showErrorMessage(e.getMessage());
                        }
                        dialog.dispose();
                    });

                    Button confirm = new Button("Accept");
                    confirm.addActionListener(evt1 -> {
                        object.put("accept", 1);
                        ParseObject trip = object.getParseObject("trip");
                        trip.addUniqueToArrayField("tripRequests", object);
                        try {
                            ParseBatch batch = ParseBatch.create();
                            batch.addObject(object, ParseBatch.EBatchOpType.UPDATE);
                            batch.addObject(trip, ParseBatch.EBatchOpType.UPDATE);
                            batch.execute();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            ToastBar.showErrorMessage(e.getMessage());
                        }
                        dialog.dispose();
                    });
                    Container container = new Container();
                    container.add(cancel);
                    container.add(confirm);
                    dialog.add(BorderLayout.CENTER, label);
                    dialog.add(BorderLayout.SOUTH, container);
                    dialog.show();

                });
            }


        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

}
