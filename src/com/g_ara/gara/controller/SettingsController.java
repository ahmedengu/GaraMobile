package com.g_ara.gara.controller;

import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import com.codename1.io.Preferences;
import com.codename1.messaging.Message;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.g_ara.gara.model.Constants;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import static userclasses.StateMachine.hideBlocking;
import static userclasses.StateMachine.showBlocking;

/**
 * Created by ahmedengu.
 */
public class SettingsController {
    public static void analyticsState(boolean analytics, MultiButton components) {
        components.setSelected(analytics);
    }

    public static void analyticsAciton(MultiButton analytics) {
        boolean selected = !Preferences.get("Analytics", true);
        analyticsState(selected, analytics);
        Preferences.set("Analytics", selected);
    }

    public static void websiteAction() {
        Display.getInstance().execute(Constants.WEBSITE);
    }

    public static void rateAction() {
        new RatingWidget(RatingWidget.getAppstoreURL(), Constants.EMAIL);
    }

    public static void feedbackAction() {
        Message m = new Message("Body of message");
        Display.getInstance().sendMessage(new String[]{Constants.EMAIL}, "[" + Display.getInstance().getPlatformName() + "] Feedback ", m);
    }

    public static void reportAction() {
        Dialog reportDialog = new Dialog("Report");
        reportDialog.setLayout(new BorderLayout());

        TextArea infoText = new TextArea();
        infoText.setHint("More Information");
        infoText.setUIID("GroupElementOnly");
        reportDialog.add(BorderLayout.CENTER, infoText);

        Button cancel = new Button("Cancel");
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
        cancel.addActionListener(evt1 -> reportDialog.dispose());


        Button reportIt = new Button("Report");
        FontImage.setMaterialIcon(reportIt, FontImage.MATERIAL_REPORT);
        reportIt.addActionListener(evt1 -> {
            try {
                showBlocking();
                ParseObject reportObject = ParseObject.create("Reports");
                reportObject.put("info", infoText.getText());
                reportObject.put("user", ParseUser.getCurrent());
                reportObject.save();
                hideBlocking();
                reportDialog.dispose();
            } catch (ParseException e) {
                e.printStackTrace();
                hideBlocking();
                ToastBar.showErrorMessage(e.getMessage());
            }
        });

        reportDialog.add(BorderLayout.SOUTH, GridLayout.encloseIn(2, cancel, reportIt));
        reportDialog.show();
    }

    public static void beforeSettingsForm(Form f, Component rate, Button feedback, Button website, MultiButton analytics, StateMachine stateMachine, Button report) {
        UserController.addUserSideMenu(f);
        FontImage.setMaterialIcon((Button) rate, FontImage.MATERIAL_STAR);
        FontImage.setMaterialIcon(feedback, FontImage.MATERIAL_FEEDBACK);
        FontImage.setMaterialIcon(website, FontImage.MATERIAL_LINK);
        FontImage.setMaterialIcon(report, FontImage.MATERIAL_REPORT);

        analyticsState(Preferences.get("Analytics", true), analytics);
    }
}
