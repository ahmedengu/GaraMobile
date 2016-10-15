package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.TextArea;
import com.codename1.ui.URLImage;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseObject;
import com.parse4cn1.ParseQuery;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static userclasses.StateMachine.data;

/**
 * Created by ahmedengu.
 */
public class UserSearch {

    public static void searchAction(TextArea searchField, MultiList users, Resources resources) {
        try {
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereStartsWith("username", searchField.getText());
//            query.whereStartsWith("name", findSearchField().getText());
//            query.whereStartsWith("email", findSearchField().getText());

            List<ParseUser> results = query.find();

//            if (results.size() > 0) {
            ArrayList<Map<String, Object>> data = new ArrayList<>();

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Line1", results.get(i).getUsername());
                entry.put("Line2", results.get(i).getString("name"));
                EncodedImage placeholder = EncodedImage.createFromImage(resources.getImage("profile_icon.png"), false);
                String url = results.get(i).getParseFile("pic").getUrl();
                entry.put("icon", URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), url));
                entry.put("object", results.get(i));
                data.add(entry);
            }

            users.setModel(new DefaultListModel<>(data));
//            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void usersAction(StateMachine stateMachine, MultiList users) {
        try {
            Map<String, Object> itemAt = (Map<String, Object>) users.getSelectedItem();
            ParseUser object = (ParseUser) itemAt.get("object");

            List<ParseUser> parseUsers = new ArrayList<>();
            parseUsers.add(ParseUser.getCurrent());
            parseUsers.add(object);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Chat");
            query.whereContainsAll("members", parseUsers);
            List<ParseObject> results = query.find();

            ParseObject chat;
            if (results.size() > 0) {
                chat = results.get(0);
            } else {
                chat = ParseObject.create("Chat");
                chat.put("members", parseUsers);
                chat.save();
            }

            data.put("chat", chat);
            stateMachine.showForm("Conversion", null);
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }



}
