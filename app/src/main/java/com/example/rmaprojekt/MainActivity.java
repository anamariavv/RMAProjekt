package com.example.rmaprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;

public class MainActivity extends AppCompatActivity {
    TextView textView;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text1);

        SingletonRequestSender.getInstance(this).sendRequest(new SingletonRequestSender.RequestResult() {
            @Override
            public String onSuccess(String result) {
                Log.d("Done", "success");
                textView.setText(result);
                return result;
            }
            @Override
            public VolleyError onError(VolleyError error) {
                Log.d("Done", error.getMessage());
                return error;
            }
        });


    }
}