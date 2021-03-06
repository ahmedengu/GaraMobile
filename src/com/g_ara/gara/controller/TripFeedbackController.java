package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseObject;
import userclasses.StateMachine;

import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class TripFeedbackController {
    public static void beforeTripFeedbackForm(Slider rate, Button cancel, Button ok, Form f) {
        UserController.addUserSideMenu(f);
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
        FontImage.setMaterialIcon(ok, FontImage.MATERIAL_DONE);
        RatingWidget.createStarRankSlider(rate);
    }

    public static void okAction(StateMachine stateMachine, Slider rate, TextArea comment) {
        ParseObject object = (ParseObject) data.get("tripObject");

        object.put("rate", rate.getProgress());
        object.put("comment", comment.getText());
        try {
            showBlocking();
            object.save();
            hideBlocking();
            showForm("Home");
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void cancelAction() {
        showForm("Home");
    }
}
