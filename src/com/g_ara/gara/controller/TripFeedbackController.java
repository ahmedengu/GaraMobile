package com.g_ara.gara.controller;

import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Slider;
import com.codename1.ui.TextArea;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseObject;
import userclasses.StateMachine;

import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class TripFeedbackController {
    public static void beforeTripFeedbackForm(Slider rate, Button cancel, Button ok) {
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
        FontImage.setMaterialIcon(ok, FontImage.MATERIAL_DONE);
        RatingWidget.createStarRankSlider(rate);
    }

    public static void okAction(StateMachine stateMachine, Slider rate, TextArea comment) {
        ParseObject object = (ParseObject) data.get("tripObject");

        object.put("rate", rate.getProgress());
        object.put("comment", comment.getText());
        try {
            object.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        stateMachine.showForm("Home", null);
    }

    public static void cancelAction(StateMachine stateMachine) {
        stateMachine.showForm("Home", null);
    }
}
