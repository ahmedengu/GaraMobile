package com.g_ara.gara.controller;

import com.codename1.capture.Capture;
import com.codename1.components.ToastBar;
import com.codename1.io.Preferences;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.util.ImageIO;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseFile;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class UserController {
    private static long last = 0l;
    static String filePath;

    public static void register(TextField username, TextField password, TextField name, TextField email, TextField mobile, Button pic, StateMachine stateMachine) {
        if (name.getText().length() == 0 || email.getText().length() == 0 || mobile.getText().length() == 0 || username.getText().length() == 0 || password.getText().length() == 0) {
            ToastBar.showErrorMessage("Please fill all the fields");
            return;
        }
        try {
            ParseUser user = ParseUser.create(username.getText(), password.getText());
            user.put("name", name.getText());
            user.put("email", email.getText());
            user.put("mobile", mobile.getText());
            showBlocking();
            user.signUp();
            if (user.isAuthenticated()) {
                Image img = Image.createImage(filePath);
                ImageIO imgIO = ImageIO.getImageIO();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                imgIO.save(img, out, ImageIO.FORMAT_JPEG, 1);
                byte[] ba = out.toByteArray();
                ParseFile file = new ParseFile(username.getText() + ".jpg", ba, "image/jpeg");
                file.save();

                user.put("pic", file);
                user.save();
                currentParseUserSave();
//                Storage.getInstance().writeObject("currentUser", user.asExternalizable());

//                Preferences.set("username", username.getText());
//                Preferences.set("password", password.getText());
                Preferences.set("sessionToken", user.getSessionToken());
                hideBlocking();
                stateMachine.showForm("Home", null);
            }
        } catch (ParseException e) {
            hideBlocking();
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void addPic(Button pic) {
        if (Dialog.show("Add Image", "Choose the image location", "Camera", "Gallery")) {
            filePath = Capture.capturePhoto();
            if (filePath != null) {
                try {
                    Image img = Image.createImage(filePath);
                    pic.setIcon(img.scaledHeight(pic.getHeight()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Display.getInstance().openGallery(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (ev != null && ev.getSource() != null) {
                        filePath = (String) ev.getSource();
                        if (filePath != null) {
                            Display.getInstance().callSerially(() -> {
                                try {
                                    Image img = Image.createImage(filePath);
                                    pic.setIcon(img.scaledHeight(pic.getHeight()));
                                    pic.repaint();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                }
            }, Display.GALLERY_IMAGE);
        }
    }


    public static void login(TextField username, TextField password, StateMachine stateMachine) {
        login(username.getText(), password.getText(), stateMachine);
    }

    public static void login(String username, String password, StateMachine stateMachine) {
        if (username.length() == 0 || password.length() == 0) {
            ToastBar.showErrorMessage("Username & Password are required");
            return;
        }
        try {
            ParseUser user = ParseUser.create(username, password);
            showBlocking();
            user.login();
            if (user.isAuthenticated()) {
                Preferences.set("sessionToken", user.getSessionToken());
                hideBlocking();
                stateMachine.showForm("Home", null);
                currentParseUserSave();
//                Storage.getInstance().writeObject("currentUser", user.asExternalizable());
//                Preferences.set("currentUser", user.getParseData().toString());
//                Preferences.set("username", username);
//                Preferences.set("password", password);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void saveUser(TextField username, TextField password, TextField mobile, Button pic, StateMachine stateMachine) {
        try {
            showBlocking();
            ParseUser user = ParseUser.getCurrent();
            user.setUsername(username.getText());
            if (password.getText().length() > 1)
                user.put("password", password.getText());
            user.put("mobile", mobile.getText());
            user.save();

            Image img = Image.createImage(filePath);
            ImageIO imgIO = ImageIO.getImageIO();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imgIO.save(img, out, ImageIO.FORMAT_JPEG, 1);
            byte[] ba = out.toByteArray();
            ParseFile file = new ParseFile(username.getText() + ".jpg", ba, "image/jpeg");
            file.save();

            user.put("pic", file);
            user.save();
            hideBlocking();
            showForm("Home");
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        } catch (IOException e) {
            hideBlocking();
            e.printStackTrace();
        }
    }

    public static void beforeProfileForm(TextField name, TextField username, TextField password, TextField mobile, Button pic, TextField email, Form f, StateMachine stateMachine, Button save) {
        UserController.addUserSideMenu(f);
        FontImage.setMaterialIcon(save, FontImage.MATERIAL_SAVE);

        ParseUser user = ParseUser.getCurrent();
        name.setText(user.getString("name"));
        username.setText(user.getUsername());
        mobile.setText(user.getString("mobile"));
        email.setText(user.getEmail());

        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayHeight() / 8, Display.getInstance().getDisplayHeight() / 8, 0xffffff), false);
        String url = (user.getParseFile("pic") != null) ? user.getParseFile("pic").getUrl() : "https://static.xx.fbcdn.net/rsrc.php/v1/yi/r/odA9sNLrE86.jpg";
        pic.setIcon(URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), url));
    }

    public static void resetPassword() {
        Dialog dialog = new Dialog("Reset Password");
        dialog.setLayout(new BorderLayout());
        TextField email = new TextField();
        email.setHint("Email");
        dialog.add(BorderLayout.CENTER, email);
        Button cancel = new Button("Cancel");
        cancel.addActionListener(evt -> dialog.dispose());
        Button reset = new Button("Reset");
        reset.addActionListener(evt -> {
            if (email.getText().length() == 0) {
                dialog.dispose();
                ToastBar.showErrorMessage("Email is required");
                return;
            }
            try {
                ParseUser.requestPasswordReset(email.getText());
                dialog.dispose();
                ToastBar.showErrorMessage("Instructions sent to " + email.getText());
            } catch (ParseException e) {
                e.printStackTrace();
                dialog.dispose();
                ToastBar.showErrorMessage(e.getMessage());
            }
        });
        dialog.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, cancel, reset));
        dialog.show();
    }


    public static void logout() {
        Preferences.clearAll();
        showForm("Login");
        showBlocking();
        try {
            MapController.stopLocationListener();
            ParseUser.getCurrent().logout();
            ParseLiveQuery.close();
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideBlocking();
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
        ParseObject parseEmptyObject = getParseEmptyObject(ParseUser.getCurrent());
        parseEmptyObject.put("pic", ParseUser.getCurrent().getParseFile("pic"));
        parseEmptyObject.setDirty(false);
        return parseEmptyObject;
    }

    public static ParseObject getParseEmptyObject(ParseObject parseObject) {
        ParseObject object = null;
        object = ParseObject.create(parseObject.getClassName());
        object.setObjectId(parseObject.getObjectId());
        object.setDirty(false);
        return object;
    }

    public static void addUserSideMenu(Form f) {
        ParseUser user = ParseUser.getCurrent();
        EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayHeight() / 8, Display.getInstance().getDisplayHeight() / 8, 0xffffff), false);
        String url = (user.getParseFile("pic") != null) ? user.getParseFile("pic").getUrl() : "https://static.xx.fbcdn.net/rsrc.php/v1/yi/r/odA9sNLrE86.jpg";
        Button profilePicBtn = new Button("  " + user.getString("name"));
        profilePicBtn.setUIID("SideMenuTitle");
        profilePicBtn.setIcon(URLImage.createToStorage(placeholder, "side_" + url.substring(url.lastIndexOf("/") + 1), url));
        profilePicBtn.addActionListener(evt -> showForm("Profile"));
        Container sidemenuTop = BorderLayout.center(profilePicBtn);
        sidemenuTop.setUIID("SidemenuTop");


        Toolbar tb = f.getToolbar();
        if (tb == null) {
            String title = f.getTitle();
            tb = new Toolbar();
            f.setToolbar(tb);
            tb.setTitle(title);
        }

        tb.addComponentToSideMenu(sidemenuTop);

        if (f.getName() == null || !f.getName().equals("UserSearch"))
            tb.addMaterialCommandToRightBar("", FontImage.MATERIAL_SEARCH, e -> showForm("UserSearch"));

        tb.addMaterialCommandToSideMenu("Home", FontImage.MATERIAL_HOME, e -> showForm("Home"));
        tb.addMaterialCommandToSideMenu("Requests", FontImage.MATERIAL_NOTIFICATIONS, e -> showForm("Requests"));
        tb.addMaterialCommandToSideMenu("History", FontImage.MATERIAL_HISTORY, e -> showForm("History"));
        tb.addMaterialCommandToSideMenu("Chat", FontImage.MATERIAL_CHAT, e -> showForm("Chat"));
        tb.addMaterialCommandToSideMenu("Groups", FontImage.MATERIAL_GROUP, e -> showForm("Groups"));
        tb.addMaterialCommandToSideMenu("Cars", FontImage.MATERIAL_DIRECTIONS_CAR, e -> showForm("Cars"));
        tb.addMaterialCommandToSideMenu("Profile", FontImage.MATERIAL_EDIT, e -> showForm("Profile"));
        tb.addMaterialCommandToSideMenu("Settings", FontImage.MATERIAL_SETTINGS, e -> showForm("Settings"));
        tb.addMaterialCommandToSideMenu("Logout", FontImage.MATERIAL_EXIT_TO_APP, e -> logout());
    }
}
