package com.g_ara.gara.controller;

import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.maps.Coord;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.util.Resources;
import com.g_ara.gara.model.Constants;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.util.ArrayList;
import java.util.List;

import static com.g_ara.gara.controller.MapController.draw2MarkerMap;
import static com.g_ara.gara.model.Constants.MASK_LOCATION_ICON;
import static com.g_ara.gara.model.Constants.PROFILE_ICON;
import static userclasses.StateMachine.data;
import static userclasses.StateMachine.showDelayedToastBar;

/**
 * Created by ahmedengu.
 */
public class RequestsController {
    public static void beforeRequestsForm(Form f, Resources resources, StateMachine stateMachine) {
        try {
            List<ParseObject> results = new ArrayList<ParseObject>();
            if (data.get("active") != null && ((ParseObject) data.get("active")).getClassName().equals("Trip")) {
                if (((ParseObject) data.get("active")).getList("tripRequests") == null || ((ParseObject) data.get("active")).getList("tripRequests").size() < ((ParseObject) data.get("active")).getInt("seats")) {
                    ParseQuery<ParseObject> q = ParseQuery.getQuery("TripRequest");
                    q.include("user");
                    q.whereEqualTo("trip", ((ParseObject) data.get("active"))).whereEqualTo("accept", -1).whereEqualTo("active", true);
                    results = q.find();




                } else {
                    showDelayedToastBar("You have no remaining seats!");
                }
            } else {
                showDelayedToastBar("You don't have an active trip!");
            }

            MapContainer map = new MapController(resources, f).map;
            for (int i = 0; i < results.size(); i++) {
                final ParseObject object = results.get(i);
                ParseObject user = object.getParseObject("user");
                ParseGeoPoint location = user.getParseGeoPoint("location");
                String url = user.getParseFile("pic").getUrl();
                URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
                URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);


                map.addMarker(image, new Coord(location.getLatitude(), location.getLongitude()), "", "", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {

                        Dialog dialog = getRequestUserDialog(object, user, "Request");

                        Container south = new Container(new GridLayout(2));
                        Button cancel = new Button("Reject");
                        cancel.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt1) {
                                object.put("accept", 0);
                                try {
                                    object.save();
                                    data.put("active", object);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    ToastBar.showErrorMessage(e.getMessage());
                                }
                                dialog.dispose();
                                stateMachine.showForm("Home", null);
                            }
                        });
                        south.add(cancel);
                        Button confirm = new Button("Accept");
                        confirm.addActionListener(evt1 -> {
                            object.put("accept", 1);

                            ParseObject trip = object.getParseObject("trip");
                            try {
                                trip.addUniqueToArrayField("tripRequests", object);
                                ParseBatch batch = ParseBatch.create();
                                batch.addObject(object, ParseBatch.EBatchOpType.UPDATE);
                                batch.addObject(trip, ParseBatch.EBatchOpType.UPDATE);
                                batch.execute();
                                data.put("active", object);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                ToastBar.showErrorMessage(e.getMessage());
                            }
                            dialog.dispose();
                            stateMachine.showForm("Home", null);
                        });
                        south.add(confirm);
                        dialog.add(BorderLayout.SOUTH, south);
                        dialog.show();

                    }
                });
            }


        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static Dialog getRequestUserDialog(ParseObject tripRequest, ParseObject TrUser, String title) {
        Dialog dialog = new Dialog(title);
        dialog.setLayout(new BorderLayout());

        Container center = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        String picUrl = TrUser.getParseFile("pic").getUrl();
        center.add(new ImageViewer(URLImage.createToStorage(PROFILE_ICON().scaledEncoded(dialog.getWidth(), -1), picUrl.substring(picUrl.lastIndexOf("/") + 1), picUrl)));
        center.add(new Label("Name: " + TrUser.getString("name")));
        center.add(new Label("Username: " + TrUser.getString("username")));
        center.add(new Label("Mobile: " + TrUser.getString("mobile")));
        center.add(new Label("Cost: " + tripRequest.get("cost")));

        Container mapContainer = new Container(new BorderLayout());
        draw2MarkerMap(TrUser.getParseGeoPoint("location"), tripRequest.getParseGeoPoint("to"), mapContainer);
        center.add(mapContainer);

        center.setScrollableY(true);
        dialog.add(BorderLayout.CENTER, center);
        return dialog;
    }

}
