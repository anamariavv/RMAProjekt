package com.example.rmaprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, String> params = new HashMap<String, String>();
        params.put("queue", "RANKED_SOLO_5x5");
        JSONObject challengerList = new MyRequest("eune", "lv4_challenger", params).createJsonRequest();

        SingletonRequestSender.getInstance(this).sendRequest(new SingletonRequestSender.RequestResult() {
            @Override
            public String onSuccess(String result) {
                Log.d("Done", "success");
                Log.d("repsonse", result);
                return result;
            }
            @Override
            public VolleyError onError(VolleyError error) {
                Log.d("Done", error.getMessage());
                return error;
            }
        }, challengerList);


    }
}