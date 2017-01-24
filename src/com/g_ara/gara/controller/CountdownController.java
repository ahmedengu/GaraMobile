package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.ui.Container;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseObject;
import userclasses.StateMachine;

import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class CountdownController {
    public static void beforeCountdownForm(Container container, StateMachine stateMachine) {
        container.add(new Countdown(300, new CallbackController() {
            @Override
            public void done(Object... objects) {
                stateMachine.showDialog();
                try {
                    ParseObject object = ParseObject.fetch("TripRequest", ((ParseObject) data.get("active")).getObjectId());
                    Integer accept = object.getInt("accept");
                    stateMachine.hideDialog();
                    if (accept != 1) {
                        ToastBar.showErrorMessage("Your request got no replay from the driver or rejected, Please choose another driver!");
                        HomeController.CancelActiveRequest(object);
                    } else {
                        ToastBar.showErrorMessage("Your request got accepted!");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    ToastBar.showErrorMessage(e.getMessage());
                }
                stateMachine.showForm("Home", null);
            }
        }));
    }
}
