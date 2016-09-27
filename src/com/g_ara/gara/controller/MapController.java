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
import java.util.List;

/**
 * Created by ahmedengu.
 */
public class MapController {

    private static Coord destCoord, locationCoord;
    private static Long lastLocationUpdate = 0L, lastLocationSent = 0L;
    private static int locationUpdateThreshold = 3000, locationSentThreshold = 10000;
    private Resources theme;

    public MapController(Resources theme) {
        this.theme = theme;
    }

    public void initMap(Form f, List<ParseObject> parseObjects, StateMachine stateMachine) {
        final MapContainer map = getMapContainer(f);

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
                    tripRequest.put("user", ParseUser.getCurrent());
                    tripRequest.put("accept", false);
                    try {
                        tripRequest.save();
                        dialog.dispose();
                        stateMachine.showForm("Home", null);
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

    public void initMap(Form f) {
        final MapContainer map = getMapContainer(f);

        handleCurrentLocation(map);
    }

    private MapContainer getMapContainer(Form f) {
        final MapContainer map = new MapContainer(new GoogleMapsProvider("AIzaSyAxlzXskkl3KKdjZUuFrV-j8oFjWOjtTIQ"));
        f.addComponent(BorderLayout.CENTER, map);
        map.setRotateGestureEnabled(true);
        return map;
    }

    private void handleCurrentLocation(MapContainer map) {
        Location currentLocation = updateMarkers(map);

        if (currentLocation != null) {
            locationCoord = new Coord(currentLocation.getLatitude(), currentLocation.getLongitude());
            map.zoom(locationCoord, 5);
            map.setShowMyLocation(true);
        }
        locationListener(map);
        destListener(map);
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
            try {
                lastLocationSent = System.currentTimeMillis();
                ParseUser user = ParseUser.getCurrent();
                user.put("location", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
                user.save();
            } catch (ParseException e) {
                e.printStackTrace();
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

        return location;
    }

    public static Coord getDestCoord() {
        return destCoord;
    }

    public static Coord getLocationCoord() {
        return locationCoord;
    }
}
