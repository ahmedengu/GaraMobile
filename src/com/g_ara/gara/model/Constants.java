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

//    public static String GPLUS_CLIENT = "478278768173-p03m0mn78pdp79op1c83rak5rg5auc11.apps.googleusercontent.com";
//    public static String GPLUS_SECRET = "kGTosZIkwVA0OQPyex0ktWpS";
//
//    public static String GPLUS_REDIRECT = "https://www.codenameone.com/oauth2callback";


    public static String GOOGLE_QR = "https://chart.googleapis.com/chart?chs=150x150&cht=qr&chl=";

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

    public static EncodedImage CODE_ICON() {
        Style s = new Style();
        s.setBgTransparency(0);
        s.setFgColor(0x000000);

        return FontImage.createMaterial(FontImage.MATERIAL_CODE, s).toEncodedImage();
    }
}
