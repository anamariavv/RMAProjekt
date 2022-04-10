package com.example.rmaprojekt;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public final class SingletonRequestSender {
    private static SingletonRequestSender instance;
    private static RequestQueue queue;
    private static Context context;

    interface RequestResult {
        JSONObject onSuccess(JSONObject result);
        VolleyError onError(VolleyError error);
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

    public static void sendRequest(JSONObject object,RequestResult callback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                context.getResources().getString(R.string.request_url),object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
               },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                });
        SingletonRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
