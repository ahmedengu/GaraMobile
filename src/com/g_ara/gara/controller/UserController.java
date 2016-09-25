package com.g_ara.gara.controller;

import com.codename1.capture.Capture;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ToastBar;
import com.codename1.io.Preferences;
import com.codename1.ui.*;
import com.codename1.ui.util.ImageIO;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseFile;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ahmedengu.
 */
public class UserController {
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
                Preferences.set("username", username.getText());
                Preferences.set("password", password.getText());

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
                Preferences.set("username", username);
                Preferences.set("password", password);
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
        String url = user.getParseFile("pic").getUrl();
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
            ParseUser.getCurrent().logout();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
        stateMachine.showForm("Login", null);
    }

    public static void onStart(StateMachine stateMachine) {
        Dialog dialog = new InfiniteProgress().showInifiniteBlocking();
        if (!(Preferences.get("username", "").equals("") && Preferences.get("password", "").equals("")))
            login(Preferences.get("username", ""), Preferences.get("password", ""), stateMachine);
        dialog.dispose();
    }
}
