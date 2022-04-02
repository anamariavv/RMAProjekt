package com.example.rmaprojekt;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/*
* This is just to understand and test asnyc tasks
*
 */
public class RequestHandlerAsync extends AsyncTask<String, String, String> {
    private String requestString = "https://eun1.api.riotgames.com/lol/summoner/v4/summoners/by-name/Suvulaan";
    private String apiKey = "RGAPI-f32d1df0-24fa-43f5-ae10-0a9e365a19c6";
    private static final int CONNECTION_TIMEOUT = 15 * 1000;
    private String response = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            sendRequest();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendRequest() {
        try {
            Log.d("sent", requestString);
            URL url = new URL(requestString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Riot-Token", apiKey);
            conn.connect();

            InputStream in = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder response = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            Log.d("RResponse:", response.toString());
            Thread.sleep(1000);
            this.response = response.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("Requested: ", "Request processed");
    }
}
