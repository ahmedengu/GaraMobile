package com.g_ara.gara.controller;

import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.maps.providers.GoogleMapsProvider;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.util.Resources;
import com.codename1.util.MathUtil;
import com.g_ara.gara.model.Constants;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

import static com.g_ara.gara.controller.ChatController.getUserChat;
import static com.g_ara.gara.controller.UserController.currentParseUserSave;
import static com.g_ara.gara.controller.UserController.getUserEmptyObject;
import static com.g_ara.gara.model.Constants.*;
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class MapController {

    private static Coord destCoord, locationCoord, lastDestCoord;
    private static Long lastLocationUpdate = 0L, lastLocationSent = 0L;
    public final MapContainer map;
    private Resources theme;
    private List<Map<String, Object>> markers = new ArrayList<Map<String, Object>>();
    private Coord[] coordsPath;

    public MapController(Container f) {
        map = new MapContainer(new GoogleMapsProvider(Constants.MAPS_KEY));
        f.addComponent(BorderLayout.CENTER, map);
        map.setRotateGestureEnabled(true);
        map.setShowMyLocation(true);
        if (locationCoord != null)
            map.zoom(locationCoord, 10);

    }

    public MapController(Resources theme, Container f) {
        this(f);
        this.theme = theme;
    }

    public void initMap(List<ParseObject> parseObjects, StateMachine stateMachine, Form cForm) {
        map.clearMapLayers();
        for (int i = 0; i < parseObjects.size(); i++) {
            final ParseObject trip = parseObjects.get(i);
            final ParseObject driver = trip.getParseObject("driver");
            final ParseObject car = trip.getParseObject("car");

//            String url = driver.getParseFile("pic").getUrl();

//            URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
//            URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

            map.addMarker(Constants.BLUE_LOCATION_ICON(), new Coord(driver.getParseGeoPoint("location").getLatitude(), driver.getParseGeoPoint("location").getLongitude()), "Driver", "", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {


                    Form form = getDriveInfoDialog(trip, driver, car, "Ride Info");
                    UserController.addUserSideMenu(form);

                    Container south = new Container(new GridLayout(2));
                    Button cancel = new Button("Cancel");
                    cancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt1) {
                            cForm.show();
                        }
                    });
                    cancel.setUIID("ToggleButtonFirst");
                    south.add(cancel);
                    Button confirm = new Button("Confirm");
                    confirm.setUIID("ToggleButtonLast");
                    confirm.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt1) {
                            ParseObject tripRequest = ParseObject.create("TripRequest");
                            tripRequest.put("trip", trip);
                            ParseUser user = ParseUser.getCurrent();
                            tripRequest.put("user", getUserEmptyObject());
                            tripRequest.put("accept", -1);
                            tripRequest.put("active", true);
                            double distanceinkilometers = distanceInKilometers(MapController.getLocationCoord(), MapController.getDestCoord());
                            tripRequest.put("cost", (int) (distanceinkilometers * trip.getInt("cost") + trip.getInt("toll")));
                            tripRequest.put("to", new ParseGeoPoint(destCoord.getLatitude(), destCoord.getLongitude()));
                            tripRequest.put("distance", distanceinkilometers);
                            tripRequest.put("from", MapController.locationCoord.getLatitude() + "," + MapController.locationCoord.getLongitude());
                            tripRequest.put("driver", (ParseUser) trip.getParseObject("driver"));
                            try {
                                showBlocking();
                                tripRequest.save();
                                user.put("tripRequest", tripRequest);
                                currentParseUserSave();

                                StateMachine.data.put("active", tripRequest);
                                hideBlocking();
                                infiniteProgressForm().show();
                                stateMachine.showForm("Countdown", null);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                ToastBar.showErrorMessage(e.getMessage());
                            }
                        }
                    });
                    south.add(confirm);
                    form.add(BorderLayout.SOUTH, south);
                    form.show();
                }
            });

        }

    }

    public static Form getDriveInfoDialog(ParseObject trip, ParseObject driver, ParseObject car, String title) {
        Form components = new Form(title);
        components.setLayout(new BorderLayout());


        Container info = new Container(new BoxLayout(BoxLayout.X_AXIS));

        List<ParseFile> carPics = car.getList("pics");
        String[] carImages = new String[carPics.size() + 1];
        for (int j = 0; j < carPics.size(); j++) {
            carImages[j] = carPics.get(j).getUrl();
        }
        carImages[carPics.size()] = driver.getParseFile("pic").getUrl();
        ImageViewer carImageViewer = new ImageViewer();
        carImageViewer.setImageList(new ImageList(carImages));
//        info.add(carImageViewer);


        double distanceInKilometers = distanceInKilometers(MapController.getLocationCoord(), MapController.getDestCoord());

        info.add(new Label("Cost: " + (int) (distanceInKilometers * trip.getInt("cost") + trip.getInt("toll"))));
        info.add(new Label("Distance: " + (int) distanceInKilometers));
        info.add(new Label("Username: " + driver.getString("username")));
        info.add(new Label("Name: " + driver.getString("name")));
        info.add(new Label("Mobile: " + driver.getString("mobile")));
        Button dial = new Button("Call");
        FontImage.setMaterialIcon(dial, FontImage.MATERIAL_CALL);
        dial.addActionListener(evt -> {
            Display.getInstance().dial(driver.getString("mobile"));
        });

        Button chat = new Button("Chat");
        FontImage.setMaterialIcon(chat, FontImage.MATERIAL_CHAT);
        chat.addActionListener(evt -> Display.getInstance().callSerially(() -> {
            infiniteProgressForm().show();
            getUserChat((ParseUser) driver);
        }));


        Button report = new Button("Report");
        FontImage.setMaterialIcon(report, FontImage.MATERIAL_REPORT);
        report.addActionListener(evt -> {
//            components.dispose();
            Dialog reportDialog = new Dialog("Report");
            reportDialog.setLayout(new BorderLayout());

            TextArea infoText = new TextArea();
            infoText.setHint("More Information");
            infoText.setUIID("GroupElementOnly");
            reportDialog.add(BorderLayout.CENTER, infoText);

            Button cancel = new Button("Cancel");
            FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
            cancel.addActionListener(evt1 -> reportDialog.dispose());


            Button reportIt = new Button("Report");
            FontImage.setMaterialIcon(reportIt, FontImage.MATERIAL_REPORT);
            reportIt.addActionListener(evt1 -> {
                try {
                    ParseObject reportObject = ParseObject.create("Reports");
                    reportObject.put("info", infoText.getText());
                    reportObject.put("user", ParseUser.getCurrent());
                    reportObject.put("to", (ParseUser) driver);
                    showBlocking();
                    reportObject.save();
                    hideBlocking();
                    reportDialog.dispose();
                } catch (ParseException e) {
                    e.printStackTrace();
                    hideBlocking();
                    ToastBar.showErrorMessage(e.getMessage());
                }
            });

            reportDialog.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, cancel, reportIt));
            reportDialog.show();
        });

        info.add(new Label("Car: " + car.getString("name")));
        info.add(new Label("Car Year: " + car.getString("year")));
        info.add(new Label("Car Notes: " + car.getString("notes")));

        info.add(new Label("Trip Notes: " + trip.getString("notes")));

        dial.setUIID("ToggleButtonFirst");
        chat.setUIID("ToggleButton");
        report.setUIID("ToggleButtonLast");


        info.setScrollableX(true);
        Container components1 = new Container(new BorderLayout());
        components1.add(BorderLayout.NORTH, info);
        Button images = new Button("Images");
        FontImage.setMaterialIcon(images, FontImage.MATERIAL_IMAGE);
        images.setUIID("ButtonGroupOnly");

        images.addActionListener(evt -> {
            Form dialog = new Form("Images");
            dialog.setLayout(new BorderLayout());
            dialog.add(BorderLayout.CENTER, carImageViewer);
            Button cancel = new Button("Cancel");
            FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
            cancel.setUIID("ToggleButtonOnly");
            cancel.addActionListener(evt1 -> {
                carImageViewer.remove();
                components.show();
            });
            dialog.add(BorderLayout.SOUTH, cancel);
            dialog.show();
        });
        components1.add(BorderLayout.CENTER, images);
        if (driver.getString("mobile").equals("-")) {
            components1.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, chat, report));
        } else {
            components1.add(BorderLayout.SOUTH, GridLayout.encloseIn(3, dial, chat, report));
        }
        components.add(BorderLayout.NORTH, components1);
        draw2MarkerMap(driver.getParseGeoPoint("location"), trip.getParseGeoPoint("to"), components);
        return components;
    }


    public void initMap() {
        destCoord = null;
        handleCurrentLocation(map);
        destListener(map);
    }

    public void initDriveMap(ParseGeoPoint to) {
        destCoord = new Coord(to.getLatitude(), to.getLongitude());
        handleCurrentLocation(map);
    }

    public void handleCurrentLocation(MapContainer map) {
        Location currentLocation = updateMarkers(map);

        if (currentLocation != null) {
            locationCoord = new Coord(currentLocation.getLatitude(), currentLocation.getLongitude());
            map.zoom(locationCoord, 10);
        }
        locationListener(map);
    }

    private void destListener(final MapContainer map) {
        map.addTapListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                destCoord = map.getCoordAtPosition(evt.getX(), evt.getY());
                coordsPath = null;
                Display.getInstance().callSerially(() -> updateMarkers(map));
            }
        });
    }

    private void locationListener(final MapContainer map) {
        LocationManager.getLocationManager().setLocationListener(new LocationListener() {

            public void locationUpdated(Location location) {
                if (System.currentTimeMillis() - lastLocationUpdate >= locationUpdateThreshold) {
                    Display.getInstance().callSerially(() -> updateMarkers(map, location));
                    sendLocation(location);
                }
            }

            public void providerStateChanged(int newState) {
            }
        });
    }

    private void sendLocation(Location location) {
        if (System.currentTimeMillis() - lastLocationSent >= locationSentThreshold) {

            lastLocationSent = System.currentTimeMillis();
            ParseUser user = ParseUser.getCurrent();
            if (user != null && user.isAuthenticated()) {
                ParseGeoPoint location1 = user.getParseGeoPoint("location");
                if (location1 == null || ((location.getLatitude() != location1.getLatitude() && location.getLongitude() != location1.getLongitude()) && (ParseUser.getCurrent().get("trip") != null || ParseUser.getCurrent().get("tripRequest") != null))) {
                    user.put("location", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
                    try {
                        currentParseUserSave();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Location updateMarkers(MapContainer map) {
        Location currentLocation;
        try {
            currentLocation = LocationManager.getLocationManager().getCurrentLocation();
        } catch (IOException e) {
            currentLocation = LocationManager.getLocationManager().getLastKnownLocation();
        }
        return updateMarkers(map, currentLocation);
    }

    private Location updateMarkers(MapContainer map, Location location) {
        map.clearMapLayers();
        if (location != null) {
            locationCoord = new Coord(location.getLatitude(), location.getLongitude());
            //TODO: for mobile :
            if (!map.isNativeMaps())
                map.addMarker(CURRENT_LOCATION_ICON(), locationCoord, "Current Location", "", null);
            lastLocationUpdate = System.currentTimeMillis();
        }
        if (destCoord != null) {
            map.addMarker(RED_LOCATION_ICON(), destCoord, "Destination", "", null);
            if (coordsPath == null && !destCoord.equals(lastDestCoord)) {
                lastDestCoord = destCoord;
                getRoutesCoordsAsync();
            }
        }
        for (int i = 0; i < markers.size(); i++) {
            Map<String, Object> m = markers.get(i);
            map.addMarker((EncodedImage) m.get("icon"), (Coord) m.get("coord"), (String) m.get("title"), (String) m.get("desc"), (ActionListener) m.get("action"));

        }

        if (coordsPath != null && coordsPath.length > 0)
            map.addPath(coordsPath);
        return location;
    }


    public static Coord getDestCoord() {
        return destCoord;
    }

    public static Coord getLocationCoord() {
        return locationCoord;
    }

    public void addToMarkers(EncodedImage encodedImage, Coord coord, String s, String s1, ActionListener o) {
        Map<String, Object> m = new HashMap<>();
        m.put("icon", encodedImage);
        m.put("coord", coord);
        m.put("title", s);
        m.put("desc", s1);
        m.put("action", o);
        markers.add(m);
        map.addMarker(encodedImage, coord, s, s1, o);
    }

    public void clearMarkers() {
        markers.clear();
    }

    public static Coord[] decode(final String encodedPath) {
        int len = encodedPath.length();
        final List<Coord> path = new ArrayList<Coord>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new Coord(lat * 1e-5, lng * 1e-5));
        }
        Coord[] p = new Coord[path.size()];
        for (int i = 0; i < path.size(); i++) {
            p[i] = path.get(i);
        }

        return p;
    }

    public static String getRoutesEncoded(Coord src, Coord dest) {
        String ret = "";
        try {
            ConnectionRequest request = new ConnectionRequest("https://maps.googleapis.com/maps/api/directions/json", false);
            request.addArgument("key", MAPS_KEY);
            request.addArgument("origin", src.getLatitude() + "," + src.getLongitude());
            request.addArgument("destination", dest.getLatitude() + "," + dest.getLongitude());

            NetworkManager.getInstance().addToQueueAndWait(request);
            Map<String, Object> response = new JSONParser().parseJSON(new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8"));
            if (response.get("routes") != null) {
                ArrayList routes = (ArrayList) response.get("routes");
                if (routes.size() > 0)
                    ret = ((LinkedHashMap) ((LinkedHashMap) ((ArrayList) response.get("routes")).get(0)).get("overview_polyline")).get("points").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void getRoutesCoordsAsync() {
        if (locationCoord != null && destCoord != null) {
            ConnectionRequest request = new ConnectionRequest("https://maps.googleapis.com/maps/api/directions/json", false) {
                @Override
                protected void readResponse(InputStream input) throws IOException {
                    Map<String, Object> response = new JSONParser().parseJSON(new InputStreamReader(input, "UTF-8"));
                    if (response.get("routes") != null) {
                        ArrayList routes = (ArrayList) response.get("routes");
                        if (routes.size() > 0)
                            coordsPath = decode(((LinkedHashMap) ((LinkedHashMap) routes.get(0)).get("overview_polyline")).get("points").toString());
                    }
                }
            };
            request.addArgument("key", MAPS_KEY);

            request.addArgument("origin", locationCoord.getLatitude() + "," + locationCoord.getLongitude());
            request.addArgument("destination", destCoord.getLatitude() + "," + destCoord.getLongitude());

            NetworkManager.getInstance().addToQueue(request);
        }
    }

    public static void stopLocationListener() {
        LocationManager.getLocationManager().setLocationListener(null);
    }

    public static double distanceInKilometers(Coord point1, Coord point2) {
        double d2r = 0.0174532925199433D;
        double lat1rad = point1.getLatitude() * d2r;
        double long1rad = point1.getLongitude() * d2r;
        double lat2rad = point2.getLatitude() * d2r;
        double long2rad = point2.getLongitude() * d2r;
        double deltaLat = lat1rad - lat2rad;
        double deltaLong = long1rad - long2rad;
        double sinDeltaLatDiv2 = Math.sin(deltaLat / 2.0D);
        double sinDeltaLongDiv2 = Math.sin(deltaLong / 2.0D);

        double a = sinDeltaLatDiv2 * sinDeltaLatDiv2 + Math.cos(lat1rad)
                * Math.cos(lat2rad) * sinDeltaLongDiv2 * sinDeltaLongDiv2;

        a = Math.min(1.0D, a);
        return 2.0D * MathUtil.asin(Math.sqrt(a)) * 6371.0D;
    }

    public static void setDestCoord(Coord destCoord) {
        MapController.destCoord = destCoord;
    }

    public static void draw2MarkerMap(ParseGeoPoint location, ParseGeoPoint dest, Container container) {
        draw2MarkerMap(new Coord(location.getLatitude(), location.getLongitude()), new Coord(dest.getLatitude(), dest.getLongitude()), container);
    }

    public static void draw2MarkerMap(Coord locationCoord, Coord destCoord, Container container) {
        MapContainer map = new MapController(container).map;
        map.addMarker(CURRENT_LOCATION_ICON(), locationCoord, "Current Location", "", null);
        map.addMarker(RED_LOCATION_ICON(), destCoord, "Destination", "", null);
        map.zoom(locationCoord, 10);
        map.setScrollableY(false);
        Coord[] coords = MapController.decode(MapController.getRoutesEncoded(locationCoord, destCoord));
        if (coords != null && coords.length > 0)
            map.addPath(coords);
    }

    public static Coord[] drawCircle(Location location, double radius) {
        return drawCircle(location.getLatitude(), location.getLongitude(), radius);
    }

    public static Coord[] drawCircle(Coord location, double radius) {
        return drawCircle(location.getLatitude(), location.getLongitude(), radius);
    }

    public static Coord[] drawCircle(double latitude, double longitude, double radius) {
        Coord[] locs = new Coord[361];
        double lat1 = latitude * Math.PI / 180.0;
        double lon1 = longitude * Math.PI / 180.0;
        double d = radius / 3956;
        for (int i = 0; i <= 360; i++) {
            double tc = (i / 90) * Math.PI / 2;
            double lat = MathUtil.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(tc));
            lat = 180.0 * lat / Math.PI;
            double lon;
            if (Math.cos(lat1) == 0) {
                lon = longitude;
            } else {
                lon = ((lon1 - MathUtil.asin(Math.sin(tc) * Math.sin(d) / Math.cos(lat1)) + Math.PI) % (2 * Math.PI)) - Math.PI;
            }
            lon = 180.0 * lon / Math.PI;
            locs[i] = new Coord(lat, lon);
        }
        return locs;
    }
}
