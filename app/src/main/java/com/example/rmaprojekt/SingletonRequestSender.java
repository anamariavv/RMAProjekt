package com.example.rmaprojekt;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public final class SingletonRequestSender {
    private static SingletonRequestSender instance;
    private static RequestQueue queue;
    private static Context context;

    interface RequestResult {
        void onSuccess(JSONObject result);
        void onError(VolleyError error);
    }

    public SingletonRequestSender(Context context) {
        SingletonRequestSender.context = context;

        queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
    }

    public static SingletonRequestSender getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonRequestSender(context);
        }
        return instance;
    }

    public static void sendRequest(JSONObject object,String url,RequestResult callback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url,object,
                response -> callback.onSuccess(response),
                error -> callback.onError(error));
        SingletonRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static JSONObject createJsonRequest(Map<String, String> params) {
        JSONObject object = new JSONObject();

        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                object.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }
}
