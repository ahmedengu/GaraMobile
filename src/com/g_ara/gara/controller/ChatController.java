package com.g_ara.gara.controller;

import ca.weblite.codename1.json.JSONException;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
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
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class ChatController {
    static ParseLiveQuery conversationLive;
    static ParseUser toUser;

    public static void beforeConversionForm(Container messages, Form f, StateMachine stateMachine, Button send, Container container1) {
        UserController.addUserSideMenu(f);
        if (ParseUser.getCurrent().get("trip") != null && MapController.getVelocity() >= 5) {
            container1.setHidden(true);
        }
        FontImage.setMaterialIcon(send, FontImage.MATERIAL_SEND);
        messages.addPullToRefresh(() -> refreshConversation(messages));

    }

    public static void postConversionForm(Container messages) {
        showBlocking();
        refreshConversation(messages);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.include("from");
        ParseObject chat = (ParseObject) data.get("chat");
        query.whereEqualTo("chat", chat);
        List<ParseUser> members = chat.getList("members");
        for (int i = 0; i < members.size(); i++) {
            if (!members.get(i).getObjectId().equals(ParseUser.getCurrent().getObjectId())) {
                toUser = members.get(i);
                break;
            }
        }
        try {
            conversationLive = new ParseLiveQuery(query) {
                @Override
                public void event(String op, int requestId, ParseObject object) {
                    if (op.equals("create")) {
                        Display.getInstance().callSerially(() -> {
                            addMessage(object, true, messages);
                        });
                    }
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        hideBlocking();
        if (ParseUser.getCurrent().get("trip") != null && MapController.getVelocity() >= 5) {
            showDelayedToastBar("Chat is disabled while driving");
        }
    }

    public static void refreshConversation(final Container messages) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
            query.include("from");
            query.whereEqualTo("chat", (ParseObject) data.get("chat"));
            query.setLimit(1000);

            List<ParseObject> results = query.find();
            messages.removeAll();
            for (int i = 0; i < results.size(); i++) {
                addMessage(results.get(i), messages);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void addMessage(ParseObject results, Container messages) {
        addMessage(results, false, messages);
    }

    public static void addMessage(ParseObject results, boolean repaint, Container messages) {
//        Label m = new Label(results.getString("message"));
//        messages.add(m);
        SpanLabel t = new SpanLabel(results.getString("message"));
//        String url = results.getParseObject("from").getParseFile("pic").getUrl();
//        URLImage image = URLImage.createToStorage(Constants.PROFILE_ICON(), "chat_" + url.substring(url.lastIndexOf("/") + 1), url);
//        t.setIcon(image);

        if (results.getParseObject("from").getObjectId().equals(ParseUser.getCurrent().getObjectId())) {
            t.setTextUIID("you");
            t.setTextBlockAlign(Component.RIGHT);
            t.setIconPosition(BorderLayout.EAST);
        } else {
            t.setTextUIID("him");
            t.setTextBlockAlign(Component.LEFT);
            t.setIconPosition(BorderLayout.WEST);
        }
        messages.addComponent(0, t);
        t.setY(messages.getHeight());
        t.setWidth(messages.getWidth());
        t.setHeight(40);
        if (repaint) {
            messages.animateLayoutAndWait(300);
            messages.scrollComponentToVisible(t);
            messages.repaint();
        }
    }

    public static void beforeChatForm(MultiList chat, Form f) {
        UserController.addUserSideMenu(f);
        chat.addPullToRefresh(() -> refreshChatForm(chat));
        FloatingActionButton floatingActionButton = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        floatingActionButton.addActionListener(evt -> showForm("UserSearch"));
        floatingActionButton.bindFabToContainer(f.getContentPane());
    }

    public static void postChatForm(MultiList chat, Form f) {
        showBlocking();
        refreshChatForm(chat);
        hideBlocking();
        if (ParseUser.getCurrent().get("trip") != null && MapController.getVelocity() >= 5) {
            showDelayedToastBar("Chat is disabled while driving");
        }
    }

    public static void refreshChatForm(MultiList chat) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Chat");
            query.include("members");
            query.whereEqualTo("members", ParseUser.getCurrent());
            List<ParseObject> results = query.find();

//            if (results.size() > 0) {
            ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                List<ParseUser> members = results.get(i).getList("members");
                for (int j = 0; j < members.size(); j++) {
                    if (members.get(j).getObjectId().equals(ParseUser.getCurrent().getObjectId()))
                        continue;
                    entry.put("Line1", (members.get(j)).getUsername());
                    EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayHeight() / 8, Display.getInstance().getDisplayHeight() / 8, 0xffffff), false);
                    String url = (members.get(j)).getParseFile("pic").getUrl();
                    entry.put("icon", URLImage.createToStorage(placeholder, "chat_" + url.substring(url.lastIndexOf("/") + 1), url));
                }

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
        showForm("Conversion");
    }

    public static void conversionSendAction(TextArea messageField, Container messages) {
        if (messageField.getText().length() == 0)
            return;
        try {
            ParseObject message = ParseObject.create("Message");
            message.put("message", messageField.getText());
            message.put("from", getUserEmptyObject());
            message.put("chat", (ParseObject) data.get("chat"));
            message.put("to", toUser);
//            addMessage(message, true, messages);
            messageField.setText("");
            message.save();
        } catch (ParseException e) {
            e.printStackTrace();
            ToastBar.showErrorMessage(e.getMessage());
        }
    }

    public static void getUserChat(ParseUser object) {
        try {
            showBlocking();
            List<ParseObject> parseUsers = new ArrayList<>();
            parseUsers.add(getUserEmptyObject());
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
            hideBlocking();
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        }
        showForm("Conversion");
    }

    public static void onConversationExit() {
        if (conversationLive != null)
            try {
                conversationLive.unsubscribe();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
