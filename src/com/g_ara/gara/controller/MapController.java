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

import static com.g_ara.gara.controller.UserController.currentParseUserSave;
import static com.g_ara.gara.model.Constants.*;

/**
 * Created by ahmedengu.
 */
public class MapController {

    private static Coord destCoord, locationCoord;
    private static Long lastLocationUpdate = 0L, lastLocationSent = 0L;
    private static int locationUpdateThreshold = 3000, locationSentThreshold = 10000;
    public final MapContainer map = new MapContainer(new GoogleMapsProvider(Constants.MAPS_KEY));
    private Resources theme;
    private List<Map<String, Object>> markers = new ArrayList<>();
    private Coord[] coordsPath;
    private static MapController mapController;

    public MapController(Resources theme, Container f) {
        this.theme = theme;

        f.addComponent(BorderLayout.CENTER, map);
        map.setRotateGestureEnabled(true);
    }

    public void initMap(List<ParseObject> parseObjects, StateMachine stateMachine) {
        map.clearMapLayers();
        for (int i = 0; i < parseObjects.size(); i++) {
            final ParseObject trip = parseObjects.get(i);
            final ParseObject driver = trip.getParseObject("driver");
            final ParseObject car = trip.getParseObject("car");

            String url = driver.getParseFile("pic").getUrl();

            URLImage.ImageAdapter adapter = URLImage.createMaskAdapter(MASK_LOCATION_ICON());
            URLImage image = URLImage.createToStorage(Constants.BLUE_LOCATION_ICON(), "map_" + url.substring(url.lastIndexOf("/") + 1), url, adapter);

            map.addMarker(image, new Coord(driver.getParseGeoPoint("location").getLatitude(), driver.getParseGeoPoint("location").getLongitude()), "Hi marker", "Optional long description", (ActionEvent evt) -> {
                Dialog dialog = new Dialog("Ride Info");
                dialog.setLayout(new BorderLayout());
                Button cancel = new Button("Cancel");
                cancel.addActionListener(evt1 -> dialog.dispose());

                Button confirm = new Button("Confirm");
                confirm.addActionListener(evt1 -> {
                    ParseObject tripRequest = ParseObject.create("TripRequest");
                    tripRequest.put("trip", trip);
                    ParseUser user = ParseUser.getCurrent();
                    user.setDirty(false);
                    tripRequest.put("user", user);
                    tripRequest.put("accept", -1);
                    tripRequest.put("active", true);
                    double distanceInKilometers = distanceInKilometers(MapController.getLocationCoord(), MapController.getDestCoord());
                    tripRequest.put("cost", distanceInKilometers * trip.getInt("cost") + trip.getInt("toll"));
                    tripRequest.put("to", new ParseGeoPoint(destCoord.getLatitude(), destCoord.getLongitude()));
                    tripRequest.put("distance", distanceInKilometers);
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
                Container container = new Container(new GridLayout(2));
                container.add(cancel);
                container.add(confirm);
                Container info = new Container(new BoxLayout(BoxLayout.Y_AXIS));


                String picUrl = driver.getParseFile("pic").getUrl();
                double distanceInKilometers = distanceInKilometers(MapController.getLocationCoord(), MapController.getDestCoord());
                info.add(new Label("Distance: " + distanceInKilometers));
                info.add(new Label("Cost: " + distanceInKilometers * trip.getInt("cost") + trip.getInt("toll")));

                info.add(new ImageViewer(URLImage.createToStorage(PROFILE_ICON().scaledEncoded(dialog.getWidth(), -1), picUrl.substring(picUrl.lastIndexOf("/") + 1), picUrl)));
                info.add(new Label("Username: " + driver.getString("username")));
                info.add(new Label("Name: " + driver.getString("name")));
                info.add(new Label("Mobile: " + driver.getString("mobile")));

                List<ParseFile> carPics = car.getList("pics");
                for (int j = 0; j < carPics.size(); j++) {
                    String carUrl = FILE_PATH + carPics.get(j).getName();
                    info.add(new ImageViewer(URLImage.createToStorage(CAR_ICON().scaledEncoded(dialog.getWidth(), -1), carUrl.substring(carUrl.lastIndexOf("/") + 1), carUrl)));
                }
                info.add(new Label("Car: " + car.getString("name")));
                info.add(new Label("Car Year: " + car.getString("year")));
                info.setScrollableY(true);
                dialog.add(BorderLayout.CENTER, info);
                dialog.add(BorderLayout.SOUTH, container);
                dialog.show();
            });

        }

    }

    public void initMap() {
        destCoord = null;
        handleCurrentLocation(map);
        destListener(map);
        mapController = this;
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
                coordsPath = null;
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
            if (user != null && user.isAuthenticated()) {
                ParseGeoPoint location1 = user.getParseGeoPoint("location");
                if (location1 == null || location.getLatitude() != location1.getLatitude() && location.getLongitude() != location1.getLongitude()) {
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


            map.addMarker(CURRENT_LOCATION_ICON(), locationCoord, "Hi marker", "Optional long description", null);
            lastLocationUpdate = System.currentTimeMillis();
        }
        if (destCoord != null) {
            map.addMarker(RED_LOCATION_ICON(), destCoord, "Hi marker", "Optional long description", null);
            if (coordsPath == null)
                getRoutesCoordsAsync();
        }
        for (int i = 0; i < markers.size(); i++) {
            Map<String, Object> m = markers.get(i);
            map.addMarker((EncodedImage) m.get("icon"), (Coord) m.get("coord"), (String) m.get("title"), (String) m.get("desc"), (ActionListener) m.get("action"));

        }

        if (coordsPath != null)
            map.addPath(coordsPath);
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
            ret = ((LinkedHashMap) ((LinkedHashMap) ((ArrayList) response.get("routes")).get(0)).get("overview_polyline")).get("points").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void getRoutesCoordsAsync() {
        ConnectionRequest request = new ConnectionRequest("https://maps.googleapis.com/maps/api/directions/json", false) {
            @Override
            protected void readResponse(InputStream input) throws IOException {
                Map<String, Object> response = new JSONParser().parseJSON(new InputStreamReader(input, "UTF-8"));
                coordsPath = decode(((LinkedHashMap) ((LinkedHashMap) ((ArrayList) response.get("routes")).get(0)).get("overview_polyline")).get("points").toString());
            }
        };
        request.addArgument("key", MAPS_KEY);
        request.addArgument("origin", locationCoord.getLatitude() + "," + locationCoord.getLongitude());
        request.addArgument("destination", destCoord.getLatitude() + "," + destCoord.getLongitude());

        NetworkManager.getInstance().addToQueue(request);
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
}
