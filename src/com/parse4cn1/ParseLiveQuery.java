package com.parse4cn1;

import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.io.websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmedengu.
 */
public abstract class ParseLiveQuery {
    private int requestId;
    private static String liveQueryServerURL;
    private final static String OP = "op";
    private static WebSocket webSocket;
    private static List<ParseLiveQuery> lQuerys = new ArrayList<>();
    private JSONObject query;

    public abstract void event(String op, int requestId, ParseObject object);

    public abstract void error(String op, int code, String error, boolean reconnect);

    public abstract void onWebsocketOpen();

    public abstract void onWebsocketClose(int i, String s);

    public abstract void onWebsocketError(Exception e);

    public ParseLiveQuery(ParseQuery query) throws JSONException, ParseException {
        this(query.encode());
    }

    public ParseLiveQuery(JSONObject query) throws JSONException, ParseException {
        this.query = query;
        lQuerys.add(this);
        requestId = lQuerys.indexOf(this);
        String sData = getSubscribe();
        if (webSocket == null)
            init(sData);
        else
            webSocket.send(sData);
    }

    private void init(String sData) throws ParseException {
        webSocket = new WebSocket(getLiveQueryServerURL()) {

            @Override
            protected void onOpen() {
                this.send(getConnect());
                onWebsocketOpen();
            }

            @Override
            protected void onClose(int i, String s) {
                onWebsocketClose(i, s);
            }

            @Override
            protected void onMessage(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    String operation = json.getString(OP);

                    switch (operation) {
                        case "connected":
                            this.send(sData);
                            break;
                        case "subscribed":
                            break;
                        case "unsubscribed":
                            break;
                        case "error":
                            error(json.getString(OP), json.getInt("code"), json.getString("error"), json.getBoolean("reconnect"));
                            break;
                        default:
                            JSONObject data = json.getJSONObject("object");
                            ParseObject parseObject = ParseObject.create(data.getString("className"));
                            parseObject.setData(data);
                            event(json.getString(OP), json.getInt("requestId"), parseObject);
                            break;
                    }
                } catch (JSONException e) {
                    onError(e);
                }
            }

            @Override
            protected void onMessage(byte[] bytes) {
            }

            @Override
            protected void onError(Exception e) {
                System.out.println(e);
                onWebsocketError(e);
            }
        };

        webSocket.connect();
    }


    private String getConnect() {
        JSONObject output = new JSONObject();
        try {
            output.put(OP, "connect");
            if (ParseUser.getCurrent() != null)
                output.put("sessionToken", ParseUser.getCurrent().getSessionToken());
            output.put("clientKey", Parse.getClientKey());
            output.put("applicationId", Parse.getApplicationId());
        } catch (JSONException ex) {
        }
        return output.toString();
    }


    public String getSubscribe() throws JSONException {
        JSONObject output = new JSONObject();
        output.put(OP, "subscribe");
        output.put("requestId", this.requestId);

        JSONObject q = new JSONObject();
        q.put("className", query.getString("className"));
        q.put("where", query.get("where"));
        if (query.has("keys"))
            q.put("fields", query.get("keys"));
        if (query.has("fields"))
            q.put("fields", query.get("fields"));

        output.put("query", q);
        if (ParseUser.getCurrent() != null)
            output.put("sessionToken", ParseUser.getCurrent().getSessionToken());
        return output.toString();
    }

    public void unsubscribe() throws JSONException {
        JSONObject output = new JSONObject();
        output.put(OP, "unsubscribe");
        output.put("requestId", requestId);
        webSocket.send(output.toString());
    }

    public static void close() {
        webSocket.close();
    }

    public static String getLiveQueryServerURL() {
        if (liveQueryServerURL == null) {
            String endpoint = Parse.getApiEndpoint();
            String portcol = (endpoint.indexOf("https") == 0) ? "wss" : "ws";
            liveQueryServerURL = portcol + ((endpoint.indexOf("https") == 0) ? endpoint.substring(5) : endpoint.substring(4));
        }
        return liveQueryServerURL;
    }

    public int getRequestId() {
        return requestId;
    }

    public JSONObject getQuery() {
        return query;
    }
}


