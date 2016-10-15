package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.maps.Coord;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.util.List;
import java.util.Map;

import static com.g_ara.gara.controller.UserController.currentParseUserSave;
import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class HomeController {
    public static void beforeHomeForm(Form f, Resources resources, Button drive, Button ride) {
        f.setBackCommand(null);
        ParseObject fetch = null;
        if (ParseUser.getCurrent().get("trip") != null) {

            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
                query.include("tripRequests");
                query.include("tripRequests.user");
                query.whereEqualTo("objectId", ParseUser.getCurrent().getParseObject("trip").getObjectId());
                fetch = query.find().get(0);

                Button active = new Button("cancel: " + fetch.getClassName());
                final ParseObject object = fetch;
                active.addActionListener(evt -> {
                    object.put("active", false);
                    ParseUser user = ParseUser.getCurrent();
                    user.remove("trip");
                    user.remove("tripRequest");
                    try {
                        object.save();
                        currentParseUserSave();
                        data.remove("active");
                        f.removeComponent(active);
                        drive.setHidden(false);
                        ride.setHidden(false);
                        new MapController(resources, f).initMap();
                        f.repaint();

                    } catch (ParseException e) {
                        e.printStackTrace();
                        ToastBar.showErrorMessage(e.getMessage());
                    }
                });

                drive.setHidden(true);
                ride.setHidden(true);
                f.add(BorderLayout.NORTH, active);
                MapController map = new MapController(resources, f);
                map.initDriveMap();

                List<ParseObject> tripRequests = fetch.getList("tripRequests");
                for (int i = 0; i < tripRequests.size(); i++) {
                    ParseGeoPoint location = tripRequests.get(i).getParseObject("user").getParseGeoPoint("location");

                    map.addToMarkers((FontImage.createMaterial(FontImage.MATERIAL_PERSON_PIN_CIRCLE, new Style()).toEncodedImage()), new Coord(location.getLatitude(), location.getLongitude()), "", "", null);
                }
                data.put("active", fetch);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (ParseUser.getCurrent().get("tripRequest") != null) {

            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("TripRequest");
                query.include("trip");
                query.include("trip.driver");
                query.whereEqualTo("objectId", ParseUser.getCurrent().getParseObject("tripRequest").getObjectId());
                fetch = query.find().get(0);


                Button active = new Button("cancel: " + fetch.getClassName());
                final ParseObject object = fetch;
                active.addActionListener(evt -> {
                    object.put("active", false);
                    ParseUser user = ParseUser.getCurrent();
//                    user.remove("trip");
                    user.remove("tripRequest");
                    try {
                        object.save();
                        currentParseUserSave();
                        data.remove("active");
                        f.removeComponent(active);
                        drive.setHidden(false);
                        ride.setHidden(false);
                        new MapController(resources, f).initMap();

                        f.repaint();
                    } catch (ParseException e) {
                        e.printStackTrace();
                        ToastBar.showErrorMessage(e.getMessage());
                    }
                });

                drive.setHidden(true);
                ride.setHidden(true);
                f.add(BorderLayout.NORTH, active);
                MapController map = new MapController(resources, f);
                map.initDriveMap();


                ParseGeoPoint location = fetch.getParseObject("trip").getParseObject("driver").getParseGeoPoint("location");

                map.addToMarkers((FontImage.createMaterial(FontImage.MATERIAL_PERSON_PIN_CIRCLE, new Style()).toEncodedImage()), new Coord(location.getLatitude(), location.getLongitude()), "", "", null);



                data.put("active", fetch);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else
            new MapController(resources, f).initMap();
    }

    public static void rideAction(StateMachine stateMachine) {
        if (MapController.getDestCoord() == null) {
            ToastBar.showErrorMessage("You should choose a destination");
            return;
        }
        if (MapController.getLocationCoord() == null) {
            ToastBar.showErrorMessage("GPS is required");
            return;
        }

        try {
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereWithinKilometers("location", new ParseGeoPoint(MapController.getLocationCoord().getLatitude(), MapController.getLocationCoord().getLongitude()), 5000000);
            ParseQuery<ParseObject> tripQuery = ParseQuery.getQuery("Trip");
            tripQuery.include("driver");
            tripQuery.whereWithinKilometers("to", new ParseGeoPoint(MapController.getDestCoord().getLatitude(), MapController.getDestCoord().getLongitude()), 5000000);
            List<ParseObject> results = tripQuery.find();
            if (results.size() == 0) {
                ToastBar.showErrorMessage("There is no rides available");
            } else {
                data.put("rides", results);
                stateMachine.showForm("RideMap", null);
            }


        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void driveAction(StateMachine stateMachine) {
        if (MapController.getDestCoord() == null) {
            ToastBar.showErrorMessage("You should choose a destination");
            return;
        }
        if (MapController.getLocationCoord() == null) {
            ToastBar.showErrorMessage("GPS is required");
            return;
        }

        MultiList list = new MultiList();
        CarsController.refreshCars(list);

        if (list.getModel().getSize() == 0) {
            ToastBar.showErrorMessage("You dont have any cars");
            return;
        }
        list.addActionListener(evt -> {
            ParseObject item = (ParseObject) ((Map<String, Object>) list.getSelectedItem()).get("object");
            data.put("car", item);
            stateMachine.showForm("DriveSummary", null);
        });
        Dialog dialog = new Dialog("Choose a car");
        dialog.setLayout(new BorderLayout());
        dialog.addComponent(BorderLayout.CENTER, list);
        Button cancel = new Button("Cancel");
        cancel.addActionListener(evt -> dialog.dispose());
        dialog.addComponent(BorderLayout.SOUTH, cancel);
        dialog.show();
    }

}
