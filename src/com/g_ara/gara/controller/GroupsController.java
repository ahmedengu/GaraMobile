package com.g_ara.gara.controller;

import com.codename1.components.FloatingActionButton;
import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
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

import static com.g_ara.gara.controller.UserController.getUserEmptyObject;
import static userclasses.StateMachine.hideBlocking;
import static userclasses.StateMachine.showBlocking;
import static userclasses.StateMachine.showForm;

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
//            if (results.size() > 0) {
            ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Line1", results.get(i).getParseObject("group").getString("domain"));
                entry.put("Line3", results.get(i).getBoolean("verified") ? "Verified" : "Check your email");
                entry.put("Line2", results.get(i).getString("email"));
                data.add(entry);
            }

            groups.setModel(new DefaultListModel<>(data));
//            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }


    public static void newGroup(TextField eMail, StateMachine stateMachine) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
            String email = eMail.getText();
            String domain = email.substring(email.lastIndexOf("@") + 1);
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
            groupUser.put("user", getUserEmptyObject());
            groupUser.put("verified", false);
            groupUser.put("email", email);

            groupUser.save();
            stateMachine.back();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void beforeGroupsForm(Form f, MultiList groups) {
        UserController.addUserSideMenu(f);
        groups.addPullToRefresh(new Runnable() {
            @Override
            public void run() {
                refreshGroups(groups);
            }
        });
        FloatingActionButton floatingActionButton = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        floatingActionButton.addActionListener(evt -> showForm("newGroup"));
        floatingActionButton.bindFabToContainer(f.getContentPane());
    }

    public static void postGroupsForm(MultiList groups) {
        showBlocking();
        refreshGroups(groups);
        hideBlocking();
    }

    public static void beforeNewGroupForm(Form f, StateMachine stateMachine, Button aNew) {
        UserController.addUserSideMenu(f);
        FontImage.setMaterialIcon(aNew, FontImage.MATERIAL_ADD);

    }
}
