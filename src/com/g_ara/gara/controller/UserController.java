package com.g_ara.gara.controller;

import com.codename1.capture.Capture;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
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
                pic.setIcon(img);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void login(TextField username, TextField password, StateMachine stateMachine) {
        ParseUser user = null;
        try {
            user = ParseUser.create(username.getText(), password.getText());
            user.login();
            if (user != null && user.isAuthenticated())
                stateMachine.showForm("Home", null);
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

}
