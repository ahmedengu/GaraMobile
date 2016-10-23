package com.g_ara.gara.controller;

import com.codename1.components.ToastBar;
import com.codename1.ui.*;
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
import static userclasses.StateMachine.showForm;

/**
 * Created by ahmedengu.
 */
public class ChatController {

    public static void beforeConversionForm(Container messages) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
            query.whereEqualTo("chat", (ParseObject) data.get("chat"));
            List<ParseObject> results = query.find();
            for (int i = 0; i < results.size(); i++) {
                addMessage(results.get(i), messages);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void addMessage(ParseObject results, Container messages) {
        addMessage(results, false, messages);
    }

    public static void addMessage(ParseObject results, boolean repaint, Container messages) {
        Label m = new Label(results.getString("message"));
        messages.add(m);
        if (repaint)
            messages.repaint();
    }

    public static void beforeChatForm(MultiList chat, Resources stateMachine) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Chat");
            query.include("members");
            query.whereEqualTo("members", ParseUser.getCurrent());
            List<ParseObject> results = query.find();

//            if (results.size() > 0) {
            ArrayList<Map<String, Object>> data = new ArrayList<>();

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Line1", ((ParseUser) results.get(i).getList("members").get(1)).getUsername());
                EncodedImage placeholder = EncodedImage.createFromImage(stateMachine.getImage("profile_icon.png"), false);
                String url = ((ParseUser) results.get(i).getList("members").get(1)).getParseFile("pic").getUrl();
                entry.put("icon", URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), url));

                entry.put("object", results.get(i));
                data.add(entry);
            }

            chat.setModel(new DefaultListModel<>(data));
//            }

        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void chatAction(StateMachine stateMachine, MultiList chat) {
        Map<String, Object> itemAt = (Map<String, Object>) chat.getSelectedItem();
        ParseObject object = (ParseObject) itemAt.get("object");
        data.put("chat", object);
        stateMachine.showForm("Conversion", null);
    }

    public static void conversionSendActcion(TextField messageField, Container messages) {
        try {
            ParseObject message = ParseObject.create("Message");
            message.put("message", messageField.getText());
            message.put("from", ParseUser.getCurrent());
            message.put("chat", (ParseObject) data.get("chat"));
            message.save();
            messageField.setText("");
            addMessage(message, true, messages);
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void getUserChat(ParseUser object) throws ParseException {

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
        showForm("Conversion");
    }
}
