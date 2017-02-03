package com.g_ara.gara.controller;

import ca.weblite.codename1.json.JSONException;
import com.codename1.components.ToastBar;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseQuery;

import static com.g_ara.gara.model.Constants.COUNTDOWN_TIMER;
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class CountdownController {
    static ParseLiveQuery countdownLiveQuery;

    public static void beforeCountdownForm(Container container, Form f) {
        UserController.addUserSideMenu(f);
        container.add(new Countdown(COUNTDOWN_TIMER, new CallbackController() {
            @Override
            public void done(Object... objects) {
                Display.getInstance().callSerially(() -> {
                    if (countdownLiveQuery != null) {
                        showBlocking();
                        exitCountdownForm();
                        try {
                            ParseObject object = ParseObject.fetch("TripRequest", ((ParseObject) data.get("active")).getObjectId());
                            handleUpdate(object);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            ToastBar.showErrorMessage(e.getMessage());
                        }
                    }
                });
            }
        }));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TripRequest");
        query.whereEqualTo("objectId", ((ParseObject) data.get("active")).getObjectId());
        try {
            countdownLiveQuery = new ParseLiveQuery(query) {
                @Override
                public void event(String op, int requestId, ParseObject object) {
                    Display.getInstance().callSerially(() -> {
                        if (op.equals("update")) {

                            LocalNotification n = new LocalNotification();
                            n.setId("Home");
                            n.setAlertBody((object.getInt("accept") != 1) ? "Your request got no replay from the driver or rejected!" : "Your request got accepted!");
                            n.setAlertTitle("Gara | Driver Reply");
                            n.setAlertImage("images-logo (3).png");

                            Display.getInstance().scheduleLocalNotification(
                                    n,
                                    System.currentTimeMillis() + 30,
                                    LocalNotification.REPEAT_NONE
                            );
                            exitCountdownForm();
                            handleUpdate(object);
                        }
                    });
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void handleUpdate(ParseObject object) {
        Integer accept = object.getInt("accept");
        if (accept != 1) {
            HomeController.CancelActiveRequest(object);
            showDelayedToastBar("Your request got no replay from the driver or rejected, Please choose another driver!");
        } else {
            showDelayedToastBar("Your request got accepted!");
        }
        if (Display.getInstance().getCurrent().getName() != null && Display.getInstance().getCurrent().getName().equals("Countdown"))
            showForm("Home");
    }

    public static void exitCountdownForm() {
        try {
            if (countdownLiveQuery != null)
                countdownLiveQuery.unsubscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
        countdownLiveQuery = null;
    }
}
