package com.g_ara.gara.controller;

import com.codename1.googlemaps.MapContainer;
import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.maps.providers.GoogleMapsProvider;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;

import java.io.IOException;

/**
 * Created by ahmedengu.
 */
public class MapController {

    private static Coord destCoord,locationCoord;
    private static Long lastLocationUpdate = 0L;
    private static int locationUpdateThreshold = 3000;
    private Resources theme;

    public MapController(Resources theme) {
        this.theme = theme;
    }

    public void initMap(Form f) {
        final MapContainer map = new MapContainer(new GoogleMapsProvider("AIzaSyAxlzXskkl3KKdjZUuFrV-j8oFjWOjtTIQ"));
        f.addComponent(BorderLayout.CENTER, map);
        map.setRotateGestureEnabled(true);

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
                // TODO: send location to server
                if (System.currentTimeMillis() - lastLocationUpdate >= locationUpdateThreshold) {
                    updateMarkers(map, location);
                }
            }


            public void providerStateChanged(int newState) {
                //TODO: handle gps state
            }
        });
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
