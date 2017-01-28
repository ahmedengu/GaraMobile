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
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.util.Resources;
import com.g_ara.gara.model.Constants;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.util.List;

import static com.g_ara.gara.controller.ChatController.getUserChat;
import static com.g_ara.gara.controller.MapController.draw2MarkerMap;
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class RequestsController {
    public static MapContainer map;

    public static void beforeRequestsForm(Form f, Resources resources, StateMachine stateMachine) {
        UserController.addUserSideMenu(f);

        if (data.get("active") != null && ((ParseObject) data.get("active")).getClassName().equals("Trip")) {
            if (((ParseObject) data.get("active")).getList("tripRequests") == null || ((ParseObject) data.get("active")).getList("tripRequests").size() < ((ParseObject) data.get("active")).getInt("seats")) {
                map = new MapController(resources, f).map;
                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_REFRESH, evt -> {
                    try {
                        refreshRequests(stateMachine);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        ToastBar.showErrorMessage(e.getMessage());
                    }
                });
            } else {
                f.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(new Label("You have no remaining seats!")));

            }
        } else {
            f.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(new Label("You don't have an active trip!")));
        }
    }

    public static void postRequestsForm(StateMachine stateMachine) {
        try {
            if (data.get("active") != null && ((ParseObject) data.get("active")).getClassName().equals("Trip")) {
                if (((ParseObject) data.get("active")).getList("tripRequests") == null || ((ParseObject) data.get("active")).getList("tripRequests").size() < ((ParseObject) data.get("active")).getInt("seats")) {
                    refreshRequests(stateMachine);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void refreshRequests(final StateMachine stateMachine) throws ParseException {
        showBlocking();
        List<ParseObject> results;
        map.clearMapLayers();
        ParseQuery<ParseObject> q = ParseQuery.getQuery("TripRequest");
        q.include("user");
        q.whereEqualTo("trip", ((ParseObject) data.get("active"))).whereEqualTo("accept", -1).whereEqualTo("active", true);
        results = q.find();
        for (int i = 0; i < results.size(); i++) {
            final ParseObject object = results.get(i);
            ParseObject user = object.getParseObject("user");
            ParseGeoPoint location = user.getParseGeoPoint("location");
//            String url = user.getParseFile("pic").getUrl();
//            URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
//            URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);


            map.addMarker(Constants.BLUE_LOCATION_ICON(), new Coord(location.getLatitude(), location.getLongitude()), "", "", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {

                    Form dialog = getRequestUserDialog(object, user, "Request");

                    Container south = new Container(new GridLayout(2));
                    Button cancel = new Button("Reject");
                    cancel.setUIID("ToggleButtonFirst");
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
                            stateMachine.showForm("Home", null);
                        }
                    });
                    south.add(cancel);
                    Button confirm = new Button("Accept");
                    confirm.setUIID("ToggleButtonLast");

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
                        stateMachine.showForm("Home", null);
                    });
                    south.add(confirm);
                    dialog.add(BorderLayout.SOUTH, south);
                    dialog.show();

                }
            });
        }
        map.revalidate();
        hideBlocking();
    }

    public static Form getRequestUserDialog(ParseObject tripRequest, ParseObject TrUser, String title) {
        Form components = new Form(title);
        UserController.addUserSideMenu(components);
        components.setLayout(new BorderLayout());

        Container north = new Container(new BoxLayout(BoxLayout.X_AXIS));

        ImageViewer imageViewer = new ImageViewer();
        imageViewer.setImageList(new ImageList(new String[]{TrUser.getParseFile("pic").getUrl()}));
        north.add(new Label("Name: " + TrUser.getString("name")));
        north.add(new Label("Username: " + TrUser.getString("username")));
        north.add(new Label("Mobile: " + TrUser.getString("mobile")));
        north.add(new Label("Cost: " + (int) tripRequest.get("cost")));

        Button dial = new Button("Call");
        FontImage.setMaterialIcon(dial, FontImage.MATERIAL_CALL);
        dial.addActionListener(evt -> {
            Display.getInstance().dial(TrUser.getString("mobile"));
        });

        Button chat = new Button("Chat");
        FontImage.setMaterialIcon(chat, FontImage.MATERIAL_CHAT);
        chat.addActionListener(evt -> getUserChat((ParseUser) TrUser));

        Button report = new Button("Report");
        FontImage.setMaterialIcon(report, FontImage.MATERIAL_REPORT);
        report.addActionListener(evt -> {
            Dialog reportDialog = new Dialog("Report");
            reportDialog.setLayout(new BorderLayout());

            TextArea info = new TextArea();
            info.setHint("More Information");
            info.setUIID("GroupElementOnly");
            reportDialog.add(BorderLayout.CENTER, info);

            Button cancel = new Button("Cancel");
            FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
            cancel.addActionListener(evt1 -> reportDialog.dispose());


            Button reportIt = new Button("Report");
            FontImage.setMaterialIcon(reportIt, FontImage.MATERIAL_REPORT);
            reportIt.addActionListener(evt1 -> {
                try {
                    ParseObject reportObject = ParseObject.create("Reports");
                    reportObject.put("info", info.getText());
                    reportObject.put("user", ParseUser.getCurrent());
                    reportObject.put("to", (ParseUser) TrUser);
                    showBlocking();
                    reportObject.save();
                    hideBlocking();
                    reportDialog.dispose();
                } catch (ParseException e) {
                    hideBlocking();
                    e.printStackTrace();
                    ToastBar.showErrorMessage(e.getMessage());
                }
            });

            reportDialog.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, cancel, reportIt));
            reportDialog.show();
        });
        dial.setUIID("ToggleButtonFirst");
        chat.setUIID("ToggleButton");
        report.setUIID("ToggleButtonLast");


        north.setScrollableX(true);
        Container components1 = new Container(new BorderLayout());
        components1.add(BorderLayout.NORTH, north);
        components1.add(BorderLayout.CENTER, imageViewer);
        components1.add(BorderLayout.SOUTH, GridLayout.encloseIn(3, dial, chat, report));
        components.add(BorderLayout.NORTH, components1);

        draw2MarkerMap(TrUser.getParseGeoPoint("location"), tripRequest.getParseGeoPoint("to"), components);
        return components;
    }

}
