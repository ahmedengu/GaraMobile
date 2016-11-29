package com.g_ara.gara.controller;

import com.codename1.capture.Capture;
import com.codename1.components.ToastBar;
import com.codename1.io.Preferences;
import com.codename1.io.Storage;
import com.codename1.ui.*;
import com.codename1.ui.util.ImageIO;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ahmedengu.
 */
public class UserController {
    private static long last = 0l;

    public static void register(TextField username, TextField password, TextField name, TextField email, TextField mobile, Button pic, StateMachine stateMachine) {
        try {
            ParseUser user = ParseUser.create(username.getText(), password.getText());
            user.put("name", name.getText());
            user.put("email", email.getText());
            user.put("mobile", mobile.getText());
            user.signUp();

            if (user.isAuthenticated()) {
                ImageIO imgIO = ImageIO.getImageIO();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                imgIO.save(pic.getIcon(), out, ImageIO.FORMAT_JPEG, 1);
                byte[] ba = out.toByteArray();
                ParseFile file = new ParseFile(username.getText() + ".jpg", ba, "image/jpeg");
                file.save();
                user.put("pic", file);
                user.save();
                currentParseUserSave();
//                Storage.getInstance().writeObject("currentUser", user.asExternalizable());

//                Preferences.set("username", username.getText());
//                Preferences.set("password", password.getText());

                stateMachine.showForm("Home", null);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPic(Button pic) {
        String filePath = Capture.capturePhoto();
        if (filePath != null) {
            try {
                Image img = Image.createImage(filePath);
                pic.setIcon(img.scaledWidth(100));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void login(TextField username, TextField password, StateMachine stateMachine) {
        login(username.getText(), password.getText(), stateMachine);
    }

    public static void login(String username, String password, StateMachine stateMachine) {
        try {
            ParseUser user = ParseUser.create(username, password);
            user.login();
            if (user.isAuthenticated()) {
                stateMachine.showForm("Home", null);
                currentParseUserSave();
//                Storage.getInstance().writeObject("currentUser", user.asExternalizable());
//                Preferences.set("currentUser", user.getParseData().toString());
//                Preferences.set("username", username);
//                Preferences.set("password", password);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void saveUser(TextField username, TextField password, TextField mobile, Button pic, StateMachine stateMachine) {
        try {
            ParseUser user = ParseUser.getCurrent();
            user.setUsername(username.getText());
            if (password.getText().length() > 1)
                user.put("password", password.getText());
            user.put("mobile", mobile.getText());
            user.save();

            ImageIO imgIO = ImageIO.getImageIO();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imgIO.save(pic.getIcon(), out, ImageIO.FORMAT_JPEG, 1);
            byte[] ba = out.toByteArray();
            ParseFile file = new ParseFile(username.getText() + ".jpg", ba, "image/jpeg");
            file.save();
            user.put("pic", file);
            user.save();
            ToastBar.showErrorMessage("Success");

            stateMachine.back();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void beforeProffileForm(TextField name, TextField username, TextField password, TextField mobile, Button pic, TextField email) {
        ParseUser user = ParseUser.getCurrent();
        name.setText(user.getString("name"));
        username.setText(user.getUsername());
        mobile.setText(user.getString("mobile"));
        email.setText(user.getEmail());

        EncodedImage placeholder = EncodedImage.createFromImage(pic.getIcon(), false);
        String url = (user.getParseFile("pic") != null) ? user.getParseFile("pic").getUrl() : "http://www.aspirehire.co.uk/aspirehire-co-uk/_img/profile.svg";
        pic.setIcon(URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), url));

    }

    public static void resetPassword(TextField username) {
        if (username.getText().length() == 0) {
            ToastBar.showErrorMessage("You should enter your Email in the username field");
            return;
        }
        try {
            ParseUser.requestPasswordReset(username.getText());
            ToastBar.showErrorMessage("Instructions sent to " + username.getText());
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }


    public static void logout(StateMachine stateMachine) {
        Preferences.clearAll();
        try {
            ParseUser user = ParseUser.getCurrent();
            Storage.getInstance().deleteStorageFile("currentUser");
            user.logout();
            MapController.stopLocationListener();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
        stateMachine.showForm("Login", null);
    }

    public static boolean onStart() {

//        ExternalizableParseObject<ParseUser> currentUser = (ExternalizableParseObject<ParseUser>) Storage.getInstance().readObject("currentUser");
//        if (ParseUser.getCurrent() != null && ParseUser.getCurrent().isAuthenticated())
//            return true;
        return false;
    }

    public static void currentParseUserSave() throws ParseException {

        ParseUser current = ParseUser.getCurrent();
        current.save();
//        if (System.currentTimeMillis() - last > 5000) {
//            ParseObject trip = current.getParseObject("trip");
//            ParseObject tripRequest = current.getParseObject("tripRequest");
//
//            Object driver = null;
//            if (trip != null) {
//                driver = trip.get("driver");
//                trip.remove("driver");
//                trip.setDirty(false);
//            }
//
//            Object user = null;
//            if (tripRequest != null) {
//                user = tripRequest.get("user");
//                tripRequest.remove("user");
//                tripRequest.setDirty(false);
//            }
//            current.setDirty(false);
//            last = System.currentTimeMillis();
//            Storage.getInstance().writeObject("currentUser", current.asExternalizable());
//
//            if (trip != null && driver != null) {
//                trip.put("driver", driver);
//                trip.setDirty(false);
//
//            }
//
//            if (tripRequest != null && user != null) {
//                tripRequest.put("user", user);
//                tripRequest.setDirty(false);
//            }
//            current.setDirty(false);
//        }
    }


    public static ParseObject getUserEmptyObject() {
        return getParseEmptyObject(ParseUser.getCurrent());
    }

    public static ParseObject getParseEmptyObject(ParseObject parseObject) {
        ParseObject object = null;
        object = ParseObject.create(parseObject.getClassName());
        object.setObjectId(parseObject.getObjectId());
        object.setDirty(false);
        return object;
    }
}
