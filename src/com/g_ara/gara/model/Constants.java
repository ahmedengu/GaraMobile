package com.g_ara.gara.model;

import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
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
    public static String FILE_PATH = "http://localhost:1337/parse/files/myAppId/";

    public static EncodedImage RED_LOCATION_ICON() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0xFF0000);
        return FontImage.createMaterial(FontImage.MATERIAL_LOCATION_ON, s).toEncodedImage();
    }

    public static EncodedImage CURRENT_LOCATION_ICON() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0);
        return FontImage.createMaterial(FontImage.MATERIAL_MY_LOCATION, s).toEncodedImage();
    }

    public static EncodedImage GREEN_LOCATION_ICON() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0x00FF00);
        return FontImage.createMaterial(FontImage.MATERIAL_LOCATION_ON, s).toEncodedImage();
    }

    public static EncodedImage BLUE_LOCATION_ICON() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0x0000FF);
        return FontImage.createMaterial(FontImage.MATERIAL_LOCATION_ON, s).toEncodedImage();
    }

    public static Image MASK_LOCATION_ICON() {
        Style s = new Style();
//        s.setBgTransparency(255);
        s.setBgColor(0);
        s.setFgColor(0xFFFFFF);

        return FontImage.createMaterial(FontImage.MATERIAL_NAVIGATION, s).toEncodedImage().rotate(180);
    }

    public static EncodedImage PROFILE_ICON() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0x0000FF);
        return FontImage.createMaterial(FontImage.MATERIAL_ACCOUNT_BOX, s).toEncodedImage();
    }

    public static EncodedImage CAR_ICON() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0x0000FF);
        return FontImage.createMaterial(FontImage.MATERIAL_DIRECTIONS_CAR, s).toEncodedImage();
    }
}
