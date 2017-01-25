package com.g_ara.gara.controller;

import ca.weblite.codename1.json.JSONException;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
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

import static com.codename1.ui.Display.SOUND_TYPE_INFO;
import static com.g_ara.gara.controller.UserController.getUserEmptyObject;
import static userclasses.StateMachine.data;
import static userclasses.StateMachine.showForm;

/**
 * Created by ahmedengu.
 */
public class ChatController {
    static ParseLiveQuery conversationLive;
    static ParseUser toUser;

    public static void beforeConversionForm(Container messages, Form f, StateMachine stateMachine, Button send) {
        UserController.addUserSideMenu(f, stateMachine);
        FontImage.setMaterialIcon(send, FontImage.MATERIAL_SEND);

        try {
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

            List<ParseObject> results = query.find();
            for (int i = 0; i < results.size(); i++) {
                addMessage(results.get(i), messages);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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
            if (repaint) {
                Display.getInstance().playBuiltinSound(SOUND_TYPE_INFO);
            }
        }
        messages.add(t);
        t.setY(messages.getHeight());
        t.setWidth(messages.getWidth());
        t.setHeight(40);
        if (repaint) {
            messages.animateLayoutAndWait(300);
            messages.scrollComponentToVisible(t);
            messages.repaint();
        }
    }

    public static void beforeChatForm(MultiList chat, Resources stateMachine, Form f, StateMachine machine) {
        UserController.addUserSideMenu(f, machine);
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Chat");
            query.include("members");
            query.whereEqualTo("members", ParseUser.getCurrent());
            List<ParseObject> results = query.find();

//            if (results.size() > 0) {
            ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Line1", ((ParseUser) results.get(i).getList("members").get(1)).getUsername());
                EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayHeight() / 8, Display.getInstance().getDisplayHeight() / 8, 0xffffff), false);
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

    public static void conversionSendAction(TextArea messageField, Container messages) {
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

    public static void getUserChat(ParseUser object) throws ParseException {

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
        showForm("Conversion");
    }

    public static void onConversationExit() {
        if (conversationLive != null)
            try {
                conversationLive.unsubscribe();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    public static void postConversionForm(Container messages) {
        if (messages.getComponentCount() > 0)
            messages.scrollComponentToVisible(messages.getComponentAt(messages.getComponentCount() - 1));
    }
}
