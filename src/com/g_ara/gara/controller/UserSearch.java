package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.parse4cn1.ParseException;
import com.parse4cn1.ParseQuery;
import com.parse4cn1.ParseUser;
import userclasses.StateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.g_ara.gara.controller.ChatController.getUserChat;
import static userclasses.StateMachine.hideBlocking;
import static userclasses.StateMachine.showBlocking;

/**
 * Created by ahmedengu.
 */
public class UserSearch {

    public static void searchAction(TextArea searchField, MultiList users, Resources resources) {
        try {
            showBlocking();
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.whereStartsWith("username", searchField.getText());
//            query.whereStartsWith("name", findSearchField().getText());
//            query.whereStartsWith("email", findSearchField().getText());

            List<ParseUser> results = query.find();

//            if (results.size() > 0) {
            ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Line1", results.get(i).getUsername());
                entry.put("Line2", results.get(i).getString("name"));
                EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayHeight() / 8, Display.getInstance().getDisplayHeight() / 8, 0xffffff), false);
                String url = results.get(i).getParseFile("pic").getUrl();
                entry.put("icon", URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), url));
                entry.put("object", results.get(i));
                data.add(entry);
            }
            hideBlocking();
            if (data.size() == 0) {
                ToastBar.showErrorMessage("No user found!");
            }
            users.setModel(new DefaultListModel<>(data));
//            }
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void usersAction(StateMachine stateMachine, MultiList users) {
            getUserChat((ParseUser) ((Map<String, Object>) users.getSelectedItem()).get("object"));
    }

    public static void beforeUserSearchForm(Form f, StateMachine stateMachine, Button search) {
        UserController.addUserSideMenu(f);
        FontImage.setMaterialIcon(search, FontImage.MATERIAL_SEARCH);
    }

}
