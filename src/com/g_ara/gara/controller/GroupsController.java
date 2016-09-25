package com.g_ara.gara.controller;

import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseQuery;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmedengu.
 */
public class GroupsController {
    public static void refreshGroups(MultiList groups) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("GroupUser");
            query.include("group");

            query.whereEqualTo("user", ParseUser.getCurrent());
            List<ParseObject> results = query.find();
            if (results.size() > 0) {
                ArrayList<Map<String, Object>> data = new ArrayList<>();

                for (int i = 0; i < results.size(); i++) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("Line1", results.get(i).getParseObject("group").getString("domain"));
                    entry.put("Line2", results.get(i).getBoolean("verified") ? "Verified" : "Check your email");
                    data.add(entry);
                }

                groups.setModel(new DefaultListModel<>(data));
            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }


    public static void newGroup(TextField eMail, StateMachine stateMachine) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
            String email = eMail.getText();
            String domain = email.substring(email.indexOf("@") + 1);
            query.whereEqualTo("domain", domain);
            List<ParseObject> results = query.find();
            ParseObject group;

            if (results.size() > 0) {
                group = results.get(0);
            } else {
                group = ParseObject.create("Group");
                group.put("domain", domain);
                group.save();
            }

            ParseObject groupUser = ParseObject.create("GroupUser");
            groupUser.put("group", group);
            groupUser.put("user", ParseUser.getCurrent());
            groupUser.put("verified", false);
            groupUser.save();
            stateMachine.back();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void beforeGroupsForm(Form f, MultiList groups) {
        refreshGroups(groups);
        f.addPullToRefresh(() -> refreshGroups(groups));
    }
}
