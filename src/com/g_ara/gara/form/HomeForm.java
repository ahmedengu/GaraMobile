package com.g_ara.gara.form;

import com.codename1.googlemaps.MapContainer;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.maps.layers.PointLayer;
import com.codename1.maps.layers.PointsLayer;
import com.codename1.maps.providers.GoogleMapsProvider;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.layouts.BorderLayout;

import java.io.IOException;

import static com.g_ara.gara.Main.fetchResourceFile;

public class HomeForm extends com.codename1.ui.Form {
    PointLayer distPoint, sourcePoint;
    PointsLayer distLayer, sourceLayer;
    Long pointerPressed;
    int pointerPressedX, pointerPressedY;
    boolean EnableGpsFlag = true;
    Long lastTimeLocationSent = 0l, lastLocationUpdate = 0l;
    int locationSendThreshold = 10000, locationUpdateThreshold = 500;

    public HomeForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    public HomeForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);

        initMap();

    }

    private void initMap() {
        final MapContainer map = new MapContainer(new GoogleMapsProvider("AIzaSyAxlzXskkl3KKdjZUuFrV-j8oFjWOjtTIQ"));
        addComponent(BorderLayout.CENTER, map);
        map.setRotateGestureEnabled(true);

        Location currentLocation = currentLocation(map);
        if (currentLocation != null)
            map.zoom(new Coord(currentLocation.getLatitude(), currentLocation.getLongitude()), 5);


//        MapComponent mapComponent = new MapComponent(new GoogleMapsProvider("AIzaSyAxlzXskkl3KKdjZUuFrV-j8oFjWOjtTIQ"), position, 5, true);
//        sourcePoint = new PointLayer(position, name, image);
//        sourcePoint.setDisplayName(true);
//        sourceLayer.addPoint(sourcePoint);
//        mapComponent.addLayer(sourceLayer);
//        addComponent(BorderLayout.CENTER, mapComponent);
//
//
//        LocationManager.getLocationManager().setLocationListener(new LocationListener() {
//            @Override
//            public void locationUpdated(Location location) {
//                if (System.currentTimeMillis() - lastTimeLocationSent >= locationSendThreshold) {
//                    String id = Preferences.get("id", "");
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    String token = Preferences.get("token", "");
//
//                   // RequestsHandler.updateMemberLocationAsync(id, latitude, longitude, token);
//                    lastTimeLocationSent = System.currentTimeMillis();
//
//                }
//                if (System.currentTimeMillis() - lastLocationUpdate >= locationUpdateThreshold) {
//                    sourceLayer.removePoint(sourcePoint);
//                    sourcePoint = new PointLayer(new Coord(location.getLatitude(), location.getLongitude()), name, image);
//                    sourcePoint.setDisplayName(true);
//                    sourceLayer.addPoint(sourcePoint);
//                    mapComponent.repaint();
//                }
//            }
//
//            @Override
//            public void providerStateChanged(int newState) {
//                if (newState != 0 && EnableGpsFlag) {
//                    Dialog.show("Error", "Please enable GPS!", "ok", null);
//                  //  findDrive().setEnabled(false);
//                   // findGetRide().setEnabled(false);
//                    EnableGpsFlag = false;
//                } else {
//                    //findDrive().setEnabled(true);
//                    //findGetRide().setEnabled(true);
//                    EnableGpsFlag = true;
//                }
//            }
//        });
//
//
//        mapComponent.addPointerPressedListener(evt ->
//                {
//                    pointerPressed = System.currentTimeMillis();
//                    pointerPressedX = evt.getX();
//                    pointerPressedY = evt.getY();
//                }
//        );
//        mapComponent.addPointerReleasedListener(evt -> {
//            int x = evt.getX();
//            int y = evt.getY();
//
//            final boolean longTap = (System.currentTimeMillis() - pointerPressed >= 200) && Math.abs(x - pointerPressedX) <= 10 && Math.abs(y - pointerPressedY) <= 10;
//            if (longTap && EnableGpsFlag) {
//                Image image1 = fetchResourceFile().getImage("map-pin-green-hi.png");
//                String name1 = "Your Dist!";
//                Coord coord = mapComponent.getCoordFromPosition(evt.getX(), evt.getY());
//                if (distLayer != null && distPoint != null)
//                    distLayer.removePoint(distPoint);
//                else {
//                    distLayer = new PointsLayer();
//                    mapComponent.addLayer(distLayer);
//                }
//                distPoint = new PointLayer(coord, name1, image1);
//                distPoint.setDisplayName(true);
//                distLayer.addPoint(distPoint);
//
//
//            }
//        });
    }

    private Location currentLocation(MapContainer map) {
        Location currentLocation = null;
        try {
            currentLocation = LocationManager.getLocationManager().getCurrentLocation();
        } catch (IOException e) {
            currentLocation = LocationManager.getLocationManager().getLastKnownLocation();
            e.printStackTrace();
        }


        if (currentLocation != null) {
            map.addMarker(EncodedImage.createFromImage(fetchResourceFile().getImage("map-pin-green-hi.png"), false), new Coord(currentLocation.getLatitude(), currentLocation.getLongitude()), "Hi marker", "Optional long description", null);
            map.setShowMyLocation(true);
        }
        return currentLocation;
    }

//-- DON'T EDIT BELOW THIS LINE!!!


    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.BorderLayout());
        setTitle("Home");
        setName("HomeForm");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
