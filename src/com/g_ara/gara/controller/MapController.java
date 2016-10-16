package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.maps.providers.GoogleMapsProvider;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseGeoPoint;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.g_ara.gara.controller.UserController.currentParseUserSave;

/**
 * Created by ahmedengu.
 */
public class MapController {

    private static Coord destCoord, locationCoord;
    private static Long lastLocationUpdate = 0L, lastLocationSent = 0L;
    private static int locationUpdateThreshold = 3000, locationSentThreshold = 10000;
    public final MapContainer map = new MapContainer(new GoogleMapsProvider("AIzaSyAxlzXskkl3KKdjZUuFrV-j8oFjWOjtTIQ"));
    private Resources theme;
    private List<Map<String, Object>> markers = new ArrayList<>();

    public MapController(Resources theme, Form f) {
        this.theme = theme;
        f.addComponent(BorderLayout.CENTER, map);
        map.setRotateGestureEnabled(true);
    }

    public void initMap(List<ParseObject> parseObjects, StateMachine stateMachine) {
        map.clearMapLayers();
        for (int i = 0; i < parseObjects.size(); i++) {
            final ParseObject driver = parseObjects.get(i).getParseObject("driver");
            final ParseObject trip = parseObjects.get(i);
            map.addMarker(EncodedImage.createFromImage(theme.getImage("map-pin-blue-hi.png"), false), new Coord(driver.getParseGeoPoint("location").getLatitude(), driver.getParseGeoPoint("location").getLongitude()), "Hi marker", "Optional long description", (ActionEvent evt) -> {
                Dialog dialog = new Dialog("Ride Info");
                dialog.setLayout(new BorderLayout());
                Label label = new Label("driver: " + driver.getString("username"));
                Button cancel = new Button("Cancel");
                cancel.addActionListener(evt1 -> dialog.dispose());

                Button confirm = new Button("Confirm");
                confirm.addActionListener(evt1 -> {
                    ParseObject tripRequest = ParseObject.create("TripRequest");
                    tripRequest.put("trip", trip);
                    ParseUser user = ParseUser.getCurrent();
                    tripRequest.put("user", user);
                    tripRequest.put("accept", -1);
                    tripRequest.put("active", true);
                    tripRequest.put("to", new ParseGeoPoint(destCoord.getLatitude(), destCoord.getLongitude()));

                    try {
                        tripRequest.save();
                        user.put("tripRequest", tripRequest);
                        currentParseUserSave();

                        StateMachine.data.put("active", tripRequest);
                        dialog.dispose();
                        stateMachine.showForm("Countdown", null);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        ToastBar.showErrorMessage(e.getMessage());
                    }
                });
                Container container = new Container();
                container.add(cancel);
                container.add(confirm);
                dialog.add(BorderLayout.CENTER, label);
                dialog.add(BorderLayout.SOUTH, container);
                dialog.show();
            });

        }

    }

    public void initMap() {
        handleCurrentLocation(map);
        destListener(map);
    }

    public void initDriveMap(ParseGeoPoint to) {
        destCoord = new Coord(to.getLatitude(), to.getLongitude());
        handleCurrentLocation(map);
    }

    private void handleCurrentLocation(MapContainer map) {
        Location currentLocation = updateMarkers(map);

        if (currentLocation != null) {
            locationCoord = new Coord(currentLocation.getLatitude(), currentLocation.getLongitude());
            map.zoom(locationCoord, 5);
            map.setShowMyLocation(true);
        }
        locationListener(map);
    }

    private void destListener(final MapContainer map) {
        map.addTapListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                destCoord = map.getCoordAtPosition(evt.getX(), evt.getY());
                updateMarkers(map);
            }
        });
    }

    private void locationListener(final MapContainer map) {
        LocationManager.getLocationManager().setLocationListener(new LocationListener() {

            public void locationUpdated(Location location) {
                if (System.currentTimeMillis() - lastLocationUpdate >= locationUpdateThreshold) {
                    updateMarkers(map, location);
                    sendLocation(location);
                }
            }


            public void providerStateChanged(int newState) {
                //TODO: handle gps state
            }
        });
    }

    private void sendLocation(Location location) {
        if (System.currentTimeMillis() - lastLocationSent >= locationSentThreshold) {

            lastLocationSent = System.currentTimeMillis();
            ParseUser user = ParseUser.getCurrent();
            if (user != null) {
                ParseGeoPoint location1 = user.getParseGeoPoint("location");
                if (location.getLatitude() != location1.getLatitude() && location.getLongitude() != location1.getLongitude()) {
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
        //TODO: edit the text and icon below
        if (location != null) {
            locationCoord = new Coord(location.getLatitude(), location.getLongitude());
            map.addMarker(EncodedImage.createFromImage(theme.getImage("map-pin-green-hi.png"), false), locationCoord, "Hi marker", "Optional long description", null);
            lastLocationUpdate = System.currentTimeMillis();
        }
        if (destCoord != null)
            map.addMarker(EncodedImage.createFromImage(theme.getImage("map-pin-blue-hi.png"), false), destCoord, "Hi marker", "Optional long description", null);

        for (int i = 0; i < markers.size(); i++) {
            Map<String, Object> m = markers.get(i);
            map.addMarker((EncodedImage) m.get("icon"), (Coord) m.get("coord"), (String) m.get("title"), (String) m.get("desc"), (ActionListener) m.get("action"));

        }
        return location;
    }

    public static Coord getDestCoord() {
        return destCoord;
    }

    public static Coord getLocationCoord() {
        return locationCoord;
    }

    public void addToMarkers(EncodedImage encodedImage, Coord coord, String s, String s1, Object o) {
        Map<String, Object> m = new HashMap<>();
        m.put("icon", encodedImage);
        m.put("coord", coord);
        m.put("title", s);
        m.put("desc", s1);
        m.put("action", o);
        markers.add(m);
        map.addMarker(encodedImage, coord, s, s1, (ActionListener) o);
    }
}
