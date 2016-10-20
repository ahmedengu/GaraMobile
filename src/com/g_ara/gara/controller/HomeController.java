package com.g_ara.gara.controller;

import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.ext.codescan.CodeScanner;
import com.codename1.ext.codescan.ScanResult;
import com.codename1.maps.Coord;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.g_ara.gara.model.Constants;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.g_ara.gara.controller.UserController.currentParseUserSave;
import static com.g_ara.gara.model.Constants.CODE_ICON;
import static com.g_ara.gara.model.Constants.MASK_LOCATION_ICON;
import static userclasses.StateMachine.data;
import static userclasses.StateMachine.showDelayedToastBar;

/**
 * Created by ahmedengu.
 */
public class HomeController {
    public static void beforeHomeForm(Form f, Resources resources, Button drive, Button ride, StateMachine stateMachine) {
        f.setBackCommand(null);
        ParseObject fetch = null;
        if (ParseUser.getCurrent().get("trip") != null) {

            tripHome(f, resources, drive, ride);

        } else if (ParseUser.getCurrent().get("tripRequest") != null) {

            tripRequestHome(f, resources, drive, ride, stateMachine);

        } else
            new MapController(resources, f).initMap();
    }

    public static void tripRequestHome(Form f, Resources resources, Button drive, Button ride, StateMachine stateMachine) {
        ParseObject fetch;
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TripRequest");
            query.include("trip");
            query.include("trip.driver");
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
            final ParseObject object = fetch;
            active.addActionListener(evt -> {
                CancelActive(f, resources, drive, ride, active, object);
            });

            drive.setHidden(true);
            ride.setHidden(true);
            Container north = new Container(new GridLayout(2));
            north.add(active);

            Button checkIn = new Button("CheckIn");
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

            if (object.getString("checkin") != null) {
                checkIn.setText("Check Out");
            }
            north.add(checkIn);

            f.add(BorderLayout.NORTH, north);
            MapController map = new MapController(resources, f);
            map.initDriveMap(fetch.getParseGeoPoint("to"));


            ParseObject tripUser = fetch.getParseObject("trip").getParseObject("driver");
            ParseGeoPoint location = tripUser.getParseGeoPoint("location");
            String url = tripUser.getParseFile("pic").getUrl();

            URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
            URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

            map.addToMarkers(image, new Coord(location.getLatitude(), location.getLongitude()), "", "", null);


            data.put("active", fetch);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void checkinAction(String contents, ParseObject object, Button checkIn, Form f, Resources resources, Button drive, Button ride, Button active, StateMachine stateMachine) {
        if (contents.equals(object.getParseObject("trip").getObjectId())) {
//            ToastBar.showErrorMessage("Checked in Successfully");
            showDelayedToastBar("Checked in Successfully");

            Map<String, Object> map = new HashMap<>();
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
                } else
                    checkIn.setText("Check Out");

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
            final ParseObject object = fetch;
            active.addActionListener(evt -> {
                CancelActive(f, resources, drive, ride, active, object);
            });

            drive.setHidden(true);
            ride.setHidden(true);
            Container north = new Container(new GridLayout(2));
            north.add(active);

            Button checkin = new Button("Check In/Out");
            checkin.addActionListener(evt -> {
                String objectId = object.getObjectId();
                String url = Constants.GOOGLE_QR + objectId;

                Dialog.show("Token:" + objectId, new ImageViewer(URLImage.createToStorage(CODE_ICON().scaledEncoded(150, -1), objectId + ".png", url)), new Command("Back"));
            });

            north.add(checkin);
            f.add(BorderLayout.NORTH, north);
            MapController map = new MapController(resources, f);
            map.initDriveMap(fetch.getParseGeoPoint("to"));


            List<ParseObject> tripRequests = fetch.getList("tripRequests");
            if (tripRequests != null)
                for (int i = 0; i < tripRequests.size(); i++) {
                    ParseObject tripUser = tripRequests.get(i).getParseObject("user");
                    ParseGeoPoint location = tripUser.getParseGeoPoint("location");
                    String url = tripUser.getParseFile("pic").getUrl();

                    URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
                    URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

                    map.addToMarkers(image, new Coord(location.getLatitude(), location.getLongitude()), "", "", null);
                }
            data.put("active", fetch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void CancelActive(Form f, Resources resources, Button drive, Button ride, Button active, ParseObject object) {

        CancelActiveRequest(object);
        new MapController(resources, f).initMap();

        f.repaint();
        f.removeComponent(active);
        drive.setHidden(false);
        ride.setHidden(false);

    }

    public static void CancelActiveRequest(ParseObject object) {
        object.put("active", false);
        ParseUser user = ParseUser.getCurrent();
        user.remove("trip");
        user.remove("tripRequest");
        try {
            object.save();
            currentParseUserSave();
            data.remove("active");
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
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

        try {
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereWithinKilometers("location", new ParseGeoPoint(MapController.getLocationCoord().getLatitude(), MapController.getLocationCoord().getLongitude()), 5000000);
            ParseQuery<ParseObject> tripQuery = ParseQuery.getQuery("Trip");
            tripQuery.include("driver");
            tripQuery.include("car");
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
        });
        Dialog dialog = new Dialog("Choose a car");
        dialog.setLayout(new GridLayout(1));
        dialog.add(list);
        TextField cost = new TextField("");
        TextField toll = new TextField("");
        TextField seats = new TextField("");
        TextArea notes = new TextArea("");

        cost.setHint("Cost per kilo");
        toll.setHint("Toll cost");
        seats.setHint("Available seats");
        notes.setHint("Notes:");

        dialog.add(cost);
        dialog.add(toll);
        dialog.add(seats);
        dialog.add(notes);

        Button cancel = new Button("Cancel");
        cancel.addActionListener(evt -> dialog.dispose());
        Button confirm = new Button("Confirm");
        confirm.addActionListener(evt -> {
            if (data.get("car") == null) {
                return;
            }
            data.put("cost", Integer.parseInt(cost.getText().length() == 0 ? "0" : cost.getText()));
            data.put("toll", Integer.parseInt(toll.getText().length() == 0 ? "0" : toll.getText()));
            data.put("seats", Integer.parseInt(seats.getText().length() == 0 ? "4" : seats.getText()));
            data.put("notes", notes.getText());

            stateMachine.showForm("DriveSummary", null);
        });
        Container container = new Container();
        container.setLayout(new GridLayout(2));
        container.add(cancel);
        container.add(confirm);
        dialog.add(container);
        dialog.show();
    }

}
