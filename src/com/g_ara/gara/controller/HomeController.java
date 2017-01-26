package com.g_ara.gara.controller;

import ca.weblite.codename1.json.JSONException;
import com.codename1.components.ImageViewer;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.ext.codescan.CodeScanner;
import com.codename1.ext.codescan.ScanResult;
import com.codename1.maps.Coord;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.util.Resources;
import com.g_ara.gara.model.Constants;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.util.*;
import java.util.List;

import static com.codename1.io.Util.encodeUrl;
import static com.codename1.ui.Display.SOUND_TYPE_INFO;
import static com.g_ara.gara.controller.CarsController.getCarsArr;
import static com.g_ara.gara.controller.GroupsController.getUserVerifiedGroups;
import static com.g_ara.gara.controller.GroupsController.verifiedGroupUserQuery;
import static com.g_ara.gara.controller.MapController.getDriveInfoDialog;
import static com.g_ara.gara.controller.RequestsController.getRequestUserDialog;
import static com.g_ara.gara.controller.UserController.currentParseUserSave;
import static com.g_ara.gara.model.Constants.*;
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class HomeController {
    public static boolean initLiveQuery = true;
    static ParseLiveQuery tripLiveQuery;

    public static void beforeHomeForm(Form f, Resources resources, StateMachine stateMachine) {
        UserController.addUserSideMenu(f);
        new MapController(resources, f).initMap();
    }

    public static void postHomeForm(Form f, Resources resources, StateMachine stateMachine) {
        Button ride = new Button("Get Ride");
        FontImage.setMaterialIcon(ride, FontImage.MATERIAL_THUMB_UP);
        ride.addActionListener(evt -> rideAction(stateMachine));
        ride.setHidden(true);
        ride.setUIID("ToggleButtonFirst");

        Button drive = new Button("Drive");
        FontImage.setMaterialIcon(drive, FontImage.MATERIAL_DRIVE_ETA);
        drive.addActionListener(evt -> driveAction(stateMachine));
        drive.setHidden(true);
        drive.setUIID("ToggleButtonLast");

        if (initLiveQuery) {
            initLiveQuery = false;
            chatLiveQuery();
        }
        if (ParseUser.getCurrent().get("trip") != null) {
            showBlocking();
            tripHome(f, resources, drive, ride);
            if (tripLiveQuery == null)
                requestsLiveQuery(stateMachine);
            hideBlocking();
        } else if (ParseUser.getCurrent().get("tripRequest") != null) {
            showBlocking();
            tripRequestHome(f, resources, drive, ride, stateMachine);
            hideBlocking();
        } else {
            ride.setHidden(false);
            drive.setHidden(false);

        }
        f.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, ride, drive));
    }

    public static void requestsLiveQuery(StateMachine stateMachine) {
        ParseQuery<ParseObject> tripRequestQuery = ParseQuery.getQuery("TripRequest");
        tripRequestQuery.include("user");
        tripRequestQuery.whereEqualTo("trip", ((ParseObject) data.get("active"))).whereEqualTo("accept", -1).whereEqualTo("active", true);
        try {
            tripLiveQuery = new ParseLiveQuery(tripRequestQuery) {
                @Override
                public void event(String op, int requestId, ParseObject object) {
                    if (op.equals("create")) {
                        if (Display.getInstance().isMinimized()) {
                            LocalNotification n = new LocalNotification();
                            n.setId("Requests");
                            n.setAlertBody("You got a new trip request");
                            n.setAlertTitle("Gara | New Trip Request");

                            Display.getInstance().scheduleLocalNotification(
                                    n,
                                    System.currentTimeMillis() + 10,
                                    LocalNotification.REPEAT_NONE
                            );
                        } else {
                            String title = Display.getInstance().getCurrent().getTitle();
                            if (title.equals("Requests")) {
                                Display.getInstance().callSerially(() -> {
                                    try {
                                        RequestsController.refreshRequests(stateMachine);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } else {
                                Display.getInstance().callSerially(() -> {
                                    ToastBar.showErrorMessage("New Trip Request");
                                    Display.getInstance().playBuiltinSound(SOUND_TYPE_INFO);
                                });
                            }
                        }
                    }
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void chatLiveQuery() {
        ParseQuery<ParseObject> chatQuery = ParseQuery.getQuery("Message");
        chatQuery.include("from");
        chatQuery.whereEqualTo("to", ParseUser.getCurrent());
        try {
            new ParseLiveQuery(chatQuery) {
                @Override
                public void event(String op, int requestId, ParseObject object) {
                    if (op.equals("create")) {
                        if (Display.getInstance().isMinimized()) {
                            LocalNotification n = new LocalNotification();
                            n.setId("chat");
                            n.setAlertBody("You got a new message");
                            n.setAlertTitle("Gara | New Message");

                            Display.getInstance().scheduleLocalNotification(
                                    n,
                                    System.currentTimeMillis() + 10,
                                    LocalNotification.REPEAT_NONE
                            );

                        } else {
                            String title = Display.getInstance().getCurrent().getTitle();
                            if (!(title.equals("Chat") || title.equals("Conversion")))
                                Display.getInstance().callSerially(() -> {
                                    ToastBar.showErrorMessage("New message");
                                    Display.getInstance().playBuiltinSound(SOUND_TYPE_INFO);
                                });
                        }
                    }
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void tripRequestHome(Form f, Resources resources, Button drive, Button ride, StateMachine stateMachine) {
        ParseObject fetch;
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TripRequest");
            query.include("trip");
            query.include("trip.driver");
            query.include("trip.car");
            query.whereEqualTo("objectId", ParseUser.getCurrent().getParseObject("tripRequest").getObjectId());
            fetch = query.find().get(0);

            if (fetch.getInt("accept") != 1) {
                CancelActiveRequest(fetch);
                new MapController(resources, f).initMap();
//                ToastBar.showErrorMessage("Your request got no replay from the driver or rejected, Please choose another driver!");
                showDelayedToastBar("Your request got no replay from the driver or rejected, Please choose another driver!");

                return;
            } else if (fetch.getParseObject("trip").getBoolean("active") == false) {
                CancelActiveRequest(fetch);
                new MapController(resources, f).initMap();
//                ToastBar.showErrorMessage("The driver canceled the trip!");
                showDelayedToastBar("The driver canceled the trip!");

                return;
            }
            Button active = new Button("cancel: " + fetch.getClassName());
            FontImage.setMaterialIcon(active, FontImage.MATERIAL_CANCEL);

            final ParseObject object = fetch;
            active.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    CancelActive(f, resources, drive, ride, active, object);
                }
            });

            drive.setHidden(true);
            ride.setHidden(true);
            Container north = new Container(new GridLayout(2));
            north.add(active);

            Button checkIn = new Button("CheckIn");
            FontImage.setMaterialIcon(checkIn, FontImage.MATERIAL_CAMERA);

            checkIn.addActionListener(evt -> {

                if (CodeScanner.isSupported())
                    CodeScanner.getInstance().scanQRCode(new ScanResult() {

                        public void scanCompleted(String contents, String formatName, byte[] rawBytes) {
                            checkinAction(contents, object, checkIn, f, resources, drive, ride, active, stateMachine);
                        }

                        public void scanCanceled() {
                            ToastBar.showErrorMessage("Cancelled");
                        }

                        public void scanError(int errorCode, String message) {
                            ToastBar.showErrorMessage(message);
                        }
                    });
                else {
                    TextField input = new TextField("");
                    input.setHint("Check in/out token");
                    Dialog.show("Enter check in/out token", input, new Command("Enter") {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
//                            super.actionPerformed(evt);
                            checkinAction(input.getText(), object, checkIn, f, resources, drive, ride, active, stateMachine);
                        }
                    });
                }

            });

            if (object.get("checkin") != null) {
                checkIn.setText("Check Out");
            }
            north.add(checkIn);

            f.add(BorderLayout.NORTH, north);
            MapController map = new MapController(resources, f);
            map.initDriveMap(fetch.getParseGeoPoint("to"));


            ParseObject trip = fetch.getParseObject("trip");
            ParseUser driver = (ParseUser) trip.getParseObject("driver");
            ParseGeoPoint location = driver.getParseGeoPoint("location");
            String url = driver.getParseFile("pic").getUrl();

            URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
            URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

            map.addToMarkers(image, new Coord(location.getLatitude(), location.getLongitude()), "", "", evt -> {
                Dialog dialog = getDriveInfoDialog(trip, driver, trip.getParseObject("car"), "info");
                getCancelSouth(driver, dialog);
                dialog.show();
            });


            data.put("active", fetch);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void checkinAction(String contents, ParseObject object, Button checkIn, Form f, Resources resources, Button drive, Button ride, Button active, StateMachine stateMachine) {
        if (contents.equals(object.getParseObject("trip").getParseObject("driver").getObjectId())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", contents);
            map.put("location", new ParseGeoPoint(MapController.getLocationCoord().getLatitude(), MapController.getLocationCoord().getLongitude()));
            object.put((checkIn.getText().equals("Check Out")) ? "checkout" : "checkin", map);
            try {
                object.save();
                if (checkIn.getText().equals("Check Out")) {
                    CancelActive(f, resources, drive, ride, active, object);
                    data.put("tripObject", object);
                    showDelayedToastBar("You should pay the driver: " + object.get("cost"));
                    stateMachine.showForm("TripFeedback", null);
                } else {
                    showDelayedToastBar("Checked in Successfully");
                    checkIn.setText("Check Out");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            showDelayedToastBar("Sorry couldn't identify the QR as the trip Qr!");
//            ToastBar.showErrorMessage("Sorry couldn't identify the QR as the trip Qr!", 5000);
        }
    }


    public static void tripHome(Form f, Resources resources, Button drive, Button ride) {
        ParseObject fetch;
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
            query.include("tripRequests");
            query.include("tripRequests.user");
            query.whereEqualTo("objectId", ParseUser.getCurrent().getParseObject("trip").getObjectId());
            fetch = query.find().get(0);

            Button active = new Button("cancel: " + fetch.getClassName());
            FontImage.setMaterialIcon(active, FontImage.MATERIAL_CANCEL);
            active.setUIID("ToggleButtonFirst");
            final ParseObject object = fetch;
            active.addActionListener(evt -> {
                CancelActive(f, resources, drive, ride, active, object);
            });

            drive.setHidden(true);
            ride.setHidden(true);
            Container north = new Container(new GridLayout(2));
            north.add(active);

            Button checkin = new Button("Check In/Out");
            FontImage.setMaterialIcon(checkin, FontImage.MATERIAL_CAMERA);
            checkin.setUIID("ToggleButtonLast");
            checkin.addActionListener(evt -> {
                String objectId = ParseUser.getCurrent().getObjectId();
                String url = Constants.GOOGLE_QR + encodeUrl(objectId);

                Dialog.show("Token: " + objectId, new ImageViewer(URLImage.createToStorage(CODE_ICON().scaledEncoded(150, -1), objectId + ".png", url)), new Command("Back"));
            });

            north.add(checkin);
            f.add(BorderLayout.NORTH, north);
            MapController map = new MapController(resources, f);
            map.initDriveMap(fetch.getParseGeoPoint("to"));


            List<ParseObject> tripRequests = fetch.getList("tripRequests");
            if (tripRequests != null)
                for (int i = 0; i < tripRequests.size(); i++) {
                    if (tripRequests.get(i).getBoolean("active")) {
                        final ParseObject tripR = tripRequests.get(i);
                        final ParseUser tripUser = (ParseUser) tripR.getParseObject("user");
                        ParseGeoPoint location = tripUser.getParseGeoPoint("location");
                        String url = tripUser.getParseFile("pic").getUrl();

                        URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
                        URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

                        map.addToMarkers(image, new Coord(location.getLatitude(), location.getLongitude()), "", "", evt -> {
                            Dialog dialog = getRequestUserDialog(tripR, tripUser, "Info");
                            getCancelSouth(tripUser, dialog);
                            dialog.show();
                        });
                    }
                }
            data.put("active", fetch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void getCancelSouth(ParseUser user, Dialog dialog) {
        Button cancel = new Button("cancel");
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);

        cancel.addActionListener(evt1 -> dialog.dispose());
        dialog.add(BorderLayout.SOUTH, cancel);
    }

    public static void CancelActive(Form f, Resources resources, Button drive, Button ride, Button active, ParseObject object) {
        CancelActiveRequest(object);
        new MapController(resources, f).initMap();
        drive.setHidden(false);
        ride.setHidden(false);
        active.getParent().remove();
        f.repaint();
    }

    public static void CancelActiveRequest(ParseObject object) {
        object.put("active", false);
        ParseUser user = ParseUser.getCurrent();
        user.remove("trip");
        user.remove("tripRequest");
        try {
            showBlocking();
            object.save();
            currentParseUserSave();
            data.remove("active");
            if (tripLiveQuery != null) {
                tripLiveQuery.unsubscribe();
                tripLiveQuery = null;
            }
            hideBlocking();
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        } catch (JSONException e) {
            hideBlocking();
            e.printStackTrace();
        } catch (Exception e) {
            hideBlocking();
            e.printStackTrace();
        }
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
        showBlocking();
        List<ParseObject> groupUser = getUserVerifiedGroups();
        if (groupUser.size() == 0) {
            hideBlocking();
            showDelayedToastBar("You don't have any active groups");
            return;
        }
        try {
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereWithinKilometers("location", new ParseGeoPoint(MapController.getLocationCoord().getLatitude(), MapController.getLocationCoord().getLongitude()), WITHIN_KILOMETERS);
            ParseQuery<ParseObject> tripQuery = ParseQuery.getQuery("Trip");
            tripQuery.include("driver");
            tripQuery.include("car");
            tripQuery.whereWithinKilometers("to", new ParseGeoPoint(MapController.getDestCoord().getLatitude(), MapController.getDestCoord().getLongitude()), WITHIN_KILOMETERS);

            tripQuery.whereMatchesKeyInQuery("groups", "group", verifiedGroupUserQuery());
            tripQuery.whereMatchesQuery("driver", query);
            tripQuery.whereEqualTo("active", true);

            List<ParseObject> results = tripQuery.find();
            Iterator<ParseObject> objectIterator = results.iterator();
            while (objectIterator.hasNext()) {
                ParseObject object = objectIterator.next();
                if (object.getList("tripRequests").size() >= object.getInt("seats")) {
                    objectIterator.remove();
                }
            }
            if (results.size() == 0) {
                hideBlocking();
                showDelayedToastBar("There is no rides available");
            } else {
                hideBlocking();
                data.put("rides", results);
                stateMachine.showForm("RideMap", null);
            }


        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }


    static ComboBox<Map<String, Object>> combo;

    public static void driveAction(StateMachine stateMachine) {
        if (MapController.getDestCoord() == null) {
            ToastBar.showErrorMessage("You should choose a destination");
            return;
        }
        if (MapController.getLocationCoord() == null) {
            ToastBar.showErrorMessage("GPS is required");
            return;
        }
        try {
            showBlocking();
            HashMap<String, Object>[] carsArr = getCarsArr();
            if (carsArr.length == 0) {
                hideBlocking();
                showDelayedToastBar("You dont have any cars");
                return;
            }
            List<ParseObject> groups = getUserVerifiedGroups();
            if (groups.size() == 0) {
                hideBlocking();
                showDelayedToastBar("You don't have any active groups");
                return;
            }
            combo = new ComboBox<>(carsArr);
            combo.setRenderer(new GenericListCellRenderer<>(new MultiButton(), new MultiButton()));

            Dialog dialog = new Dialog("Drive Settings");
            dialog.setLayout(new BorderLayout());
            dialog.setUIID("Form");
            TextField cost = new TextField("");
            TextField toll = new TextField("");
            TextField seats = new TextField("");
            TextArea notes = new TextArea("");
            notes.setGrowByContent(false);

            cost.setHint("Cost per kilo");
            toll.setHint("Toll cost");
            seats.setHint("Available seats");
            notes.setHint("Notes:");

            combo.setUIID("ButtonGroupFirst");
            cost.setUIID("GroupElement1");
            toll.setUIID("GroupElement2");
            seats.setUIID("GroupElement3");
            notes.setUIID("GroupElementLast");

            Button cancel = new Button("Cancel");
            cancel.addActionListener(evt -> dialog.dispose());
            Button confirm = new Button("Confirm");
            confirm.addActionListener(evt -> {
                ParseObject item = (ParseObject) ((Map<String, Object>) combo.getSelectedItem()).get("object");
                data.put("car", item);
                data.put("cost", Integer.parseInt(cost.getText().length() == 0 ? "0" : cost.getText()));
                data.put("toll", Integer.parseInt(toll.getText().length() == 0 ? "0" : toll.getText()));
                data.put("seats", Integer.parseInt(seats.getText().length() == 0 ? "4" : seats.getText()));
                data.put("notes", notes.getText());
                data.put("groups", groups);
                stateMachine.showForm("DriveSummary", null);
            });


            dialog.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, cancel, confirm));
            dialog.add(BorderLayout.CENTER, BoxLayout.encloseY(combo, GridLayout.encloseIn(3, cost, toll, seats), notes));
            hideBlocking();
            dialog.show();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }

    }

}
