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

    public interface RequestResult {
        String onSuccess(String result);
        VolleyError onError(VolleyError error);
    }

    public SingletonRequestSender(Context context) {
        this.context = context;

        queue = SingletonRequestQueue.getInstance(context).getRequestQueue();
    }

    public static SingletonRequestSender getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonRequestSender(context);
        }
        return instance;
    }

    public static void sendRequest(RequestResult callback) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name","testname");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                context.getResources().getString(R.string.request_url),jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SendRequest response", response.toString());
                        callback.onSuccess(response.toString());
                    }
               },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SendRequest error", error.getMessage());
                        callback.onError(error);
                    }
                });
        SingletonRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
