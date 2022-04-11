package com.example.rmaprojekt;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

//creates json objects for SingletonRequestSender class
public class MyRequest {
    public org.json.JSONObject requestObject;
    private Map<String, String> params;
    private String api;
    private String server;

    public MyRequest(String server, String api, Map<String,String> params) {
        this.server = server;
        this.api = api;
        this.params = params;
    }

    public MyRequest(Map<String, String> params) {
        this(null, null, params);
    }

    public JSONObject createJsonRequest() {
        JSONObject object = new JSONObject();

        try {
            if(server != null && api != null) {
                object.put("server", server);
                object.put("api", api);
            }
            for(Map.Entry<String, String> entry : params.entrySet()) {
                object.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
