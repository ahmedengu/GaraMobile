package com.g_ara.gara.model;

import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.plaf.Style;

/**
 * Created by ahmedengu.
 */
public class Constants {
    public static final String MAPS_KEY = "AIzaSyAxlzXskkl3KKdjZUuFrV-j8oFjWOjtTIQ";
    public static String WEBSITE = "http://www.g-ara.com";
    public static String EMAIL = "feedback@g-ara.com";
    public static String Play_STORE = "http://www.g-ara.com";
    public static String APPLE_STORE = "http://www.g-ara.com";
    public static String PAYMENT = "http://www.g-ara.com";

    public static Style STYLE_CURRENT_LOCATION() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0);
        return s;
    }

    public static Style STYLE_DESTINATION_LOCATION() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0xFF0000);
        return s;
    }

    public static EncodedImage DESTINATION_LOCATION_ICON() {
        return FontImage.createMaterial(FontImage.MATERIAL_LOCATION_ON, STYLE_DESTINATION_LOCATION()).toEncodedImage();
    }

    public static EncodedImage CURRENT_LOCATION_ICON() {
        return FontImage.createMaterial(FontImage.MATERIAL_MY_LOCATION, STYLE_CURRENT_LOCATION()).toEncodedImage();
    }

}
