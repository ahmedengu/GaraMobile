package com.g_ara.gara.controller;

import com.codename1.maps.Coord;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.parse4cn1.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codename1.io.Util.split;
import static com.g_ara.gara.controller.MapController.draw2MarkerMap;
import static userclasses.StateMachine.hideBlocking;
import static userclasses.StateMachine.showBlocking;

/**
 * Created by ahmedengu.
 */
public class HistoryController {
    public static void refreshHistory(MultiList historyList) {
        try {
            ParseQuery tripRequest = ParseQuery.getQuery("TripRequest");
            tripRequest.whereEqualTo("user", ParseUser.getCurrent());
            List<ParseObject> results = tripRequest.find();

            ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Line1", results.get(i).getCreatedAt().toString());
                entry.put("Line3", results.get(i).getInt("accept") == 1 ? "Accepted" : "Not Accepted");
                entry.put("object", results.get(i));

                data.add(entry);
            }

            ParseQuery trip = ParseQuery.getQuery("Trip");
            tripRequest.whereEqualTo("driver", ParseUser.getCurrent());
            results = trip.find();
            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Line1", results.get(i).getCreatedAt().toString());
                entry.put("Line3", "Driver");
                entry.put("object", results.get(i));

                data.add(entry);
            }
            historyList.setModel(new DefaultListModel<>(data));
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }


    public static void beforeHistoryForm(Form f, MultiList historyList) {
        UserController.addUserSideMenu(f);
        historyList.addPullToRefresh(new Runnable() {
            @Override
            public void run() {
                refreshHistory(historyList);
            }
        });
    }

    public static void postHistoryForm(MultiList historyList) {
        showBlocking();
        refreshHistory(historyList);
        hideBlocking();
    }


    public static void showHistoryTripOnClick(ActionEvent event) {
        final Form f = Display.getInstance().getCurrent();
        ParseObject item = (ParseObject) ((Map<String, Object>) ((MultiList) event.getSource()).getSelectedItem()).get("object");
        Form form = new Form("Previous trip");
        UserController.addUserSideMenu(form);
        form.setLayout(new BorderLayout());
        Button cancel = new Button("Cancel");
        cancel.addActionListener(evt -> f.show());
        cancel.setUIID("ToggleButtonOnly");
        FontImage.setMaterialIcon(cancel, FontImage.MATERIAL_CANCEL);
        form.add(BorderLayout.SOUTH, cancel);
        String[] strings = split(item.getString("from"), ",");

        ParseGeoPoint to = item.getParseGeoPoint("to");
        draw2MarkerMap(new Coord(Double.parseDouble(strings[0]), Double.parseDouble(strings[1])), new Coord(to.getLatitude(), to.getLongitude()), form);
        form.show();
    }

}
