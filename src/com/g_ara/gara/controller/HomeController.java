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
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.list.GenericListCellRenderer;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.g_ara.gara.model.Constants;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.util.*;
import java.util.List;

import static com.codename1.io.Util.encodeUrl;
import static com.g_ara.gara.controller.CarsController.getCarsArr;
import static com.g_ara.gara.controller.GroupsController.getUserVerifiedGroups;
import static com.g_ara.gara.controller.GroupsController.verifiedGroupUserQuery;
import static com.g_ara.gara.controller.MapController.getDriveInfoDialog;
import static com.g_ara.gara.controller.RequestsController.getRequestUserDialog;
import static com.g_ara.gara.model.Constants.*;
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class HomeController {
    public static boolean initLiveQuery = true;
    static ParseLiveQuery tripLiveQuery;
    static Timer timer;

    public static void beforeHomeForm(Form f, Resources resources, StateMachine stateMachine) {
        UserController.addUserSideMenu(f);
        new MapController(resources, f, f.getName()).initMap();
    }

    public static void postHomeForm(Form f, Resources resources, StateMachine stateMachine) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        Button ride = new Button("Get Ride");
        FontImage.setMaterialIcon(ride, FontImage.MATERIAL_THUMB_UP);
        ride.addActionListener(evt -> rideAction(stateMachine));
        ride.setHidden(true);
        ride.setUIID("ToggleButtonFirst");

        Button drive = new Button("Drive");
        FontImage.setMaterialIcon(drive, FontImage.MATERIAL_DRIVE_ETA);
        drive.addActionListener(evt -> driveAction(stateMachine, f));
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
                        final Integer anInt = ((ParseObject) data.get("active")).getInt("autoAccept");
                        if (anInt == 0 || (anInt == 2 && object.getBoolean("female")) || (anInt == 1 && !object.getBoolean("female"))) {
                            LocalNotification n = new LocalNotification();
                            n.setId("Requests");
                            n.setAlertBody("You got a new trip request");
                            n.setAlertTitle("Gara | New Trip Request");

                            Display.getInstance().scheduleLocalNotification(
                                    n,
                                    System.currentTimeMillis() + 10,
                                    LocalNotification.REPEAT_NONE
                            );
                            if (Display.getInstance().getCurrent() != null && Display.getInstance().getCurrent().getName().equals("Requests")) {
                                Display.getInstance().callSerially(() -> {
                                    try {
                                        RequestsController.refreshRequests(stateMachine);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        } else {
                            try {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                object.put("accept", 1);

                                ParseObject trip = object.getParseObject("trip").fetchIfNeeded();
                                trip.addUniqueToArrayField("tripRequests", object);
                                ParseBatch batch = ParseBatch.create();
                                batch.addObject(object, ParseBatch.EBatchOpType.UPDATE);
                                batch.addObject(trip, ParseBatch.EBatchOpType.UPDATE);
                                batch.execute();
                                data.put("active", trip);
                                LocalNotification n = new LocalNotification();
                                n.setId("Home");
                                n.setAlertBody("You got a new auto accepted trip request");
                                n.setAlertTitle("Gara | Accepted Trip Request");

                                Display.getInstance().scheduleLocalNotification(
                                        n,
                                        System.currentTimeMillis() + 10,
                                        LocalNotification.REPEAT_NONE
                                );

                                Display.getInstance().callSerially(() -> showDelayedToastBar("You got a new auto accepted trip request"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
        } catch (Exception e) {
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
                        if (Display.getInstance().getCurrent().getName() != null && !Display.getInstance().getCurrent().getName().equals("Conversion") && (ParseUser.getCurrent().get("trip") == null || MapController.getVelocity() < 5)) {
                            LocalNotification n = new LocalNotification();
                            n.setId("Chat");
                            n.setAlertBody("You got a new message");
                            n.setAlertTitle("Gara | New Message");

                            Display.getInstance().scheduleLocalNotification(
                                    n,
                                    System.currentTimeMillis() + 10,
                                    LocalNotification.REPEAT_NONE
                            );
                        }
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tripRequestHome(Form f, Resources resources, Button drive, Button ride, StateMachine stateMachine) {
        ParseObject fetch;
        try {
            fetch = tripRequestHomeQuery();

            if (fetch.getInt("accept") != 1) {
                CancelActiveRequest(fetch);
                ride.setHidden(false);
                drive.setHidden(false);
                showDelayedToastBar("Your request got no replay from the driver or rejected, Please choose another driver!");

                return;
            } else if (fetch.getParseObject("trip").getBoolean("active") == false) {
                CancelActiveRequest(fetch);
                ride.setHidden(false);
                drive.setHidden(false);
                showDelayedToastBar("The driver canceled the trip!");

                return;
            }
            Button active = new Button("cancel: " + fetch.getClassName());
            active.setUIID("ToggleButtonFirst");
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
            checkIn.setUIID("ToggleButtonLast");
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
            MapController map = new MapController(resources, f, f.getName());
            map.initDriveMap(fetch.getParseGeoPoint("to"));


            addDriverToMarkers(fetch, map, f);
            if (timer == null)
                timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Display.getInstance().callSerially(() -> {
                        try {
                            if (Display.getInstance().getCurrent().getName() != null && Display.getInstance().getCurrent().getName().equals("Home")) {
                                addDriverToMarkers(tripRequestHomeQuery(), map, f);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, TASK_DELAY, TASK_DELAY);

            data.put("active", fetch);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void addDriverToMarkers(ParseObject fetch, MapController map, Form cF) {
        map.clearMarkers();
        if (fetch.has("checkin")) {
            exitHomeForm();
        } else {
            ParseObject trip = fetch.getParseObject("trip");
            ParseUser driver = (ParseUser) trip.getParseObject("driver");
            ParseGeoPoint location = driver.getParseGeoPoint("location");
//        String url = driver.getParseFile("pic").getUrl();

//        URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
//        URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

            map.addToMarkers(Constants.BLUE_LOCATION_ICON(), new Coord(location.getLatitude(), location.getLongitude()), "", "", evt -> {
                Form dialog = getDriveInfoDialog(trip, driver, trip.getParseObject("car"), "info");
                getCancelSouth(driver, dialog, cF);
                dialog.show();
            });
        }
    }

    public static ParseObject tripRequestHomeQuery() throws ParseException {
        ParseObject fetch;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TripRequest");
        query.include("trip");
        query.include("trip.driver");
        query.include("trip.car");
        query.whereEqualTo("objectId", ParseUser.getCurrent().getParseObject("tripRequest").getObjectId());
        fetch = query.find().get(0);
        return fetch;
    }

    private static void checkinAction(String contents, ParseObject object, Button checkIn, Form f, Resources resources, Button drive, Button ride, Button active, StateMachine stateMachine) {
        if (contents.equals(object.getParseObject("trip").getParseObject("driver").getObjectId())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", contents);
            map.put("location", new ParseGeoPoint(MapController.getLocationCoord().getLatitude(), MapController.getLocationCoord().getLongitude()));
            object.put((checkIn.getText().equals("Check Out")) ? "checkout" : "checkin", map);
            try {
                showBlocking();
                object.save();
                hideBlocking();
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
        }
    }


    public static void tripHome(Form f, Resources resources, Button drive, Button ride) {
        ParseObject fetch;
        try {
            fetch = tripHomeQuery();

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

                Dialog.show("Token: " + objectId, new ImageViewer(URLImage.createToStorage(CODE_ICON().scaledEncoded(150, -1), "qr_" + objectId + ".png", url)), new Command("Back"));
            });

            north.add(checkin);
            f.add(BorderLayout.NORTH, north);
            MapController map = new MapController(resources, f, f.getName());
            map.initDriveMap(fetch.getParseGeoPoint("to"));


            addTripRequestMarkers(fetch, map, f);
            if (timer == null)
                timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Display.getInstance().callSerially(() -> {
                        try {
                            if (Display.getInstance().getCurrent().getName() != null && Display.getInstance().getCurrent().getName().equals("Home")) {
                                addTripRequestMarkers(tripHomeQuery(), map, f);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, TASK_DELAY, TASK_DELAY);
            data.put("active", fetch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static ParseObject tripHomeQuery() throws ParseException {
        ParseObject fetch;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
        query.include("tripRequests");
        query.include("tripRequests.user");
        query.whereEqualTo("objectId", ParseUser.getCurrent().getParseObject("trip").getObjectId());
        fetch = query.find().get(0);
        return fetch;
    }

    public static void addTripRequestMarkers(ParseObject fetch, MapController map, Form cF) {
        List<ParseObject> tripRequests = fetch.getList("tripRequests");

        map.clearMarkers();
        if (tripRequests != null)
            for (int i = 0; i < tripRequests.size(); i++) {
                if (tripRequests.get(i).getBoolean("active") && !tripRequests.get(i).has("checkin")) {
                    final ParseObject tripR = tripRequests.get(i);
                    final ParseUser tripUser = (ParseUser) tripR.getParseObject("user");
                    ParseGeoPoint location = tripUser.getParseGeoPoint("location");
//                    String url = tripUser.getParseFile("pic").getUrl();

//                    URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
//                    URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

                    map.addToMarkers(Constants.BLUE_LOCATION_ICON(), new Coord(location.getLatitude(), location.getLongitude()), "", "", evt -> {
                        Form dialog = getRequestUserDialog(tripR, tripUser, "Info");
                        getCancelSouth(tripUser, dialog, cF);
                        dialog.show();
                    });
                }
            }
    }

    public static void getCancelSouth(ParseUser user, Form dialog, Form cForm) {
        Button cancel = new Button("cancel");
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);

        cancel.addActionListener(evt1 -> {
            cForm.show();
        });
        cancel.setUIID("ToggleButtonOnly");
        dialog.add(BorderLayout.SOUTH, cancel);
    }

    public static void CancelActive(Form f, Resources resources, Button drive, Button ride, Button active, ParseObject object) {
        CancelActiveRequest(object);
        new MapController(resources, f, f.getName()).initMap();
        drive.setHidden(false);
        ride.setHidden(false);
        active.getParent().remove();
        f.repaint();
    }

    public static void CancelActiveRequest(ParseObject object) {
        exitHomeForm();
        object.put("active", false);
        ParseUser user = ParseUser.getCurrent();
        user.remove("trip");
        user.remove("tripRequest");
        try {
            showBlocking();
            object.save();
            user.save();
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
        if (MapController.getLocationCoord() == null || !ParseUser.getCurrent().has("location")) {
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
            tripQuery.include("tripRequests");
            tripQuery.whereWithinKilometers("to", new ParseGeoPoint(MapController.getDestCoord().getLatitude(), MapController.getDestCoord().getLongitude()), WITHIN_KILOMETERS);

            tripQuery.whereMatchesKeyInQuery("groups", "group", verifiedGroupUserQuery());
            tripQuery.whereMatchesQuery("driver", query);
            tripQuery.whereEqualTo("active", true);

            List<ParseObject> results = tripQuery.find();
            Iterator<ParseObject> objectIterator = results.iterator();
            while (objectIterator.hasNext()) {
                ParseObject object = objectIterator.next();
                if (object.has("tripRequests")) {
                    List<ParseObject> tripRequests = object.getList("tripRequests");
                    int count = 0;
                    for (int i = 0; i < tripRequests.size(); i++) {
                        if (tripRequests.get(i).getBoolean("active"))
                            count++;
                    }
                    if (count >= object.getInt("seats"))
                        objectIterator.remove();
                }
            }
            if (results.size() == 0) {
                hideBlocking();
                showDelayedToastBar("There is no rides available");
            } else {
                hideBlocking();
                infiniteProgressForm().show();
                data.put("rides", results);
                showForm("RideMap");
            }


        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }


    static ComboBox<Map<String, Object>> carsCombo;

    public static void driveAction(StateMachine stateMachine, Form f) {
        if (MapController.getDestCoord() == null) {
            ToastBar.showErrorMessage("You should choose a destination");
            return;
        }
        if (MapController.getLocationCoord() == null || !ParseUser.getCurrent().has("location")) {
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
            hideBlocking();
            infiniteProgressForm().show();
            data.put("carsArr", carsArr);
            data.put("groups", groups);
            showForm("DriveSettings");
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }

    }

    public static void driveSettings(Form dialog) {
        UserController.addUserSideMenu(dialog);
        carsCombo = new ComboBox<>((HashMap<String, Object>[]) data.get("carsArr"));
        carsCombo.setRenderer(new GenericListCellRenderer<>(new MultiButton(), new MultiButton()));
        Label autoAcceptLabel = new Label("Auto Accept: ");
        autoAcceptLabel.setUIID("ToggleButtonFirst");
        autoAcceptLabel.setEnabled(false);
        Picker autoAccept = new Picker();
        autoAccept.setType(Display.PICKER_TYPE_STRINGS);
        final String[] autoAccepts = {"No One", "Females", "Males", "All"};
        autoAccept.setStrings(autoAccepts);
        autoAccept.setSelectedString(autoAccepts[0]);
        autoAccept.setUIID("ToggleButtonFirst");


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

//            carsCombo.setUIID("ButtonGroupFirst");
        cost.setUIID("GroupElementFirst");
        toll.setUIID("GroupElement");
        seats.setUIID("GroupElement");
        notes.setUIID("GroupElementLast");


        cost.setConstraint(TextField.DECIMAL);
        toll.setConstraint(TextField.DECIMAL);
        seats.setConstraint(TextField.NUMERIC);


        Button cancel = new Button("Cancel");
        cancel.addActionListener(evt -> showForm("Home"));
        cancel.setUIID("ToggleButtonFirst");
        Button confirm = new Button("Confirm");
        confirm.setUIID("ToggleButtonLast");

        confirm.addActionListener(evt -> {
            ParseObject item = (ParseObject) carsCombo.getSelectedItem().get("object");
            data.put("car", item);
            data.put("cost", Double.parseDouble(cost.getText().length() == 0 ? "0" : cost.getText()));
            data.put("toll", Double.parseDouble(toll.getText().length() == 0 ? "0" : toll.getText()));
            data.put("seats", Integer.parseInt(seats.getText().length() == 0 ? "4" : seats.getText()));
            data.put("notes", notes.getText());
            data.put("groups", (List<ParseObject>) data.get("groups"));
            data.put("autoAccept", StringsIndexOf(autoAccepts, autoAccept.getSelectedString()));

            showForm("DriveSummary");
        });


        dialog.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, cancel, confirm));
        dialog.add(BorderLayout.CENTER, FlowLayout.encloseCenterMiddle(BoxLayout.encloseY(carsCombo, GridLayout.encloseIn(2, autoAcceptLabel, autoAccept), cost, toll, seats, notes)));
    }

    public static void exitHomeForm() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
